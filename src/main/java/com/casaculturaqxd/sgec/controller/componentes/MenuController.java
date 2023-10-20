package com.casaculturaqxd.sgec.controller.componentes;

import java.io.IOException;

import com.casaculturaqxd.sgec.App;

public class MenuController {

    public void goToHome() throws IOException{
        try {
            App.setRoot("view/home");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void goToPesquisarEvento() throws IOException{
        App.setRoot("view/pesquisarEvento");
    }

    public void goToCadastrarEvento() throws IOException{
        if(App.getUsuario().isEditor())
            App.setRoot("view/cadastrarEvento");
    }

    public void goToPesquisarGrupoEventos() throws IOException{
        App.setRoot("view/pesquisarGrupoEventos");
    }

    public void goToCadastrarGrupoEventos() throws IOException{
        if(App.getUsuario().isEditor())
            App.setRoot("view/cadastrarGrupoEvento");
    }

    public void backTela() throws IOException{
        if(!App.lastVisitedPages.empty())
            App.lastVisitedPages.pop();
        if(!App.lastVisitedPages.empty())
            App.setRoot(App.lastVisitedPages.lastElement()); 
    }

    public void goToConfiguracoes() throws IOException{
        if(App.getUsuario() != null)
            App.setRoot("view/configuracoes");
    }

    public void logout() throws IOException{
        App.setUsuario(null);
        App.setRoot("view/login");
    }

}
