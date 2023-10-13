package com.casaculturaqxd.sgec.service;

public class ServiceFactory {
    public static Service getService(String type, String envAccessKey, String envSecretKey){
        if(type.equalsIgnoreCase("S3") ){
            return new S3Service(envAccessKey, envSecretKey);
        }

        return null;
    }
}
