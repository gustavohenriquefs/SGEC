package com.casaculturaqxd.sgec.models;

/**
 * Instituições incluem aquelas que agem como colaboradores ou como organizadores de eventos em que
 * a Casa de Saberes esteja envolvida. Uma instituição pode ser organizadora de vários eventos e
 * também realizadora de outros, mas não Organizadora e Colaboradora ao mesmo tempo
 */
public class Instituicao {
    private Integer idInstituicao;
    private String nome;
    private String descricaoContribuicao;
    private String valorContribuicao;
    private Integer idServiceFile;

    public Instituicao(String nome, String descricaoContribuicao, String valorContribuicao,
            Integer idServiceFile) {
        this.nome = nome;
        this.descricaoContribuicao = descricaoContribuicao;
        this.valorContribuicao = valorContribuicao;
        this.idServiceFile = idServiceFile;
    }

    public Instituicao(String nome) {
        this.nome = nome;
    }

    public Instituicao(int idInstituicao) {
        this.idInstituicao = idInstituicao;
    }

    public Instituicao() {
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

    public String getValorContribuicao() {
        return valorContribuicao;
    }

    public void setValorContribuicao(String valorContribuicao) {
        this.valorContribuicao = valorContribuicao;
    }

    public Integer getIdInstituicao() {
        return idInstituicao;
    }

    public void setIdInstituicao(Integer idInstituicao) {
        this.idInstituicao = idInstituicao;
    }

    public Integer getIdServiceFile() {
        return idServiceFile;
    }

    public void setIdServiceFile(Integer idServiceFile) {
        this.idServiceFile = idServiceFile;
    }

}
