package com.casaculturaqxd.sgec.enums;

public enum ServiceType {
    S3("s3");

    private String type;

    ServiceType(String type){
        this.type = type;
    }

    public void setType(String type) {
      this.type = type;
    }
    
    public String getType(){
        return this.type;
    }
}
