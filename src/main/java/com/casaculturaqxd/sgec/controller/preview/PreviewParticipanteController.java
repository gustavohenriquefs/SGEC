package com.casaculturaqxd.sgec.controller.preview;

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.casaculturaqxd.sgec.DAO.ParticipanteDAO;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
import com.casaculturaqxd.sgec.controller.ControllerEvento;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
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
import javafx.scene.control.Hyperlink;
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

public class PreviewParticipanteController {
    private ControllerEvento parentController;
    private Participante participante;
    private DatabasePostgres database = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    private ServiceFileDAO serviceFileDAO;
    private Stage stage;
    @FXML
    private Parent container;
    @FXML
    private HBox campoNome, campoAreaAtuacao, campoMinibio, campoLink;
    @FXML
    private Label labelNomeArtista, labelAreaAtuacao, labelMinibio;
    @FXML
    private Hyperlink linkMapaCultural;
    @FXML
    private ImageView imageViewParticipante;
    @FXML
    private Button buttonAlterarCapa;
    private ObservableMap<Parent, List<Node>> previousChildren = FXCollections.observableHashMap();

    private ArrayList<String> nomeParticipantes;

    public void initialize() {;
        // manter o botao sempre no topo da imagem
        buttonAlterarCapa.setViewOrder(-1);
        serviceFileDAO = new ServiceFileDAO(database.getConnection());
        initNomes();
    }

    private void initNomes() {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(database.getConnection());

        try {
            this.nomeParticipantes = participanteDAO.obterListaNomesParticipantes();
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Erro");
            alert.setContentText("Não foi possível obter a lista de nomes de participantes!");

            alert.showAndWait();

            this.nomeParticipantes = new ArrayList<>();

            e.printStackTrace();
        }
    }

    public ControllerEvento getParentController() {
        return parentController;
    }

    /**
     * seta o stage desse controller para o mesmo do controller pai passado
     */
    public void setParentController(ControllerEvento parentController) {
        this.parentController = parentController;
    }

    public void setParticipante(Participante participante) throws SQLException {
        this.participante = participante;
        loadContent();
    }

    public Participante getParticipante() {
        return participante;
    }

    public void loadContent() throws SQLException {
        labelNomeArtista.setText(participante.getNome());
        labelAreaAtuacao.setText(participante.getAreaDeAtuacao());
        labelMinibio.setText(participante.getBio());
        linkMapaCultural.setText(participante.getLinkMapaDaCultura());
        loadImagem();
    }

    public void loadImagem() throws SQLException {
        InputStream fileAsStream;

        try {
            if (participante.getImagemCapa().getContent() == null) {
                participante.getImagemCapa().setContent(serviceFileDAO.getContent(participante.getImagemCapa()));
            }
            fileAsStream = new FileInputStream(participante.getImagemCapa().getContent());
            imageViewParticipante.setImage(new Image(fileAsStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remover() {
        parentController.removerParticipante(getParticipante());
    }

    public void updateImagemCapa() throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Alterar foto de participante");
        ExtensionFilter filterImagens = new ExtensionFilter("imagem", "*.jpeg", "*.jpg", "*.png", "*.bmp");
        fileChooser.getExtensionFilters().add(filterImagens);
        participante.setImagemCapa(new ServiceFile(fileChooser.showOpenDialog(stage)));
        loadImagem();
    }

    public void updateNome() {
        updateFieldNome(labelNomeArtista, campoNome);
    }

    private void updateFieldNome(Labeled labeled, Pane fieldParent) {
        ObservableList<Node> oldNodes = FXCollections.observableArrayList(fieldParent.getChildren());
        previousChildren.put(campoNome, oldNodes);
        TextField textField = new TextField(labeled.getText());

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {

                if(nomeJaExiste(textField.getText())){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setContentText("Já existe um participante com esse nome!");
                    alert.showAndWait();
                    return;
                }

                labeled.setText(textField.getText());
                fieldParent.getChildren().setAll(previousChildren.get(campoNome));
            } else if (event.getCode() == KeyCode.ESCAPE) {
                fieldParent.getChildren().setAll(previousChildren.get(campoNome));
            }
        });
        fieldParent.getChildren().setAll(textField);
    }

    private boolean nomeJaExiste(String text) {
        return this.nomeParticipantes.contains(text);
    }

    public void updateMinibio() {
        updateField(labelMinibio, campoMinibio);
    }

    public void updateAreaAtuacao() {
        updateField(labelAreaAtuacao, campoAreaAtuacao);
    }

    public void updateLinkMapaCultural() {
        updateField(linkMapaCultural, campoLink);
    }

    // substitui o campo especificado por um textfield, se a alteracao for cancelada
    // retorna ao estado anterior, se o novo texto for inserido, modifica o label
    // presente
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

    public void openInBrowser() {
        try {
            Desktop.getDesktop().browse(new URI(linkMapaCultural.getText()));
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Falha ao abrir o link");
            alert.showAndWait();
        }
    }

    public Parent getContainer() {
        return container;
    }

    public void setContainer(Parent container) {
        this.container = container;
    }
}
