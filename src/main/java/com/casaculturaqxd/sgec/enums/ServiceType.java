package com.casaculturaqxd.sgec.enums;

public enum ServiceType {
    S3("s3");

    private final String type;

    ServiceType(String type){
        this.type = type;
    }
    
    String getType(){
        return this.type;
    }
}
