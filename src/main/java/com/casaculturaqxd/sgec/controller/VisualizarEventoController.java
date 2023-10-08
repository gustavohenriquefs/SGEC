package com.casaculturaqxd.sgec.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Indicador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class VisualizarEventoController {
    private Evento evento;
    private DatabasePostgres db = DatabasePostgres.getInstance("URL","USER_NAME","PASSWORD");
    private EventoDAO eventoDAO = new EventoDAO();; 
    @FXML
    AnchorPane page;
    @FXML
    AnchorPane headerField;
    @FXML
    AnchorPane secaoArquivos;
    @FXML 
    TextArea descricao;
    @FXML 
    Label titulo;
    @FXML 
    DatePicker dataInicial, dataFinal;
    @FXML
    TextField cargaHoraria;
    @FXML 
    CheckBox certificavel, libras;
    @FXML
    ChoiceBox<String> classificacaoEtaria;
    @FXML
    private String[] classificacoes = {"Livre","10 anos","12 anos","14 anos","16 anos","18 anos"};
    //Tabelas de indicadores
    @FXML
    TableView<Indicador> tabelaIndicadoresGerais,tabelaIndicadoresMeta1,tabelaIndicadoresMeta2;
    ObservableList<TableView<Indicador>> tabelas = FXCollections.observableArrayList();
    
    @FXML
    Button novoParticipante,novoOrganizador,novoColaborador,salvarAlteracoes,adicionarArquivo,visualizarTodos;

    public void initialize() throws IOException{
        System.out.println(getAllNodes(page));
        FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        headerField.getChildren().add(carregarMenu.load());

        eventoDAO.setConnection(db.getConnection());
        Evento eventoMock = new Evento();
        //capturando evento de mock do banco
        eventoMock.setIdEvento(1);
        setEvento(eventoDAO.buscarEvento(eventoMock).get());
        loadContent();

        tabelas.addAll(tabelaIndicadoresGerais,tabelaIndicadoresMeta1,tabelaIndicadoresMeta2);
        for(TableView<Indicador> tabela : tabelas){
            loadTable(tabela);
        }
        Indicador numeroPublico = new Indicador("Quantidade de público", evento.getPublicoEsperado(), evento.getPublicoAlcancado());
        Indicador numeroMestres = new Indicador("Número de mestres da cultura", evento.getParticipantesEsperado(), evento.getListaParticipantes().size());
        Indicador numeroMunicipios = new Indicador("Número de municípios", evento.getMunicipiosEsperado(), eventoDAO.getNumeroMunicipiosDiferentes(evento.getIdEvento()));

        addIndicador(tabelaIndicadoresGerais, numeroPublico);
        addIndicador(tabelaIndicadoresMeta1, numeroMestres);
        addIndicador(tabelaIndicadoresMeta2, numeroMunicipios);

        /* TODO: adicionar funcionalidade de alterar evento e
         *  reativar o botao
         */
        temporaryHideUnimplementedFields();
    }
    private void loadContent(){
        classificacaoEtaria.getItems().addAll(classificacoes);
        titulo.setText(evento.getNome());
        descricao.setText(evento.getDescricao());
        classificacaoEtaria.getSelectionModel().select(evento.getClassificacaoEtaria());
        if(evento.getDataInicial() != null){
        dataInicial.setValue(evento.getDataInicial().toLocalDate());
        }
        if(evento.getDataFinal() != null){
        dataFinal.setValue(evento.getDataFinal().toLocalDate());
        }
        if(evento.getCargaHoraria() != null){
        cargaHoraria.setText(evento.getCargaHoraria().toString());
        }
        certificavel.setSelected(evento.isCertificavel());
        libras.setSelected(evento.isAcessivelEmLibras());
    }

    private void setEvento(Evento evento){
        this.evento = evento;
    }
    public boolean salvarAlteracoes(){
        return eventoDAO.alterarEvento(evento);
    }
    private void loadTable(TableView<Indicador> tabela){
        TableColumn<Indicador,String> nomeIndicador = new TableColumn<>("Nome do indicador");
        nomeIndicador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        TableColumn<Indicador,Integer> valorEsperado = new TableColumn<>("Valor esperado");
        valorEsperado.setCellValueFactory(new PropertyValueFactory<>("valorEsperado"));
        
        TableColumn<Indicador,Integer> valorAlcancado = new TableColumn<>("Valor alcançado");
        valorAlcancado.setCellValueFactory(new PropertyValueFactory<>("valorAlcancado"));

        tabela.getColumns().add(nomeIndicador);
        tabela.getColumns().add(valorEsperado);
        tabela.getColumns().add(valorAlcancado);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }
    private void addIndicador(TableView<Indicador> tabela, Indicador indicador){
        tabela.setItems(FXCollections.observableArrayList(indicador));
    }

    /**
     * <p>
     * Retorna todos os elementos que suportam interacao do usuario 
     * presentes na pagina <p>
    */
    public static ArrayList<Node> getAllNodes(Parent root) {
    ArrayList<Node> nodes = new ArrayList<>();
    addAllDescendents(root, nodes);
    return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if(!(node instanceof Label || node instanceof Button || node instanceof Pane)){
            nodes.add(node);
            }
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }
    
    /** 
     * oculta e desabilita todas as funcionalidades nao implementadas 
     * TODO: remover o metodo apos arquivos serem implementados
     */
    private void temporaryHideUnimplementedFields(){
        for(Node node : secaoArquivos.getChildren()){
            node.setVisible(false);
        } 
    }
}
