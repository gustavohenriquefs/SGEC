package com.casaculturaqxd.sgec.models;

public class Localizacao {
    private int idLocalizacao;
    private String nome;
    private String rua;
    private int numeroRua;
    private String bairro;
    private String cep;
    private String cidade;
    private String estado;
    private String pais;

    public Localizacao(String nome, String rua, String cidade, String estado, String pais) {
        this.nome = nome;
        this.rua = rua;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
    }

    public Localizacao() {

    }

    public Localizacao(int idLocalizacao) {
        this.idLocalizacao = idLocalizacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNumeroRua() {
        return numeroRua;
    }

    public void setNumeroRua(int numeroRua) {
        this.numeroRua = numeroRua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getIdLocalizacao() {
        return idLocalizacao;
    }

    public void setIdLocalizacao(int idLocalizacao) {
        this.idLocalizacao = idLocalizacao;
    }

    public String toString() {
        String[] firstLine = { rua, Integer.toString(numeroRua), bairro };
        String secondLine = "\n" + cidade + " (" + cep + ") " + estado + "-" + pais;
        String[] fields = { String.join(", ", firstLine), secondLine };
        return String.join(" ", fields);
    }
}
