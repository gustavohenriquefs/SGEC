package com.casaculturaqxd.sgec.models;

public class Indicador<T> {
    String nome;
    T valorEsperado;
    T valorAlcancado;

    public Indicador(String nome, T valorEsperado, T valorAlcancado) {
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

    public T getValorEsperado() {
        return valorEsperado;
    }

    public void setValorEsperado(T valorEsperado) {
        this.valorEsperado = valorEsperado;
    }

    @SuppressWarnings("unchecked")
    /**
     * metodo auxiliar especifico para valores numericos
     * 
     * @param valorEsperado
     */
    public void setValorEsperadoNumeric(Number valorEsperado) {
        this.valorEsperado = (T) valorEsperado;
    }

    @SuppressWarnings("unchecked")
    /**
     * metodo auxiliar especifico para valores numericos
     * 
     * @param valorEsperado
     */
    public void setValorAlcancadoNumeric(Number valorAlcancado) {
        this.valorAlcancado = (T) valorAlcancado;
    }

    public T getValorAlcancado() {
        return valorAlcancado;
    }

    public void setValorAlcancado(T valorAlcancado) {
        this.valorAlcancado = valorAlcancado;
    }

}
