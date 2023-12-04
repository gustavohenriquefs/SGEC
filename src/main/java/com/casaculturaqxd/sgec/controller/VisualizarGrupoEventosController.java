package com.casaculturaqxd.sgec.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.GrupoEventosDAO;
import com.casaculturaqxd.sgec.controller.dialog.DialogNovaInstituicao;
import com.casaculturaqxd.sgec.controller.preview.PreviewEventoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewInstituicaoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewParticipanteController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Indicador;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Localizacao;
import com.casaculturaqxd.sgec.models.Meta;
import com.casaculturaqxd.sgec.models.Participante;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

public class VisualizarGrupoEventosController implements ControllerEvento{

    private ObservableList<PreviewInstituicaoController> listaPreviewOrganizadores = FXCollections.observableArrayList();
    private ObservableList<PreviewInstituicaoController> listaPreviewColaboradores = FXCollections.observableArrayList();
    private ObservableList<PreviewEventoController> listaPreviewEventos;
    private GrupoEventos grupoEventos;
    private Stage stage;
    private DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    private GrupoEventosDAO dao;
     // listas
    ObservableList<PreviewParticipanteController> participantes = FXCollections
            .<PreviewParticipanteController>observableList(new ArrayList<>());
    
    @FXML
    private VBox root;

    @FXML
    private DatePicker dataInicial, dataFinal;

    @FXML
    private TextArea descricao;

    @FXML
    private ImageView imagemCapa;

    @FXML
    ArrayList<CheckBox> checkBoxesMetas = new ArrayList<>();

    @FXML
    ChoiceBox<String> classificacaoEtaria;

    @FXML
    private String[] classificacoes = { "Livre", "10 anos", "12 anos", "14 anos", "16 anos", "18 anos" };

    @FXML
    Button salvarAlteracoes, cancelar, adicionarCapa, adicionarOrganizadores, adicionarColaboradores;

    @FXML
    ImageView copiaCola;

    @FXML
    HBox secaoMetas;

    @FXML
    Label tituloEvento;

    @FXML
    FlowPane listaOrganizadores, listaColaboradores, acoesRealizadas;
    //Indicadores
    Indicador numeroPublico, numeroMestres, numeroMunicipios, numeroAcoesRealizadas;

    //Colocando todos os indicadores em uma tabela
    @FXML
    TableView<Indicador> tabelaIndicadores;

    @FXML
    private Tooltip tooltipCliboard;
    private Alert mensagem = new Alert(AlertType.NONE);

    public void initialize() throws IOException {
        compararDatas();
        tooltipCliboard = new Tooltip("Copiado para a área de transferência");
        tooltipCliboard.setHideDelay(Duration.seconds(1));
        Tooltip.install(copiaCola, tooltipCliboard);
        addListenersOrganizador(listaPreviewOrganizadores);
        addListenersColaborador(listaPreviewColaboradores);
        copiaCola.setOnMouseClicked(event -> copyToClipboard(event));
        dao = new GrupoEventosDAO(db.getConnection());
        loadMenu();
    }

    private void copyToClipboard(MouseEvent event) {
        getDescricao();
        // Exibe a mensagem e determina sua duração
        tooltipCliboard.show(copiaCola, event.getScreenX(), event.getScreenY());
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(e -> tooltipCliboard.hide());
        pause.play();
    }

    private void getDescricao() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(descricao.getText());
        clipboard.setContent(content);
    }

    public void compararDatas() {
        // Impede que data posteriores à dataFinal sejam seleciondas no campo
        // dataInicial
        dataFinal.valueProperty().addListener((observable, oldValue, newValue) -> {
            dataInicial.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate currentDate = dataFinal.getValue();
                    setDisable(empty || date.compareTo(currentDate) > 0);
                }
            });
        });
        // Impede que datas anteriores à dataInicial sejam selecionadas em dataFinal
        dataInicial.valueProperty().addListener((observable, oldValue, newValue) -> {
            dataFinal.setDayCellFactory(picker -> new DateCell() {

                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate currentDate = dataInicial.getValue();
                    setDisable(empty || date.compareTo(currentDate) < 0);
                }

            });
        });
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }

    public void loadContent(){
        loadImagemCapa();
        loadOrganizadores();
        loadColaboradores();
        loadMetas();
        classificacaoEtaria.getItems().addAll(classificacoes);
        tituloEvento.setText(grupoEventos.getNome());
        descricao.setText(grupoEventos.getDescricao());
        classificacaoEtaria.getSelectionModel().select(grupoEventos.getClassificacaoEtaria());
        loadTable(tabelaIndicadores);

        if (grupoEventos.getDataInicial() != null) {
            dataInicial.setValue(grupoEventos.getDataInicial().toLocalDate());
        }

        if (grupoEventos.getDataFinal() != null) {
            dataFinal.setValue(grupoEventos.getDataFinal().toLocalDate());
        }

    }

    private void loadImagemCapa(){
        if(this.grupoEventos.getImagemCapa() == null) {
            return;
        }else{
            try {
                File imageFile = this.grupoEventos.getImagemCapa().getContent();
                if(imageFile != null && imageFile.exists()) {
                    try (FileInputStream fileAsStream = new FileInputStream(imageFile)) {
                        this.imagemCapa.setImage(new Image(fileAsStream));
                    } catch (Exception e) {
                        imagemCapa.setImage(null);
                        e.printStackTrace();
                    }
                }else{
                    imagemCapa.setImage(null);
                }
            } catch (IOException e) {
                imagemCapa.setImage(null);
                e.printStackTrace();
            } 
        }
    }

    public void adicionarOrganizador() throws IOException {
        ButtonType buttonTypeVincularOrganizadora = new ButtonType("Vincular como organizadora",
                ButtonBar.ButtonData.OK_DONE);
        DialogNovaInstituicao dialogNovaInstituicao = new DialogNovaInstituicao(buttonTypeVincularOrganizadora);
        Optional<Instituicao> novaInstituicao = dialogNovaInstituicao.showAndWait();
        if (novaInstituicao.isPresent()) {
            adicionarOrganizador(novaInstituicao.get());
        }
    }

    public void adicionarOrganizador(Instituicao instituicao){
        if (!contemInstituicao(listaPreviewOrganizadores, instituicao)
                && !contemInstituicao(listaPreviewColaboradores, instituicao)) {
            FXMLLoader loaderInstituicao = new FXMLLoader(
                    App.class.getResource("view/preview/previewInstituicao.fxml"));
            try {
                loaderInstituicao.load();
                PreviewInstituicaoController controller = loaderInstituicao.getController();
                controller.setInstituicao(instituicao);
                listaPreviewOrganizadores.add(controller);
            } catch (IOException e) {
                Alert alert = new Alert(AlertType.WARNING,  "falha carregando organizador");
                alert.show();
            }
        } else {
            mensagem.setAlertType(AlertType.ERROR);
            mensagem.setContentText("Não foi possivel realizar a vinculação: Instituição já foi vinculada!");
            mensagem.show();
        }
    }

    public static boolean contemInstituicao(ObservableList<PreviewInstituicaoController> listaPreview,
            Instituicao instituicao) {
        for (PreviewInstituicaoController preview : listaPreview) {
            if (preview.getInstituicao().equals(instituicao)) {
                return true;
            }
        }
        return false;
    }

    public void loadColaboradores(){
        if(grupoEventos.getColaboradores() == null){
            return;
        }
        for (Instituicao instituicao : grupoEventos.getColaboradores()) {
            adicionarColaborador(instituicao);
        }
    }

    public void loadOrganizadores(){
        if(grupoEventos.getOrganizadores() == null){
            return;
        }
        for (Instituicao instituicao : grupoEventos.getOrganizadores()) {
            adicionarOrganizador(instituicao);
        }
    }

    public void adicionarColaborador() throws IOException {
        ButtonType buttonTypeVincularColaborador = new ButtonType("Vincular como colaborador",
                ButtonBar.ButtonData.OK_DONE);
        DialogNovaInstituicao dialogNovaInstituicao = new DialogNovaInstituicao(buttonTypeVincularColaborador);
        Optional<Instituicao> novaInstituicao = dialogNovaInstituicao.showAndWait();
        if (novaInstituicao.isPresent()) {
            adicionarColaborador(novaInstituicao.get());
        }
    }

    public void adicionarColaborador(Instituicao instituicao) {
        if (!contemInstituicao(listaPreviewOrganizadores, instituicao)
                && !contemInstituicao(listaPreviewColaboradores, instituicao)) {
            FXMLLoader loaderInstituicao = new FXMLLoader(
                    App.class.getResource("view/preview/previewInstituicao.fxml"));
            try {
                loaderInstituicao.load();
                PreviewInstituicaoController controller = loaderInstituicao.getController();
                controller.setInstituicao(instituicao);
                listaPreviewColaboradores.add(controller);
            } catch (IOException e) {
                Alert alert = new Alert(AlertType.WARNING, "falha carregando organizador");
                alert.show();
            }
        } else {
            mensagem.setAlertType(AlertType.ERROR);
            mensagem.setContentText("Não foi possivel realizar a vinculação: Instituição já foi vinculada!");
            mensagem.show();
        }
    }

    public void loadAcoes(){

    }

    private void loadMetas() {
        if(grupoEventos.getMetas() == null){
            return;
        }
        for (Node node : secaoMetas.getChildren()) {
            if (node instanceof CheckBox) {
                checkBoxesMetas.add((CheckBox) node);
            }
        }
        for (Meta meta : grupoEventos.getMetas()) {
            // ids sao 1-based
            checkBoxesMetas.get(meta.getIdMeta() - 1).setSelected(true);
        }
    }

    private void loadTable(TableView<Indicador> tabela) {
        TableColumn<Indicador, String> nomeIndicador = new TableColumn<>("Nome do indicador");
        nomeIndicador.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Indicador, Integer> valorEsperado = new TableColumn<>("Valor esperado");
        valorEsperado.setCellValueFactory(new PropertyValueFactory<>("valorEsperado"));
        valorEsperado.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        valorEsperado.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Indicador, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Indicador, Integer> t) {
                ((Indicador) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setValorEsperado(t.getNewValue());
            }
        });
        TableColumn<Indicador, Integer> valorAlcancado = new TableColumn<>("Valor alcançado");
        valorAlcancado.setCellValueFactory(new PropertyValueFactory<>("valorAlcancado"));
        valorAlcancado.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        valorAlcancado.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Indicador, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Indicador, Integer> t) {
                ((Indicador) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setValorAlcancado(t.getNewValue());
            }
        });

        tabela.getColumns().add(nomeIndicador);
        tabela.getColumns().add(valorEsperado);
        tabela.getColumns().add(valorAlcancado);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    public void addListenersOrganizador(ObservableList<PreviewInstituicaoController> observableList) {
        ControllerEvento Controller = this;
        observableList.addListener(new ListChangeListener<PreviewInstituicaoController>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends PreviewInstituicaoController> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (PreviewInstituicaoController addedController : change.getAddedSubList()) {
                            addedController.setParentController(Controller);

                            listaOrganizadores.getChildren().add(addedController.getContainer());
                        }
                    }
                    if (change.wasRemoved()) {
                        for (PreviewInstituicaoController removedController : change.getRemoved()) {
                            // Remover o Pane de preview ao deletar um Participante da lista
                            listaOrganizadores.getChildren().remove(removedController.getContainer());
                        }
                    }
                }
            }
        });

    }
    public void addListenersColaborador(ObservableList<PreviewInstituicaoController> observableList) {
        ControllerEvento Controller = this;
        observableList.addListener(new ListChangeListener<PreviewInstituicaoController>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends PreviewInstituicaoController> change) {
                while (change.next()) {

                    if (change.wasAdded()) {
                        for (PreviewInstituicaoController addedController : change.getAddedSubList()) {
                            addedController.setParentController(Controller);

                            listaColaboradores.getChildren().add(addedController.getContainer());
                        }
                    }
                    if (change.wasRemoved()) {
                        for (PreviewInstituicaoController removedController : change.getRemoved()) {
                            // Remover o Pane de preview ao deletar um Participante da lista
                            listaColaboradores.getChildren().remove(removedController.getContainer());
                        }
                    }
                }
            }
        });
    }

    @FXML
    public void salvarAlteracoes(){
        
    }

    private void getTargetGrupoEventos(){

    }

    public void setGrupoEventos(GrupoEventos grupoEventos) {
        this.grupoEventos = grupoEventos;
        loadContent();
    }

    @Override
    public void adicionarParticipante(Participante participante) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adicionarParticipante'");
    }

    @Override
    public void removerParticipante(Participante participante) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removerParticipante'");
    }

    @Override
    public Stage getStage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStage'");
    }

    @Override
    public void removerInstituicao(Instituicao instituicao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removerInstituicao'");
    }

    @Override
    public void adicionarLocalizacao(Localizacao localizacao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adicionarLocalizacao'");
    }

    @Override
    public void removerLocalizacao(Localizacao localizacao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removerLocalizacao'");
    }
}
