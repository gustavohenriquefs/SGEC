package com.casaculturaqxd.sgec.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

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
    @FXML
    ChoiceBox<String> classificacaoEtaria;
    private String[] classificacoes = {"Livre","10 anos","12 anos","14 anos","16 anos","18 anos"};
    @FXML
    CheckBox checkMeta1;
    @FXML
    CheckBox checkMeta2;
    @FXML
    CheckBox checkMeta3;
    @FXML
    CheckBox checkMeta4;

    //Botoes
    @FXML
    Button adicionarLocalizacao;
    @FXML
    Button botaoOrganizadores;
    @FXML
    Button botaoColaboradores;
    @FXML
    Button botaoParticipantes;
    @FXML
    Button botaoArquivos;
    @FXML
    Button cancelar;
    @FXML
    Button criarEvento;

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
        /* aplicando restrições aos inputs */
        classificacaoEtaria.getItems().addAll(classificacoes);
        publicoEsperado.setTextFormatter(getNumericalFormatter());
        publicoAlcancado.setTextFormatter(getNumericalFormatter());
        horas.setTextFormatter(getTimeFormatter());
        minutos.setTextFormatter(getTimeFormatter());
    }

    public void addLocalizacao(Localizacao local){
        locais.add(local);
    }
}
