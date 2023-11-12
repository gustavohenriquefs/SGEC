package com.casaculturaqxd.sgec.models;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class Participante {
    private int idParticipante;
    private String nome;
    private String areaDeAtuacao;
    private String bio;
    private String linkMapaDaCultura;
    private ServiceFile imagemCapa;

    public Participante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    public Participante(int idParticipante, String nome, String areaDeAtuacao, String bio,
            String linkMapaDaCultura, ServiceFile imagemCapa) {
        this.idParticipante = idParticipante;
        this.nome = nome;
        this.areaDeAtuacao = areaDeAtuacao;
        this.bio = bio;
        this.linkMapaDaCultura = linkMapaDaCultura;
        this.imagemCapa = imagemCapa;
    }

    public Participante(int idParticipante, String nome, String areaDeAtuacao, String bio,
            String linkMapaDaCultura) {
        this.idParticipante = idParticipante;
        this.nome = nome;
        this.areaDeAtuacao = areaDeAtuacao;
        this.bio = bio;
        this.linkMapaDaCultura = linkMapaDaCultura;
        this.imagemCapa = null;
    }

    public Participante(String nome, String areaDeAtuacao, String bio, String linkMapaDaCultura) {
        this.nome = nome;
        this.areaDeAtuacao = areaDeAtuacao;
        this.bio = bio;
        this.linkMapaDaCultura = linkMapaDaCultura;
        this.imagemCapa = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAreaDeAtuacao() {
        return areaDeAtuacao;
    }

    public void setAreaDeAtuacao(String areaDeAtuacao) {
        this.areaDeAtuacao = areaDeAtuacao;
    }

    public String getLinkMapaDaCultura() {
        return linkMapaDaCultura;
    }

    public void setLinkMapaDaCultura(String linkMapaDaCultura) {
        this.linkMapaDaCultura = linkMapaDaCultura;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public ServiceFile getImagemCapa() {
        return imagemCapa;
    }

    public void setImagemCapa(ServiceFile imagemCapa) {
        this.imagemCapa = imagemCapa;
    }

}
