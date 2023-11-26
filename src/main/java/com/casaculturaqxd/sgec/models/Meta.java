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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idMeta == null) ? 0 : idMeta.hashCode());
        result = prime * result + ((nomeMeta == null) ? 0 : nomeMeta.hashCode());
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
        Meta other = (Meta) obj;
        if (idMeta == null) {
            if (other.idMeta != null)
                return false;
        } else if (!idMeta.equals(other.idMeta))
            return false;
        if (nomeMeta == null) {
            if (other.nomeMeta != null)
                return false;
        } else if (!nomeMeta.equals(other.nomeMeta))
            return false;
        return true;
    }

}
