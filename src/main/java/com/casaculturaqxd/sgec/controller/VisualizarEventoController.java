package com.casaculturaqxd.sgec.controller;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Indicador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class VisualizarEventoController {
    private Evento evento;
    
    @FXML
    ImageView capa;
    @FXML 
    TextArea descricao;
    @FXML 
    Label titulo;
    //Tabelas de indicadores
    @FXML
    TableView<Indicador> tabelaIndicadoresGerais,tabelaIndicadoresMeta1,tabelaIndicadoresMeta2;
    ObservableList<TableView<Indicador>> tabelas = FXCollections.observableArrayList();
    
    public void initialize(){
        Indicador numeroPublico = new Indicador("Quantidade de público", 0, 100);
        Indicador numeroMestres = new Indicador("Número de mestres da cultura", 0, 15);
        Indicador numeroMunicipios = new Indicador("Número de municípios", 3, 3);

        tabelas.addAll(tabelaIndicadoresGerais,tabelaIndicadoresMeta1,tabelaIndicadoresMeta2);
        for(TableView<Indicador> tabela : tabelas){
            loadTable(tabela);
        }
        

        addIndicador(tabelaIndicadoresGerais, numeroPublico);
        addIndicador(tabelaIndicadoresMeta1, numeroMestres);
        addIndicador(tabelaIndicadoresMeta2, numeroMunicipios);
    }


    private void loadTable(TableView<Indicador> tabela){
        TableColumn<Indicador,String> nomeIndicador = new TableColumn<>("Nome do indicador");
        nomeIndicador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        TableColumn<Indicador,Integer> valorEsperado = new TableColumn<>("Valor esperado");
        valorEsperado.setCellValueFactory(new PropertyValueFactory<>("valorEsperado"));
        
        TableColumn<Indicador,Integer> valorAlcancado = new TableColumn<>("Valor esperado");
        valorAlcancado.setCellValueFactory(new PropertyValueFactory<>("valorEsperado"));
        tabela.getColumns().add(nomeIndicador);
        tabela.getColumns().add(valorEsperado);
        tabela.getColumns().add(valorAlcancado);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }
    private void addIndicador(TableView<Indicador> tabela, Indicador indicador){
        tabela.setItems(FXCollections.observableArrayList(indicador));
    }
}
