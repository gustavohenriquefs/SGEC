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
      String sql = "insert into user (id_usuario,nome_usuario,email,senha,editor)"
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
    String sql = "SELECT * FROM user WHERE id=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, obj.getIdUsuario());
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
    User usuario = getUsuario(obj);
    if(usuario != null){
      if(obj.getEmail().equals(usuario.getEmail()) && obj.getSenha().equals(usuario.getSenha())){
        return true;
      }
      return false;
    }
    return false;
  }

  public boolean update(User obj){
    return true;
  }

}
