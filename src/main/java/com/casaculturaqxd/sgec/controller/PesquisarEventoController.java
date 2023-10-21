package com.casaculturaqxd.sgec.controller;

import java.io.IOException;

import com.casaculturaqxd.sgec.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PesquisarEventoController {
    @FXML
    private VBox root;
    @FXML
    private HBox header;

    public void initialize() throws IOException{
        loadMenu();
    }

    private void loadMenu() throws IOException{
        FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0,carregarMenu.load());
    }

}
