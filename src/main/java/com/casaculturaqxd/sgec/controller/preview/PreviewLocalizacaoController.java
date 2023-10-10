package com.casaculturaqxd.sgec.controller.preview;

import com.casaculturaqxd.sgec.models.Localizacao;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PreviewLocalizacaoController {
    Localizacao localizacao;
    @FXML
    Label descricaoLocal;
    public void initialize(){
    }
    public void setLocalizacao(Localizacao localizacao){
        this.localizacao = localizacao; 
        loadContent();
    }
    private void loadContent(){ 
        descricaoLocal.setText(localizacao.toString());
    }
}
