package com.casaculturaqxd.sgec.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.DAO.LocalizacaoDAO;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
import com.casaculturaqxd.sgec.controller.dialog.DialogNovaInstituicao;
import com.casaculturaqxd.sgec.controller.dialog.ParticipanteDialog;
import com.casaculturaqxd.sgec.builder.EventoBuilder;
import com.casaculturaqxd.sgec.controller.preview.PreviewArquivoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewInstituicaoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewLocalizacaoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewParticipanteController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Indicador;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Localizacao;
import com.casaculturaqxd.sgec.models.Meta;
import com.casaculturaqxd.sgec.models.Participante;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

public class VisualizarEventoController implements ControllerServiceFile, ControllerEvento {
    private Evento evento;
    private Stage stage;
    private DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    private EventoDAO eventoDAO = new EventoDAO();
    private LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
    private File lastDirectoryOpen;
    private ArrayList<ServiceFile> removedFiles;
    private DateFormat formatterHorario;
    @FXML
    VBox root;
    @FXML
    FlowPane secaoParticipantes, secaoArquivos,  secaoOrganizadores, secaoColaboradores;
    @FXML
    VBox frameLocais;
    @FXML
    HBox secaoMetas;
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
    ObservableList<PreviewParticipanteController> participantes = FXCollections.<PreviewParticipanteController>observableList(new ArrayList<>());
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
    private Alert mensagem = new Alert(AlertType.NONE);

    public void initialize() throws IOException {
        formatterHorario = new SimpleDateFormat("HH:mm");
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

        formatterHorario = new SimpleDateFormat("HH:mm");
        removedFiles = new ArrayList<>();
        eventoDAO.setConnection(db.getConnection());
        localizacaoDAO.setConnection(db.getConnection());
        compararDatas();
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

    private void loadContent() throws IOException, SQLException {
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

        if (evento.getLocais() != null) {
            for (Localizacao local : evento.getLocais()) {
                FXMLLoader loaderLocal = new FXMLLoader(App.class.getResource("view/preview/previewLocalizacao.fxml"));

                try {
                    local = localizacaoDAO.getLocalizacao(local).get();
                } catch (NoSuchElementException e) {
                    throw new RuntimeException();
                }

                Parent previewLocal = loaderLocal.load();
                PreviewLocalizacaoController controller = loaderLocal.getController();
                controller.setLocalizacao(local);
                frameLocais.getChildren().add(previewLocal);
            }
        }

        this.loadParticipantes();

        tabelas.addAll(tabelaIndicadoresGerais, tabelaIndicadoresMeta1, tabelaIndicadoresMeta2);

        for (TableView<Indicador> tabela : tabelas) {
            loadTable(tabela);
        }

        numeroPublico = new Indicador("Quantidade de público", evento.getPublicoEsperado(),
                evento.getPublicoAlcancado());
        numeroMestres = new Indicador("Número de mestres da cultura", evento.getNumParticipantesEsperado(),
                evento.getListaParticipantes().size());
        numeroMunicipios = new Indicador("Número de municípios", evento.getNumMunicipiosEsperado(),
                evento.getNumMunicipiosAlcancado());

        addIndicador(tabelaIndicadoresGerais, numeroPublico);
        addIndicador(tabelaIndicadoresMeta1, numeroMestres);
        addIndicador(tabelaIndicadoresMeta2, numeroMunicipios);
    }

    private void loadParticipantes() {
        for (Participante paricipante : evento.getListaParticipantes()) {
            adicionarParticipante(paricipante);
        }
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

    public void setEvento(Evento evento) throws IOException, SQLException {
        this.evento = evento;
        loadContent();
    }

    public void salvarAlteracoes() {
        if (alterarEvento()) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Alterações salvas com sucesso");
            alert.getButtonTypes().remove(ButtonType.CANCEL);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Erro atualizando evento");
            alert.showAndWait();
        }

    }

    /**
     * constroi um novo evento utilizando os elementos atuais da view
     * 
     * @return evento atualizado
     */
    private Evento getTargetEvento() {
        EventoBuilder eventoBuilder = new EventoBuilder();
        Date novaDataInicial = dataInicial.getValue() != null ? Date.valueOf(dataInicial.getValue()) : null;
        Date novaDataFinal = dataFinal.getValue() != null ? Date.valueOf(dataFinal.getValue()) : null;
        eventoBuilder.setId(evento.getIdEvento()).setNome(titulo.getText()).setDescricao(descricao.getText())
                .setDataInicial(novaDataInicial).setDataFinal(novaDataFinal)
                .setClassificacaoEtaria(classificacaoEtaria.getSelectionModel().getSelectedItem())
                .setPublicoEsperado(numeroPublico.getValorEsperado())
                .setPublicoAlcancado(numeroPublico.getValorAlcancado());

        eventoBuilder.setAcessivelEmLibras(libras.isSelected()).setCertificavel(certificavel.isSelected())
                .setHorario(formatTimeInputField(horario)).setCargaHoraria(formatTimeInputField(cargaHoraria))
                .setNumParticipantesEsperado(numeroMestres.getValorEsperado())
                .setMunicipiosEsperado(numeroMunicipios.getValorEsperado());

        return eventoBuilder.getEvento();
    }

    /**
     * Chama o metodo responsavel pela atualizacao de um evento no banco
     * 
     */
    public boolean alterarEvento() {
        // TODO: adicionar validacao de convocatorias, pelo menos um colaborador deve
        // existir
        try {
            return updateEvento(getTargetEvento());
        } catch (Exception e) {
            Alert erroAtualizacao = new Alert(AlertType.ERROR);
            erroAtualizacao.setContentText("Erro ao alterar evento");
            erroAtualizacao.show();
            return false;
        }
    }

    public List<ServiceFile> getAddedFiles() throws SQLException {
        ServiceFileDAO serviceFileDAO = new ServiceFileDAO(db.getConnection());
        ArrayList<ServiceFile> oldFiles = serviceFileDAO.listarArquivosEvento(evento, 5);
        ArrayList<ServiceFile> newFiles = new ArrayList<>(mapServiceFiles.keySet());

        return newFiles.stream()
                .filter(newFile -> oldFiles.stream()
                        .noneMatch(oldFile -> oldFile.getServiceFileId().equals(newFile.getServiceFileId())))
                .collect(Collectors.toList());
    }

    /**
     * Inicia uma transacao para atualizar o evento e cada entidade vinculada,
     * realiza rollback em caso de qualquer excecao SQL
     * 
     * @param evento
     * @return true se todas as alteracoes forem sucedidas
     * @throws SQLException
     */
    private boolean updateEvento(Evento evento) throws SQLException {
        db.getConnection().setAutoCommit(false);
        try {
            // TODO: update de colaboradores, organizadores, participantes e locais
            if (!eventoDAO.alterarEvento(evento)) {
                return false;
            }
            if (!updateMetas(evento)) {
                return false;
            }
            if (!updateArquivos(evento)) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            db.getConnection().rollback();
            return false;
        } finally {
            db.getConnection().setAutoCommit(true);
        }
    }

    private boolean updateMetas(Evento evento) throws SQLException {
        return eventoDAO.atualizarMetasEvento(getMetasSelecionadas(), evento);
    }

    private boolean updateArquivos(Evento evento) throws SQLException {
        ServiceFileDAO serviceFileDAO = new ServiceFileDAO(db.getConnection());
        for (ServiceFile addedFile : getAddedFiles()) {
            Optional<ServiceFile> optionalFile = serviceFileDAO.getArquivo(addedFile.getFileKey());
            if (optionalFile.isPresent()) {
                addedFile = optionalFile.get();
            } else {
                serviceFileDAO.inserirArquivo(addedFile);
            }
            boolean checkNovoArquivo = serviceFileDAO.vincularArquivo(addedFile.getServiceFileId(),
                    evento.getIdEvento());
            if (!checkNovoArquivo) {
                return false;
            }
        }
        for (ServiceFile removedFile : removedFiles) {
            boolean checkArquivoRemovido = serviceFileDAO.desvincularArquivo(removedFile.getServiceFileId(),
                    evento.getIdEvento());
            if (!checkArquivoRemovido) {
                return false;
            }
        }
        return true;
    }

    public void goToMidiaEvento() throws IOException, SQLException {
        FXMLLoader loadTelaMidia = new FXMLLoader(App.class.getResource("view/midiaEvento.fxml"));
        Parent nextScreen = loadTelaMidia.load();
        MidiaEventoController controllerNextScreen = loadTelaMidia.getController();
        controllerNextScreen.setEvento(evento);

        App.setRoot(nextScreen);
    }

    private ArrayList<Meta> getMetasSelecionadas() {
        ArrayList<Meta> metasSelecionadas = new ArrayList<>();
        for (CheckBox checkBox : checkBoxesMetas) {
            if (checkBox.isSelected()) {
                // id das metas e 1-based
                Meta selectedMeta = new Meta(checkBoxesMetas.indexOf(checkBox) + 1);
                metasSelecionadas.add(selectedMeta);
            }
        }
        return metasSelecionadas;
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
        valorEsperado
                .setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        valorEsperado
                .setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Indicador, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Indicador, Integer> t) {
                        ((Indicador) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                                .setValorEsperado(t.getNewValue());
                    }
                });
        TableColumn<Indicador, Integer> valorAlcancado = new TableColumn<>("Valor alcançado");
        valorAlcancado.setCellValueFactory(new PropertyValueFactory<>("valorAlcancado"));
        valorAlcancado
                .setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        valorAlcancado
                .setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Indicador, Integer>>() {
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
     * pagina, exceto
     * botoes, labels e tableviews
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

    public void loadArquivos() throws SQLException {
        ServiceFileDAO serviceFileDAO = new ServiceFileDAO(db.getConnection());
        for (ServiceFile arquivo : serviceFileDAO.listarArquivosEvento(evento, 5)) {
            try {
                adicionarArquivo(arquivo);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void adicionarArquivo() {
        FileChooser fileChooser = new FileChooser();
        if (lastDirectoryOpen != null) {
            fileChooser.setInitialDirectory(lastDirectoryOpen);
        }
        File arquivoSelecionado = fileChooser.showOpenDialog(stage);
        try {
            if (arquivoSelecionado != null) {
                lastDirectoryOpen = arquivoSelecionado.getParentFile();

                ServiceFile arquivo = new ServiceFile(arquivoSelecionado);
                ServiceFileDAO serviceFileDAO = new ServiceFileDAO(db.getConnection());
                if (!serviceFileDAO.arquivoJaVinculado(arquivo.getFileKey(), evento)) {
                    adicionarArquivo(arquivo);
                } else {
                    Alert alert = new Alert(AlertType.ERROR, "Arquivo já foi adicionado");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Arquivo já foi adicionado");
            alert.showAndWait();
        }
    }

    @Override
    public void adicionarArquivo(ServiceFile serviceFile) throws IOException {
        mapServiceFiles.put(serviceFile,
                new FXMLLoader(App.class.getResource("view/preview/previewArquivo.fxml")));
    }

    @Override
    public void removerArquivo(ServiceFile serviceFile) {
        mapServiceFiles.remove(serviceFile);
        if (serviceFile.getServiceFileId() != null) {
            removedFiles.add(serviceFile);
        }
    }

    public void addListenersServiceFile(ObservableMap<ServiceFile, FXMLLoader> observablemap) {
        ControllerServiceFile superController = this;
        observablemap.addListener(new MapChangeListener<ServiceFile, FXMLLoader>() {
            @Override
            public void onChanged(
                    MapChangeListener.Change<? extends ServiceFile, ? extends FXMLLoader> change) {

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
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
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

    public void adicionarParticipante() throws SQLException {
        Dialog<Participante> participanteDialog = new ParticipanteDialog(new Participante(0));

        Optional<Participante> novoParticipanteOp = participanteDialog.showAndWait();

        if(novoParticipanteOp.isPresent()) {
            Participante novoParticipante = novoParticipanteOp.get();

            if(!participanteJaEstaNaLista(novoParticipante)) {
                adicionarParticipante(novoParticipanteOp.get());
            } else {
                mensagem.setAlertType(AlertType.ERROR);
                mensagem.setContentText("Não foi possivel realizar a vinculação: Participante já foi vinculado!");
                mensagem.show();
            } 
        }
    }

    private boolean participanteJaEstaNaLista(Participante participanteTarget) {
        for(PreviewParticipanteController previewParticipanteController: participantes) {
            Participante participante = previewParticipanteController.getParticipante();

            if(participante.getIdParticipante() == participanteTarget.getIdParticipante()) {
                return true;
            }
        }

        return false;
    }


    @Override
    public void adicionarParticipante(Participante participante) {
        Optional<PreviewParticipanteController> previewParticipanteControllerOp;

        try {
            previewParticipanteControllerOp = jaExisteParticipante(participante);
        } catch (SQLException e) {
            previewParticipanteControllerOp = Optional.empty();
        }

        if(previewParticipanteControllerOp.isPresent()) {
            try {
                previewParticipanteControllerOp.get().setParticipante(participante);
            } catch (SQLException e) {
                Alert alert = new Alert(AlertType.ERROR);

                alert.setTitle("Erro");
                alert.setContentText("Não foi possível adicionar o participante!");

                alert.showAndWait();

                e.printStackTrace();
            }
        } else {
            this.adicionarNovoParticipante(participante);
        }
    }

    private void adicionarNovoParticipante(Participante participante) {
        FXMLLoader loaderParticipante = new FXMLLoader(App.class.getResource("view/preview/previewParticipante.fxml"));
        try {
            loaderParticipante.load();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Erro");
            alert.setContentText("Não foi possível carregar o preview do participante!");

            alert.showAndWait();

            e.printStackTrace();
        }

        PreviewParticipanteController controller = loaderParticipante.getController();

        try {
            controller.setParticipante(participante);
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Erro");
            alert.setContentText("Não foi possível adicionar o participante!");

            alert.showAndWait();
            
            e.printStackTrace();
        }

        participantes.add(controller);
    }

    private Optional<PreviewParticipanteController> jaExisteParticipante(Participante participante) throws SQLException {
        for(PreviewParticipanteController previewParticipanteController: participantes) {
            Participante participanteTemp = previewParticipanteController.getParticipante();

            if(participanteTemp.getIdParticipante() == participante.getIdParticipante()) {
                return Optional.of(previewParticipanteController);
            }
        }

        return Optional.empty();
    }

    @Override
    public void removerParticipante(Participante participante) {
        Optional<PreviewParticipanteController> previewOptional = Optional.empty();

        try {
            previewOptional = jaExisteParticipante(participante);
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Erro");
            alert.setContentText("Não foi possível remover o participante!");

            alert.showAndWait();
            e.printStackTrace();
        }

        if(previewOptional.isPresent()) {
            participantes.remove(previewOptional.get());
        }

    }


    @Override
    public Stage getStage() {
        return this.stage;
    }

    public void addListenersParticipante(ObservableList<PreviewParticipanteController> observableList) {
        VisualizarEventoController superController = this;
        observableList.addListener(new ListChangeListener<PreviewParticipanteController>() {
            @Override
            public void onChanged(
                    ListChangeListener.Change<? extends PreviewParticipanteController> change) {
                    
                while (change.next()) {
                    if (change.wasAdded()) {

                        for (PreviewParticipanteController addedController : change.getAddedSubList()) {
                            addedController.setParentController(superController);

                            secaoParticipantes.getChildren().add(addedController.getContainer());
                        }
                        
                    }
                    
                    if (change.wasRemoved()) {
    
                        for(PreviewParticipanteController removedController: change.getRemoved()){
                            secaoParticipantes.getChildren().remove(removedController.getContainer());
                        }
                    }
                }
            }
        });
    }

    /**
     * formata um horario inserido em um textfield
     * 
     * @param inputField
     * @return o horario definido em horas e minutos no textfield, ou o horario
     *         zerado em caso de ParseException
     */
    private Time formatTimeInputField(TextInputControl inputField) {
        try {
            return new Time(formatterHorario.parse(inputField.getText()).getTime());
        } catch (ParseException e) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            return new Time(calendar.getTimeInMillis());
        }
    }

    @Override
    public void adicionarLocalizacao(Localizacao localizacao) {

    }

    @Override
    public void removerLocalizacao(Localizacao localizacao) {
    }

    public void adicionarOrganizador() throws IOException {
        ButtonType buttonTypeVincularOrganizadora = new ButtonType("Vincular como organizadora", ButtonBar.ButtonData.OK_DONE);
        DialogNovaInstituicao dialogNovaInstituicao = new DialogNovaInstituicao(buttonTypeVincularOrganizadora);
        Optional<Instituicao> novaInstituicao = dialogNovaInstituicao.showAndWait();
        if(novaInstituicao.isPresent()){
            adicionarOrganizador(novaInstituicao.get());
        }
    }

    public void adicionarOrganizador(Instituicao instituicao) {
        if(!contemInstituicao(organizadorObservableMap, instituicao.getNome()) && 
                !contemInstituicao(colaboradorObservableMap, instituicao.getNome())){
                    organizadorObservableMap.put(instituicao, new FXMLLoader(App.class.getResource("view/preview/previewInstituicao.fxml")));
        } else {
            mensagem.setAlertType(AlertType.ERROR);
            mensagem.setContentText("Não foi possivel realizar a vinculação: Instituição já foi vinculada!");
            mensagem.show();
        }
    }

    public void adicionarColaborador() throws IOException {
        ButtonType buttonTypeVincularColaborador = new ButtonType("Vincular como colaborador", ButtonBar.ButtonData.OK_DONE);
        DialogNovaInstituicao dialogNovaInstituicao = new DialogNovaInstituicao(buttonTypeVincularColaborador);
        Optional<Instituicao> novaInstituicao = dialogNovaInstituicao.showAndWait();
        if(novaInstituicao.isPresent()){
            adicionarColaborador(novaInstituicao.get());   
        }
    }

    public void adicionarColaborador(Instituicao instituicao) {
        if(!contemInstituicao(organizadorObservableMap, instituicao.getNome()) && 
                !contemInstituicao(colaboradorObservableMap, instituicao.getNome())){
                    colaboradorObservableMap.put(instituicao, new FXMLLoader(App.class.getResource("view/preview/previewInstituicao.fxml")));
        } else {
            mensagem.setAlertType(AlertType.ERROR);
            mensagem.setContentText("Não foi possivel realizar a vinculação: Instituição já foi vinculada!");
            mensagem.show();
        }
    }

    public static boolean contemInstituicao(ObservableMap<Instituicao, FXMLLoader> organizadorObservableMap, String nome) {
        for (Instituicao instituicao : organizadorObservableMap.keySet()) {
            if (instituicao.getNome().equals(nome)) {
                return true; 
            }
        }
        return false; 
    }

    public void removerInstituicao(Instituicao instituicao){
        if(contemInstituicao(organizadorObservableMap, instituicao.getNome())){
            Iterator<Instituicao> iterator = organizadorObservableMap.keySet().iterator();
        
            while (iterator.hasNext()) {
                Instituicao instituicaoTemp = iterator.next();
                if (instituicaoTemp.getNome().equals(instituicao.getNome())) {
                    iterator.remove();
                }
            }
        } else {
            Iterator<Instituicao> iterator = colaboradorObservableMap.keySet().iterator();
        
            while (iterator.hasNext()) {
                Instituicao instituicaoTemp = iterator.next();
                if (instituicaoTemp.getNome().equals(instituicao.getNome())) {
                    iterator.remove();
                }
            }
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
}
