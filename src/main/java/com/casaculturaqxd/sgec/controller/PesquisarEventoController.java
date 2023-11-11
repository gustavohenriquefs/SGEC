package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.DAO.LocalizacaoDAO;
import com.casaculturaqxd.sgec.DAO.MetaDAO;
import com.casaculturaqxd.sgec.controller.preview.PreviewEventoController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Localizacao;
import com.casaculturaqxd.sgec.models.Meta;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class PesquisarEventoController {
    private EventoDAO eventoDAO = new EventoDAO();
    private LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
    private MetaDAO metaDAO = new MetaDAO(null);
    private final DatabasePostgres pesquisaConnection = DatabasePostgres.getInstance("URL_TEST","USER_NAME_TEST","PASSWORD_TEST");
    private ObservableMap<Evento, FXMLLoader> mapEventos = FXCollections.observableHashMap();;
    @FXML
    private VBox root;
    @FXML
    private DatePicker dataInicio, dataFim;
    @FXML
    private TextField nomeLocalizacao, textFieldPesquisa;
    @FXML
    private CheckBox acessivelLibras;
    @FXML
    private MenuButton opcoesMetas;
    @FXML
    private FlowPane campoResultados;
    @FXML
    private ScrollPane scrollResultados;

    public void initialize() throws IOException {
        loadMenu();
        dimensionarFlowPane();
        eventoDAO.setConnection(pesquisaConnection.getConnection());
        localizacaoDAO.setConnection(pesquisaConnection.getConnection());
        metaDAO.setConnection(pesquisaConnection.getConnection());
        addListenersEventos(mapEventos);
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu =
                new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }

    public void pesquisarEvento(){
        String nome = textFieldPesquisa.getText();
        String cidade = nomeLocalizacao.getText();
        Date dataInicial = null;
        Date dataFinal = null;
        
        if(dataInicio.getValue() != null){
            dataInicial = Date.valueOf(dataInicio.getValue());
        }
        if(dataFim.getValue() != null){
            dataFinal = Date.valueOf(dataFim.getValue());
        }
        
        ArrayList<Evento> eventos = eventoDAO.pesquisarEvento(nome, dataInicial, dataFinal);
        ArrayList<Localizacao> localizacaos = localizacaoDAO.pesquisarLocalizacao(cidade);
        ArrayList<Evento> eventosFinais = new ArrayList<>();
    
        if(nomeLocalizacao.getLength() != 0){
            for (Evento evento : eventos) {
                SortedSet<Integer> idsLocais = eventoDAO.buscarLocaisPorEvento(evento.getIdEvento());
                for (Localizacao local : localizacaos) {
                    if(idsLocais.contains((Integer)local.getIdLocalizacao())){
                        eventosFinais.add(evento);
                        break;
                    }
                }
            }
        }else{
            eventosFinais = eventos;
        }

        if(acessivelLibras.isSelected()){
            ArrayList<Evento> eventosFinaisLibras = new ArrayList<>();
            for (Evento evento : eventosFinais) {
                if(evento.isAcessivelEmLibras()){
                    eventosFinaisLibras.add(evento);
                }
            }
            eventosFinais = eventosFinaisLibras;
        }

        ArrayList<Integer> metasSelecionadas = new ArrayList<>();
        for (MenuItem menuItem : opcoesMetas.getItems()) {
            if (menuItem instanceof CheckMenuItem) {
                CheckMenuItem checkMenuItem = (CheckMenuItem) menuItem;
                if (checkMenuItem.isSelected()) {
                    Meta meta = metaDAO.getMetaPorNome(checkMenuItem.getText());
                    if(meta != null)
                        metasSelecionadas.add(meta.getIdMeta());
                } 
            }
        }

        if(!metasSelecionadas.isEmpty()){
            ArrayList<Evento> eventoTemp = new ArrayList<>();
            for (Evento evento : eventosFinais) {
                ArrayList<Meta> metas = metaDAO.listarMetasEvento(evento.getIdEvento());
                int cont = 0;
                if(!metas.isEmpty()){
                    for (Meta metaTemp : metas) {
                        if(metasSelecionadas.contains(metaTemp.getIdMeta())){
                            cont++;
                        }
                    }
                    if(cont == metasSelecionadas.size()){
                        eventoTemp.add(evento);
                    }
                }
            }
            eventosFinais = eventoTemp;
        }

        campoResultados.getChildren().clear();
        mapEventos.clear(); // limpar os resultados anteriores
        for(Evento  evento : eventosFinais){ //adicionar resultados na lista
            mapEventos.put(evento, new FXMLLoader(App.class.getResource("view/preview/previewEventoExistente.fxml")));
        }
    }

    public void addListenersEventos(ObservableMap<Evento, FXMLLoader> observablemap) {
        PesquisarEventoController superController = this;
    
        observablemap.addListener(new MapChangeListener<Evento, FXMLLoader>() {
            @Override
            public void onChanged(
                MapChangeListener.Change<? extends Evento, ? extends FXMLLoader> change) {
                    if (change.wasAdded()) {
                        Evento addedKey = change.getKey();
                        try {
                            Parent previewEvento = change.getValueAdded().load();
                            PreviewEventoController controller =
                                change.getValueAdded().getController();
                            controller.setEvento(addedKey);
                            controller.setParentController(superController);
                            campoResultados.getChildren().add(previewEvento);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    }
            }
        });  
    }

    private void dimensionarFlowPane(){
        scrollResultados.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            campoResultados.setPrefWidth(newValue.getWidth());
            campoResultados.setPrefHeight(newValue.getHeight());
        });
        campoResultados.prefHeightProperty().bind(scrollResultados.heightProperty());
    }

}
