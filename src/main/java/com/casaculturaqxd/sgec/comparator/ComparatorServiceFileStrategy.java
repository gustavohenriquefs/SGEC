package com.casaculturaqxd.sgec.comparator;

import java.util.Comparator;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public interface ComparatorServiceFileStrategy extends Comparator<ServiceFile> {
    public int compare(ServiceFile one, ServiceFile another);

    public int compare(ServiceFile one, ServiceFile another, boolean reversed);

}