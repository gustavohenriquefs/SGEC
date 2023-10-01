package com.casaculturaqxd.sgec.models;

import com.casaculturaqxd.sgec.enums.Atribuicao;

/**
 * Instituições incluem aquelas que agem como colaboradores
 * ou como organizadores de eventos em que a Casa de Saberes esteja envolvida.
 * Uma instituição pode ser organizadora de vários eventos e também realizadora de outros,
 * mas não Organizadora e Colaboradora ao mesmo tempo
 */
public class Instituicao {
    private Integer idInstituicao = null;
    private Atribuicao atribuicao;
    private String nome;
    private String descricaoContribuicao;
    private float valorContribuicao;

    
    public Instituicao(String nome, String descricaoContribuicao, float f,Atribuicao atribuicao) {
        this.nome = nome;
        this.descricaoContribuicao = descricaoContribuicao;
        this.valorContribuicao = f;
        this.atribuicao = atribuicao;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricaoContribuicao() {
        return descricaoContribuicao;
    }
    public void setDescricaoContribuicao(String descricaoContribuicao) {
        this.descricaoContribuicao = descricaoContribuicao;
    }
    public float getValorContribuicao() {
        return valorContribuicao;
    }
    public void setValorContribuicao(float valorContribuicao) {
        this.valorContribuicao = valorContribuicao;
    }
    public Atribuicao getAtribuicao() {
        return atribuicao;
    }
    public void setAtribuicao(Atribuicao atribuicao) {
        this.atribuicao = atribuicao;
    }

    public Integer getIdInstituicao() {
        return idInstituicao;
    }

    public void setIdInstituicao(Integer idInstituicao) {
        this.idInstituicao = idInstituicao;
    }
    
}
