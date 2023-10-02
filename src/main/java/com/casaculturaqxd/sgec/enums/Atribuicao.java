package com.casaculturaqxd.sgec.enums;

public enum Atribuicao {
    ORGANIZADOR("Organizador"),
    COLABORADOR("Colaborador");

    private final String atribuicao;

    Atribuicao(String atribuicao){
        this.atribuicao = atribuicao;
    }
    
    String getClassificacao(){
        return this.atribuicao;
    }
}
