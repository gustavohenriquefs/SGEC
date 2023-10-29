package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
import com.casaculturaqxd.sgec.service.Service;

public class ServiceFileDAO {
  Connection connection;
  Service service;
  String bucket;

  ServiceFileDAO(String bucket, Service service){
    this.bucket = bucket;
    this.service = service;
  }

  public void setConnection(Connection connection){
    this.connection = connection;
  }

  public boolean inserirArquivo(ServiceFile arquivo){
    try {
      service.enviarArquivo(arquivo);
      //1° passo - criar comando sql
      String sql = "insert into service_file (file_key,suffix,service,bucket,ultima_modificacao)"
              + " values(?,?,?,?,?)";
      //2° passo - conectar o banco de dados e organizar o comando sql
      PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      stmt.setString(1, arquivo.getFileKey());
      stmt.setString(2, arquivo.getSuffix());
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

  public ServiceFile getArquivo(ServiceFile arquivo){
    try {
      service.getArquivo(arquivo.getBucket(), arquivo.getFileKey());
      String sql = "select * from service_file where id_service_file=?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, arquivo.getServiceFileId());
      ResultSet resultSet = stmt.executeQuery();
      ServiceFile arquivoRetorno = null;
      if (resultSet.next()) {
        arquivoRetorno = arquivo;
        arquivoRetorno.setServiceFileId(resultSet.getInt("id_service_file"));
        arquivoRetorno.setFileKey(resultSet.getString("file_key"));
        arquivoRetorno.setSuffix(resultSet.getString("suffix"));
        arquivoRetorno.setService(resultSet.getString("service"));
        arquivoRetorno.setBucket(resultSet.getString("bucket"));
        arquivoRetorno.setUltimaModificacao(resultSet.getDate("ultima_modificacao"));
      }
      return arquivo;
    } catch (Exception e) {
      return null;
    }
  }

  public boolean deleteArquivo(ServiceFile arquivo){
    try {
      service.deletarArquivo(arquivo.getBucket(), arquivo.getFileKey());
      String sql = "delete from service_file where id_service_file=?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, arquivo.getServiceFileId());
      stmt.execute();
      stmt.close();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean vincularAllArquivos(Evento evento){
    SortedSet<Integer> listaArquivos = evento.getListaArquivos();
    for (Integer idServiceFile : listaArquivos) {
      boolean temp = vincularArquivo(idServiceFile, evento.getIdEvento());
      if(temp == false)
        return false;
    }
    return true;
  }

  public boolean vincularArquivo(int idServiceFile, int idEvento){
    String sql = "INSERT INTO service_file_evento(id_evento, id_service_file) VALUES (?, ?);";
    try {
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, idEvento);
        stmt.setInt(2, idServiceFile);
        stmt.execute();
        stmt.close();
        return true;
    } catch (SQLException e) {
        return false;
    }
  }

  public boolean desvincularArquivo(int idServiceFile, int idEvento) {
    String sql = "delete from service_file_evento where id_service_file=? and id_evento=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, idServiceFile);
      stmt.setInt(2, idEvento);
      stmt.execute();
      stmt.close();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean desvincularAllArquivos(Evento evento) {
    SortedSet<Integer> listaArquivos = evento.getListaArquivos();
    for (Integer idServiceFile : listaArquivos) {
      boolean temp = desvincularArquivo(idServiceFile, evento.getIdEvento());
      if(temp == false)
        return false;
    }
    evento.setListaArquivos(null);
    return true;
  }
}
