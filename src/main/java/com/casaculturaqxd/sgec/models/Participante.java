package com.casaculturaqxd.sgec.models;


public class Participante {
    private int idParticipante;
    private String nome;
    private String areaDeAtuacao;
    private String bio;
    private String linkMapaDaCultura;
    private Integer idImagemCapa;

    public Participante(int idParticipante, String nome, String areaDeAtuacao, String bio,
            String linkMapaDaCultura, Integer idImagemCapa) {
        this.idParticipante = idParticipante;
        this.nome = nome;
        this.areaDeAtuacao = areaDeAtuacao;
        this.bio = bio;
        this.linkMapaDaCultura = linkMapaDaCultura;
        this.idImagemCapa = idImagemCapa;
    }

    public Participante(int idParticipante, String nome, String areaDeAtuacao, String bio,
            String linkMapaDaCultura) {
        this.idParticipante = idParticipante;
        this.nome = nome;
        this.areaDeAtuacao = areaDeAtuacao;
        this.bio = bio;
        this.linkMapaDaCultura = linkMapaDaCultura;
        this.idImagemCapa = 0;
    }

    public Participante(String nome, String areaDeAtuacao, String bio, String linkMapaDaCultura) {
        this.nome = nome;
        this.areaDeAtuacao = areaDeAtuacao;
        this.bio = bio;
        this.linkMapaDaCultura = linkMapaDaCultura;
        this.idImagemCapa = null;
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

    public Integer getIdImagemCapa() {
        return idImagemCapa;
    }

    public void setIdImagemCapa(Integer idImagemCapa) {
        this.idImagemCapa = idImagemCapa;
    }


}
