package com.casaculturaqxd.sgec.comparator;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class ComparatorServiceFileKey implements ComparatorServiceFileStrategy {

    @Override
    public int compare(ServiceFile one, ServiceFile another) {
        if (one.getFileKey() == another.getFileKey()) {
            return 0;
        }
        if (one.getFileKey() == null) {
            return -1;
        }
        if (another.getFileKey() == null) {
            return 1;
        }
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
