package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.DAO.LocalizacaoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Localizacao;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class PesquisarEventoController {
    private EventoDAO eventoDAO = new EventoDAO();
    private LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
    private final DatabasePostgres pesquisaConnection = DatabasePostgres.getInstance("URL_TEST","USER_NAME_TEST","PASSWORD_TEST");
    @FXML
    private VBox root;
    @FXML
    private DatePicker dataInicio, dataFim;
    @FXML
    private TextField nomeLocalizacao, textFieldPesquisa;
    @FXML
    private CheckBox acessivelLibras;
    //@FXML
    //private FlowPane campoResultados;

    public void initialize() throws IOException {
        loadMenu();
        eventoDAO.setConnection(pesquisaConnection.getConnection());
        localizacaoDAO.setConnection(pesquisaConnection.getConnection());
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu =
                new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }

    public void pesquisarEvento(){
        String nome = textFieldPesquisa.getText();
        String cidade = nomeLocalizacao.getText();
        Date dataInicial = null;
        Date dataFinal = null;
        
        if(dataInicio.getValue() != null){
            dataInicial = Date.valueOf(dataInicio.getValue());
        }
        if(dataFim.getValue() != null){
            dataFinal = Date.valueOf(dataFim.getValue());
        }
        ArrayList<Evento> eventos = eventoDAO.pesquisarEvento(nome, dataInicial, dataFinal,acessivelLibras.isSelected());
        ArrayList<Localizacao> localizacaos = localizacaoDAO.pesquisarLocalizacao(cidade);
        ArrayList<Evento> eventosFinais = new ArrayList<>();
        for (Evento evento : eventos) {
            for (Localizacao localizacaoTemp : localizacaos) {
                if(localizacaoDAO.verificaLocalidade(evento.getIdEvento(), localizacaoTemp.getIdLocalizacao())){
                    System.out.println(localizacaoTemp.getIdLocalizacao() + " " + localizacaoTemp.getCidade());
                    System.out.println(evento.getIdEvento() + " " + evento.getNome());
                    eventosFinais.add(evento);
                }
            }
        }
        for (Evento evento : eventosFinais) {
            System.out.println("==========================");
            System.out.println(evento);
            System.out.println("==========================");
        }

    }

}
