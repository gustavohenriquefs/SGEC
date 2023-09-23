package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.casaculturaqxd.sgec.jdbc.ConnectionFactory;
import com.casaculturaqxd.sgec.models.User;

public class UserDAO {
  private Connection connection; 

  public UserDAO(String urlDataBase, String nomeUsuario, String senha){
    this.connection = new ConnectionFactory(urlDataBase,nomeUsuario,senha).conectar();
}

  public Connection getConnection() {
    return connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public boolean inserir(User obj){
    try {
      //1° passo - criar comando sql
      String sql = "insert into tb_clientes (nome,email,senha)"
              + " values(?,?,?)";
      //2° passo - conectar o banco de dados e organizar o comando sql
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, obj.getNome_usuario());
      stmt.setString(2, obj.getEmail());
      stmt.setString(3, obj.getSenha());
      //3° passo - executar o comando sql
      stmt.execute();
      stmt.close();
      return true;
    } catch (SQLException e) {
      return false;
    }
    
  }



}
