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

    public Localizacao(int idLocalizacao) {
        this.idLocalizacao = idLocalizacao;
    }

    public Localizacao(String nome, String rua, String cidade, String estado, String pais) {
        this.nome = nome;
        this.rua = rua;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Localizacao() {
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

    private boolean verifyValidFildStrValue(String fieldVal) {
        return fieldVal != null && fieldVal.trim().length() > 0;
    }
    
    public String toString(){
        StringBuilder localizacao = new StringBuilder();

        if (verifyValidFildStrValue(rua)) {
            localizacao.append(rua);
        }

        if (numeroRua != 0) {
            localizacao.append(" ").append(numeroRua);
        }

        if (verifyValidFildStrValue(bairro)) {
            localizacao.append(", ").append(bairro);
        }

        localizacao.append("\n");
        
        if (verifyValidFildStrValue(cidade)) {
            localizacao.append(cidade);
        }

        if (verifyValidFildStrValue(cep)) {
            localizacao.append(" (").append(cep).append(")");
        }

        if (verifyValidFildStrValue(estado)) {
            localizacao.append(" ").append(estado);
        }

        if (verifyValidFildStrValue(pais)) {
            localizacao.append("-").append(pais);
        }
    
        return localizacao.toString();
    }

    
}
