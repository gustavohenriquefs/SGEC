package com.casaculturaqxd.sgec.controller.componentes;

import java.io.IOException;

import com.casaculturaqxd.sgec.App;

public class MenuController {

    public void goToHome() throws IOException{
        try {
            App.setRoot("view/home");       
        }
    }

    public void goToPesquisarEvento() throws IOException{
        App.setRoot("view/pesquisarEvento");
    }

    public void goToCadastrarEvento() throws IOException{
        App.setRoot("view/cadastrarEvento");
    }

    public void goToPesquisarGrupoEventos() throws IOException{
        App.setRoot("view/pesquisarGrupoEventos");
    }

    public void goToCadastrarGrupoEventos() throws IOException{
        App.setRoot("view/cadastrarGrupoEventos");
    }
}