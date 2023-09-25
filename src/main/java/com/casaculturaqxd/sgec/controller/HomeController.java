package com.casaculturaqxd.sgec.controller;

import java.io.IOException;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.models.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Alert.AlertType;

public class HomeController {
    
    private User usuario;

    @FXML
    Hyperlink pesquisar;
    @FXML
    Hyperlink cadastrarEvento;
    @FXML
    Hyperlink metas;

    Alert erroPermissao = new Alert(AlertType.WARNING);

    public void initialize(){
        
    }

    public void goToPesquisar() throws IOException{
        App.setRoot("view/pesquisar");
    }

    public void goToCadastrarEvento() throws IOException{
        if(usuario.isEditor()){
            App.setRoot("view/CriarEvento");
        }
        else{
            erroPermissao.setContentText("Você não tem permissão para criar novos eventos");
            erroPermissao.show();
        }
    }

    public void goToMetas() throws IOException{
        App.setRoot("view/metas");
    }
}
