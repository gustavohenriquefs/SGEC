package com.casaculturaqxd.sgec.service;

import com.casaculturaqxd.sgec.enums.ServiceType;

public class ServiceFactory {
    
    public static Service getService(ServiceType type, String envAccessKey, String envSecretKey){
        if(type.toString().equalsIgnoreCase("S3") ){
            return new S3Service(envAccessKey, envSecretKey);
        }

        return null;
    }

}
