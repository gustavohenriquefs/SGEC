package com.casaculturaqxd.sgec.DAO;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
import com.casaculturaqxd.sgec.service.Service;

public class ServiceFileDAO {
  Connection connection;
  Service service;

  public ServiceFileDAO(Connection connection) {
    this.connection = connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public boolean inserirArquivo(ServiceFile arquivo) {
    try {
      setService(arquivo);
      service.enviarArquivo(arquivo);
      String sql = "insert into service_file (file_key,suffix,service,bucket,ultima_modificacao)"
          + " values(?,?,?,?,?)";
      PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      stmt.setString(1, arquivo.getFileKey());
      stmt.setString(2, arquivo.getSuffix());
      stmt.setObject(3, arquivo.getService().toString(), Types.OTHER);
      stmt.setString(4, arquivo.getBucket());
      stmt.setDate(5, arquivo.getUltimaModificacao());
      int numRemocoes = stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        arquivo.setServiceFileId(rs.getInt("id_service_file"));
      }
      stmt.close();
      return numRemocoes > 0;
    } catch (Exception e) {
      logException(e);
      return false;
    }
  }

  public ServiceFile getArquivo(ServiceFile arquivo) {
    try {
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
      logException(e);
      return null;
    }
  }

  public ServiceFile getArquivo(String nomeArquivo) {
    try {
      String sql = "select * from service_file where file_key=?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, nomeArquivo);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        ServiceFile arquivoRetorno = new ServiceFile(resultSet.getInt("id_service_file"));
        arquivoRetorno.setFileKey(resultSet.getString("file_key"));
        arquivoRetorno.setSuffix(resultSet.getString("suffix"));
        arquivoRetorno.setService(resultSet.getString("service"));
        arquivoRetorno.setBucket(resultSet.getString("bucket"));
        arquivoRetorno.setUltimaModificacao(resultSet.getDate("ultima_modificacao"));

        return arquivoRetorno;
      } else {
        return null;
      }
    } catch (Exception e) {
      logException(e);
      return null;
    }
  }

  public File getContent(ServiceFile serviceFile) {
    try {
      setService(serviceFile);
      return service.getArquivo(serviceFile.getBucket(), serviceFile.getFileKey());
    } catch (Exception e) {
      logException(e);
      return null;
    }
  }

  public boolean deleteArquivo(ServiceFile arquivo) {
    try {
      setService(arquivo);
      service.deletarArquivo(arquivo.getBucket(), arquivo.getFileKey());
      String sql = "delete from service_file where id_service_file=?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, arquivo.getServiceFileId());
      int numRemocoes = stmt.executeUpdate();
      stmt.close();
      return numRemocoes > 0;
    } catch (Exception e) {
      logException(e);
      return false;
    }
  }

  public ArrayList<ServiceFile> listarArquivosEvento(Evento evento) {
    String sql = "SELECT id_service_file FROM service_file_evento WHERE id_evento=?";
    ArrayList<ServiceFile> listaArquivos = new ArrayList<>();
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, evento.getIdEvento());
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        ServiceFile serviceFile = new ServiceFile(resultSet.getInt("id_service_file"));
        listaArquivos.add(getArquivo(serviceFile));
      }
      statement.close();
      return listaArquivos;
    } catch (Exception e) {
      logException(e);
      return null;
    }
  }

  public boolean vincularAllArquivos(Evento evento) {
    ArrayList<ServiceFile> listaArquivos = evento.getListaArquivos();
    for (ServiceFile serviceFile : listaArquivos) {
      boolean temp = vincularArquivo(serviceFile.getServiceFileId(), evento.getIdEvento());
      if (temp == false)
        return false;
    }
    return true;
  }

  public boolean vincularArquivo(int idServiceFile, int idEvento) {
    String sql = "INSERT INTO service_file_evento(id_evento, id_service_file) VALUES (?, ?);";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, idEvento);
      stmt.setInt(2, idServiceFile);
      stmt.execute();
      stmt.close();
      return true;

    } catch (Exception e) {
      logException(e);
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
      logException(e);
      return false;
    }
  }

  public boolean desvincularAllArquivos(Evento evento) {
    ArrayList<ServiceFile> listaArquivos = evento.getListaArquivos();
    for (ServiceFile serviceFile : listaArquivos) {
      boolean temp = desvincularArquivo(serviceFile.getServiceFileId(), evento.getIdEvento());
      if (temp == false)
        return false;
    }
    evento.setListaArquivos(null);
    return true;
  }

  public ArrayList<ServiceFile> pesquisarArquivoEvento(String fileKey, int idEVento) {
    String sql = "SELECT * FROM nome_arquivo_evento WHERE file_key ILIKE ? AND id_evento = ?";
    ArrayList<ServiceFile> listaArquivos = new ArrayList<>();
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, "%" + fileKey + "%");
      statement.setInt(2, idEVento);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        ServiceFile serviceFile = new ServiceFile(resultSet.getInt("id_service_file"));
        listaArquivos.add(getArquivo(serviceFile));
      }
      statement.close();
      return listaArquivos;
    } catch (Exception e) {
      logException(e);
      return null;
    }
  }

  public void setService(ServiceFile arquivo) {
    this.service = arquivo.getService();
  }

  private void logException(Exception exception) {
    if (exception instanceof SQLException) {
      Logger erro = Logger.getLogger("erroSQL");
      erro.log(Level.SEVERE, "excecao levantada:", exception);
    } else if (exception instanceof IllegalArgumentException) {
      Logger erro = Logger.getLogger("erro service");
      erro.log(Level.SEVERE, "excecao levantada:", exception);
    } else if (exception instanceof IOException) {
      Logger erro = Logger.getLogger("erro arquivo");
      erro.log(Level.SEVERE, "excecao levantada:", exception);
    }
  }
}
