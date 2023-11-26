package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Optional;

import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class InstituicaoDAO extends DAO {
  private Connection conn;

  public InstituicaoDAO(Connection connection) {
    conn = connection;
  }

  public Connection getConn() {
    return conn;
  }

  public void setConnection(Connection conn) {
    this.conn = conn;

  }

  public Optional<Instituicao> getInstituicao(Instituicao instituicao) throws SQLException {
    String getInstituicaoQuery = "SELECT id_instituicao,nome_instituicao,id_service_file FROM instituicao WHERE id_instituicao=?";
    ServiceFileDAO serviceFileDAO = new ServiceFileDAO(conn);
    PreparedStatement statement = conn.prepareStatement(getInstituicaoQuery);
    try {
      statement.setInt(1, instituicao.getIdInstituicao());

      ResultSet resultado = statement.executeQuery();

      if (resultado.next()) {
        ServiceFile imagemCapa = new ServiceFile(resultado.getInt("id_service_file"));
        if (imagemCapa.getServiceFileId() > 0) {
          // setar a capa somente se o arquivo existir
          instituicao.setImagemCapa(serviceFileDAO.getArquivo(imagemCapa).get());
        }
        instituicao.setIdInstituicao(resultado.getInt("id_instituicao"));
        instituicao.setNome(resultado.getString("nome_instituicao"));

        return Optional.of(instituicao);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha buscando instituicao", e);
    } finally {
      statement.close();
    }
  }

  public Optional<Instituicao> getInstituicao(String nome) throws SQLException {
    String getInstituicaoQuery = "SELECT id_instituicao,nome_instituicao,id_service_file FROM instituicao WHERE nome_instituicao=?";
    Instituicao instituicao = new Instituicao();
    ServiceFileDAO serviceFileDAO = new ServiceFileDAO(conn);

    PreparedStatement statement = conn.prepareStatement(getInstituicaoQuery);
    try {

      statement.setString(1, nome);

      ResultSet resultado = statement.executeQuery();

      if (resultado.next()) {
        ServiceFile imagemCapa = new ServiceFile(resultado.getInt("id_service_file"));
        if (imagemCapa.getServiceFileId() > 0) {
          // setar a capa somente se o arquivo existir
          instituicao.setImagemCapa(serviceFileDAO.getArquivo(imagemCapa).get());
        }

        instituicao.setIdInstituicao(resultado.getInt("id_instituicao"));
        instituicao.setNome(resultado.getString("nome_instituicao"));

        return Optional.of(instituicao);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha buscando instituicao " + nome, e);
    } finally {
      statement.close();
    }
  }

  public boolean inserirInstituicao(Instituicao instituicao) throws SQLException {
    String inserirInstituicaoQuery = "INSERT INTO instituicao (nome_instituicao, id_service_file) VALUES (?, ?)";
    PreparedStatement statement = conn.prepareStatement(inserirInstituicaoQuery, Statement.RETURN_GENERATED_KEYS);
    try {
      Integer idServiceFile = null;
      if (instituicao.getImagemCapa() != null) {
        instituicao.getImagemCapa().getServiceFileId();
      }
      statement.setString(1, instituicao.getNome());
      statement.setObject(2, idServiceFile, Types.INTEGER);

      int numInsercoes = statement.executeUpdate();

      ResultSet rs = statement.getGeneratedKeys();
      if (rs.next()) {
        instituicao.setIdInstituicao(rs.getInt("id_instituicao"));
      }
      return numInsercoes > 0;
    } catch (Exception e) {
      String nomeInstituicaoCausa = instituicao != null && instituicao.getNome() != null ? instituicao.getNome() : "";
      throw new SQLException("falha inserindo instituicao" + nomeInstituicaoCausa, e);
    } finally {
      statement.close();
    }
  }

  public boolean atualizarInstituicao(Instituicao instituicao) throws SQLException {
    String atualizarInstituicaoQuery = "UPDATE instituicao SET nome_instituicao=?, id_service_file=? WHERE id_instituicao=?";
    PreparedStatement statement = conn.prepareStatement(atualizarInstituicaoQuery);

    try {
      Integer idServiceFile = null;
      if (instituicao.getImagemCapa() != null) {
        instituicao.getImagemCapa().getServiceFileId();
      }
      statement.setString(1, instituicao.getNome());
      statement.setObject(2, idServiceFile, Types.INTEGER);
      statement.setInt(3, instituicao.getIdInstituicao());

      int numAtualizacoes = statement.executeUpdate();

      return numAtualizacoes > 0;
    } catch (Exception e) {
      String nomeInstituicaoCausa = instituicao != null && instituicao.getNome() != null ? instituicao.getNome() : "";
      logException(e);
      throw new SQLException("falha atualizando instituicao " + nomeInstituicaoCausa, e);
    } finally {
      statement.close();
    }
  }

  public boolean removerInstituicao(Instituicao instituicao) throws SQLException {
    String removerInstituicaoQuery = "DELETE FROM instituicao WHERE id_instituicao=?";
    PreparedStatement statement = conn.prepareStatement(removerInstituicaoQuery);

    try {
      statement.setInt(1, instituicao.getIdInstituicao());

      int numRemocoes = statement.executeUpdate();

      return numRemocoes > 0;
    } catch (Exception e) {
      String nomeInstituicaoCausa = instituicao != null && instituicao.getNome() != null ? instituicao.getNome() : "";
      logException(e);
      throw new SQLException("falha deletando instituicao " + nomeInstituicaoCausa, e);
    } finally {
      statement.close();
    }
  }

  public ArrayList<Instituicao> listarOrganizadoresEvento(int idEvento) throws SQLException {
    String sql = "SELECT id_instituicao, nome_instituicao, descricao_contribuicao,valor_contribuicao,id_service_file FROM organizador_evento INNER JOIN instituicao USING(id_instituicao) WHERE id_evento=?";

    ArrayList<Instituicao> listaOrganizadores = new ArrayList<>();
    ServiceFileDAO serviceFileDAO = new ServiceFileDAO(getConn());
    PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    try {
      statement.setInt(1, idEvento);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Instituicao organizador = new Instituicao();
        ServiceFile imagemCapa = new ServiceFile(resultSet.getInt("id_service_file"));
        if (imagemCapa.getServiceFileId() > 0) {
          // setar a capa somente se o arquivo existir
          organizador.setImagemCapa(serviceFileDAO.getArquivo(imagemCapa).get());
        }
        organizador.setIdInstituicao(resultSet.getInt("id_instituicao"));
        organizador.setNome(resultSet.getString("nome_instituicao"));
        organizador.setDescricaoContribuicao(resultSet.getString("descricao_contribuicao"));
        organizador.setValorContribuicao(resultSet.getString("valor_contribuicao"));

        listaOrganizadores.add(organizador);
      }
      return listaOrganizadores;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha listando organizadores de evento", e);
    } finally {
      statement.close();
    }
  }

  public ArrayList<Instituicao> listarColaboradoresEvento(int idEvento) throws SQLException {
    String sql = "SELECT id_instituicao, nome_instituicao, descricao_contribuicao,valor_contribuicao,id_service_file FROM colaborador_evento INNER JOIN instituicao USING(id_instituicao) WHERE id_evento=?";
    ArrayList<Instituicao> listaColaboradores = new ArrayList<>();
    ServiceFileDAO serviceFileDAO = new ServiceFileDAO(getConn());
    PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    try {
      statement.setInt(1, idEvento);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Instituicao colaborador = new Instituicao();
        ServiceFile imagemCapa = new ServiceFile(resultSet.getInt("id_service_file"));
        if (imagemCapa.getServiceFileId() > 0) {
          // setar a capa somente se o arquivo existir
          colaborador.setImagemCapa(serviceFileDAO.getArquivo(imagemCapa).get());
        }

        colaborador.setIdInstituicao(resultSet.getInt("id_instituicao"));
        colaborador.setNome(resultSet.getString("nome_instituicao"));
        colaborador.setDescricaoContribuicao(resultSet.getString("descricao_contribuicao"));
        colaborador.setValorContribuicao(resultSet.getString("valor_contribuicao"));

        listaColaboradores.add(colaborador);
      }
      return listaColaboradores;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha listando colaboradores de evento", e);
    } finally {
      statement.close();
    }
  }

  public boolean vincularOrganizadorEvento(Instituicao instituicao, Integer idEvento) throws SQLException {
    String sql = "insert into organizador_evento (id_instituicao,id_evento,descricao_contribuicao, valor_contribuicao)"
        + " values(?, ?, ?, ?)";
    PreparedStatement statement = conn.prepareStatement(sql);
    try {

      statement.setInt(1, instituicao.getIdInstituicao());
      statement.setInt(2, idEvento);
      statement.setString(3, instituicao.getDescricaoContribuicao());
      statement.setString(4, instituicao.getValorContribuicao());
      statement.execute();
      statement.close();
      return true;
    } catch (Exception e) {
      String nomeInstituicaoCausa = instituicao != null && instituicao.getNome() != null ? instituicao.getNome() : "";
      logException(e);
      throw new SQLException("falha vinculando organizador " + nomeInstituicaoCausa + " a evento ", e);
    } finally {
      statement.close();
    }
  }

  public boolean vincularOrganizadorEvento(Integer idInstituicao, Integer idEvento, String descricaoContribuicao,
      String valorContribuicao) throws SQLException {
    String sql = "insert into organizador_evento (id_instituicao,id_evento,descricao_contribuicao, valor_contribuicao)"
        + " values(?, ?, ?, ?)";
    PreparedStatement statement = conn.prepareStatement(sql);
    try {

      statement.setInt(1, idInstituicao);
      statement.setInt(2, idEvento);
      statement.setString(3, descricaoContribuicao);
      statement.setString(4, valorContribuicao);
      int numInsercoes = statement.executeUpdate();
      return numInsercoes > 0;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha vinculando organizador a evento ", e);
    } finally {
      statement.close();
    }
  }

  public boolean vincularColaboradorEvento(Instituicao instituicao, Integer idEvento) throws SQLException {
    String sql = "insert into colaborador_evento (id_instituicao,id_evento,descricao_contribuicao, valor_contribuicao)"
        + " values(?, ?, ?, ?)";
    PreparedStatement statement = conn.prepareStatement(sql);
    try {

      statement.setInt(1, instituicao.getIdInstituicao());
      statement.setInt(2, idEvento);
      statement.setString(3, instituicao.getDescricaoContribuicao());
      statement.setString(4, instituicao.getValorContribuicao());
      int numInsercoes = statement.executeUpdate();
      return numInsercoes > 0;
    } catch (Exception e) {
      String nomeInstituicaoCausa = instituicao != null && instituicao.getNome() != null ? instituicao.getNome() : "";
      logException(e);
      throw new SQLException("falha vinculando colaborador " + nomeInstituicaoCausa + "a evento", e);
    } finally {
      statement.close();
    }
  }

  public boolean vincularColaboradorEvento(Integer idInstituicao, Integer idEvento, String descricaoContribuicao,
      String valorContribuicao) throws SQLException {
    String sql = "insert into colaborador_evento (id_instituicao,id_evento,descricao_contribuicao, valor_contribuicao)"
        + " values(?, ?, ?, ?)";
    PreparedStatement statement = conn.prepareStatement(sql);
    try {

      statement.setInt(1, idInstituicao);
      statement.setInt(2, idEvento);
      statement.setString(3, descricaoContribuicao);
      statement.setString(4, valorContribuicao);
      int numInsercoes = statement.executeUpdate();
      return numInsercoes > 0;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha vinculando colaborador a evento", e);
    } finally {
      statement.close();
    }
  }

  public boolean atualizarOrganizadorEvento(Instituicao instituicao, Integer idEvento) throws SQLException {
    String sql = "UPDATE organizador_evento SET descricao_contribuicao=?, valor_contribuicao=?"
        + " WHERE id_instituicao = ? AND id_evento = ?";
    PreparedStatement stmt = conn.prepareStatement(sql);
    try {

      stmt.setString(1, instituicao.getDescricaoContribuicao());
      stmt.setString(2, instituicao.getValorContribuicao());
      stmt.setInt(3, instituicao.getIdInstituicao());
      stmt.setInt(4, idEvento);

      int numAtualizacoes = stmt.executeUpdate();
      return numAtualizacoes > 0;
    } catch (Exception e) {
      logException(e);
      throw new SQLException(null, e);
    } finally {
      stmt.close();
    }
  }

  public boolean atualizarColaboradorEvento(Instituicao instituicao, Integer idEvento) throws SQLException {
    String sql = "UPDATE colaborador_evento SET descricao_contribuicao=?, valor_contribuicao=?"
        + " WHERE id_instituicao = ? AND id_evento = ?";
    PreparedStatement stmt = conn.prepareStatement(sql);
    try {

      stmt.setString(1, instituicao.getDescricaoContribuicao());
      stmt.setString(2, instituicao.getValorContribuicao());
      stmt.setInt(3, instituicao.getIdInstituicao());
      stmt.setInt(4, idEvento);

      int numAtualizacoes = stmt.executeUpdate();
      return numAtualizacoes > 0;
    } catch (Exception e) {
      String nomeInstituicaoCausa = instituicao != null && instituicao.getNome() != null ? instituicao.getNome() : "";
      logException(e);
      throw new SQLException("falha atualizando colaborador " + nomeInstituicaoCausa, e);
    } finally {
      stmt.close();
    }
  }

  public boolean desvincularOrganizadorEvento(Integer idInstituicao, Integer idEvento) throws SQLException {
    String desvincularOrganizadorQuery = "DELETE FROM organizador_evento WHERE id_instituicao=? AND id_evento=?";
    PreparedStatement statement = conn.prepareStatement(desvincularOrganizadorQuery);

    try {
      statement.setInt(1, idInstituicao);
      statement.setInt(2, idEvento);

      int numRemocoes = statement.executeUpdate();
      statement.close();
      return numRemocoes > 0;

    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha desvinculando instituicao de evento", e);
    } finally {
      statement.close();
    }
  }

  public boolean desvincularColaboradorEvento(Integer idInstituicao, Integer idEvento) throws SQLException {
    String desvincularColaboradorQuery = "DELETE FROM colaborador_evento WHERE id_instituicao=? AND id_evento=?";
    PreparedStatement stmt = conn.prepareStatement(desvincularColaboradorQuery);

    try {
      stmt.setInt(1, idInstituicao);
      stmt.setInt(2, idEvento);

      int numRemocoes = stmt.executeUpdate();
      return numRemocoes > 0;

    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha desvinculando colaborador de evento", e);
    } finally {
      stmt.close();
    }
  }

  public boolean vincularOrganizadorGrupoEventos(int idGrupoEventos, int idInstituicao) throws SQLException {
    String sql = "INSERT INTO organizador_grupo_eventos(id_grupo_eventos,id_instituicao) VALUES(?,?)";
    PreparedStatement preparedStatement = conn.prepareStatement(sql);
    try {
      preparedStatement.setInt(1, idGrupoEventos);
      preparedStatement.setInt(2, idInstituicao);

      preparedStatement.execute();
      return true;
    } catch (Exception e) {
      throw new SQLException("falha vinculando organizador a grupo de eventos", e);
    } finally {
      preparedStatement.close();
    }
  }

  public boolean vincularColaboradorGrupoEventos(int idGrupoEventos, int idInstituicao) throws SQLException {
    String sql = "INSERT INTO colaborador_grupo_eventos(id_grupo_eventos,id_instituicao) VALUES(?,?)";
    PreparedStatement preparedStatement = conn.prepareStatement(sql);
    try {
      preparedStatement.setInt(1, idGrupoEventos);
      preparedStatement.setInt(2, idInstituicao);

      preparedStatement.execute();
      return true;
    } catch (Exception e) {
      throw new SQLException("falha vinculando colaborador a grupo de eventos", e);
    } finally {
      preparedStatement.close();
    }
  }

  public boolean desvincularOrganizadorGrupoEventos(int idGrupoEventos, int idInstituicao) throws SQLException {
    String sql = "DELETE FROM organizador_grupo_eventos WHERE id_grupo_eventos =? AND id_instituicao =?";
    PreparedStatement preparedStatement = conn.prepareStatement(sql);
    try {
      preparedStatement.setInt(1, idGrupoEventos);
      preparedStatement.setInt(2, idInstituicao);

      int numRemocoes = preparedStatement.executeUpdate();
      return numRemocoes > 0;
    } catch (Exception e) {
      throw new SQLException("falha vinculando organizador a grupo de eventos", e);
    } finally {
      preparedStatement.close();
    }
  }

  public boolean desvincularColaboradorGrupoEventos(int idGrupoEventos, int idInstituicao) throws SQLException {
    String sql = "DELETE FROM colaborador_grupo_eventos WHERE id_grupo_eventos = ? AND id_instituicao = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(sql);
    try {
      preparedStatement.setInt(1, idGrupoEventos);
      preparedStatement.setInt(2, idInstituicao);

      int numRemocoes = preparedStatement.executeUpdate();
      return numRemocoes > 0;
    } catch (Exception e) {
      throw new SQLException("falha vinculando organizador a grupo de eventos", e);
    } finally {
      preparedStatement.close();
    }
  }
}
