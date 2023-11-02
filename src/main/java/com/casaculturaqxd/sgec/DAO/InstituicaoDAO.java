package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.casaculturaqxd.sgec.models.Instituicao;

public class InstituicaoDAO {
  private Connection conn;

  public Connection getConn() {
    return conn;
  }

  public void setConnection(Connection conn) {
    this.conn = conn;

    try {
      conn.setAutoCommit(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Optional<Instituicao> getInstituicao(Instituicao instituicao) {
    String getInstituicaoQuery = "SELECT * FROM instituicao WHERE id_instituicao=?";

    try {
      PreparedStatement statement = conn.prepareStatement(getInstituicaoQuery);

      statement.setInt(1, instituicao.getIdInstituicao());

      ResultSet resultado = statement.executeQuery();

      if (resultado.next()) {
        instituicao.setIdInstituicao(resultado.getInt("id_instituicao"));
        instituicao.setNome(resultado.getString("nome_instituicao"));
        instituicao.setIdServiceFile(resultado.getInt("id_service_file"));
      } else {
        return Optional.empty();
      }

      statement.close();
    } catch (Exception e) {
      return Optional.empty();
    }

    return Optional.of(instituicao);
  }

  public Optional<Instituicao> getInstituicao(String nome) {
    String getInstituicaoQuery = "SELECT * FROM instituicao WHERE nome_instituicao=?";
    Instituicao product = new Instituicao();
    try {
      PreparedStatement statement = conn.prepareStatement(getInstituicaoQuery);

      statement.setString(1, nome);

      ResultSet resultado = statement.executeQuery();

      if (resultado.next()) {
        product.setIdInstituicao(resultado.getInt("id_instituicao"));
        product.setNome(resultado.getString("nome_instituicao"));
        product.setIdServiceFile(resultado.getInt("id_service_file"));
      } else {
        return Optional.empty();
      }

      statement.close();
    } catch (Exception e) {
      return Optional.empty();
    }

    return Optional.of(product);
  }

  public boolean inserirInstituicao(Instituicao instituicao) throws SQLException {
    String inserirInstituicaoQuery =
        "INSERT INTO instituicao (nome_instituicao, id_service_file) VALUES (?, ?)";

    try {
      PreparedStatement statement =
          conn.prepareStatement(inserirInstituicaoQuery, Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, instituicao.getNome());
      statement.setObject(2, instituicao.getIdServiceFile(), Types.INTEGER);

      statement.executeUpdate();

      ResultSet rs = statement.getGeneratedKeys();
      if (rs.next()) {
        instituicao.setIdInstituicao(rs.getInt("id_instituicao"));
      }
      statement.close();
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }

    return true;
  }

  public boolean atualizarInstituicao(Instituicao instituicao) throws SQLException {
    String atualizarInstituicaoQuery =
        "UPDATE instituicao SET nome_instituicao=?, id_service_file=? WHERE id_instituicao=?";

    try {
      PreparedStatement statement = conn.prepareStatement(atualizarInstituicaoQuery);

      statement.setString(1, instituicao.getNome());
      statement.setObject(2, instituicao.getIdServiceFile());
      statement.setInt(3, instituicao.getIdInstituicao());

      int numAtualizacoes = statement.executeUpdate();
      statement.close();

      return numAtualizacoes > 0;
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  public boolean removerInstituicao(Instituicao instituicao) throws SQLException {
    String removerInstituicaoQuery = "DELETE FROM instituicao WHERE id_instituicao=?";

    try {
      PreparedStatement statement = conn.prepareStatement(removerInstituicaoQuery);

      statement.setInt(1, instituicao.getIdInstituicao());

      int numRemocoes = statement.executeUpdate();
      statement.close();

      return numRemocoes > 0;
    } catch (Exception e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  public boolean vincularOrganizador(Instituicao instituicao, Integer idEvento) {
    try {
      String sql =
          "insert into organizador_evento (id_instituicao,id_evento,descricao_contribuicao, valor_contribuicao)"
              + " values(?, ?, ?, ?)";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, instituicao.getIdInstituicao());
      stmt.setInt(2, idEvento);
      stmt.setString(3, instituicao.getDescricaoContribuicao());
      stmt.setString(4, instituicao.getValorContribuicao());
      stmt.execute();
      stmt.close();
      return true;
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQL");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    } catch (NullPointerException e) {
      Logger erro = Logger.getLogger("ID faltando");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  public boolean vincularOrganizador(Integer idInstituicao, Integer idEvento,
      String descricaoContribuicao, String valorContribuicao) {
    try {
      String sql =
          "insert into organizador_evento (id_instituicao,id_evento,descricao_contribuicao, valor_contribuicao)"
              + " values(?, ?, ?, ?)";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, idInstituicao);
      stmt.setInt(2, idEvento);
      stmt.setString(3, descricaoContribuicao);
      stmt.setString(4, valorContribuicao);
      stmt.execute();
      stmt.close();
      return true;
    } catch (SQLException e) {
      return false;
    } catch (NullPointerException e) {
      Logger erro = Logger.getLogger("ID faltando");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  public boolean vincularColaborador(Instituicao instituicao, Integer idEvento) {
    try {
      String sql =
          "insert into colaborador_evento (id_instituicao,id_evento,descricao_contribuicao, valor_contribuicao)"
              + " values(?, ?, ?, ?)";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, instituicao.getIdInstituicao());
      stmt.setInt(2, idEvento);
      stmt.setString(3, instituicao.getDescricaoContribuicao());
      stmt.setString(4, instituicao.getValorContribuicao());
      stmt.execute();
      stmt.close();
      return true;
    } catch (SQLException e) {
      return false;
    } catch (NullPointerException e) {
      Logger erro = Logger.getLogger("ID faltando");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  public boolean vincularColaborador(Integer idInstituicao, Integer idEvento,
      String descricaoContribuicao, String valorContribuicao) {
    try {
      String sql =
          "insert into colaborador_evento (id_instituicao,id_evento,descricao_contribuicao, valor_contribuicao)"
              + " values(?, ?, ?, ?)";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, idInstituicao);
      stmt.setInt(2, idEvento);
      stmt.setString(3, descricaoContribuicao);
      stmt.setString(4, valorContribuicao);
      stmt.execute();
      stmt.close();
      return true;
    } catch (SQLException e) {
      return false;
    } catch (NullPointerException e) {
      Logger erro = Logger.getLogger("ID faltando");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  public boolean atualizarOrganizador(Instituicao instituicao, Integer idEvento) {
    try {
      String sql = "UPDATE organizador_evento SET descricao_contribuicao=?, valor_contribuicao=?"
          + " WHERE id_instituicao = ? AND id_evento = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, instituicao.getDescricaoContribuicao());
      stmt.setString(2, instituicao.getValorContribuicao());
      stmt.setInt(3, instituicao.getIdInstituicao());
      stmt.setInt(4, idEvento);

      int numAtualizacoes = stmt.executeUpdate();
      stmt.close();
      return numAtualizacoes > 0;
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQL");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    } catch (NullPointerException e) {
      Logger erro = Logger.getLogger("ID faltando");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  public boolean atualizarColaborador(Instituicao instituicao, Integer idEvento) {
    try {
      String sql = "UPDATE colaborador_evento SET descricao_contribuicao=?, valor_contribuicao=?"
          + " WHERE id_instituicao = ? AND id_evento = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, instituicao.getDescricaoContribuicao());
      stmt.setString(2, instituicao.getValorContribuicao());
      stmt.setInt(3, instituicao.getIdInstituicao());
      stmt.setInt(4, idEvento);

      int numAtualizacoes = stmt.executeUpdate();
      stmt.close();
      return numAtualizacoes > 0;
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQL");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    } catch (NullPointerException e) {
      Logger erro = Logger.getLogger("ID faltando");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    } catch (Exception e) {
      Logger outraExcecao = Logger.getLogger("metodo executou");
      outraExcecao.log(Level.SEVERE, "excecao nao capturada adequadamente", e);
      return false;
    }
  }

  public boolean desvincularOrganizador(Integer idInstituicao, Integer idEvento) {
    String desvincularOrganizadorQuery =
        "DELETE FROM organizador_evento WHERE id_instituicao=? AND id_evento=?";

    try {
      PreparedStatement stmt = conn.prepareStatement(desvincularOrganizadorQuery);
      stmt.setInt(1, idInstituicao);
      stmt.setInt(2, idEvento);

      int numRemocoes = stmt.executeUpdate();
      stmt.close();
      return numRemocoes > 0;

    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    } catch (NullPointerException e) {
      Logger erro = Logger.getLogger("ID faltando");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  public boolean desvincularColaborador(Integer idInstituicao, Integer idEvento) {
    String desvincularColaboradorQuery =
        "DELETE FROM colaborador_evento WHERE id_instituicao=? AND id_evento=?";

    try {
      PreparedStatement stmt = conn.prepareStatement(desvincularColaboradorQuery);
      stmt.setInt(1, idInstituicao);
      stmt.setInt(2, idEvento);

      int numRemocoes = stmt.executeUpdate();
      stmt.close();
      return numRemocoes > 0;

    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    } catch (NullPointerException e) {
      Logger erro = Logger.getLogger("ID faltando");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }
}
