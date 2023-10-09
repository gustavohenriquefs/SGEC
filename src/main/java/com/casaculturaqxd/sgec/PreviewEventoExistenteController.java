package com.casaculturaqxd.sgec;

import com.casaculturaqxd.sgec.models.Evento;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PreviewEventoExistenteController {
    @FXML
    private Button botao;

    @FXML
    private HBox campoBotao;

    @FXML
    private HBox campoDataHora;

    @FXML
    private HBox campoImagem;

    @FXML
    private HBox campoTitulo;

    @FXML
    private Label dataHora;

    @FXML
    private ImageView imagem;

    @FXML
    private Label titulo;

    @FXML
    public void initialize() {
    }

    public void setEvento(Evento evento) {

        if(evento == null) {
            return;
        }
        
        if(evento.getNome() != null) {
            this.titulo.setText(evento.getNome());
        }

        if(evento.getHorario() != null) {
            this.dataHora.setText(evento.getHorario().toString());
        }
    }

}
