package com.casaculturaqxd.sgec.controller.preview;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.casaculturaqxd.sgec.controller.ControllerEvento;
import com.casaculturaqxd.sgec.models.Participante;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PreviewParticipanteController {
    private ControllerEvento parentController;
    private Participante participante;
    @FXML
    private Parent container;
    @FXML
    private Label nomeArtista, areaAtuacao, minibio;
    @FXML
    private Hyperlink link;
    @FXML
    private ImageView imagem;

    public void initialize() {
    }

    public ControllerEvento getParentController() {
        return parentController;
    }

    /**
     * seta o stage desse controller para o mesmo do controller pai passado
     */
    public void setParentController(ControllerEvento parentController) {
        this.parentController = parentController;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
        loadContent();

    }

    public Participante getParticipante() {
        return participante;
    }

    public void loadContent() {
        nomeArtista.setText(participante.getNome());
        areaAtuacao.setText(participante.getAreaDeAtuacao());
        minibio.setText(participante.getBio());
        link.setText(participante.getLinkMapaDaCultura());
        loadImagem();
    }

    public void loadImagem() {
        InputStream fileAsStream;

        try {
            fileAsStream = new FileInputStream(participante.getImagemCapa().getContent());
            imagem.setImage(new Image(fileAsStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void remover() {
        parentController.removerParticipante(getParticipante());
    }

    public Parent getContainer() {
        return container;
    }

    public void setContainer(Parent container) {
        this.container = container;
    }
}
