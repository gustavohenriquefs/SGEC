package com.casaculturaqxd.sgec.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class CadastrarEventoController {
    private final int MAX_LOCALIZACOES = 4;

    Stage stage;
    @FXML
    VBox Localizacoes;
    @FXML HBox Participantes;
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

    public void initialize(){
        /* aplicando restrições aos inputs */
        classificacaoEtaria.getItems().addAll(classificacoes);
        publicoEsperado.setTextFormatter(getNumericalFormatter());
        publicoAlcancado.setTextFormatter(getNumericalFormatter());
        

        showCertificavel(checkMeta3.isSelected());
        /* desabilitando o botao de arquivo enquanto nao eh implementado*/
        botaoArquivos.setDisable(true);
        botaoArquivos.setVisible(false);
    }

    public void criarNovoEvento(){
    }

    public void adicionarLocalizacao() throws IOException{
        if(Localizacoes.getChildren().size() >= MAX_LOCALIZACOES){
            botaoNovaLocalizacao.setDisable(true);
        }
        SubSceneLoader loaderLocais = new SubSceneLoader();
        GridPane novoLocal = (GridPane) loaderLocais.getPage("fields/fieldLocalizacao");
        Localizacoes.getChildren().add(novoLocal);
    }

    public void adicionarParticipante() throws IOException{
        SubSceneLoader loaderParticipantes = new SubSceneLoader();
        AnchorPane novoParticipante = (AnchorPane) loaderParticipantes.getPage("fields/fieldParticipante");
        Participantes.getChildren().add(novoParticipante);
    }
    public void adicionarOrganizador(){
        
    }
    public void adicionarColaborador(){

    }
    public void adicionarArquivo(){
        FileChooser fileChooser = new FileChooser();
        File arquivoSelecionado = fileChooser.showOpenDialog(stage);
    }

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

    public void showCargaHoraria(boolean value){
        
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
