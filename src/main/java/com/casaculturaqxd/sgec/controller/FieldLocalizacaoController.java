package com.casaculturaqxd.sgec.controller;

import java.io.IOException;

import org.controlsfx.control.textfield.TextFields;

import com.casaculturaqxd.sgec.DAO.LocalizacaoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Localizacao;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;


public class FieldLocalizacaoController {
    DatabasePostgres db = DatabasePostgres.getInstance("URL","USER_NAME","PASSWORD");
    @FXML
    Parent paneLocalizacoes;
    @FXML
    Button botaoRemover; 
    @FXML 
    TextField fdNomeLocal, rua, bairro, numero, cidade, cep, estado, pais;

    ControllerEvento parentController;

    Alert campoFaltando = new Alert(AlertType.WARNING);

    Localizacao localizacao = new Localizacao();

    public Parent getPaneLocalizacao() {
        return paneLocalizacoes;
    }

    public void setPaneLocalizacao(Parent paneLocalizacao) {
        this.paneLocalizacoes = paneLocalizacao;
    }

    public void initialize() {
        pais.setText("Brasil");
        
        cep.setTextFormatter(new TextFormatter<>(change -> {
            System.out.println(change.getText());
            if(change.getText().matches("\\d+") && change.getRangeEnd() < 9){
                if(change.getRangeEnd() == 5){
                    change.setText("-"); 
                }
                return change;
            } else{
                change.setText(""); 
                return change;
            }
        }));
        
        
        this.initAutoComplete();
        this.completeLocalizacaoOnEnterLocalizacaoFd();
    }

    private void completeLocalizacaoOnEnterLocalizacaoFd() {
        fdNomeLocal.setOnKeyReleased(event -> {
            if(event.getCode().toString().equals("ENTER")){
                this.completeLocaleData(fdNomeLocal.getText());
            }
        });
    }

    private void completeLocaleData(String localName) {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
        localizacaoDAO.setConnection(db.getConnection());

        Localizacao localizacao = localizacaoDAO.getLocalizacaoByNome(fdNomeLocal.getText().trim());

        if(localizacao != null){
            rua.setText(localizacao.getRua());
            bairro.setText(localizacao.getBairro());
            numero.setText(String.valueOf(localizacao.getNumeroRua()));
            cep.setText(localizacao.getCep());
            cidade.setText(localizacao.getCidade());
            estado.setText(localizacao.getEstado());
            pais.setText(localizacao.getPais());
        }
    }

    private void initAutoComplete() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();

        localizacaoDAO.setConnection(db.getConnection());
        
        TextFields.bindAutoCompletion(fdNomeLocal,  localizacaoDAO.getListaLocais())
            .setOnAutoCompleted(
                event -> {
                    this.completeLocaleData(event.getCompletion());
                }
            );
    }

    public void remover() throws IOException{
      parentController.removerLocalizacao(this.getLocalizacao());
    }

    public void setParentController(ControllerEvento parentController) {
        this.parentController = parentController;
    }

    public Localizacao getLocalizacao() {
    // TODO: get do dao por nome
    // evento on click
    // TODO: Dialog de Participante
        if(    fdNomeLocal.getText() == null  
            || rua.getText() == null
            || cidade.getText() == null
            || estado.getText() == null
            || pais.getText() == null)
        {
               campoFaltando.show();
               return null;
        }
            else{
            localizacao.setNome(fdNomeLocal.getText());
            localizacao.setRua(rua.getText());
            localizacao.setBairro(bairro.getText());
            
            if(!numero.getText().isEmpty()){
                localizacao.setNumeroRua(Integer.parseInt(numero.getText()));
            }

            localizacao.setCep(cep.getText());
            localizacao.setCidade(cidade.getText());
            localizacao.setEstado(estado.getText());
            localizacao.setPais(pais.getText());
        }
        
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
        localizacaoDAO.setConnection(db.getConnection());
        
        return localizacao;
    }

}
