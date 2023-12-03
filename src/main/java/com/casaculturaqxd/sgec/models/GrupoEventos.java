package com.casaculturaqxd.sgec.models;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class GrupoEventos {
    private Integer idGrupoEventos;
    private String nome;
    private String descricao;
    private String classificacaoEtaria;
    private Date dataInicial;
    private Date dataFinal;
    private Time cargaHoraria;
    private int publicoEsperado;
    private int publicoAlcancado;
    private int numAcoesEsperado;
    private int numAcoesAlcancado;
    private int numParticipantesEsperado;
    private int numParticipantesAlcancado;
    private int numMunicipiosEsperado;
    private int numMunicipiosAlcancado;
    private int numColaboradoresEsperado;
    private int numColaboradoresAlcancado;
    private ArrayList<Evento> eventos;
    private ArrayList<Instituicao> organizadores;
    private ArrayList<Instituicao> colaboradores;
    private ArrayList<Meta> metas;

    private ServiceFile imagemCapa;

    public GrupoEventos() {

    }

    public GrupoEventos(Integer idGrupoEventos) {
        this.idGrupoEventos = idGrupoEventos;
    }

    public Integer getIdGrupoEventos() {
        return idGrupoEventos;
    }

    public void setIdGrupoEventos(Integer idGrupoEventos) {
        this.idGrupoEventos = idGrupoEventos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getClassificacaoEtaria() {
        return classificacaoEtaria;
    }

    public void setClassificacaoEtaria(String classificacaoEtaria) {
        this.classificacaoEtaria = classificacaoEtaria;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public int getPublicoEsperado() {
        return publicoEsperado;
    }

    public void setPublicoEsperado(int publicoEsperado) {
        this.publicoEsperado = publicoEsperado;
    }

    public int getPublicoAlcancado() {
        return publicoAlcancado;
    }

    public void setPublicoAlcancado(int publicoAlcancado) {
        this.publicoAlcancado = publicoAlcancado;
    }

    public int getNumAcoesEsperado() {
        return numAcoesEsperado;
    }

    public void setNumAcoesEsperado(int numAcoesEsperado) {
        this.numAcoesEsperado = numAcoesEsperado;
    }

    public int getNumAcoesAlcancado() {
        return numAcoesAlcancado;
    }

    public void setNumAcoesAlcancado(int numAcoesAlcancado) {
        this.numAcoesAlcancado = numAcoesAlcancado;
    }

    public int getNumParticipantesEsperado() {
        return numParticipantesEsperado;
    }

    public void setNumParticipantesEsperado(int numParticipantesEsperado) {
        this.numParticipantesEsperado = numParticipantesEsperado;
    }

    public int getNumParticipantesAlcancado() {
        return numParticipantesAlcancado;
    }

    public void setNumParticipantesAlcancado(int numParticipantesAlcancado) {
        this.numParticipantesAlcancado = numParticipantesAlcancado;
    }

    public int getNumMunicipiosEsperado() {
        return numMunicipiosEsperado;
    }

    public void setNumMunicipiosEsperado(int numMunicipiosEsperado) {
        this.numMunicipiosEsperado = numMunicipiosEsperado;
    }

    public int getNumMunicipiosAlcancado() {
        return numMunicipiosAlcancado;
    }

    public void setNumMunicipiosAlcancado(int numMunicipiosAlcancado) {
        this.numMunicipiosAlcancado = numMunicipiosAlcancado;
    }

    public int getNumColaboradoresEsperado() {
        return numColaboradoresEsperado;
    }

    public void setNumColaboradoresEsperado(int numColaboradoresEsperado) {
        this.numColaboradoresEsperado = numColaboradoresEsperado;
    }

    public int getNumColaboradoresAlcancado() {
        return numColaboradoresAlcancado;
    }

    public void setNumColaboradoresAlcancado(int numColaboradoresAlcancado) {
        this.numColaboradoresAlcancado = numColaboradoresAlcancado;
    }

    public ServiceFile getImagemCapa() {
        return imagemCapa;
    }

    public void setImagemCapa(ServiceFile imagemCapa) {
        this.imagemCapa = imagemCapa;
    }

    public ArrayList<Instituicao> getOrganizadores() {
        return organizadores;
    }

    public void setOrganizadores(ArrayList<Instituicao> listaOrganizadores) {
        this.organizadores = listaOrganizadores;
    }

    public ArrayList<Instituicao> getColaboradores() {
        return colaboradores;
    }

    public void setColaboradores(ArrayList<Instituicao> listaColaboradores) {
        this.colaboradores = listaColaboradores;
    }

    public ArrayList<Meta> getMetas() {
        return metas;
    }

    public void setMetas(ArrayList<Meta> metas) {
        this.metas = metas;
    }

    public ArrayList<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(ArrayList<Evento> eventos) {
        this.eventos = eventos;
    }

    public Time getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Time cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

}
