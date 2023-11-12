package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Localizacao;

public class LocalizacaoDAO {
  private Connection connection;

  public Connection getConnection() {
    return connection;
  }
  
  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public Localizacao getLocalizacao(Localizacao obj) {
    String sql = "SELECT * FROM localizacao WHERE id_localizacao=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, obj.getIdLocalizacao());
      ResultSet resultado = stmt.executeQuery();
      if (resultado.next()) {
        obj.setIdLocalizacao(resultado.getInt("id_localizacao"));
        obj.setNome(resultado.getString("nome_localizacao"));
        obj.setRua(resultado.getString("rua"));
        obj.setNumeroRua(resultado.getInt("numero_rua"));
        obj.setBairro(resultado.getString("bairro"));
        obj.setCep(resultado.getString("cep"));
        obj.setCidade(resultado.getString("cidade"));
        obj.setEstado(resultado.getString("estado"));
        obj.setPais(resultado.getString("pais"));
        return obj;
      } else {
        return null;
      }
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return null;
    }
  }

  public boolean inserirLocalizacao(Localizacao obj) {
    try {
      String sql = "insert into localizacao (nome_localizacao,rua,numero_rua,bairro,cep,cidade,estado,pais)"
              + " values(?,?,?,?,?,?,?,?)";
      PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      stmt.setString(1, obj.getNome());
      stmt.setString(2, obj.getRua());
      stmt.setInt(3, obj.getNumeroRua());
      stmt.setString(4, obj.getBairro());
      stmt.setString(5, obj.getCep());
      stmt.setString(6, obj.getCidade());
      stmt.setString(7, obj.getEstado());
      stmt.setString(8, obj.getPais());
      stmt.executeUpdate();
      
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        obj.setIdLocalizacao(rs.getInt("id_localizacao"));
      }

      stmt.close();
      return true;
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }


  boolean updateLocalizacao(Localizacao obj){
    String sql = "UPDATE localizacao SET nome_localizacao=?,rua=?,numero_rua=?,bairro=?,cep=?,cidade=?,estado=?,pais=? WHERE id_localizacao=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, obj.getNome());
      stmt.setString(2, obj.getRua());
      stmt.setInt(3, obj.getNumeroRua());
      stmt.setString(4, obj.getBairro());
      stmt.setString(5, obj.getCep());
      stmt.setString(6, obj.getCidade());
      stmt.setString(7, obj.getEstado());
      stmt.setString(8, obj.getPais());
      stmt.setInt(9, obj.getIdLocalizacao());

      int numModificacoes = stmt.executeUpdate();
      stmt.close();
      return numModificacoes>0;
      
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  boolean deletarLocalizacao(Localizacao obj) {
    String sql = "DELETE FROM localizacao WHERE id_localizacao=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, obj.getIdLocalizacao());
      
      int numRemocoes = stmt.executeUpdate();
      stmt.close();
      return numRemocoes>0;
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  boolean vincularEvento(int idLocalizacao, int idEvento) {
    try {
      String sql = "insert into localizacao_evento (id_localizacao,id_evento)" + " values(?,?)";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, idLocalizacao);
      stmt.setInt(2, idEvento);
      stmt.execute();
      stmt.close();
      return true;
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  public ArrayList<Localizacao> pesquisarLocalizacao(String nomeCidade){
    String sql = "select * from localizacao where cidade like ?";
    ArrayList<Localizacao> localizacaos = new ArrayList<>();
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, "%"+nomeCidade+"%");
      ResultSet resultSet = stmt.executeQuery();

      while (resultSet.next()) {
        Localizacao localizacao = new Localizacao();
        localizacao.setIdLocalizacao(resultSet.getInt("id_localizacao"));
        localizacao.setEstado(resultSet.getString("estado"));
        localizacao.setRua(resultSet.getString("rua"));
        localizacao.setBairro(resultSet.getString("bairro"));
        localizacao.setNumeroRua(resultSet.getInt("numero_rua"));
        localizacao.setPais(resultSet.getString("pais"));
        localizacao.setCidade(resultSet.getString("cidade"));
        localizacao.setCep(resultSet.getString("cep"));
        localizacao.setNome(resultSet.getString("nome_localizacao"));
        localizacaos.add(localizacao);
      }
      stmt.close();
      return localizacaos;
    } catch (SQLException e) {
      return null;
    }
  }

  public boolean verificaLocalidade(int id_evento, int id_localizacao){
    String sql = "select * from localizacao_evento where id_evento = ? and id_localizacao = ?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, id_evento);
      stmt.setInt(2, id_localizacao);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        stmt.close();
        return true;
      }
      stmt.close();
      return false;
    } catch (SQLException e) {
      return false;
    }
  }
  boolean desvincularEvento(Integer idLocalizacao, Integer idEvento) {
    String vincLocaisSql = "DELETE FROM localizacao_evento WHERE id_localizacao=? AND id_evento=?";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincLocaisSql);
      stmt.setInt(1, idLocalizacao);
      stmt.setInt(2, idEvento);

      int numRemocoes = stmt.executeUpdate();
      stmt.close();
      return numRemocoes>0;
      
    } catch (SQLException e) {
      Logger erro  = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }

  }

}
