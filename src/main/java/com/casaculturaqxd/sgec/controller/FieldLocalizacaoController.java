package com.casaculturaqxd.sgec.controller;


import java.io.IOException;
import java.net.URL;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.models.Localizacao;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;


public class FieldLocalizacaoController {
    @FXML
    Button remover; 
    @FXML 
    TextField rua, bairro, numero, cidade, cep, estado, pais;

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

    public void remover(){
      
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
        return novoLocal;
    }

}
