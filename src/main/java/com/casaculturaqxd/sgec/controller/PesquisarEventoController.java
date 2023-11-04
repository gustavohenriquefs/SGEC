package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class PesquisarEventoController {
    MenuButton metas;
    DatePicker dataInicio, dataFim;
    TextField nomeLocalizacao, textFieldPesquisa;
    FlowPane campoResultados;
    private EventoDAO eventoDAO = new EventoDAO();
    private final DatabasePostgres pesquisaConnection = DatabasePostgres.getInstance("URL_TEST","USER_NAME_TEST","PASSWORD_TEST");
    @FXML
    private VBox root;

    public void initialize() throws IOException {
        loadMenu();
        eventoDAO.setConnection(pesquisaConnection.getConnection());
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu =
                new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }

    public void pesquisarEvento(){
        String nome = "";
        Date dataInicial = Date.valueOf(LocalDate.of(2023, 12, 15));
        Date dataFinal = Date.valueOf(LocalDate.of(2024, 02, 29));
        ArrayList<Evento> eventos = eventoDAO.pesquisarEvento(nome, null, dataFinal);
        for (Evento evento : eventos) {
            System.out.println(evento);
        }
    }

}
