package com.casaculturaqxd.sgec.controller;

import com.casaculturaqxd.sgec.models.Participante;

/**
 * Interface para os controllers das telas relacionadas a eventos individuais
 */
public interface ControllerEvento {
    public void adicionarParticipante(Participante participante);

    public void removerParticipante(Participante participante);

    public void adicionarLocalizacao(String localizacao);

    public void removerLocalizacao(String localizacao);
}
