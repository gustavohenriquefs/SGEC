package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;

import com.casaculturaqxd.sgec.jdbc.ConnectionFactory;
import com.casaculturaqxd.sgec.models.User;

public class UserDAO {
  private Connection connection; 

  public UserDAO(){
    this.connection = new ConnectionFactory("","","").conectar();
}

  public Connection getConnection() {
    return connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public boolean inserir(User user){
  
    return true;
  }

}
