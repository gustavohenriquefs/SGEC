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
    public Builder setNome(String nome) {
        grupoEventos.setNome(nome);
        return this;
    }

    @Override
    public Builder setDescricao(String descricao) {
        grupoEventos.setDescricao(descricao);
        return this;
    }

    @Override
    public Builder setDataInicial(Date dataInicial) {
        grupoEventos.setDataInicial(dataInicial);
        return this;
    }

    @Override
    public Builder setDataFinal(Date dataFinal) {
        grupoEventos.setDataFinal(dataFinal);
        return this;
    }

    @Override
    public Builder setPublicoEsperado(int publicoEsperado) {
        grupoEventos.setPublicoEsperado(publicoEsperado);
        return this;
    }

    @Override
    public Builder setPublicoAlcancado(int publicoAlcancado) {
        grupoEventos.setPublicoAlcancado(publicoAlcancado);
        return this;
    }

    @Override
    public Builder setClassificacaoEtaria(String classificacaoEtaria) {
        grupoEventos.setClassificacaoEtaria(classificacaoEtaria);
        return this;
    }

    @Override
    public Builder setColaboradores(SortedSet<Integer> colaboradores) {
        // TODO Remover metodo apos refactor em evento
        return this;
    }

    @Override
    public Builder setOrganizadores(SortedSet<Integer> organizadores) {
        // TODO Remover metodo apos refactor em evento
        return this;
    }

    @Override
    public Builder setColaboradores(ArrayList<Instituicao> colaboradores) {
        grupoEventos.setColaboradores(colaboradores);
        return this;
    }

    @Override
    public Builder setOrganizadores(ArrayList<Instituicao> organizadores) {
        grupoEventos.setOrganizadores(organizadores);
        return this;
    }

}
