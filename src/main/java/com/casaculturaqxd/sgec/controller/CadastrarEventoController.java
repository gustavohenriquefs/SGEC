package com.casaculturaqxd.sgec.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class CadastrarEventoController {
    Stage stage;

    @FXML 
    TextField publicoEsperado;
    @FXML
    TextField publicoAlcancado;
    @FXML 
    Label textoHoras;
    @FXML
    TextField horas;
    @FXML
    Label textoMinutos;
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
    @FXML
    RadioButton certificavel;
    //Botoes
    @FXML
    Button botaoNovaLocalizacao;
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

        showCargaHoraria(checkMeta3.isSelected());
        showCertificavel(checkMeta3.isSelected());
    }

    public void criarNovoEvento(){
    }

    public void adicionarLocalizacao(){
    }

    public void adicionarParticipante(){

    }
    public void adicionarOrganizador(){
        
    }
    public void adicionarColaborador(){

    }
    public void adicionarArquivo(){
        FileChooser fileChooser = new FileChooser();
        File arquivoSelecionado = fileChooser.showOpenDialog(stage);
    }

    public void showCargaHoraria(boolean value){
        if(value == false){
            horas.clear();
            minutos.clear();
        }
        horas.setVisible(value);
        textoHoras.setVisible(value);
        minutos.setVisible(value);
        textoMinutos.setVisible(value);
    }
    public void showCertificavel(boolean value){
        if(value == false){
            certificavel.setSelected(value);
        }
        certificavel.setVisible(value);
    }
    public void onClickMeta3(){
        showCargaHoraria(checkMeta3.isSelected());
        showCertificavel(checkMeta3.isSelected());
    }
}
