package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import java.util.ArrayList;
import java.util.Optional;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.DAO.LocalizacaoDAO;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
import com.casaculturaqxd.sgec.controller.dialog.DialogNovaInstituicao;
import com.casaculturaqxd.sgec.controller.preview.PreviewArquivoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewInstituicaoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewLocalizacaoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewParticipanteController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Indicador;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Localizacao;
import com.casaculturaqxd.sgec.models.Participante;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
import com.casaculturaqxd.sgec.service.Service;
import com.casaculturaqxd.sgec.models.Meta;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class VisualizarEventoController implements ControllerServiceFile, ControllerEvento {
    private Evento evento;
    private Stage stage;
    private DatabasePostgres db = DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
    private EventoDAO eventoDAO = new EventoDAO();
    private LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
    @FXML
    VBox root;
    @FXML
    FlowPane secaoParticipantes, secaoArquivos;
    @FXML
    VBox frameLocais;
    @FXML
    HBox secaoMetas, secaoOrganizadores, secaoColaboradores;
    @FXML
    TextArea descricao;
    @FXML
    Label titulo;
    @FXML
    DatePicker dataInicial, dataFinal;
    @FXML
    TextField cargaHoraria, horario;
    @FXML
    CheckBox certificavel, libras;
    ArrayList<CheckBox> checkBoxesMetas = new ArrayList<>();
    @FXML
    ChoiceBox<String> classificacaoEtaria;
    @FXML
    private String[] classificacoes = { "Livre", "10 anos", "12 anos", "14 anos", "16 anos", "18 anos" };
    // listas
    ObservableMap<Participante, FXMLLoader> participantes = FXCollections.<Participante, FXMLLoader>observableHashMap();
    ObservableMap<Instituicao, FXMLLoader> organizadorObservableMap = FXCollections.<Instituicao, FXMLLoader>observableHashMap();
    ObservableMap<Instituicao, FXMLLoader> colaboradorObservableMap = FXCollections.<Instituicao, FXMLLoader>observableHashMap();
    
    private ObservableMap<ServiceFile, FXMLLoader> mapServiceFiles = FXCollections.observableHashMap();
    // Tabela com todos os campos de input
    ObservableList<Control> camposInput = FXCollections.observableArrayList();
    // Indicadores
    Indicador numeroPublico, numeroMestres, numeroMunicipios;
    // Tabelas de indicadores
    @FXML
    TableView<Indicador> tabelaIndicadoresGerais, tabelaIndicadoresMeta1, tabelaIndicadoresMeta2;
    ObservableList<TableView<Indicador>> tabelas = FXCollections.observableArrayList();

    @FXML
    Button salvarAlteracoes;
    @FXML
    private ImageView copiaCola;
    @FXML
    private Tooltip tooltipCliboard;

    public void initialize() throws IOException {
        tooltipCliboard = new Tooltip("Copiado para a área de transferência");
        tooltipCliboard.setHideDelay(Duration.seconds(1));
        Tooltip.install(copiaCola, tooltipCliboard);
        copiaCola.setOnMouseClicked(event -> copyToClipboard(event));
        addControls(root, camposInput);
        addPropriedadeAlterar(camposInput);
        addListenersServiceFile(mapServiceFiles);
        addListenersParticipante(participantes);
        addListenersColaborador(colaboradorObservableMap);
        addListenersOrganizador(organizadorObservableMap);
        loadMenu();

        eventoDAO.setConnection(db.getConnection());
        localizacaoDAO.setConnection(db.getConnection());
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }

    private void loadContent() throws IOException {
        loadMetas();
        classificacaoEtaria.getItems().addAll(classificacoes);
        titulo.setText(evento.getNome());
        descricao.setText(evento.getDescricao());
        classificacaoEtaria.getSelectionModel().select(evento.getClassificacaoEtaria());
        loadArquivos();
        if (evento.getHorario() != null) {
            horario.setText(evento.getHorario().toString());
        }

        if (evento.getDataInicial() != null) {
            dataInicial.setValue(evento.getDataInicial().toLocalDate());
        }

        if (evento.getDataFinal() != null) {
            dataFinal.setValue(evento.getDataFinal().toLocalDate());
        }

        if (evento.getCargaHoraria() != null) {
            cargaHoraria.setText(String.valueOf(evento.getCargaHoraria().toString()));
        }

        certificavel.setSelected(evento.isCertificavel());
        libras.setSelected(evento.isAcessivelEmLibras());

        FXMLLoader loaderLocal = new FXMLLoader(App.class.getResource("view/preview/previewLocalizacao.fxml"));

        if (evento.getLocais() != null) {
            for (Integer idLocal : evento.getLocais()) {
                Localizacao local = new Localizacao();
                local.setIdLocalizacao(idLocal);
                local = localizacaoDAO.getLocalizacao(local);
                Parent previewLocal = loaderLocal.load();
                PreviewLocalizacaoController controller = loaderLocal.getController();
                controller.setLocalizacao(local);
                frameLocais.getChildren().add(previewLocal);
            }
        }

        tabelas.addAll(tabelaIndicadoresGerais, tabelaIndicadoresMeta1, tabelaIndicadoresMeta2);

        for (TableView<Indicador> tabela : tabelas) {
            loadTable(tabela);
        }

        numeroPublico = new Indicador("Quantidade de público", evento.getPublicoEsperado(),
                evento.getPublicoAlcancado());
        numeroMestres = new Indicador("Número de mestres da cultura", evento.getParticipantesEsperado(),
                evento.getListaParticipantes().size());
        numeroMunicipios = new Indicador("Número de municípios", evento.getMunicipiosEsperado(),
                eventoDAO.getNumeroMunicipiosDiferentes(evento.getIdEvento()));

        addIndicador(tabelaIndicadoresGerais, numeroPublico);
        addIndicador(tabelaIndicadoresMeta1, numeroMestres);
        addIndicador(tabelaIndicadoresMeta2, numeroMunicipios);
    }

    private void loadMetas() {
        for (Node node : secaoMetas.getChildren()) {
            if (node instanceof CheckBox) {
                checkBoxesMetas.add((CheckBox) node);
            }
        }
        for (Meta meta : evento.getListaMetas()) {
            // ids sao 1-based
            checkBoxesMetas.get(meta.getIdMeta() - 1).setSelected(true);
        }
    }

    public void setEvento(Evento evento) throws IOException {
        this.evento = evento;
        loadContent();
    }

    public boolean salvarAlteracoes() {
        alterarEvento();
        return eventoDAO.alterarEvento(evento);
    }

    public void alterarEvento() {
        try {
            evento.setDescricao(descricao.getText());
            evento.setDataInicial(Date.valueOf(dataInicial.getValue()));
            evento.setDataFinal(Date.valueOf(dataFinal.getValue()));
            evento.setClassificacaoEtaria(classificacaoEtaria.getSelectionModel().getSelectedItem());
            evento.setAcessivelEmLibras(libras.isSelected());
            evento.setCertificavel(certificavel.isSelected());
            evento.setHorario(Time.valueOf(horario.getText()));
            evento.setCargaHoraria(Time.valueOf(cargaHoraria.getText()));
            evento.setPublicoAlcancado(numeroPublico.getValorAlcancado());
            evento.setPublicoEsperado(numeroPublico.getValorEsperado());
            evento.setParticipantesEsperado(numeroMestres.getValorEsperado());
            evento.setMunicipiosEsperado(numeroMunicipios.getValorEsperado());
            eventoDAO.alterarEvento(evento);

            Alert sucessoAtualizacao = new Alert(AlertType.INFORMATION);
            sucessoAtualizacao.setContentText("Alterações salvas");
            sucessoAtualizacao.show();
        } catch (Exception e) {
            Alert erroAtualizacao = new Alert(AlertType.ERROR);
            erroAtualizacao.setContentText("Erro ao alterar evento");
            erroAtualizacao.show();
        }
    }

    public void goToMidiaEvento() throws IOException {
        FXMLLoader loadTelaMidia = new FXMLLoader(App.class.getResource("view/midiaEvento.fxml"));
        Parent nextScreen = loadTelaMidia.load();
        MidiaEventoController controllerNextScreen = loadTelaMidia.getController();
        controllerNextScreen.setEvento(evento);

        App.setRoot(nextScreen);
    }

    /**
     * <p>
     * adiciona a propriedade de tornar um campo editável após o clique <\p>
     */
    private void addPropriedadeAlterar(ObservableList<Control> listaInputs) {
        for (Control input : listaInputs) {
            input.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    input.setDisable(false);
                    salvarAlteracoes.setDisable(false);
                }
            });

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

    private void addIndicador(TableView<Indicador> tabela, Indicador indicador) {
        tabela.setItems(FXCollections.observableArrayList(indicador));
    }

    /**
     * <p>
     * Retorna todos os elementos que suportam interacao do usuario presentes na
     * pagina, exceto botoes, labels e tableviews
     * <p>
     */
    public void addControls(Parent parent, ObservableList<Control> list) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof Control && !(node instanceof Button || node instanceof Label)) {
                list.add((Control) node);
            } else if (node instanceof Parent) {
                addControls((Parent) node, list);
            }
        }
    }

    public void loadArquivos() {
        ServiceFileDAO serviceFileDAO = new ServiceFileDAO(db.getConnection());
        for (ServiceFile arquivo : serviceFileDAO.listarArquivosEvento(evento, 5)) {
            try {
                adicionarArquivo(arquivo);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void adicionarArquivo(ServiceFile serviceFile) throws IOException {
        for (ServiceFile existingFile : mapServiceFiles.keySet()) {
            if (serviceFile.getFileKey().equals(existingFile.getFileKey())) {
                throw new IOException("arquivo ja foi inserido");
            }
        }
        mapServiceFiles.put(serviceFile, new FXMLLoader(App.class.getResource("view/preview/previewArquivo.fxml")));
    }

    @Override
    public void removerArquivo(ServiceFile serviceFile) {
        Service service = serviceFile.getService();
        try {
            service.deletarArquivo(serviceFile.getBucket(), serviceFile.getFileKey());

        } catch (IllegalArgumentException e) {
            // caso arquivo ja nao esteja registrado
            mapServiceFiles.remove(serviceFile);
        } catch (Exception e) {
            // em qualquer outro erro
            e.printStackTrace();
        }
    }

    public void addListenersServiceFile(ObservableMap<ServiceFile, FXMLLoader> observablemap) {
        ControllerServiceFile superController = this;
        observablemap.addListener(new MapChangeListener<ServiceFile, FXMLLoader>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends ServiceFile, ? extends FXMLLoader> change) {

                if (change.wasAdded()) {
                    ServiceFile addedKey = change.getKey();
                    // carregar o fxml de preview e setar o ServiceFile deste para o
                    // arquivo adicionado
                    try {
                        Parent previewParticipante = change.getValueAdded().load();
                        PreviewArquivoController controller = change.getValueAdded().getController();
                        controller.setServiceFile(addedKey);
                        controller.setParentController(superController);

                        secaoArquivos.getChildren().add(previewParticipante);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (change.wasRemoved()) {
                    PreviewArquivoController removedController = change.getValueRemoved().getController();
                    // Remover o Pane de preview ao deletar um Arquivo da lista
                    secaoArquivos.getChildren().remove(removedController.getRoot());
                }
            }
        });
    }

    private void getDescricao() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(descricao.getText());
        clipboard.setContent(content);
    }

    private void copyToClipboard(MouseEvent event) {
        getDescricao();
        // Exibe a mensagem e determina sua duração
        tooltipCliboard.show(copiaCola, event.getScreenX(), event.getScreenY());
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(e -> tooltipCliboard.hide());
        pause.play();
    }

    @Override
    public void adicionarParticipante(Participante participante) {
        // TODO chamar metodo para todo participante vinculado ao evento
        participantes.put(participante, new FXMLLoader(App.class.getResource("view/preview/previewParticipante.fxml")));
    }

    @Override
    public void removerParticipante(Participante participante) {
        participantes.remove(participante);
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    public void addListenersParticipante(ObservableMap<Participante, FXMLLoader> observablemap) {
        VisualizarEventoController superController = this;
        observablemap.addListener(new MapChangeListener<Participante, FXMLLoader>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Participante, ? extends FXMLLoader> change) {

                if (change.wasAdded()) {
                    Participante addedKey = change.getKey();
                    // carregar o fxml de preview e setar o participante deste para o
                    // participante adicionado
                    try {
                        Parent previewParticipante = change.getValueAdded().load();
                        PreviewParticipanteController controller = change.getValueAdded().getController();
                        controller.setParticipante(addedKey);
                        controller.setParentController(superController);

                        secaoParticipantes.getChildren().add(previewParticipante);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (change.wasRemoved()) {
                    PreviewParticipanteController removedController = change.getValueRemoved().getController();
                    // Remover o Pane de preview ao deletar um Participante da lista
                    secaoParticipantes.getChildren().remove(removedController.getContainer());

                }
            }
        });
    }

    public void adicionarOrganizador() throws IOException {
        ButtonType buttonTypeVincularOrganizadora = new ButtonType("Vincular como organizadora", ButtonBar.ButtonData.OK_DONE);
        DialogNovaInstituicao dialogNovaInstituicao = new DialogNovaInstituicao(buttonTypeVincularOrganizadora);
        Optional<Instituicao> novaInstituicao = dialogNovaInstituicao.showAndWait();
        if(novaInstituicao.isPresent()){
            organizadorObservableMap.put(novaInstituicao.get(), new FXMLLoader(App.class.getResource("view/preview/previewInstituicao.fxml")));
        }
    }

    public void adicionarColaborador() throws IOException {
        ButtonType buttonTypeVincularColaborador = new ButtonType("Vincular como colaborador", ButtonBar.ButtonData.OK_DONE);
        DialogNovaInstituicao dialogNovaInstituicao = new DialogNovaInstituicao(buttonTypeVincularColaborador);
        Optional<Instituicao> novaInstituicao = dialogNovaInstituicao.showAndWait();
        if(novaInstituicao.isPresent()){
            colaboradorObservableMap.put(novaInstituicao.get(), new FXMLLoader(App.class.getResource("view/preview/previewInstituicao.fxml")));
        }
    }

    public void addListenersOrganizador(ObservableMap<Instituicao, FXMLLoader> observablemap) {
        VisualizarEventoController superController = this;
        observablemap.addListener(new MapChangeListener<Instituicao, FXMLLoader>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Instituicao, ? extends FXMLLoader> change) {

                if (change.wasAdded()) {
                    Instituicao addedKey = change.getKey();
                    // carregar o fxml de preview e setar o participante deste para o
                    // participante adicionado
                    try {
                        Parent previewInstituicao = change.getValueAdded().load();
                        PreviewInstituicaoController controller = change.getValueAdded().getController();
                        controller.setInstituicao(addedKey);
                        controller.setParentController(superController);

                        secaoOrganizadores.getChildren().add(previewInstituicao);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (change.wasRemoved()) {
                    PreviewInstituicaoController removedController = change.getValueRemoved().getController();
                    // Remover o Pane de preview ao deletar um Participante da lista
                    secaoOrganizadores.getChildren().remove(removedController.getContainer());

                }
            }
        });
    }

    public void addListenersColaborador(ObservableMap<Instituicao, FXMLLoader> observablemap) {
        VisualizarEventoController superController = this;
        observablemap.addListener(new MapChangeListener<Instituicao, FXMLLoader>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Instituicao, ? extends FXMLLoader> change) {

                if (change.wasAdded()) {
                    Instituicao addedKey = change.getKey();
                    // carregar o fxml de preview e setar o participante deste para o
                    // participante adicionado
                    try {
                        Parent previewInstituicao = change.getValueAdded().load();
                        PreviewInstituicaoController controller = change.getValueAdded().getController();
                        controller.setInstituicao(addedKey);
                        controller.setParentController(superController);

                        secaoColaboradores.getChildren().add(previewInstituicao);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (change.wasRemoved()) {
                    PreviewInstituicaoController removedController = change.getValueRemoved().getController();
                    // Remover o Pane de preview ao deletar um Participante da lista
                    secaoColaboradores.getChildren().remove(removedController.getContainer());

                }
            }
        });
    }

    @Override
    public void removerInstituicao(Instituicao instituicao) {
        if(organizadorObservableMap.containsKey(instituicao)){
            organizadorObservableMap.remove(instituicao);
        } else {
            colaboradorObservableMap.remove(instituicao);
        }
    }
}
