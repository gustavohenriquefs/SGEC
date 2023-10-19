package com.casaculturaqxd.sgec.controller;

import java.io.IOException;

import com.casaculturaqxd.sgec.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class PesquisarEventoController {
    @FXML
    AnchorPane root;
    @FXML
    HBox header;

    public void initialize() throws IOException{
        loadMenu();
    }

    private void loadMenu() throws IOException{
        FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(1,carregarMenu.load());
    }





































    


}
