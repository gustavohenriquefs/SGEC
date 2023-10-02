package com.casaculturaqxd.sgec.controller;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Indicador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class VisualizarEventoController {
    private Evento evento;
    
    @FXML
    AnchorPane secaoArquivos;
    @FXML 
    TextArea descricao;
    @FXML 
    Label titulo;
    //Tabelas de indicadores
    @FXML
    TableView<Indicador> tabelaIndicadoresGerais,tabelaIndicadoresMeta1,tabelaIndicadoresMeta2;
    ObservableList<TableView<Indicador>> tabelas = FXCollections.observableArrayList();
    
    @FXML
    Button novoParticipante,novoOrganizador,novoColaborador,salvarAlteracoes,adicionarArquivo,visualizarTodos;

    public void initialize(){
        setEvento(new Evento());
        titulo.setText(evento.getNome());
        
        Indicador numeroPublico = new Indicador("Quantidade de público", evento.getPublicoEsperado(), evento.getPublicoAlcancado());
        Indicador numeroMestres = new Indicador("Número de mestres da cultura", 0, 15);
        Indicador numeroMunicipios = new Indicador("Número de municípios", 3, 3);

        tabelas.addAll(tabelaIndicadoresGerais,tabelaIndicadoresMeta1,tabelaIndicadoresMeta2);
        for(TableView<Indicador> tabela : tabelas){
            loadTable(tabela);
        }
        

        addIndicador(tabelaIndicadoresGerais, numeroPublico);
        addIndicador(tabelaIndicadoresMeta1, numeroMestres);
        addIndicador(tabelaIndicadoresMeta2, numeroMunicipios);

        /* TODO: adicionar funcionalidade de alterar evento e
         *  reativar o botao
         */
        temporaryHideUnimplementedFields();
    }
    private void setEvento(Evento evento){
        this.evento = evento;
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
     * oculta e desabilita todas as funcionalidades nao implementadas 
     * TODO: remover o metodo apos arquivos serem implementados
     */
    public void temporaryHideUnimplementedFields(){
        for(Node node : secaoArquivos.getChildren()){
            node.setVisible(false);
        } 
    }
}
