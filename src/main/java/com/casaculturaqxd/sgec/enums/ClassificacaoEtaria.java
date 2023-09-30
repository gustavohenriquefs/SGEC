package com.casaculturaqxd.sgec.enums;

public enum ClassificacaoEtaria {
    LIVRE("Livre"),
    IDADE10("10 anos"),
    IDADE12("12 anos"),
    IDADE14("14 anos"),
    IDADE16("16 anos"),
    IDADE18("18 anos");

    private final String classificacaoEtaria;

    ClassificacaoEtaria(String classificacaoEtaria){
        this.classificacaoEtaria = classificacaoEtaria;
    }
    
    String getClassificacao(){
        return this.classificacaoEtaria;
    }
}
