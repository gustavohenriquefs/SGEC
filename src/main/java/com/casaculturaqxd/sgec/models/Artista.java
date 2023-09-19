package com.casaculturaqxd.sgec.models;

import java.io.ByteArrayInputStream;

public class Artista {
    private String nome;
    private String areaDeAtuacao;
    private String linkMapaDaCultura;
    private ByteArrayInputStream imagemArtista;

    public Artista(String nome, String areaDeAtuacao, String linkMapaDaCultura, ByteArrayInputStream imagemArtista) {
        this.nome = nome;
        this.areaDeAtuacao = areaDeAtuacao;
        this.linkMapaDaCultura = linkMapaDaCultura;
        this.imagemArtista = imagemArtista;
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
    public ByteArrayInputStream getImagemArtista() {
        return imagemArtista;
    }
    public void setImagemArtista(ByteArrayInputStream imagemArtista) {
        this.imagemArtista = imagemArtista;
    }

    
}
