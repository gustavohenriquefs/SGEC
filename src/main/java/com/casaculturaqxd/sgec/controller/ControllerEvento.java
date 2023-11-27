package com.casaculturaqxd.sgec.controller;

import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Participante;
import javafx.stage.Stage;

/**
 * Interface para os controllers das telas relacionadas a eventos individuais
 */
public interface ControllerEvento {
    public void adicionarParticipante(Participante participante);

    public void removerParticipante(Participante participante);

    public void adicionarOrganizador(Instituicao instituicao);

    public void adicionarColaborador(Instituicao instituicao);

    // retorna o stage da tela, necessario para abrir o FileChooser
    public Stage getStage();

    public void removerInstituicao(Instituicao instituicao);
}
