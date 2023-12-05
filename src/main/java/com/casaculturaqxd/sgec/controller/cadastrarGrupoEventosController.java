package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.controller.dialog.DialogNovaInstituicao;
import com.casaculturaqxd.sgec.controller.preview.PreviewEventoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewInstituicaoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewParticipanteController;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Localizacao;
import com.casaculturaqxd.sgec.models.Participante;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CadastrarGrupoEventosController implements ControllerServiceFile, ControllerEvento {
    @FXML
    private VBox root;
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

    ObservableList<PreviewInstituicaoController> listaControllersOrganizadores = FXCollections.<PreviewInstituicaoController>observableList(new ArrayList<>()), 
            listaControllersColaboradores = FXCollections.<PreviewInstituicaoController>observableList(new ArrayList<>());
    ObservableList<PreviewEventoController> listaControllersEventos = FXCollections
            .<PreviewEventoController>observableList(new ArrayList<>());

    private Alert mensagem = new Alert(AlertType.NONE);

    public void initialize() throws IOException {
        loadMenu();
        classificacaoEtaria.getItems().addAll(classificacoes);
        addListenersColaborador(listaControllersColaboradores);
        addListenersOrganizador(listaControllersOrganizadores);
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu =
                new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
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

}
