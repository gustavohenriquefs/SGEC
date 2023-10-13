package com.casaculturaqxd.sgec.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.print.ServiceUIFactory;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
import com.casaculturaqxd.sgec.service.Service;
import com.casaculturaqxd.sgec.service.ServiceFactory;

public class FileManager {
    Service service = ServiceFactory.getService("s3", null, null);
    String bucket;
    ArrayList<File> rawFiles;
    ArrayList<ServiceFile> serviceFiles;

    public FileManager() {
    }

    public ServiceFile getServiceFile(File file) throws IllegalArgumentException, IOException {
    }
    public void addServiceFile(ServiceFile serviceFile){
    }
    public void removeFile(String fileKey){
        }
}
