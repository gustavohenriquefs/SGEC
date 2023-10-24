package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.casaculturaqxd.sgec.enums.ServiceType;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
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

   public boolean inserirArquivo(ServiceFile arquivo){
    try {
      service.enviarArquivo(bucket, arquivo.getFileKey(), arquivo.getContent());
      //1° passo - criar comando sql
      String sql = "insert into service_file (file_key,service,bucket,ultima_modificacao)"
              + " values(?,?,?,?)";
      //2° passo - conectar o banco de dados e organizar o comando sql
      PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      stmt.setString(2, arquivo.getFileKey());
      stmt.setString(3, arquivo.getService());
      stmt.setString(4, arquivo.getBucket());
      stmt.setDate(5, arquivo.getUltimaModificacao());
      //3° passo - executar o comando sql
      stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
          arquivo.setServiceFileId(rs.getInt("id_service_file"));
      }
      stmt.close();
      return true;
    } catch (Exception e) {
      return false;
    }
   }
}
