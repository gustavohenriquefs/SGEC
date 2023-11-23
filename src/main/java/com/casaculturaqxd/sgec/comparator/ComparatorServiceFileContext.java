package com.casaculturaqxd.sgec.comparator;

import java.util.Comparator;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class ComparatorServiceFileContext implements Comparator<ServiceFile> {
    private ComparatorServiceFileStrategy strategy;

    public void setStrategy(ComparatorServiceFileStrategy strategy) {
        this.strategy = strategy;
    }

    public int compare(ServiceFile one, ServiceFile another, boolean reversed) {
        return strategy.compare(one, another, reversed);
    }

    @Override
    public int compare(ServiceFile one, ServiceFile another) {
        return strategy.compare(one, another);
    }
}
