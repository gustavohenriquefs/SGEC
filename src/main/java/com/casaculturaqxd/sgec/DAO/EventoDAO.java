package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.SortedSet;

public class EventoDAO {
  private Connection connection;

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

 }