package com.casaculturaqxd.sgec.controller;

import com.casaculturaqxd.sgec.models.Evento;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
    TableView<String> tabelaIndicadoresGerais,tabelaIndicadoresMeta1,tabelaIndicadoresMeta2;

    public void initialize(){
    }

}
