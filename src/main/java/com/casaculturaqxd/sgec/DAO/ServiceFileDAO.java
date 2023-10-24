package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;

import com.casaculturaqxd.sgec.enums.ServiceType;
import com.casaculturaqxd.sgec.service.Service;

public class ServiceFileDAO {
  String suffix;
  ServiceType serviceType;
  Connection connection;
  Service service;
  String bucket;

  ServiceFileDAO(String bucket, Service service){
    this.bucket = bucket;
    this.service = service;
    serviceType = ServiceType.S3;
  }

  public void setConnection(Connection connection){
    this.connection = connection;
  }
}
