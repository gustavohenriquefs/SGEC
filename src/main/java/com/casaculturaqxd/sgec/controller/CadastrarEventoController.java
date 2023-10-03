package com.casaculturaqxd.sgec.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.builder.EventoBuilder;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;


public class CadastrarEventoController {
    private final int MAX_LOCALIZACOES = 4;
    DatabasePostgres db = DatabasePostgres.getInstance("URL","USER_NAME","PASSWORD");
    EventoBuilder builderEvento = new EventoBuilder();
    EventoDAO eventoDAO = new EventoDAO();

    Stage stage;
    @FXML
    VBox Localizacoes, cargaHoraria;
    @FXML 
    HBox Participantes, Organizadores, Colaboradores;
    @FXML
    TextField titulo, publicoEsperado, publicoAlcancado, horas, minutos, horasCargaHoraria ,numParticipantesEsperado,numMunicipiosEsperado;
    @FXML
    TextArea descricao;
    @FXML 
    DatePicker dataInicial, dataFinal;
    @FXML
    ChoiceBox<String> classificacaoEtaria;
    private String[] classificacoes = {"Livre","10 anos","12 anos","14 anos","16 anos","18 anos"};
    @FXML
    CheckBox checkMeta1, checkMeta2, checkMeta3, checkMeta4;
    @FXML
    RadioButton certificavel, acessivelEmLibras;

    //Botoes
    @FXML
    Button botaoNovaLocalizacao, botaoOrganizadores, botaoColaboradores,
    botaoParticipantes, botaoArquivos, cancelar, criarEvento;

    public void initialize() throws IOException{
        adicionarCampoLocalizacao();
        classificacaoEtaria.getItems().addAll(classificacoes);
        addInputConstraints();

        showCargaHoraria(checkMeta3.isSelected());
        showCertificavel(checkMeta3.isSelected());
    }

    public void criarNovoEvento() throws IOException{
        builderEvento.resetar();
        eventoDAO.setConnection(db.getConnection());

            builderEvento.setNome(titulo.getText());
            builderEvento.setClassificacaoEtaria(classificacaoEtaria.getSelectionModel().getSelectedItem());
            builderEvento.setDataInicial( Date.valueOf(dataInicial.getValue()));
            builderEvento.setDataFinal(Date.valueOf(dataFinal.getValue()));
            if(!publicoEsperado.getText().isEmpty()){
            builderEvento.setPublicoEsperado(Integer.parseInt(publicoEsperado.getText()));
            }
            if(!publicoAlcancado.getText().isEmpty()){
            builderEvento.setPublicoAlcancado(Integer.parseInt(publicoAlcancado.getText()));    
            }
            builderEvento.setCertificavel(certificavel.isSelected());
            builderEvento.setAcessivelEmLibras(acessivelEmLibras.isSelected());
            if(!numMunicipiosEsperado.getText().isEmpty()){
                builderEvento.setMunicipiosEsperado(Integer.parseInt(numMunicipiosEsperado.getText()));
            }
            if(!numParticipantesEsperado.getText().isEmpty()){
            builderEvento.setParticipantesEsperado(Integer.parseInt(numParticipantesEsperado.getText()));
            }
        Evento novoEvento = builderEvento.getEvento();
        System.out.println(novoEvento);
        if(eventoDAO.inserirEvento(novoEvento)){
            novoEvento = eventoDAO.buscarEvento(novoEvento).get();
            App.setRoot("view/home");
        }

        if(emptyLocalizacoes()){
            Alert erroLocalizacao = new Alert(AlertType.ERROR, "Um evento deve possuir pelo menos uma localização associada");
            erroLocalizacao.show();
        }
        if(!camposObrigatoriosPreenchidos()){
            Alert erroLocalizacao = new Alert(AlertType.ERROR, "nem todos os campos obrigatorios foram preenchidos");
            erroLocalizacao.show();
            }
        }     

    public void cancelar() throws IOException{
        /** 
         * TODO: implementar uma fila na classe App, para retornar a ultima
         * tela visitada
         */
        builderEvento.resetar();
        App.setRoot("view/home");
    }

    public void adicionarCampoLocalizacao() throws IOException{
        if(Localizacoes.getChildren().size() >= MAX_LOCALIZACOES){
            botaoNovaLocalizacao.setDisable(true);
        }
        SubSceneLoader loaderLocais = new SubSceneLoader();
        GridPane novoLocal = (GridPane) loaderLocais.getPage("fields/fieldLocalizacao");
        Localizacoes.getChildren().add(novoLocal);
    }
    public void removerUltimaLocalizacao() throws IOException{
        int ultimoLocal = Localizacoes.getChildren().size();
        Localizacoes.getChildren().remove(ultimoLocal-1);
    }
    
    public void adicionarParticipante() throws IOException{
        SubSceneLoader loaderParticipantes = new SubSceneLoader();
        VBox novoParticipante = (VBox) loaderParticipantes.getPage("fields/fieldParticipante");
        Participantes.getChildren().add(novoParticipante);
    }
    public void adicionarOrganizador() throws IOException{
        //TODO: permitir adicionar uma instituicao existente, neste caso o preview dela
        // que e adicionado a pagina
        SubSceneLoader loaderOrganizadores = new SubSceneLoader();
        AnchorPane novoOrganizador = (AnchorPane) loaderOrganizadores.getPage("fields/fieldInstituicao");
        Organizadores.getChildren().add(novoOrganizador);

    }
    public void adicionarColaborador() throws IOException{
        //TODO: permitir adicionar uma instituicao existente, neste caso o preview dela
        // que e adicionado a pagina
        SubSceneLoader loaderColaboradores = new SubSceneLoader();
        AnchorPane novoColaborador = (AnchorPane) loaderColaboradores.getPage("fields/fieldInstituicao");
        Colaboradores.getChildren().add(novoColaborador);
    }
    public void adicionarArquivo(){
        FileChooser fileChooser = new FileChooser();
        File arquivoSelecionado = fileChooser.showOpenDialog(stage);
    }

    public boolean camposObrigatoriosPreenchidos(){
        if(classificacaoEtaria.getSelectionModel().getSelectedItem() == null
           || descricao.getText().isEmpty()
           || titulo.getText().isEmpty()
           ||  dataInicial.getValue() == null 
           ||dataFinal.getValue() == null){
            return false; 
        }
        return true;
    }
    public void addInputConstraints(){
        /* aplicando restrições aos inputs */
        horas.setTextFormatter(getTimeFormatter());
        minutos.setTextFormatter(getTimeFormatter());
        horasCargaHoraria.setTextFormatter(getTimeFormatter());
        publicoEsperado.setTextFormatter(getNumericalFormatter());
        publicoAlcancado.setTextFormatter(getNumericalFormatter());
        numParticipantesEsperado.setTextFormatter(getNumericalFormatter());
        numMunicipiosEsperado.setTextFormatter(getNumericalFormatter());

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
        cargaHoraria.setVisible(value);
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
    public boolean emptyLocalizacoes(){
        return Localizacoes.getChildren().isEmpty();
    }
}
