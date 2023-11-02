package com.casaculturaqxd.sgec.controller;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import com.casaculturaqxd.sgec.builder.EventoBuilder;
import com.casaculturaqxd.sgec.controller.preview.PreviewParticipanteController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Participante;


public class CadastrarEventoController implements ControllerEvento {
    private final int MAX_LOCALIZACOES = 4;
    DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    EventoBuilder builderEvento = new EventoBuilder();
    EventoDAO eventoDAO = new EventoDAO();
    ArrayList<FieldLocalizacaoController> controllersLocais =
            new ArrayList<FieldLocalizacaoController>();
    DateFormat formatterHorario;
    Stage stage;
    @FXML
    VBox root;
    @FXML
    VBox Localizacoes, cargaHoraria;
    @FXML
    HBox header, secaoParticipantes, Organizadores, Colaboradores;
    @FXML
    TextField titulo, publicoEsperado, publicoAlcancado, horas, minutos, horasCargaHoraria,
            numParticipantesEsperado, numMunicipiosEsperado;
    @FXML
    TextArea descricao;
    @FXML
    DatePicker dataInicial, dataFinal;
    @FXML
    ChoiceBox<String> classificacaoEtaria;
    private String[] classificacoes =
            {"Livre", "10 anos", "12 anos", "14 anos", "16 anos", "18 anos"};
    @FXML
    CheckBox checkMeta1, checkMeta2, checkMeta3, checkMeta4;
    @FXML
    RadioButton certificavel, acessivelEmLibras;

    // Botoes
    @FXML
    Button botaoNovaLocalizacao, botaoOrganizadores, botaoColaboradores, botaoParticipantes,
            botaoArquivos, cancelar, criarEvento;
    // Listas
    ObservableMap<Participante, FXMLLoader> participantes =
            FXCollections.<Participante, FXMLLoader> observableHashMap();


    public void initialize() throws IOException {
        formatterHorario = new SimpleDateFormat("HH:mm");

        loadMenu();
        // inicia com o local obrigatorio carregado na pagina
        addListenersParticipante(participantes);

        // FXMLLoader loaderMenu = new
        // FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        // header.getChildren().add(loaderMenu.load());
        // inicia com o local obrigatorio carregado na pagina
        carregarCampoLocalizacao();
        classificacaoEtaria.getItems().addAll(classificacoes);
        addInputConstraints();

        showCargaHoraria(checkMeta3.isSelected());
        showCertificavel(checkMeta3.isSelected());
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
        builderEvento.setLocalizacoes(idLocais);
        Evento novoEvento = builderEvento.getEvento();
        if (eventoDAO.inserirEvento(novoEvento)) {
            novoEvento = eventoDAO.buscarEvento(novoEvento).get();
            App.setRoot("view/home");
        }

    }


    public void cancelar() throws IOException {
        /**
         * TODO: implementar uma fila na classe App, para retornar a ultima tela visitada
         */
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
        System.out.println(controllersLocais);
    }

    public void removerLocalizacao() throws IOException {
    }

    public void adicionarParticipante(Participante participante) {
        // TODO remover implementacao de teste
        participantes.put(
                new Participante(1, "new_participante", "new_area atuacao", "new link", null),
                new FXMLLoader(App.class.getResource("view/preview/previewParticipante.fxml")));
    }

    public void removerParticipante(Participante participante) {
        participantes.remove(participante);
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
    }

    public boolean camposObrigatoriosPreenchidos() {
        if (classificacaoEtaria.getSelectionModel().getSelectedItem() == null
                || titulo.getText().isEmpty() || dataInicial.getValue() == null
                || dataFinal.getValue() == null) {
            return false;
        }
        return true;
    }

    public void addListenersParticipante(ObservableMap<Participante, FXMLLoader> observablemap) {
        CadastrarEventoController superController = this;
        observablemap.addListener(new MapChangeListener<Participante, FXMLLoader>() {
            @Override
            public void onChanged(
                    MapChangeListener.Change<? extends Participante, ? extends FXMLLoader> change) {

                if (change.wasAdded()) {
                    Participante addedKey = change.getKey();
                    // carregar o fxml de preview e setar o participante deste para o
                    // participante adicionado
                    try {
                        Parent previewParticipante = change.getValueAdded().load();
                        PreviewParticipanteController controller =
                                change.getValueAdded().getController();
                        controller.setParticipante(addedKey);
                        controller.setParentController(superController);

                        secaoParticipantes.getChildren().add(previewParticipante);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (change.wasRemoved()) {
                    PreviewParticipanteController removedController =
                            change.getValueRemoved().getController();
                    // Remover o Pane de preview ao deletar um Participante da lista
                    secaoParticipantes.getChildren().remove(removedController.getContainer());

                }
            }
        });
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
