package com.casaculturaqxd.sgec.controller;

import com.casaculturaqxd.sgec.models.Participante;
import javafx.stage.Stage;

/**
 * Interface para os controllers das telas relacionadas a eventos individuais
 */
public interface ControllerEvento {
    public void adicionarParticipante(Participante participante);

    public void removerParticipante(Participante participante);

    // retorna o stage da tela, necessario para abrir o FileChooser
    public Stage getStage();
}
