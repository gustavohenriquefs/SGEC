package com.casaculturaqxd.sgec.models;


public class Indicador {
    String nome;
    Integer valorEsperado;
    Integer valorAlcancado;
    
    public Indicador(String nome, Integer valorEsperado, Integer valorAlcancado) {
        this.nome = nome;
        this.valorEsperado = valorEsperado;
        this.valorAlcancado = valorAlcancado;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Integer getValorEsperado() {
        return valorEsperado;
    }
    public void setValorEsperado(Integer valorEsperado) {
        this.valorEsperado = valorEsperado;
    }
    public Integer getValorAlcancado() {
        return valorAlcancado;
    }
    public void setValorAlcancado(Integer valorAlcancado) {
        this.valorAlcancado = valorAlcancado;
    }
    
}
