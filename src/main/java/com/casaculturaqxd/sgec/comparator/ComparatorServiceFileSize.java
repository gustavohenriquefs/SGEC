package com.casaculturaqxd.sgec.comparator;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class ComparatorServiceFileSize implements ComparatorServiceFileStrategy {

    @Override
    public int compare(ServiceFile one, ServiceFile another) {
        return Long.compare(one.getFileSize(), another.getFileSize());
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
