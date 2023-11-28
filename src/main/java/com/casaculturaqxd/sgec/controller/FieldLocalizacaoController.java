package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
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

        Localizacao localizacao;
        try {
            localizacao = localizacaoDAO.getLocalizacaoByNome(fdNomeLocal.getText().trim()).get();
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar localização");
            alert.setContentText(e.getMessage());
            alert.show();
            return;
        }

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

        List<String> locais = new ArrayList<>();

        try {
            locais = localizacaoDAO.getListaLocais();
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar lista de locais");
            return;
        }
        
        TextFields.bindAutoCompletion(fdNomeLocal,  locais)
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

    public void destacarCamposNaoPreenchidos() {
        if (cidade.getText().isEmpty()) {
            cidade.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            cidade.setStyle(null);
        }
        if (estado.getText().isEmpty()) {
            estado.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            estado.setStyle(null);
        }
        if (pais.getText().isEmpty()) {
            pais.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            pais.setStyle(null);
        }
    }

    public Localizacao getLocalizacao() {
        Localizacao novoLocal = new Localizacao();
        if (rua.getText().isEmpty() || cidade.getText().isEmpty() || estado.getText().isEmpty()
                || pais.getText().isEmpty()) {
            destacarCamposNaoPreenchidos();
            campoFaltando.setAlertType(AlertType.ERROR);
            campoFaltando.setContentText("Nem todos os campos foram preenchidos");
            campoFaltando.show();
        } else {
            novoLocal.setRua(rua.getText());
            novoLocal.setBairro(bairro.getText());
            if (!numero.getText().isEmpty()) {
                novoLocal.setNumeroRua(Integer.parseInt(numero.getText()));
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
