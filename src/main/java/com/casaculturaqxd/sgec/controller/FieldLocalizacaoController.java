package com.casaculturaqxd.sgec.controller;



import java.io.IOException;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.LocalizacaoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Localizacao;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;


public class FieldLocalizacaoController {
    DatabasePostgres db = DatabasePostgres.getInstance("URL","USER_NAME","PASSWORD");
    @FXML
    GridPane paneLocalizacoes;
    @FXML
    Button botaoRemover; 
    @FXML 
    TextField rua, bairro, numero, cidade, cep, estado, pais;

    // EventoController parentController;

    Alert campoFaltando = new Alert(AlertType.WARNING);

    public void initialize(){
        pais.setText("Brasil");
        cep.setTextFormatter(new TextFormatter<>(change -> {
            if(change.getText().matches("\\d+") && change.getRangeEnd() < 9){
                if(change.getRangeEnd() == 5){
                    change.setText("-"); 
                }
                return change;
            }

            else{
                change.setText(""); 
                return change;
            }
        }));
    }

    public void remover() throws IOException{
      FXMLLoader loaderSuperScene = new FXMLLoader(App.class.getResource("view/cadastrarEvento.fxml"));
    }

    public Localizacao getLocalizacao(){
        Localizacao novoLocal = new Localizacao();
        if(rua.getText() == null
            ||cidade.getText() == null
            || estado.getText() == null
            || pais.getText() == null){
                campoFaltando.show();
            }
            else{
            novoLocal.setRua(rua.getText());
            novoLocal.setBairro(bairro.getText());
            if(!numero.getText().isEmpty()){
            novoLocal.setNumeroRua(Integer.parseInt(numero.getText()));
            }
            novoLocal.setCep(cep.getText());
            novoLocal.setCidade(cidade.getText());
            novoLocal.setEstado(estado.getText());
            novoLocal.setPais(pais.getText());
        }
        System.out.println(novoLocal);
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
        localizacaoDAO.setConnection(db.getConnection());
        if(localizacaoDAO.inserirLocalizacao(novoLocal)){
            return novoLocal;
        }
        return null;
    }

}
