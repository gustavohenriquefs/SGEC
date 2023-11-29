package com.casaculturaqxd.sgec.comparator;

import java.sql.Date;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class ComparatorServiceFileDate implements ComparatorServiceFileStrategy {

    @Override
    public int compare(ServiceFile one, ServiceFile another) {
        if (one.getUltimaModificacao() == another.getUltimaModificacao()) {
            return 0;
        }
        if (one.getUltimaModificacao() == null) {
            return -1;
        }
        if (another.getUltimaModificacao() == null) {
            return 1;
        }
        return one.getUltimaModificacao().compareTo(another.getUltimaModificacao());
    }

    @Override
    public int compare(ServiceFile one, ServiceFile another, boolean reversed) {
        if (reversed) {
            return this.reversed().compare(one, another);
        } else {
            return this.compare(one, another);
        }
    }

}
