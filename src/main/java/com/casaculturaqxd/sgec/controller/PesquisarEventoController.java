package com.casaculturaqxd.sgec.controller;

import java.io.IOException;

import com.casaculturaqxd.sgec.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class PesquisarEventoController {
    @FXML
    HBox header;

    public void initialize() throws IOException{
        FXMLLoader loaderMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        header.getChildren().add(loaderMenu.load());
    }


}
