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
    private final DatabasePostgres pesquisaConnection = DatabasePostgres.getInstance("URL","USER_NAME","PASSWORD");
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
        ArrayList<Evento> eventosFinais;
        String nome = textFieldPesquisa.getText();
        String cidade = nomeLocalizacao.getText();
        Date dataInicial = null;
        Date dataFinal = null;
        
        eventosFinais = filtroNomeDataEvento(nome, dataInicial, dataFinal);
    
        if(nomeLocalizacao.getLength() != 0){
            eventosFinais = filtroNomeCidade(eventosFinais, cidade);
        }

        if(acessivelLibras.isSelected()){
            eventosFinais = filtroLibras(eventosFinais);
        }

        ArrayList<Integer> metasSelecionadas = veridicaMetas();
        if(!metasSelecionadas.isEmpty()){
            eventosFinais = filtroMetas(eventosFinais, metasSelecionadas);
        }

        carregarEventos(eventosFinais);
    }

    private ArrayList<Evento> filtroNomeDataEvento(String nomeEvento, Date dataInicial, Date dataFinal){
        if(dataInicio.getValue() != null){
            dataInicial = Date.valueOf(dataInicio.getValue());
        }

        if(dataFim.getValue() != null){
            dataFinal = Date.valueOf(dataFim.getValue());
        }
        
        return eventoDAO.pesquisarEvento(nomeEvento, dataInicial, dataFinal);
    }

    private ArrayList<Evento> filtroNomeCidade(ArrayList<Evento> eventos, String nomeCidade){
        ArrayList<Localizacao> localizacaos = localizacaoDAO.pesquisarLocalizacao(nomeCidade);
        ArrayList<Evento> eventosTemp = new ArrayList<>();
        for (Evento evento : eventos) {
            SortedSet<Integer> idsLocais = eventoDAO.buscarLocaisPorEvento(evento.getIdEvento());
            for (Localizacao local : localizacaos) {
                if(idsLocais.contains((Integer)local.getIdLocalizacao())){
                    eventosTemp.add(evento);
                    break;
                }
            }
        }
        return eventosTemp;
    }

    private ArrayList<Evento> filtroLibras(ArrayList<Evento> eventos){
        ArrayList<Evento> eventosFinaisLibras = new ArrayList<>();
        for (Evento evento : eventos) {
            if(evento.isAcessivelEmLibras()){
                eventosFinaisLibras.add(evento);
            }
        }
        return eventosFinaisLibras;
    }

    private ArrayList<Integer> veridicaMetas(){
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
        return metasSelecionadas;
    } 

    private ArrayList<Evento> filtroMetas(ArrayList<Evento> eventos, ArrayList<Integer> metasSelecionadas){
        ArrayList<Evento> eventoTemp = new ArrayList<>();
        for (Evento evento : eventos) {
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
        return eventoTemp;
    }

    public void carregarEventos(ArrayList<Evento> eventos){
        campoResultados.getChildren().clear();
        mapEventos.clear();
        for(Evento  evento : eventos){
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
        });
        campoResultados.prefHeightProperty().bind(scrollResultados.heightProperty());
    }

}
