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
}
