package com.casaculturaqxd.sgec.models;

public class Localizacao {
    private int idLocalizacao;
    private String rua;
    private int numeroRua;
    private String bairro;
    private String cep;
    private String cidade;
    private String estado;
    private String pais;

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
    
    public String toString(){
        String[] fields = {rua,Integer.toString(numeroRua),bairro,cidade,cep,estado,pais};
        return String.join(",",fields);
    }
}
