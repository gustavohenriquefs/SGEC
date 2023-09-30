package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.casaculturaqxd.sgec.models.Localizacao;

public class LocalizacaoDAO {
  private Connection connection;

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public Localizacao getLocalizacao(Localizacao obj){
    String sql = "SELECT * FROM localizacao WHERE idLocalizacao=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, obj.getIdLocalizacao());
      System.out.println(stmt);
      ResultSet resultado = stmt.executeQuery();
      if (resultado.next()) {
        obj.setIdLocalizacao(resultado.getInt("id_localizacao"));
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
    } catch (Exception e) {
      return null;
    }
  }

  boolean inserirLocalizacao(Localizacao obj){
    try {
      String sql = "insert into localizacao (id_localizacao,rua,numero_rua,bairro,cep,cidade,estado,pais)"
              + " values(?,?,?,?,?,?,?,?)";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, obj.getIdLocalizacao());
      stmt.setString(2, obj.getRua());
      stmt.setInt(3, obj.getNumeroRua());
      stmt.setString(4, obj.getBairro());
      stmt.setString(5, obj.getCep());
      stmt.setString(6, obj.getCidade());
      stmt.setString(7, obj.getEstado());
      stmt.setString(8, obj.getPais());
      stmt.execute();
      stmt.close();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  boolean updateLocalizacao(Localizacao obj){
    String sql = "UPDATE localizacao SET rua=?,numero_rua=?,bairro=?,cep=?,cidade=?,estado=?,pais=? WHERE id_localizacao=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, obj.getRua());
      stmt.setInt(2, obj.getNumeroRua());
      stmt.setString(3, obj.getBairro());
      stmt.setString(4, obj.getCep());
      stmt.setString(5, obj.getCidade());
      stmt.setString(6, obj.getEstado());
      stmt.setString(7, obj.getPais());
      stmt.setInt(8, obj.getIdLocalizacao());
      stmt.execute();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  boolean deletarLocalizacao(Localizacao obj){
    String sql = "DELETE FROM localizacao WHERE id_localizacao=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, obj.getIdLocalizacao());
      stmt.execute();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
