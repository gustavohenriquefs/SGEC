package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import com.casaculturaqxd.sgec.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class ConfiguracoesController {
    @FXML
    private VBox root;

    public void initialize() {
        try {
            loadMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu =
                new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }
}
