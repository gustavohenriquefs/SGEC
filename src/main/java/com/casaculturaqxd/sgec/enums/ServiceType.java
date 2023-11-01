package com.casaculturaqxd.sgec.enums;

public enum ServiceType {
    S3("s3");

    private static String type;

    ServiceType(String type){
        type = type;
    }

    public void setType(String type) {
      this.type = type;
    }
    
    public static String getType(){
        return type;
    }
}
