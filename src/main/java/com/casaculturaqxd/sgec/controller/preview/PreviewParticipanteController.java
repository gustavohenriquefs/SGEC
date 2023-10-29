package com.casaculturaqxd.sgec.controller.preview;

import com.casaculturaqxd.sgec.controller.ControllerEvento;
import com.casaculturaqxd.sgec.models.Participante;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
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
        // ...
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
