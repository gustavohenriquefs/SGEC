package com.casaculturaqxd.sgec.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.DAO.GrupoEventosDAO;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
import com.casaculturaqxd.sgec.builder.GrupoEventosBuilder;
import com.casaculturaqxd.sgec.controller.dialog.DialogNovaInstituicao;
import com.casaculturaqxd.sgec.controller.preview.PreviewEventoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewInstituicaoController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Localizacao;
import com.casaculturaqxd.sgec.models.Meta;
import com.casaculturaqxd.sgec.models.Participante;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class CadastrarGrupoEventosController implements ControllerServiceFile, ControllerEvento {
    DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    EventoDAO eventoDAO = new EventoDAO(db.getConnection());
    ServiceFileDAO serviceFileDAO = new ServiceFileDAO(db.getConnection());
    GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
    AutoCompletionBinding<String> binding;
    GrupoEventosBuilder builderGrupoEvento = new GrupoEventosBuilder();
    @FXML
    private VBox root;
    @FXML
    HBox secaoMetas;
    @FXML 
    TextField titulo, publicoEsperado, publicoAlcancado, participantesEsperado, colaboradoresEsperados, acoesDeselvolvidasEsperadas;
    @FXML
    ComboBox<String> classificacaoEtaria;
    private final String[] classificacoes = { "Livre", "10 anos", "12 anos", "14 anos", "16 anos", "18 anos" };
    @FXML
    DatePicker dataInicial, dataFinal;
    @FXML
    CheckBox checkMeta1, checkMeta2, checkMeta3, checkMeta4;
    @FXML
    TextArea descricao;
    @FXML
    FlowPane secaoEvento, secaoColaboradores, secaoOrganizadores;
    ArrayList<String> nomeEventos;

    ObservableList<PreviewInstituicaoController> listaControllersOrganizadores = FXCollections.<PreviewInstituicaoController>observableList(new ArrayList<>()), 
            listaControllersColaboradores = FXCollections.<PreviewInstituicaoController>observableList(new ArrayList<>());
    ObservableList<PreviewEventoController> listaControllersEventos = FXCollections
            .<PreviewEventoController>observableList(new ArrayList<>());
   
    private Alert mensagem = new Alert(AlertType.NONE);
    @FXML
    ImageView capaEvento;
    File file = null;
    Stage stage;

    public void initialize() throws IOException, SQLException {
        loadMenu();
        classificacaoEtaria.getItems().addAll(classificacoes);
        addListenersColaborador(listaControllersColaboradores);
        addListenersOrganizador(listaControllersOrganizadores);
        addListenersEvento(listaControllersEventos);
        nomeEventos = eventoDAO.listarNomesEventos();
        compararDatas();
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu =
                new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }

    public void criarNovoGrupoEvento() {
        verificarInput();

        try {
            GrupoEventos grupoEventos = getTargetGrupoEvento();
            insertGrupoEvento(grupoEventos);
            Alert alertaSucesso = new Alert(AlertType.CONFIRMATION, "Grupo Evento cadastrado com sucesso");
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

    public void cancelar() throws IOException {
        builderGrupoEvento.resetar();
        App.setRoot("view/home");
    }

    private void verificarInput() {
        if (!camposObrigatoriosPreenchidos()) {
            Alert erroLocalizacao = new Alert(AlertType.ERROR, "nem todos os campos obrigatorios foram preenchidos");
            erroLocalizacao.show();
            throw new RuntimeException("campos obrigatorios nao preenchidos");
        } else if (secaoColaboradores.getChildren().isEmpty() && checkMeta4.isSelected()) {
            Alert mensagemErro = new Alert(AlertType.ERROR, "Convocatórias precisam de pelo menos um colaborador");
            mensagemErro.show();
            throw new RuntimeException("Convocatorias precisam de pelo menos um colaborador");
        }
    }

    public boolean camposObrigatoriosPreenchidos() {
        if (classificacaoEtaria.getSelectionModel().getSelectedItem() == null || titulo.getText().isEmpty()
                || dataInicial.getValue() == null || dataFinal.getValue() == null) {
            destacarCamposNaoPreenchidos();
            return false;
        }
        return true;
    }

    public void destacarCamposNaoPreenchidos() {
        if (classificacaoEtaria.getSelectionModel().getSelectedItem() == null) {
            classificacaoEtaria.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            classificacaoEtaria.setStyle(null);
        }
        if (titulo.getText().isEmpty()) {
            titulo.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            titulo.setStyle(null);
        }
        if (dataInicial.getValue() == null) {
            dataInicial.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            dataInicial.setStyle(null);
        }
        if (dataFinal.getValue() == null) {
            dataFinal.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            dataFinal.setStyle(null);
        }
    }

    private boolean insertGrupoEvento(GrupoEventos grupoEventos) throws SQLException {
        db.getConnection().setAutoCommit(false);

        try {
            if (!grupoEventosDAO.insertGrupoEventos(grupoEventos)) {
                throw new SQLException();
            }
            ArrayList<Meta> metas = grupoEventos.getMetas();
            for (Meta meta : metas) {
                if (!grupoEventosDAO.vincularMeta(meta, grupoEventos)) {
                    throw new SQLException();
                }
            }
            
            if (!grupoEventosDAO.vincularAllColaboradores(grupoEventos, grupoEventos.getColaboradores())) {
                throw new SQLException();
            }
            if (!grupoEventosDAO.vincularAllOrganizadores(grupoEventos, grupoEventos.getOrganizadores())) {
                throw new SQLException();
            }

            ArrayList<Evento> eventos = grupoEventos.getEventos();
            for (Evento evento : eventos) {
                if (!grupoEventosDAO.vincularEvento(grupoEventos, evento)) {
                    throw new SQLException();
                } 
            }
                    
            return true;
        } catch (SQLException e) {
            db.getConnection().rollback();
            return false;
        } finally {
            db.getConnection().setAutoCommit(true);
        }
    }

    private GrupoEventos getTargetGrupoEvento() throws SQLException {
        builderGrupoEvento = new GrupoEventosBuilder();
        Date novaDataInicial = dataInicial.getValue() != null ? Date.valueOf(dataInicial.getValue()) : null;
        Date novaDataFinal = dataFinal.getValue() != null ? Date.valueOf(dataFinal.getValue()) : null;
        int novoPublicoEsperado = formatNumericInputField(publicoEsperado);
        int novoPublicoAlcancado = formatNumericInputField(publicoAlcancado);
        int numParticipantesEsperado = formatNumericInputField(participantesEsperado);
        int numColaboradoresEsperados = formatNumericInputField(colaboradoresEsperados);
        int numAcoesDeselvolvidasEsperadas = formatNumericInputField(acoesDeselvolvidasEsperadas);


        builderGrupoEvento.setNome(titulo.getText()).setDescricao(descricao.getText())
                .setClassificacaoEtaria(classificacaoEtaria.getSelectionModel().getSelectedItem())
                .setDataInicial(novaDataInicial).setDataFinal(novaDataFinal).setPublicoEsperado(novoPublicoEsperado)
                .setPublicoAlcancado(novoPublicoAlcancado)
                .setPublicoEsperado(novoPublicoEsperado)
                .setNumParticipantesEsperado(numParticipantesEsperado)
                .setNumColaboradoresEsperado(numColaboradoresEsperados)
                .setNumAcoesEsperado(numAcoesDeselvolvidasEsperadas);
        builderGrupoEvento.setMetas(getMetasSelecionadas());

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
            builderGrupoEvento.setImagemCapa(serviceFileTemp);
        }

        

        ArrayList<Instituicao> colaboradores = new ArrayList<>();

        for (PreviewInstituicaoController controller : listaControllersColaboradores) {
            Instituicao instituicao = controller.getInstituicao();
            colaboradores.add(instituicao);
        }

        ArrayList<Instituicao> organizadores = new ArrayList<>();

        for (PreviewInstituicaoController controller : listaControllersOrganizadores) {
            Instituicao instituicao = controller.getInstituicao();
            organizadores.add(instituicao);
        }

        ArrayList<Evento> eventos = new ArrayList<>();

        for (PreviewEventoController controller : listaControllersEventos) {
            Evento evento = controller.getEvento();
            eventos.add(evento);
        }

        builderGrupoEvento.setColaboradores(colaboradores);
        builderGrupoEvento.setOrganizadores(organizadores);
        builderGrupoEvento.setEventos(eventos);

        return builderGrupoEvento.getGrupoEventos();
    }

    private int formatNumericInputField(TextInputControl inputField) {
        try {
            return Integer.parseInt(inputField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }

    }

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

    public void addListenersColaborador(ObservableList<PreviewInstituicaoController> observableList) {
        CadastrarGrupoEventosController superController = this;
        observableList.addListener(new ListChangeListener<PreviewInstituicaoController>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends PreviewInstituicaoController> change) {
                while (change.next()) {

                    if (change.wasAdded()) {
                        for (PreviewInstituicaoController addedController : change.getAddedSubList()) {
                            addedController.setParentController(superController);

                            secaoColaboradores.getChildren().add(addedController.getContainer());
                        }
                    }
                    if (change.wasRemoved()) {
                        for (PreviewInstituicaoController removedController : change.getRemoved()) {
                            // Remover o Pane de preview ao deletar um Participante da lista
                            secaoColaboradores.getChildren().remove(removedController.getContainer());
                        }
                    }
                }
            }
        });
    }

    public void addListenersOrganizador(ObservableList<PreviewInstituicaoController> observableList) {
        CadastrarGrupoEventosController superController = this;
        observableList.addListener(new ListChangeListener<PreviewInstituicaoController>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends PreviewInstituicaoController> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (PreviewInstituicaoController addedController : change.getAddedSubList()) {
                            addedController.setParentController(superController);

                            secaoOrganizadores.getChildren().add(addedController.getContainer());
                        }
                    }
                    if (change.wasRemoved()) {
                        for (PreviewInstituicaoController removedController : change.getRemoved()) {
                            // Remover o Pane de preview ao deletar um Participante da lista
                            secaoOrganizadores.getChildren().remove(removedController.getContainer());
                        }
                    }
                }
            }
        });

    }

    public void addListenersEvento(ObservableList<PreviewEventoController> observableList) {
        CadastrarGrupoEventosController superController = this;
        observableList.addListener(new ListChangeListener<PreviewEventoController>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends PreviewEventoController> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (PreviewEventoController addedController : change.getAddedSubList()) {
                            addedController.setParentController(superController);

                            secaoEvento.getChildren().add(addedController.getContainer());
                        }
                    }
                    if (change.wasRemoved()) {
                        for (PreviewEventoController removedController : change.getRemoved()) {
                            // Remover o Pane de preview ao deletar um Participante da lista
                            secaoEvento.getChildren().remove(removedController.getContainer());
                        }
                    }
                }
            }
        });

    }

    public void adicionarEventos(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Adicionar Evento");
        dialog.setHeaderText("Digite o nome do evento:");
        dialog.setContentText("Evento:");
        dialog.getEditor().setText("");
        dialog.getEditor().setPromptText("Digite ou selecione um evento");
        binding = TextFields.bindAutoCompletion(dialog.getEditor(), nomeEventos);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nomeEvento -> {
            Optional<Evento> evento = null;
            try {
                evento = eventoDAO.getEvento(nomeEvento);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(evento.isPresent())
                adicionarEventos(evento.get());
        });
    
    }

    public void adicionarEventos(Evento evento){
        if (!contemEvento(listaControllersEventos, evento)) {
            FXMLLoader loaderEvento = new FXMLLoader(
                    App.class.getResource("view/preview/previewEventoExistente.fxml"));
            try {
                loaderEvento.load();
                PreviewEventoController controller = loaderEvento.getController();
                controller.setEvento(evento);
                listaControllersEventos.add(controller);
            } catch (IOException e) {
                Alert alert = new Alert(AlertType.WARNING, "falha carregando evento");
                alert.show();
            }
        } else {
            mensagem.setAlertType(AlertType.ERROR);
            mensagem.setContentText("Não foi possivel realizar a vinculação: Evento já foi vinculada!");
            mensagem.show();
        }

        ArrayList<Instituicao> colaboradores = evento.getListaColaboradores();
        if(!colaboradores.isEmpty()){
            for (Instituicao colaborador : colaboradores) {
                adicionarColaborador(colaborador);
            }
        }

        ArrayList<Instituicao> organizadores = evento.getListaOrganizadores();
        if(!organizadores.isEmpty()){
            for (Instituicao organizador : organizadores) {
                adicionarOrganizador(organizador);
            }
        }
    }

    public static boolean contemEvento(ObservableList<PreviewEventoController> listaPreview,
    Evento evento) {
        for (PreviewEventoController preview : listaPreview) {
            if (preview.getEvento().equals(evento)) {
                return true;
            }
        }
        return false;
    }

    public void removerEvento(Evento evento) {
        if (contemEvento(listaControllersEventos, evento)) {
            Iterator<PreviewEventoController> iterator = listaControllersEventos.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getEvento().equals(evento)) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void adicionarParticipante(Participante participante) {}

    @Override
    public void removerParticipante(Participante participante) {}

    public void adicionarOrganizador() throws IOException {
        ButtonType buttonTypeVincularOrganizadora = new ButtonType("Vincular como organizadora",
                ButtonBar.ButtonData.OK_DONE);
        DialogNovaInstituicao dialogNovaInstituicao = new DialogNovaInstituicao(buttonTypeVincularOrganizadora);
        Optional<Instituicao> novaInstituicao = dialogNovaInstituicao.showAndWait();
        if (novaInstituicao.isPresent()) {
            adicionarOrganizador(novaInstituicao.get());
        }
    }

    @Override
    public void adicionarOrganizador(Instituicao instituicao) {
        if (!contemInstituicao(listaControllersOrganizadores, instituicao)
                && !contemInstituicao(listaControllersOrganizadores, instituicao)) {
            FXMLLoader loaderInstituicao = new FXMLLoader(
                    App.class.getResource("view/preview/previewInstituicao.fxml"));
            try {
                loaderInstituicao.load();
                PreviewInstituicaoController controller = loaderInstituicao.getController();
                controller.setInstituicao(instituicao);
                listaControllersOrganizadores.add(controller);
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

    public void adicionarColaborador() throws IOException {
        ButtonType buttonTypeVincularColaborador = new ButtonType("Vincular como colaborador",
                ButtonBar.ButtonData.OK_DONE);
        DialogNovaInstituicao dialogNovaInstituicao = new DialogNovaInstituicao(buttonTypeVincularColaborador);
        Optional<Instituicao> novaInstituicao = dialogNovaInstituicao.showAndWait();
        if (novaInstituicao.isPresent()) {
            adicionarColaborador(novaInstituicao.get());
        }
    }

    @Override
    public void adicionarColaborador(Instituicao instituicao) {
        if (!contemInstituicao(listaControllersColaboradores, instituicao)
                && !contemInstituicao(listaControllersColaboradores, instituicao)) {
            FXMLLoader loaderInstituicao = new FXMLLoader(
                    App.class.getResource("view/preview/previewInstituicao.fxml"));
            try {
                loaderInstituicao.load();
                PreviewInstituicaoController controller = loaderInstituicao.getController();
                controller.setInstituicao(instituicao);
                listaControllersColaboradores.add(controller);
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

    public static boolean contemInstituicao(ObservableList<PreviewInstituicaoController> listaPreview,
    Instituicao instituicao) {
        for (PreviewInstituicaoController preview : listaPreview) {
            if (preview.getInstituicao().equals(instituicao)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Stage getStage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStage'");
    }

    @Override
    public void removerInstituicao(Instituicao instituicao) {
        if (contemInstituicao(listaControllersOrganizadores, instituicao)) {
            Iterator<PreviewInstituicaoController> iterator = listaControllersOrganizadores.iterator();

            while (iterator.hasNext()) {
                if (iterator.next().getInstituicao().equals(instituicao)) {
                    iterator.remove();
                }
            }
        } else {
            Iterator<PreviewInstituicaoController> iterator = listaControllersColaboradores.iterator();

            while (iterator.hasNext()) {
                if (iterator.next().getInstituicao().equals(instituicao)) {
                    iterator.remove();
                }
            }
        }
    }

    public void compararDatas() {

        // Impede que data posteriores á dataFinal sejam seleciondas no campo
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

    @Override
    public void adicionarArquivo(ServiceFile serviceFile) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adicionarArquivo'");
    }

    @Override
    public void removerArquivo(ServiceFile serviceFile) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removerArquivo'");
    }

    public void loadImagem(){
        InputStream fileAsStream;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Alterar foto do Grupo Evento");
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
