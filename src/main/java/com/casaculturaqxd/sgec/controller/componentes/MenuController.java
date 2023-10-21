package com.casaculturaqxd.sgec.controller.componentes;

import java.io.IOException;

import com.casaculturaqxd.sgec.App;

import javafx.fxml.FXML;

public class MenuController {

    @FXML
    public void goToHome() throws IOException{
        App.setRoot("view/home");
    
    }

    @FXML
    public void goToPesquisarEvento() throws IOException{
        App.setRoot("view/pesquisarEvento");
    }

    @FXML
    public void goToCadastrarEvento() throws IOException{
        if(App.getUsuario().isEditor())
            App.setRoot("view/cadastrarEvento");
    }

    @FXML
    public void goToPesquisarGrupoEventos() throws IOException{
        App.setRoot("view/pesquisarGrupoEventos");
    }

    @FXML
    public void goToCadastrarGrupoEventos() throws IOException{
        //desabilitado por enquanto
        //if(App.getUsuario().isEditor())
            //App.setRoot("view/cadastrarGrupoEvento");
    }

    @FXML
    public void backTela() throws IOException{
        if(App.lastVisitedPages.empty() == false)
            App.lastVisitedPages.pop();
        if(App.lastVisitedPages.empty() == false)
            App.setRoot(App.lastVisitedPages.lastElement()); 
    }

    @FXML
    public void goToConfiguracoes() throws IOException{
        if(App.getUsuario() != null)
            App.setRoot("view/configuracoes");
    }

    @FXML
    public void logout() throws IOException{
        App.setUsuario(null);
        App.setRoot("view/login");
    }

}
