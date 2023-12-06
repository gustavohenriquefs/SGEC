package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.GrupoEventosDAO;
import com.casaculturaqxd.sgec.DAO.MetaDAO;
import com.casaculturaqxd.sgec.controller.preview.PreviewGrupoEventoController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Meta;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class pesquisarGrupoEventosController {
    DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    private MetaDAO metaDAO = new MetaDAO(db.getConnection());
    private GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
    @FXML
    private VBox root;
    @FXML
    private MenuButton opcoesMetas;
    @FXML
    DatePicker dataInicio, dataFim;
    @FXML
    TextField nomeGrupoEventos;
    @FXML
    ChoiceBox<String> classificacaoEtaria;
    @FXML
    FlowPane secaoResultados;
    @FXML
    ScrollPane campoResultados;
    @FXML
    Label exibirResultados;

    private final String[] classificacoes = { "Livre", "10 anos", "12 anos", "14 anos", "16 anos", "18 anos" };
    ObservableList<PreviewGrupoEventoController> listaPreviewGrupoEventoControllers = FXCollections.observableArrayList();


    public void initialize() throws IOException {
        loadMenu();
        classificacaoEtaria.getItems().addAll(classificacoes);
        addListenersParticipante(listaPreviewGrupoEventoControllers);
        dimensionarFlowPane();
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu =
                new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }

    public void pesquisarGrupoEvento(){
        ArrayList<GrupoEventos> grupoEventosFinais = new ArrayList<>();
        String nome = nomeGrupoEventos.getText();
        Date dataInicial = null;
        Date dataFinal = null;

        grupoEventosFinais = filtroNomeDataGrupoEvento(nome, dataInicial, dataFinal);
        grupoEventosFinais = filtroFaixa(grupoEventosFinais);

        ArrayList<Integer> metasSelecionadas = verificaMetas();
        if (!metasSelecionadas.isEmpty() && !grupoEventosFinais.isEmpty()) {
            grupoEventosFinais = filtroMetas(grupoEventosFinais, metasSelecionadas);
        }
        
        secaoResultados.getChildren().clear();
        listaPreviewGrupoEventoControllers.clear();
        loadGrupoEventos(grupoEventosFinais);

    }

    private void dimensionarFlowPane() {
        campoResultados.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            campoResultados.setPrefWidth(newValue.getWidth());
        });
        campoResultados.prefHeightProperty().bind(campoResultados.heightProperty());
    }

    private void loadGrupoEventos(ArrayList<GrupoEventos> grupoEventosFinais){
        secaoResultados.setVisible(true);
        campoResultados.setVisible(true);
        exibirResultados.setVisible(true);
        for (GrupoEventos grupoEventos : grupoEventosFinais) {
            FXMLLoader loaderInstituicao = new FXMLLoader(
                    App.class.getResource("view/preview/previewGrupoEvento.fxml"));
            try {
                loaderInstituicao.load();
                PreviewGrupoEventoController controller = loaderInstituicao.getController();
                controller.setGrupoEventos(grupoEventos);
                listaPreviewGrupoEventoControllers.add(controller);
            } catch (IOException e) {
                Alert alert = new Alert(AlertType.WARNING, "falha carregando organizador");
                alert.show();
            }
        }
        
    }

    private ArrayList<GrupoEventos> filtroMetas(ArrayList<GrupoEventos> grupoEventosFinais, ArrayList<Integer> metasSelecionadas) {
        Iterator<GrupoEventos> iterator = grupoEventosFinais.iterator();
        while (iterator.hasNext()) {
            GrupoEventos grupoEventos = iterator.next();
            ArrayList<Integer> metasIds = grupoEventosDAO.pesquisarGrupoMetas(grupoEventos.getIdGrupoEventos());
            for (Integer id : metasSelecionadas) {
                if(!metasIds.contains(id)){
                    iterator.remove();
                    break;
                }
            }
        }
        return grupoEventosFinais;
    }

    private ArrayList<GrupoEventos> filtroNomeDataGrupoEvento(String nome, Date dataInicial, Date dataFinal) {
        if (dataInicio.getValue() != null) {
            dataInicial = Date.valueOf(dataInicio.getValue());
        }

        if (dataFim.getValue() != null) {
            dataFinal = Date.valueOf(dataFim.getValue());
        }

        return grupoEventosDAO.pesquisarGrupoEventos(nome, dataInicial, dataFinal);
    }

    private ArrayList<GrupoEventos> filtroFaixa(ArrayList<GrupoEventos> grupoEventosFinais){
        String escolhaFaixa = classificacaoEtaria.getSelectionModel().getSelectedItem();
        if(escolhaFaixa != null){
            Iterator<GrupoEventos> iterator = grupoEventosFinais.iterator();

            while (iterator.hasNext()) {
                GrupoEventos grupoEventos = iterator.next();
                if (grupoEventos.getClassificacaoEtaria() == null || !grupoEventos.getClassificacaoEtaria().equals(escolhaFaixa)) {
                    iterator.remove();
                }
            }
        }

        return grupoEventosFinais;
    }

    private ArrayList<Integer> verificaMetas() {
        ArrayList<Integer> metasSelecionadas = new ArrayList<>();
        for (MenuItem menuItem : opcoesMetas.getItems()) {
            if (menuItem instanceof CheckMenuItem) {
                CheckMenuItem checkMenuItem = (CheckMenuItem) menuItem;
                if (checkMenuItem.isSelected()) {
                    Optional<Meta> metaResult;
                    try {
                        metaResult = metaDAO.getMeta(checkMenuItem.getText());
                    } catch (SQLException e) {
                        metaResult = Optional.empty();
                    }
                    if (metaResult.isPresent()) {
                        metasSelecionadas.add(metaResult.get().getIdMeta());
                    }
                }
            }
        }
        return metasSelecionadas;
    }

    public void addListenersParticipante(ObservableList<PreviewGrupoEventoController> observableList) {
        pesquisarGrupoEventosController superController = this;
        observableList.addListener((ListChangeListener<PreviewGrupoEventoController>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (PreviewGrupoEventoController addedController : change.getAddedSubList()) {
                        addedController.setParentController(superController);
                        secaoResultados.getChildren().add(addedController.getContainer());
                    }
                }
        }
      });
    }

}
