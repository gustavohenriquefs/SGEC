package com.casaculturaqxd.sgec.controller;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeSet;
import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
import com.casaculturaqxd.sgec.builder.EventoBuilder;
import com.casaculturaqxd.sgec.controller.preview.PreviewArquivoController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
import com.casaculturaqxd.sgec.service.Service;
import com.casaculturaqxd.sgec.models.Meta;

public class CadastrarEventoController implements ControllerServiceFile {
    private final int MAX_LOCALIZACOES = 4;
    DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    EventoBuilder builderEvento = new EventoBuilder();
    EventoDAO eventoDAO = new EventoDAO();
    ServiceFileDAO serviceFileDAO;
    ArrayList<FieldLocalizacaoController> controllersLocais =
            new ArrayList<FieldLocalizacaoController>();
    DateFormat formatterHorario;
    Stage stage;
    @FXML
    VBox root;
    @FXML
    VBox Localizacoes, cargaHoraria;
    @FXML
    HBox header, Participantes, Organizadores, Colaboradores, secaoMetas;
    @FXML
    FlowPane secaoArquivos;

    @FXML
    TextField titulo, publicoEsperado, publicoAlcancado, horas, minutos, horasCargaHoraria,
            numParticipantesEsperado, numMunicipiosEsperado;
    @FXML
    TextArea descricao;
    @FXML
    DatePicker dataInicial, dataFinal;
    @FXML
    ChoiceBox<String> classificacaoEtaria;
    private final String[] classificacoes =
            {"Livre", "10 anos", "12 anos", "14 anos", "16 anos", "18 anos"};
    @FXML
    CheckBox checkMeta3;
    @FXML
    RadioButton certificavel, acessivelEmLibras;
    private ObservableMap<ServiceFile, FXMLLoader> mapServiceFiles = FXCollections.observableHashMap();

    // Botoes
    @FXML
    Button botaoNovaLocalizacao;

    public void initialize() throws IOException {
        formatterHorario = new SimpleDateFormat("HH:mm");
        addListenersServiceFile(mapServiceFiles);
        loadMenu();
        // inicia com o local obrigatorio carregado na pagina
        carregarCampoLocalizacao();
        classificacaoEtaria.getItems().addAll(classificacoes);
        addInputConstraints();

    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu =
                new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));

        root.getChildren().add(0, carregarMenu.load());
    }

    public void criarNovoEvento() throws IOException, ParseException {
        if (emptyLocalizacoes()) {
            Alert erroLocalizacao = new Alert(AlertType.ERROR,
                    "Um evento deve possuir pelo menos uma localização associada");
            erroLocalizacao.show();
        }
        if (!camposObrigatoriosPreenchidos()) {
            Alert erroLocalizacao = new Alert(AlertType.ERROR,
                    "nem todos os campos obrigatorios foram preenchidos");
            erroLocalizacao.show();
        }

        builderEvento.resetar();
        eventoDAO.setConnection(db.getConnection());

        serviceFileDAO = new ServiceFileDAO(eventoDAO.getConnection());

        builderEvento.setNome(titulo.getText());
        builderEvento.setDescricao(titulo.getText());
        builderEvento
                .setClassificacaoEtaria(classificacaoEtaria.getSelectionModel().getSelectedItem());
        if (dataInicial.getValue() != null) {
            builderEvento.setDataInicial(Date.valueOf(dataInicial.getValue()));
        }
        if (dataFinal.getValue() != null) {
            builderEvento.setDataFinal(Date.valueOf(dataFinal.getValue()));
        }
        if (!publicoEsperado.getText().isEmpty()) {
            builderEvento.setPublicoEsperado(Integer.parseInt(publicoEsperado.getText()));
        }
        if (!publicoAlcancado.getText().isEmpty()) {
            builderEvento.setPublicoAlcancado(Integer.parseInt(publicoAlcancado.getText()));
        }
        builderEvento.setCertificavel(certificavel.isSelected());
        if (!horasCargaHoraria.getText().isEmpty()) {
            builderEvento.setCargaHoraria(new java.sql.Time(
                    formatterHorario.parse(horasCargaHoraria.getText() + ":00:00").getTime()));
        }
        builderEvento.setAcessivelEmLibras(acessivelEmLibras.isSelected());
        if (!numMunicipiosEsperado.getText().isEmpty()) {
            builderEvento.setMunicipiosEsperado(Integer.parseInt(numMunicipiosEsperado.getText()));
        }
        if (!numParticipantesEsperado.getText().isEmpty()) {
            builderEvento
                    .setParticipantesEsperado(Integer.parseInt(numParticipantesEsperado.getText()));
        }
        TreeSet<Integer> idLocais = new TreeSet<>();
        for (FieldLocalizacaoController controller : controllersLocais) {
            idLocais.add(controller.getLocalizacao().getIdLocalizacao());
        }
        ArrayList<ServiceFile> listaArquivos = new ArrayList<>(mapServiceFiles.keySet());
        builderEvento.setListaArquivos(listaArquivos);
        builderEvento.setLocalizacoes(idLocais);
        builderEvento.setListaMetas(getMetasSelecionadas());

        Evento novoEvento = builderEvento.getEvento();
        if (eventoDAO.inserirEvento(novoEvento)) {
            // procura pelo arquivo no banco, se nao estiver realiza a insercao
            for (ServiceFile arquivo : novoEvento.getListaArquivos()) {
                ServiceFile arquivoExistente = serviceFileDAO.getArquivo(arquivo.getFileKey());
                if (arquivoExistente == null) {
                    serviceFileDAO.inserirArquivo(arquivo);
                } else {
                    int idxArquivo = novoEvento.getListaArquivos().indexOf(arquivo);

                    novoEvento.getListaArquivos().set(idxArquivo, arquivoExistente);
                }
            }
            eventoDAO.vincularArquivos(novoEvento);
            eventoDAO.vincularMetas(novoEvento.getListaMetas(), novoEvento.getIdEvento());
            novoEvento = eventoDAO.buscarEvento(novoEvento).get();

            App.setRoot("view/home");
        }

    }

    public void cancelar() throws IOException {
        
        builderEvento.resetar();
        App.setRoot("view/home");
    }

    public void carregarCampoLocalizacao() throws IOException {
        if (Localizacoes.getChildren().size() >= MAX_LOCALIZACOES) {
            botaoNovaLocalizacao.setDisable(true);
        }
        SubSceneLoader loaderLocais = new SubSceneLoader();
        GridPane novoLocal = (GridPane) loaderLocais.getPage("fields/fieldLocalizacao");
        Localizacoes.getChildren().add(novoLocal);
        FieldLocalizacaoController controller = loaderLocais.getLoader().getController();
        controllersLocais.add(controller);
    }

    /**
     * retorna uma lista de metas com id igual ao indice de cada checkbox de meta selecionada,
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

    public void removerLocalizacao() throws IOException {
    }

    public void adicionarParticipante() throws IOException {
        SubSceneLoader loaderParticipantes = new SubSceneLoader();
        VBox novoParticipante = (VBox) loaderParticipantes.getPage("fields/fieldParticipante");
        Participantes.getChildren().add(novoParticipante);
    }

    public void adicionarOrganizador() throws IOException {
        // TODO: permitir adicionar uma instituicao existente, neste caso o preview dela
        // que e adicionado a pagina
        SubSceneLoader loaderOrganizadores = new SubSceneLoader();
        AnchorPane novoOrganizador =
                (AnchorPane) loaderOrganizadores.getPage("fields/fieldInstituicao");
        Organizadores.getChildren().add(novoOrganizador);

    }

    public void adicionarColaborador() throws IOException {
        // TODO: permitir adicionar uma instituicao existente, neste caso o preview dela
        // que e adicionado a pagina
        SubSceneLoader loaderColaboradores = new SubSceneLoader();
        AnchorPane novoColaborador =
                (AnchorPane) loaderColaboradores.getPage("fields/fieldInstituicao");
        Colaboradores.getChildren().add(novoColaborador);
    }

    public void adicionarArquivo() {
        FileChooser fileChooser = new FileChooser();
        File arquivoSelecionado = fileChooser.showOpenDialog(stage);
        adicionarArquivo(new ServiceFile(arquivoSelecionado));
    }

    @Override
    public void adicionarArquivo(ServiceFile serviceFile) {
        mapServiceFiles.put(serviceFile,
                new FXMLLoader(App.class.getResource("view/preview/previewArquivo.fxml")));
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

    public boolean camposObrigatoriosPreenchidos() {
        if (classificacaoEtaria.getSelectionModel().getSelectedItem() == null
                || titulo.getText().isEmpty() || dataInicial.getValue() == null
                || dataFinal.getValue() == null) {
            return false;
        }
        return true;
    }

    public void addInputConstraints() {
        /* aplicando restrições aos inputs */
        horas.setTextFormatter(getTimeFormatter());
        minutos.setTextFormatter(getTimeFormatter());
        horasCargaHoraria.setTextFormatter(getTimeFormatter());
        publicoEsperado.setTextFormatter(getNumericalFormatter());
        publicoAlcancado.setTextFormatter(getNumericalFormatter());
        numParticipantesEsperado.setTextFormatter(getNumericalFormatter());
        numMunicipiosEsperado.setTextFormatter(getNumericalFormatter());

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
  
    public TextFormatter<String> getNumericalFormatter() {
        return new TextFormatter<>(change -> {
            if (change.getText().matches("\\d+")) {
                return change;
            } else {
                change.setText("");

                return change;
            }
        });
    }

    public TextFormatter<String> getTimeFormatter() {
        return new TextFormatter<>(change -> {
            if (change.getText().matches("\\d+") && change.getRangeEnd() < 2) {
                return change;
            } else {
                change.setText("");
                return change;
            }
        });
    }

    public void showCargaHoraria(boolean value) {
        cargaHoraria.setVisible(value);
    }

    public void showCertificavel(boolean value) {
        if (value == false) {
            certificavel.setSelected(value);
        }
        certificavel.setVisible(value);
    }

    public void onClickMeta3() {
        showCargaHoraria(checkMeta3.isSelected());
        showCertificavel(checkMeta3.isSelected());
    }

    public boolean emptyLocalizacoes() {
        return Localizacoes.getChildren().isEmpty();
    }
}
