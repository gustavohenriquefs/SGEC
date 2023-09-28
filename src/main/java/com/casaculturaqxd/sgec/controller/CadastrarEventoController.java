package com.casaculturaqxd.sgec.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import com.casaculturaqxd.sgec.models.Localizacao;

public class CadastrarEventoController {
    @FXML 
    TextField publicoEsperado;
    @FXML
    TextField publicoAlcancado;
    @FXML
    TextField horas;
    @FXML
    TextField minutos;
    /*    
    @FXML

    @FXML

    @FXML

    @FXML

    @FXML

    @FXML

    @FXML

    @FXML
    */
    ObservableList<Localizacao> locais;
    public TextFormatter<String> getNumericalFormatter(){
        return new TextFormatter<>(change -> {
            if(change.getText().matches("\\d+")){
                return change;
            }
            else{
                change.setText(""); 

                return change;
            }
        });
    }
    public TextFormatter<String> getTimeFormatter(){
        return new TextFormatter<>(change -> {
            if(change.getText().matches("\\d+") && change.getRangeEnd() < 2){
                return change;
            }   
            else{
                change.setText(""); 
                return change;
            }
        });
    }
    

    public void initialize(){
        publicoEsperado.setTextFormatter(getNumericalFormatter());
        horas.setTextFormatter(getTimeFormatter());
        minutos.setTextFormatter(getTimeFormatter());
    }

    public void addLocalizacao(Localizacao local){
        locais.add(local);
    }
}
