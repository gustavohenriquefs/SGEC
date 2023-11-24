package com.casaculturaqxd.sgec.DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Optional;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
import com.casaculturaqxd.sgec.service.Service;

public class ServiceFileDAO extends DAO {
  Connection connection;
  Service service;

  public ServiceFileDAO(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() {
    return this.connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public boolean inserirArquivo(ServiceFile arquivo) throws SQLException {
    String sql = "insert into service_file (file_key,suffix,service,bucket,ultima_modificacao)" + " values(?,?,?,?,?)";
    PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    try {
      setService(arquivo.getService());
      service.enviarArquivo(arquivo);
      stmt.setString(1, arquivo.getFileKey());
      stmt.setString(2, arquivo.getSuffix());
      // nome do service e definido em uma enum no banco
      stmt.setObject(3, arquivo.getService().toString(), Types.OTHER);
      stmt.setString(4, arquivo.getBucket());
      stmt.setDate(5, arquivo.getUltimaModificacao());
      int numInsercoes = stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        arquivo.setServiceFileId(rs.getInt("id_service_file"));
      }
      return numInsercoes > 0;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("erro inserindo arquivo: " + arquivo.getFileKey(), e);
    } finally {
      stmt.close();
    }
  }

  public Optional<ServiceFile> getArquivo(ServiceFile arquivo) throws SQLException {
    String sql = "select * from service_file where id_service_file=?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    try {
      stmt.setInt(1, arquivo.getServiceFileId());
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        ServiceFile arquivoRetorno = arquivo;
        arquivoRetorno.setFileKey(resultSet.getString("file_key"));
        arquivoRetorno.setBucket(resultSet.getString("bucket"));
        arquivoRetorno.setService(resultSet.getString("service"));

        setService(arquivo.getService());
        arquivoRetorno.copyMetadata(service.getMetadata(arquivo.getBucket(), arquivo.getFileKey()));
      }
      return Optional.of(arquivo);
    } catch (Exception e) {
      logException(e);
      throw new SQLException("erro buscando arquivo: " + arquivo.getFileKey(), e);
    } finally {
      stmt.close();
    }
  }

  public Optional<ServiceFile> getArquivo(String nomeArquivo) throws SQLException {
    String sql = "select * from service_file where file_key=?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    try {
      stmt.setString(1, nomeArquivo);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        ServiceFile arquivoRetorno = new ServiceFile(resultSet.getInt("id_service_file"));
        arquivoRetorno.setFileKey(resultSet.getString("file_key"));
        arquivoRetorno.setSuffix(resultSet.getString("suffix"));
        arquivoRetorno.setService(resultSet.getString("service"));
        arquivoRetorno.setBucket(resultSet.getString("bucket"));

        setService(arquivoRetorno.getService());
        arquivoRetorno.copyMetadata(service.getMetadata(arquivoRetorno.getBucket(), arquivoRetorno.getFileKey()));
        return Optional.of(arquivoRetorno);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      logException(e);
      throw new SQLException("erro buscando arquivo: " + nomeArquivo, e);
    } finally {
      stmt.close();
    }
  }

  public File getContent(ServiceFile serviceFile) throws SQLException {
    try {
      setService(serviceFile.getService());
      return service.getArquivo(serviceFile.getBucket(), serviceFile.getFileKey());
    } catch (Exception e) {
      logException(e);
      throw new SQLException("erro resgatando conteudo do arquivo: " + serviceFile.getFileKey(), e);
    }
  }

  public boolean deleteArquivo(ServiceFile arquivo) throws SQLException {
    String sql = "delete from service_file where id_service_file=?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    try {
      setService(arquivo.getService());
      service.deletarArquivo(arquivo.getBucket(), arquivo.getFileKey());

      stmt.setInt(1, arquivo.getServiceFileId());
      int numRemocoes = stmt.executeUpdate();
      return numRemocoes > 0;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("erro deletando arquivo: " + arquivo.getFileKey(), e);
    } finally {
      stmt.close();
    }
  }

  public ArrayList<ServiceFile> listarArquivosEvento(Evento evento, int limit) throws SQLException {
    String sql = "SELECT id_service_file FROM service_file_evento INNER JOIN service_file USING(id_service_file) WHERE id_evento=? ORDER BY adicionado_em DESC LIMIT ?";
    ArrayList<ServiceFile> listaArquivos = new ArrayList<>();
    PreparedStatement statement = connection.prepareStatement(sql);

    try {
      statement.setInt(1, evento.getIdEvento());
      statement.setInt(2, limit);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        ServiceFile serviceFile = new ServiceFile(resultSet.getInt("id_service_file"));
        listaArquivos.add(getArquivo(serviceFile).get());
      }
      statement.close();
      return listaArquivos;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("erro listando arquivos do evento: " + evento.getNome(), e);
    } finally {
      statement.close();
    }
  }

  public ArrayList<ServiceFile> listarArquivosEvento(Evento evento) throws SQLException {
    String sql = "SELECT id_service_file FROM service_file_evento WHERE id_evento=?";
    ArrayList<ServiceFile> listaArquivos = new ArrayList<>();
    PreparedStatement statement = connection.prepareStatement(sql);
    try {
      statement.setInt(1, evento.getIdEvento());
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        ServiceFile serviceFile = new ServiceFile(resultSet.getInt("id_service_file"));
        try {
          listaArquivos.add(getArquivo(serviceFile).get());
        } catch (SQLException e) {
          continue;
        }
      }
      statement.close();
      return listaArquivos;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("erro listando arquivos do evento: " + evento.getNome(), e);
    } finally {
      statement.close();
    }
  }

  public boolean vincularAllArquivos(Evento evento) throws SQLException {
    ArrayList<ServiceFile> listaArquivos = evento.getListaArquivos();
    for (ServiceFile serviceFile : listaArquivos) {
      boolean temp = vincularArquivo(serviceFile.getServiceFileId(), evento.getIdEvento());
      if (temp == false)
        return false;
    }
    return true;
  }

  public boolean vincularArquivo(int idServiceFile, int idEvento) throws SQLException {
    String sql = "INSERT INTO service_file_evento(id_evento, id_service_file) VALUES (?, ?);";
    PreparedStatement stmt = connection.prepareStatement(sql);
    try {

      stmt.setInt(1, idEvento);
      stmt.setInt(2, idServiceFile);
      stmt.execute();
      return true;

    } catch (Exception e) {
      logException(e);
      throw new SQLException(
          "erro vinculando arquivo ao evento: id do arquivo-" + idServiceFile + " id do evento-" + idEvento, e);
    } finally {
      stmt.close();
    }
  }

  public boolean desvincularArquivo(int idServiceFile, int idEvento) throws SQLException {
    String sql = "delete from service_file_evento where id_service_file=? and id_evento=?";
    PreparedStatement stmt = connection.prepareStatement(sql);

    try {
      stmt.setInt(1, idServiceFile);
      stmt.setInt(2, idEvento);
      stmt.execute();
      return true;
    } catch (Exception e) {
      logException(e);
      throw new SQLException(
          "falha desvinculando arquivo do evento" + ", id do arquivo:" + idServiceFile + " id do evento:" + idEvento,
          e);
    } finally {
      stmt.close();
    }
  }

  public boolean desvincularAllArquivos(Evento evento) throws SQLException {
    ArrayList<ServiceFile> listaArquivos = evento.getListaArquivos();
    for (ServiceFile serviceFile : listaArquivos) {
      boolean temp = desvincularArquivo(serviceFile.getServiceFileId(), evento.getIdEvento());
      if (temp == false)
        return false;
    }
    evento.setListaArquivos(null);
    return true;
  }

  public ArrayList<ServiceFile> pesquisarArquivoEvento(String fileKey, int idEVento) throws SQLException {
    String sql = "SELECT * FROM nome_arquivo_evento WHERE file_key ILIKE ? AND id_evento = ?";
    ArrayList<ServiceFile> listaArquivos = new ArrayList<>();
    PreparedStatement statement = connection.prepareStatement(sql);
    try {
      statement.setString(1, "%" + fileKey + "%");
      statement.setInt(2, idEVento);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        ServiceFile serviceFile = new ServiceFile(resultSet.getInt("id_service_file"));
        listaArquivos.add(getArquivo(serviceFile).get());
      }
      return listaArquivos;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha pesquisando arquivos do evento: id = " + idEVento, e);
    } finally {
      statement.close();
    }
  }

  public void setService(Service service) {
    this.service = service;
  }

}
