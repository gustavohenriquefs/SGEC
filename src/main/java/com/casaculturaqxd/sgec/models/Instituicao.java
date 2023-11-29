package com.casaculturaqxd.sgec.models;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

/**
 * Instituições incluem aquelas que agem como colaboradores ou como
 * organizadores de eventos em que a Casa de Saberes esteja envolvida. Uma
 * instituição pode ser organizadora de vários eventos e também realizadora de
 * outros, mas não Organizadora e Colaboradora ao mesmo tempo
 */
public class Instituicao {
    private Integer idInstituicao;
    private String nome;
    private String descricaoContribuicao;
    private String valorContribuicao;
    private ServiceFile imagemCapa;

    public Instituicao(String nome, String descricaoContribuicao, String valorContribuicao, ServiceFile serviceFile) {
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idInstituicao == null) ? 0 : idInstituicao.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Instituicao other = (Instituicao) obj;
        if (idInstituicao == null) {
            if (other.idInstituicao != null)
                return false;
        } else if (!idInstituicao.equals(other.idInstituicao))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        return true;
    }

}
