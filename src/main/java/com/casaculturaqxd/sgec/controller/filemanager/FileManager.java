package com.casaculturaqxd.sgec.controller.filemanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class FileManager {
    //private ServiceFileDAO dao;
    private String bucket;
    private ArrayList<ServiceFile> serviceFiles;

    public FileManager(String bucket) {
        this.bucket = bucket;
    }

    public FileManager(String bucket, ArrayList<File> rawFiles) {
        this.bucket = bucket;
        for(File rawFile : rawFiles){
            serviceFiles.add(new ServiceFile(rawFile, bucket));
        }
    }
    
    public ServiceFile getServiceFile(File file) throws IllegalArgumentException, IOException {
        // if(ServiceFileDAO.getServiceFile() != null){
        //     ServiceFileDAO.getServiceFile()
        // }
        // else{
        //     service.enviarArquivo()
        // }
        return null;
    }
    public void addServiceFile(File File){
        serviceFiles.add(new ServiceFile(File, bucket));
    }
    public void removeServiceFile(String fileKey){
    }

}
