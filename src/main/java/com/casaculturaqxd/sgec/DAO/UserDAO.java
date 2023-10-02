package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.casaculturaqxd.sgec.models.User;

public class UserDAO {
  private Connection connection; 

  public Connection getConnection() {
    return connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public boolean inserir(User obj){
    try {
      //1° passo - criar comando sql
      String sql = "insert into usuario (id_usuario,nome_usuario,email,senha,editor)"
              + " values(?,?,?,?,?)";
      //2° passo - conectar o banco de dados e organizar o comando sql
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, obj.getIdUsuario());
      stmt.setString(2, obj.getNomeUsuario());
      stmt.setString(3, obj.getEmail());
      stmt.setString(4, obj.getSenha());
      stmt.setBoolean(5, obj.isEditor());
      //3° passo - executar o comando sql
      stmt.execute();
      stmt.close();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  public User getUsuario(User obj){
    String sql = "SELECT * FROM usuario WHERE id_usuario=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, obj.getIdUsuario());
      System.out.println(stmt);
      ResultSet resultado = stmt.executeQuery();
      if (resultado.next()) {
        obj.setIdUsuario(resultado.getInt("id_usuario"));
        obj.setNomeUsuario(resultado.getString("nome_usuario"));
        obj.setEmail(resultado.getString("email"));
        obj.setSenha(resultado.getString("senha"));
        obj.setEditor(resultado.getBoolean("editor"));
        return obj;
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }

  public boolean validar(User obj){
    String sql = "SELECT * FROM usuario WHERE email=? and senha=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, obj.getEmail());
      stmt.setString(2, obj.getSenha());
      ResultSet resultado = stmt.executeQuery();
      if (resultado.next()) {
        obj.setIdUsuario(resultado.getInt("id_usuario"));
        obj.setNomeUsuario(resultado.getString("nome_usuario"));
        obj.setEmail(resultado.getString("email"));
        obj.setSenha(resultado.getString("senha"));
        obj.setEditor(resultado.getBoolean("editor"));
        return true;
      } 
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean update(User obj){
    String sql = "UPDATE usuario SET nome_usuario=?, email=?, senha=?, editor=? WHERE id_usuario=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, obj.getNomeUsuario());
      stmt.setString(2, obj.getEmail());
      stmt.setString(3, obj.getSenha());
      stmt.setBoolean(4, obj.isEditor());
      stmt.setInt(5, obj.getIdUsuario());
      stmt.execute();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean deletar(User obj){
    String sql = "DELETE FROM usuario WHERE id_usuario=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, obj.getIdUsuario());
      stmt.execute();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
