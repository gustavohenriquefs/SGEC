package com.casaculturaqxd.sgec.builder;

import java.sql.Date;
import java.util.ArrayList;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class GrupoEventosBuilder implements Builder {
    GrupoEventos grupoEventos;

    public GrupoEventosBuilder() {
        resetar();
    }

    public GrupoEventos getGrupoEventos() {
        return grupoEventos;
    }

    @Override
    public void resetar() {
        grupoEventos = new GrupoEventos();
    }

    @Override
    public GrupoEventosBuilder setNome(String nome) {
        grupoEventos.setNome(nome);
        return this;
    }

    @Override
    public GrupoEventosBuilder setDescricao(String descricao) {
        grupoEventos.setDescricao(descricao);
        return this;
    }

    @Override
    public GrupoEventosBuilder setDataInicial(Date dataInicial) {
        grupoEventos.setDataInicial(dataInicial);
        return this;
    }

    @Override
    public GrupoEventosBuilder setDataFinal(Date dataFinal) {
        grupoEventos.setDataFinal(dataFinal);
        return this;
    }

    @Override
    public GrupoEventosBuilder setPublicoEsperado(int publicoEsperado) {
        grupoEventos.setPublicoEsperado(publicoEsperado);
        return this;
    }

    @Override
    public GrupoEventosBuilder setPublicoAlcancado(int publicoAlcancado) {
        grupoEventos.setPublicoAlcancado(publicoAlcancado);
        return this;
    }

    @Override
    public GrupoEventosBuilder setClassificacaoEtaria(String classificacaoEtaria) {
        grupoEventos.setClassificacaoEtaria(classificacaoEtaria);
        return this;
    }

    @Override
    public GrupoEventosBuilder setColaboradores(ArrayList<Instituicao> colaboradores) {
        grupoEventos.setColaboradores(colaboradores);
        return this;
    }

    @Override
    public GrupoEventosBuilder setOrganizadores(ArrayList<Instituicao> organizadores) {
        grupoEventos.setOrganizadores(organizadores);
        return this;
    }

    public GrupoEventosBuilder setImagemCapa(ServiceFile serviceFile) {
        return this;
    }

    public GrupoEventosBuilder setNumAcoesEsperado(int int1) {
        return this;
    }

    public GrupoEventosBuilder setNumAcoesAlcancado(int int1) {
        return this;
    }

    public GrupoEventosBuilder setNumMunicipiosEsperado(int int1) {
        return this;
    }

    public GrupoEventosBuilder setNumMunicipiosAlcancado(int int1) {
        return this;
    }

    public GrupoEventosBuilder setNumParticipantesEsperado(int int1) {
        return this;
    }

    public GrupoEventosBuilder setNumParticipantesAlcancado(int int1) {
        return this;
    }

    public GrupoEventosBuilder setIdGrupoEventos(int idGrupoEventos) {
        grupoEventos.setIdGrupoEventos(idGrupoEventos);
        return this;
    }

}
