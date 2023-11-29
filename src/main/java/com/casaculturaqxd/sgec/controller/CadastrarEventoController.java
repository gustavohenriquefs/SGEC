package com.casaculturaqxd.sgec.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.UnaryOperator;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.DAO.LocalizacaoDAO;
import com.casaculturaqxd.sgec.DAO.ParticipanteDAO;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
import com.casaculturaqxd.sgec.builder.EventoBuilder;
import com.casaculturaqxd.sgec.controller.dialog.DialogNovaInstituicao;
import com.casaculturaqxd.sgec.controller.preview.PreviewArquivoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewInstituicaoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewParticipanteController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Localizacao;
import com.casaculturaqxd.sgec.models.Meta;
import com.casaculturaqxd.sgec.models.Participante;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;

public class CadastrarEventoController implements ControllerServiceFile, ControllerEvento {
    private final int MAX_LOCALIZACOES = 4;
    DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    EventoBuilder builderEvento = new EventoBuilder();
    EventoDAO eventoDAO;
    ParticipanteDAO participanteDAO = new ParticipanteDAO();
    ServiceFileDAO serviceFileDAO;

    private File lastDirectoryOpen;
    
    ArrayList<FieldLocalizacaoController> controllersLocais = new ArrayList<FieldLocalizacaoController>();

    DateFormat formatterHorario;
    Stage stage;
    @FXML
    VBox root;
    @FXML
    VBox paneLocalizacoes, paneCargaHoraria;
    @FXML
    FlowPane secaoParticipantes, secaoOrganizadores, secaoColaboradores;
    @FXML
    FlowPane secaoArquivos;
    @FXML
    HBox secaoMetas;

    @FXML
    TextField fieldTitulo, fieldPublicoEsperado, fieldPublicoAlcancado, fieldHorario, fieldCargaHoraria,
            fieldNumParticipantesEsperado, fieldNumMunicipiosEsperado;
    @FXML
    TextArea fieldDescricao;
    @FXML
    DatePicker pickerDataInicial, pickerDataFinal;
    @FXML
    ChoiceBox<String> choiceClassificacaoEtaria;
    private final String[] classificacoes = { "Livre", "10 anos", "12 anos", "14 anos", "16 anos", "18 anos" };
    @FXML
    CheckBox checkMeta1, checkMeta2, checkMeta3, checkMeta4;
    @FXML
    RadioButton optionCertificavel, optionAcessivelEmLibras;
    private ObservableMap<ServiceFile, FXMLLoader> mapServiceFiles = FXCollections.observableHashMap();
    ObservableMap<Instituicao, FXMLLoader> organizadorObservableMap = FXCollections.<Instituicao, FXMLLoader>observableHashMap();
    ObservableMap<Instituicao, FXMLLoader> colaboradorObservableMap = FXCollections.<Instituicao, FXMLLoader>observableHashMap();
    @FXML
    Button botaoNovaLocalizacao;
    @FXML
    ImageView capaEvento;
    ObservableMap<Participante, FXMLLoader> participantes = FXCollections.<Participante, FXMLLoader>observableHashMap();
    private Alert mensagem = new Alert(AlertType.NONE);
    File file = null;

    public void initialize() throws IOException {
        eventoDAO = new EventoDAO(db.getConnection());
        participanteDAO.setConnection(db.getConnection());
        serviceFileDAO = new ServiceFileDAO(db.getConnection());

        formatterHorario = new SimpleDateFormat("HH:mm");
        addListenersServiceFile(mapServiceFiles);
        loadMenu();
        addListenersParticipante(participantes);
        addListenersColaborador(colaboradorObservableMap);
        addListenersOrganizador(organizadorObservableMap);

        // inicia com o local obrigatorio carregado na pagina
        carregarCampoLocalizacao();
        choiceClassificacaoEtaria.getItems().addAll(classificacoes);
        addInputConstraints();
        compararDatas();
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));

        root.getChildren().add(0, carregarMenu.load());
    }

    public void compararDatas() {
        
        // Impede que data posteriores á dataFinal sejam seleciondas no campo
        // dataInicial
        pickerDataFinal.valueProperty().addListener((observable, oldValue, newValue) -> {
            pickerDataInicial.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate currentDate = pickerDataFinal.getValue();
                    setDisable(empty || date.compareTo(currentDate) > 0);
                }
            });
        });

         // Impede que datas anteriores à dataInicial sejam selecionadas em dataFinal
        pickerDataInicial.valueProperty().addListener((observable, oldValue, newValue) -> {
            pickerDataFinal.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) { 
                    super.updateItem(date, empty);
                    LocalDate currentDate = pickerDataInicial.getValue();
                    setDisable(empty || date.compareTo(currentDate) < 0);
                }
            });
        });
    }

    private boolean insertEvento(Evento evento) throws SQLException {
        db.getConnection().setAutoCommit(false);

        try {
            eventoDAO.inserirEvento(evento);
            // procura pelo arquivo no banco, se nao estiver realiza a insercao
            for (ServiceFile arquivo : evento.getListaArquivos()) {
                Optional<ServiceFile> arquivoExistente = serviceFileDAO.getArquivo(arquivo.getFileKey());
                if (arquivoExistente.isEmpty()) {
                    serviceFileDAO.inserirArquivo(arquivo);
                } else {
                    int idxArquivo = evento.getListaArquivos().indexOf(arquivo);

                    evento.getListaArquivos().set(idxArquivo, arquivoExistente.get());
                }
            }
            eventoDAO.vincularArquivos(evento);
            eventoDAO.vincularMetas(evento.getListaMetas(), evento.getIdEvento());

            for(Localizacao localizacao: evento.getLocais()){
                adicionarLocalizacao(localizacao);
            }

            eventoDAO.vincularLocais(evento.getLocais(), evento.getIdEvento());
            
            return true;
        } catch (SQLException e) {
            db.getConnection().rollback();
            return false;
        } finally {
            db.getConnection().setAutoCommit(true);
        }
    }

    public void criarNovoEvento() {
        verificarInput();

        try {
            insertEvento(getTargetEvento());
            Alert alertaSucesso = new Alert(AlertType.CONFIRMATION, "Evento cadastrado com sucesso");
            ButtonType sucesso = new ButtonType("OK", ButtonData.FINISH);
            alertaSucesso.getButtonTypes().setAll(sucesso);
            alertaSucesso.setResultConverter(dialogButton -> {
                if (dialogButton == sucesso) {
                    try {
                        App.setRoot("view/home");
                        return dialogButton;
                    } catch (IOException e) {
                        return null;
                    }
                }
                return null;
            });
            alertaSucesso.showAndWait();
        } catch (Exception e) {
            Alert erroInsercao = new Alert(AlertType.ERROR, "falha cadastrando novo evento");
            erroInsercao.show();
        }
    }

    /**
     * constroi uma nova instancia de evento utilizando os parametros presentes nos
     * campos preenchidos da tela
     * 
     * @return o novo evento a ser inserido
     * @throws SQLException
     */
    private Evento getTargetEvento() throws SQLException {
        builderEvento = new EventoBuilder();
        Date novaDataInicial = pickerDataInicial.getValue() != null ? Date.valueOf(pickerDataInicial.getValue()) : null;
        Date novaDataFinal = pickerDataFinal.getValue() != null ? Date.valueOf(pickerDataFinal.getValue()) : null;
        Time novaCargaHoraria = formatTimeInputField(fieldCargaHoraria);
        int novoPublicoEsperado = formatNumericInputField(fieldPublicoEsperado);
        int novoPublicoAlcancado = formatNumericInputField(fieldPublicoAlcancado);
        int numMunicipiosEsperadoValue = formatNumericInputField(fieldNumMunicipiosEsperado);
        int numParticipanteEsperado = formatNumericInputField(fieldNumParticipantesEsperado);

        builderEvento.setNome(fieldTitulo.getText()).setDescricao(fieldDescricao.getText())
                .setClassificacaoEtaria(choiceClassificacaoEtaria.getSelectionModel().getSelectedItem())
                .setDataInicial(novaDataInicial).setDataFinal(novaDataFinal).setPublicoEsperado(novoPublicoEsperado)
                .setPublicoAlcancado(novoPublicoAlcancado);
        builderEvento.setAcessivelEmLibras(optionAcessivelEmLibras.isSelected());
        builderEvento.setCertificavel(optionCertificavel.isSelected());
        builderEvento.setCargaHoraria(novaCargaHoraria);
        builderEvento.setMunicipiosEsperado(numMunicipiosEsperadoValue);
        builderEvento.setNumParticipantesEsperado(numParticipanteEsperado);
        // TODO: substituir id de locais por models
        ArrayList<Localizacao> locais = new ArrayList<>();
        for (FieldLocalizacaoController controller : controllersLocais) {
            locais.add(controller.getLocalizacao());
        }
        ArrayList<ServiceFile> listaArquivos = new ArrayList<>(mapServiceFiles.keySet());
        builderEvento.setListaArquivos(listaArquivos);
        builderEvento.setLocalizacoes(locais);
        builderEvento.setListaMetas(getMetasSelecionadas());
        if(file != null){
            ServiceFile serviceFileTemp = new ServiceFile(file);
            try {
                if(serviceFileDAO.getArquivo(serviceFileTemp.getFileKey()).isEmpty()){
                  serviceFileDAO.inserirArquivo(serviceFileTemp);
                  serviceFileTemp = serviceFileDAO.getArquivo(serviceFileTemp.getFileKey()).get();
                } else {
                  serviceFileTemp = serviceFileDAO.getArquivo(serviceFileTemp.getFileKey()).get();
                }
              } catch (SQLException e) {
                e.printStackTrace();
              }
            builderEvento.setImagemCapa(serviceFileTemp);
        } 

        return builderEvento.getEvento();
    }

    /** verifica se as condicoes para cadastrar um evento sao atendidas*/
    private void verificarInput() {
        if (emptyLocalizacoes()) {
            Alert erroLocalizacao = new Alert(AlertType.ERROR,
                    "Um evento deve possuir pelo menos uma localização associada");
            erroLocalizacao.show();
            throw new RuntimeException("nenhum local inserido");
        } else if (!camposObrigatoriosPreenchidos()) {
            Alert erroLocalizacao = new Alert(AlertType.ERROR, "nem todos os campos obrigatorios foram preenchidos");
            erroLocalizacao.show();
            throw new RuntimeException("campos obrigatorios nao preenchidos");
        } else if (secaoColaboradores.getChildren().isEmpty() && checkMeta4.isSelected()) {
            Alert mensagemErro = new Alert(AlertType.ERROR, "Convocatórias precisam de pelo menos um colaborador");
            mensagemErro.show();
            throw new RuntimeException("Convocatorias precisam de pelo menos um colaborador");
        }
    }
    
    public void cancelar() throws IOException {
        builderEvento.resetar();
        App.setRoot("view/home");
    }

    public void carregarCampoLocalizacao() throws IOException {
        if (paneLocalizacoes.getChildren().size() >= MAX_LOCALIZACOES) {
            botaoNovaLocalizacao.setDisable(true);
        }

        FXMLLoader loaderLocais = new FXMLLoader(App.class.getResource("view/fields/fieldLocalizacao.fxml"));
        Parent novoLocal = loaderLocais.load();

        paneLocalizacoes.getChildren().add(novoLocal);

        FieldLocalizacaoController controller = loaderLocais.getController();
        
        controller.setPaneLocalizacao(novoLocal);
        controller.setParentController(this);
        controllersLocais.add(controller);
    }

    /**
     * retorna uma lista de metas com id igual ao indice de cada checkbox de meta
     * selecionada,
     */
    public ArrayList<Meta> getMetasSelecionadas() {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        ArrayList<Meta> metasSelecionadas = new ArrayList<>();
        for (Node node : secaoMetas.getChildren()) {
            if (node instanceof CheckBox) {
                checkBoxes.add((CheckBox) node);
            }
        }
        for (CheckBox checkBox : checkBoxes) {
            // ids sao 1-based
            if (checkBox.isSelected()) {
                metasSelecionadas.add(new Meta(checkBoxes.indexOf(checkBox) + 1));
            }
        }
        return metasSelecionadas;
    }

    public void adicionarParticipante() throws SQLException {
        // TODO: substituir por abrir o dialog de participante e chamar
        // adicionarParticipante(resultado)
        participantes.put(participanteDAO.getParticipante(new Participante(1)).get(),
                new FXMLLoader(App.class.getResource("view/preview/previewParticipante.fxml")));
    }

    public void adicionarParticipante(Participante participante) {
        // TODO remover implementacao de teste
        participantes.put(new Participante(1, "new_participante", "new_area atuacao", "new link", null),
                new FXMLLoader(App.class.getResource("view/preview/previewParticipante.fxml")));
    }

    public void removerParticipante(Participante participante) {
        participantes.remove(participante);
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

    public void adicionarArquivo() throws IOException {
        FileChooser fileChooser = new FileChooser();
        if (lastDirectoryOpen != null) {
            fileChooser.setInitialDirectory(lastDirectoryOpen);
        }
        File arquivoSelecionado = fileChooser.showOpenDialog(stage);
        lastDirectoryOpen = arquivoSelecionado.getParentFile();

        try {
            adicionarArquivo(new ServiceFile(arquivoSelecionado));
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Arquivo já foi adicionado");
            alert.showAndWait();
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
        try {
            mapServiceFiles.remove(serviceFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destacarCamposNaoPreenchidos() {
        if (choiceClassificacaoEtaria.getSelectionModel().getSelectedItem() == null) {
            choiceClassificacaoEtaria.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            choiceClassificacaoEtaria.setStyle(null);
        }
        if (fieldTitulo.getText().isEmpty()) {
            fieldTitulo.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            fieldTitulo.setStyle(null);
        }
        if (pickerDataInicial.getValue() == null) {
            pickerDataInicial.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            pickerDataInicial.setStyle(null);
        }
        if (pickerDataFinal.getValue() == null) {
            pickerDataFinal.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            pickerDataFinal.setStyle(null);
        }
    }

    public boolean camposObrigatoriosPreenchidos() {
        if (choiceClassificacaoEtaria.getSelectionModel().getSelectedItem() == null || fieldTitulo.getText().isEmpty()
                || pickerDataInicial.getValue() == null || pickerDataFinal.getValue() == null) {
            destacarCamposNaoPreenchidos();
            return false;
        }
        return true;
    }

    public void addListenersParticipante(ObservableMap<Participante, FXMLLoader> observablemap) {
        CadastrarEventoController superController = this;
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
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
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

    public void addListenersOrganizador(ObservableMap<Instituicao, FXMLLoader> observablemap) {
        CadastrarEventoController superController = this;
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
        CadastrarEventoController superController = this;
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

    private TextFormatter<LocalTime> formatter() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");

        // Criar um conversor para converter entre String e LocalTime
        LocalTimeStringConverter converter = new LocalTimeStringConverter(formato, null);

        // Criar um filtro para validar e formatar a entrada do usuário
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d{0,2}(:\\d{0,2})?$")) {
                return change;
            }
            return null;
        };

        // Criar o TextFormatter
        return new TextFormatter<>(converter, null, filter);
    }

    public void addInputConstraints() {
        /* aplicando restrições aos inputs */
        fieldHorario.setTextFormatter(formatter());
        fieldCargaHoraria.setTextFormatter(getTimeFormatter());
        fieldPublicoEsperado.setTextFormatter(getNumericalFormatter());
        fieldPublicoAlcancado.setTextFormatter(getNumericalFormatter());
        fieldNumParticipantesEsperado.setTextFormatter(getNumericalFormatter());
        fieldNumMunicipiosEsperado.setTextFormatter(getNumericalFormatter());

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

    private int formatNumericInputField(TextInputControl inputField) {
        try {
            return Integer.parseInt(inputField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    private TextFormatter<String> getNumericalFormatter() {
        return new TextFormatter<>(change -> {
            if (change.getText().matches("\\d+")) {
                return change;
            } else {
                change.setText("");

                return change;
            }
        });
    }

    private TextFormatter<String> getTimeFormatter() {
        return new TextFormatter<>(change -> {
            if (change.getText().matches("\\d+") && change.getRangeEnd() < 2) {
                return change;
            } else {
                change.setText("");
                return change;
            }
        });
    }

    private void showCargaHoraria(boolean value) {
        paneCargaHoraria.setVisible(value);
    }

    private void showCertificavel(boolean value) {
        if (value == false) {
            optionCertificavel.setSelected(value);
        }
        optionCertificavel.setVisible(value);
    }

    public void onClickMeta3() {
        showCargaHoraria(checkMeta3.isSelected());
        showCertificavel(checkMeta3.isSelected());
    }

    private boolean emptyLocalizacoes() {
        return paneLocalizacoes.getChildren().isEmpty();
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public void adicionarLocalizacao(Localizacao localizacao) {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
        localizacaoDAO.setConnection(db.getConnection());

        int id = localizacao.getIdLocalizacao(); 
        
        if(id == 0){
            try {
                localizacaoDAO.inserirLocalizacao(localizacao);
            } catch (SQLException e) {
                Alert erroInsercao = new Alert(AlertType.ERROR, "falha cadastrando nova localizacao");
                erroInsercao.show();
            }  
        }
    }

    @Override
    public void removerLocalizacao(Localizacao localizacao) {
        if(controllersLocais.size() == 1){
            Alert erroLocalizacao = new Alert(AlertType.ERROR,
                    "Um evento deve possuir pelo menos uma localização associada");
            erroLocalizacao.show();
            return;
        }

        for(FieldLocalizacaoController controller: controllersLocais){
            if(controller.getLocalizacao().equals(localizacao)){
                controllersLocais.remove(controller);
                paneLocalizacoes.getChildren().remove(controller.getPaneLocalizacao());
                break;
            }
        }

        botaoNovaLocalizacao.setDisable(false);
    }

    public void loadImagem(){
        InputStream fileAsStream;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Alterar foto da instituição");
        ExtensionFilter filterImagens = new ExtensionFilter("imagem", "*.jpeg", "*.jpg", "*.png", "*.bmp");
        fileChooser.getExtensionFilters().add(filterImagens);
        try {
            file = fileChooser.showOpenDialog(stage);
            fileAsStream = new FileInputStream(file);
            capaEvento.setImage(new Image(fileAsStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
    }
}
