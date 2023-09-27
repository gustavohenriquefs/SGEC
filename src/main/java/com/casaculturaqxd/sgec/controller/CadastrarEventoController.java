package com.casaculturaqxd.sgec.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class CadastrarEventoController {
    @FXML 
    TextField publicoEsperado;
    @FXML
    TextField publicoAlcancado;
    @FXML
    TextField horas;
    @FXML
    TextField minutos;

    TextFormatter<String> numericalFormatter = new TextFormatter<>(change -> {
    if(change.getText().matches("\\d+")){
        return change;
    }
    else{
        change.setText(""); 

        return change;
    }
});
    public TextFormatter<String> getTimeFormatter(){
        return new TextFormatter<>(change -> {
            if(change.getText().matches("\\d+") && change.getRangeEnd() < 2){
                System.out.println(change);
                return change;
            }   
            else{
                change.setText(""); 
                return change;
            }
        });
    }
    

    public void initialize(){
        publicoEsperado.setTextFormatter(numericalFormatter);
        horas.setTextFormatter(getTimeFormatter());
        minutos.setTextFormatter(getTimeFormatter());
    }

}
