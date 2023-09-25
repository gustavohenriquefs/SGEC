package com.casaculturaqxd.sgec.models;

import java.io.ByteArrayInputStream;

public class Participante {
    private int idParticipante;
    private String nome;
    private String areaDeAtuacao;
    private String linkMapaDaCultura;
    private ByteArrayInputStream imagemParticipante;

    public Participante(String nome, String areaDeAtuacao, String linkMapaDaCultura, ByteArrayInputStream imagemParticipante) {
        this.nome = nome;
        this.areaDeAtuacao = areaDeAtuacao;
        this.linkMapaDaCultura = linkMapaDaCultura;
        this.imagemParticipante = imagemParticipante;
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
    public ByteArrayInputStream getImagemParticipante() {
        return imagemParticipante;
    }
    public void setImagemParticipante(ByteArrayInputStream imagemParticipante) {
        this.imagemParticipante = imagemParticipante;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    
}
