package com.casaculturaqxd.sgec.models;

public class Meta {
    Integer idMeta;
    String nomeMeta;

    public Meta(Integer idMeta, String nomeMeta) {
        this.idMeta = idMeta;
        this.nomeMeta = nomeMeta;
    }


    public Meta(Integer idMeta) {
        this.idMeta = idMeta;
    }


    public Meta(String nomeMeta) {
        this.nomeMeta = nomeMeta;
    }

    public Integer getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(Integer idMeta) {
        this.idMeta = idMeta;
    }

    public String getNomeMeta() {
        return nomeMeta;
    }

    public void setNomeMeta(String nomeMeta) {
        this.nomeMeta = nomeMeta;
    }


}
