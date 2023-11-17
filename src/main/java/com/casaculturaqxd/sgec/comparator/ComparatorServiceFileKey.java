package com.casaculturaqxd.sgec.comparator;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class ComparatorServiceFileKey implements ComparatorServiceFileStrategy {

    @Override
    public int compare(ServiceFile one, ServiceFile another) {
        return one.getFileKey().compareTo(another.getFileKey());
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
