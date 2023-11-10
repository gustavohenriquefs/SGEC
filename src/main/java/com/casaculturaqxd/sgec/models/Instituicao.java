package com.casaculturaqxd.sgec.models;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

/**
 * Instituições incluem aquelas que agem como colaboradores ou como
 * organizadores de eventos em que
 * a Casa de Saberes esteja envolvida. Uma instituição pode ser organizadora de
 * vários eventos e
 * também realizadora de outros, mas não Organizadora e Colaboradora ao mesmo
 * tempo
 */
public class Instituicao {
    private Integer idInstituicao;
    private String nome;
    private String descricaoContribuicao;
    private String valorContribuicao;
    private ServiceFile imagemCapa;

    public Instituicao(String nome, String descricaoContribuicao, String valorContribuicao,
            ServiceFile serviceFile) {
        this.nome = nome;
        this.descricaoContribuicao = descricaoContribuicao;
        this.valorContribuicao = valorContribuicao;
        this.imagemCapa = serviceFile;
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

    public ServiceFile getImagemCapa() {
        return imagemCapa;
    }

    public void setImagemCapa(ServiceFile serviceFile) {
        this.imagemCapa = serviceFile;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Instituicao)) {
            return false;
        }

        Instituicao asInstituicao = (Instituicao) other;

        return this.idInstituicao == asInstituicao.getIdInstituicao() &&
                this.nome == asInstituicao.getNome() &&
                this.descricaoContribuicao == asInstituicao.getDescricaoContribuicao() &&
                this.valorContribuicao == asInstituicao.getValorContribuicao() &&
                this.imagemCapa == asInstituicao.getImagemCapa();
    }

    @Override
    public int hashCode() {
        int hash = 5;

        hash = 29 * hash + (idInstituicao == null ? 0 : idInstituicao.hashCode());
        hash = 29 * hash + (nome == null ? 0 : nome.hashCode());
        hash = 29 * hash + (descricaoContribuicao == null ? 0 : descricaoContribuicao.hashCode());
        hash = 29 * hash + (valorContribuicao == null ? 0 : valorContribuicao.hashCode());
        hash = 29 * hash + (imagemCapa == null ? 0 : imagemCapa.hashCode());

        return hash;
    }
}
