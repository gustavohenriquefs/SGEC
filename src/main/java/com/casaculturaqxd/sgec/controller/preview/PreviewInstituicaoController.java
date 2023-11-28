package com.casaculturaqxd.sgec.controller.preview;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.textfield.AutoCompletionBinding;

import com.casaculturaqxd.sgec.DAO.InstituicaoDAO;
import com.casaculturaqxd.sgec.controller.ControllerEvento;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Participante;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class PreviewInstituicaoController {
    @FXML
    private Parent container; //pane raiz do fxml
    @FXML
    private HBox campoNome, campoContribuicao, campoValorContribuicao;
    @FXML
    private Label nomeInstituicao, contribuicao, valorContribuicao; 
    @FXML
    private Button buttonAlterarCapa, buttonRemover;
    @FXML
    private ImageView imagemViewInstituicao;

    private Instituicao instituicao;
    private ControllerEvento parentController;
    private ObservableMap<Parent, List<Node>> previousChildren = FXCollections.observableHashMap();
    private Stage stage;
    DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    InstituicaoDAO instituicaoDAO = new InstituicaoDAO(db.getConnection());
    ArrayList<String> listaNomes;
    private Alert mensagem = new Alert(AlertType.NONE);

    public void initialize() {
        // manter o botao sempre no topo da imagem
        buttonAlterarCapa.setViewOrder(-1);
        buttonRemover.setViewOrder(-1);
        try {
        listaNomes = instituicaoDAO.listarInstituicoes();
        } catch (SQLException e) {
        listaNomes = new ArrayList<>();
        }
    }

    public ControllerEvento getParentController() {
        return parentController;
    }

    public void setParentController(ControllerEvento parentController) {
        this.parentController = parentController;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
        loadContent();

    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void loadContent() {
        nomeInstituicao.setText(instituicao.getNome());
        contribuicao.setText(instituicao.getDescricaoContribuicao());
        valorContribuicao.setText(instituicao.getValorContribuicao());
        loadImagem();
    }

    public void loadImagem() {
        InputStream fileAsStream;
        if(instituicao.getImagemCapa() != null){
            try {
                fileAsStream = new FileInputStream(instituicao.getImagemCapa().getContent());
                imagemViewInstituicao.setImage(new Image(fileAsStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
    }

    public void updateImagemCapa() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Alterar foto da instituição");
        ExtensionFilter filterImagens = new ExtensionFilter("imagem", "*.jpeg", "*.jpg", "*.png", "*.bmp");
        fileChooser.getExtensionFilters().add(filterImagens);
        instituicao.setImagemCapa(new ServiceFile(fileChooser.showOpenDialog(stage)));
        loadImagem();
    }
    

    private void updateFieldNome(Labeled labeled, Pane fieldParent) {
        ObservableList<Node> oldNodes = FXCollections.observableArrayList(fieldParent.getChildren());
        previousChildren.put(campoNome, oldNodes);
        TextField textField = new TextField(labeled.getText());
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if(!listaNomes.contains(textField.getText())){
                    labeled.setText(textField.getText());
                    fieldParent.getChildren().setAll(previousChildren.get(campoNome));
                } else {
                    mensagem.setAlertType(AlertType.ERROR);
                    mensagem.setContentText("Não foi possivel alterar nome: Instituição já cadastrada!");
                    mensagem.show();
                    fieldParent.getChildren().setAll(previousChildren.get(campoNome));
                }
                
            } else if (event.getCode() == KeyCode.ESCAPE) {
                fieldParent.getChildren().setAll(previousChildren.get(campoNome));
            }
        });
        fieldParent.getChildren().setAll(textField);
    }

    private void updateField(Labeled labeled, Pane fieldParent) {
        ObservableList<Node> oldNodes = FXCollections.observableArrayList(fieldParent.getChildren());
        previousChildren.put(campoNome, oldNodes);
        TextField textField = new TextField(labeled.getText());
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                labeled.setText(textField.getText());
                fieldParent.getChildren().setAll(previousChildren.get(campoNome)); 
            } else if (event.getCode() == KeyCode.ESCAPE) {
                fieldParent.getChildren().setAll(previousChildren.get(campoNome));
            }
        });
        fieldParent.getChildren().setAll(textField);
    }

    public Parent getContainer() {
        return container;
    }

    public void setContainer(Parent container) {
        this.container = container;
    }

    public void remover() {
        parentController.removerInstituicao(getInstituicao());
    }

    public void updateNome() {
        updateFieldNome(nomeInstituicao, campoNome);
    }

    public void updateContribuica() {
        updateField(contribuicao, campoContribuicao);
    }

    public void updateValorContribuicao() {
        updateField(valorContribuicao, campoValorContribuicao);
    }
}
