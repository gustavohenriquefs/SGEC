package com.casaculturaqxd.sgec.controller.componentes;

import java.io.FileNotFoundException;
import java.net.URL;

import com.casaculturaqxd.sgec.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MenuController {

    @FXML
    VBox vboxmenu;

    private FXMLLoader sceneLoader;

    public void initialize() throws FileNotFoundException{
        
        String menu = "menu";
        URL pagePath = App.class.getResource("view/" + menu + ".fxml");
        if (pagePath == null) {
            throw new FileNotFoundException(menu + ".fxml nao encontrado");
        }
        sceneLoader = new FXMLLoader(pagePath);
        try {
            Pane header = sceneLoader.load();
            vboxmenu.getChildren().add(header);
        } catch (Exception e) {
            e.printStackTrace();
            // Lidar com exceções, se necessário
        }
    } 
}
