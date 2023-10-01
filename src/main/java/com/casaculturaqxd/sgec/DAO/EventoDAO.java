package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Localizacao;

public class EventoDAO {
  private Connection connection;

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  private boolean vincularLocais(SortedSet<Integer> locais) {
    for(Integer local: locais) {
      if(!this.vincularLocal(local)) {
        return false;
      }
    }

    return true;
  }
  
  private boolean vincularLocal(Integer local) {
    String vincLocaisSql = "INSERT INTO localizacao_evento(id_localizacao, id_evento) VALUES (?, ?)";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincLocaisSql);
      stmt.setInt(1, local);
      stmt.setInt(2, 1);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  private boolean vincularOrganizadores(SortedSet<Integer> organizadores) {
    for(Integer organizador: organizadores) {
      if(!this.vincularOrganizador(organizador)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularOrganizador(Integer organizador) {
    String vincOrganizadoresSql = "INSERT INTO organizador_evento(id_evento, id_instituicao) VALUES (?, ?);";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincOrganizadoresSql);
      stmt.setInt(1, organizador);
      stmt.setInt(2, 1);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  private boolean vincularColaboradores(SortedSet<Integer> colaboradores) {
    for(Integer colaborador: colaboradores) {
      if(!this.vincularColaborador(colaborador)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularColaborador(Integer colaborador) {
    String vincColaboradoresSql = "INSERT INTO colaborador_evento(id_evento, id_instituicao) VALUES (?, ?);";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincColaboradoresSql);
      stmt.setInt(1, colaborador);
      stmt.setInt(2, 1);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }
}
