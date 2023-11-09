package com.casaculturaqxd.sgec.builder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.SortedSet;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Instituicao;

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
    public GrupoEventosBuilder setColaboradores(SortedSet<Integer> colaboradores) {
        // TODO Remover metodo apos refactor em evento
        return this;
    }

    @Override
    public GrupoEventosBuilder setOrganizadores(SortedSet<Integer> organizadores) {
        // TODO Remover metodo apos refactor em evento
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

}
