package com.casaculturaqxd.sgec.controller.componentes;

import java.io.IOException;

import com.casaculturaqxd.sgec.App;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MenuController {
    @FXML
    Label campoNomeUsuario;

    public void initialize() {
        campoNomeUsuario.setText(App.getUsuario().getNomeUsuario());
    }

    @FXML
    public void goToHome() throws IOException {
        App.setRoot("view/home");

    }

    @FXML
    public void goToPesquisarEvento() throws IOException {
        App.setRoot("view/pesquisarEvento");
    }

    @FXML
    public void goToCadastrarEvento() throws IOException {
        if (App.getUsuario().isEditor())
            App.setRoot("view/cadastrarEvento");
    }

    @FXML
    public void goToPesquisarGrupoEventos() throws IOException {
        App.setRoot("view/pesquisarGrupoEventos");
    }

    @FXML
    public void goToCadastrarGrupoEventos() throws IOException {
        if (App.getUsuario().isEditor())
            App.setRoot("view/cadastrarGrupoEvento");
    }

    @FXML
    public void backTela() throws IOException {
        if (!App.lastVisitedPages.empty()
                && !App.lastVisitedPages.lastElement().getId().equals(App.getRoot().getId()))

            App.backLastScreen();
    }

    @FXML
    public void goToConfiguracoes() throws IOException {
        if (App.getUsuario() != null)
            App.setRoot("view/configuracoes");
    }

    @FXML
    public void logout() throws IOException {
        App.logout();
    }

}
