package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Optional;

import com.casaculturaqxd.sgec.models.Participante;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class ParticipanteDAO extends DAO {
  private Connection conn;
  private ServiceFileDAO serviceFileDAO;

  public ParticipanteDAO() {
    serviceFileDAO = new ServiceFileDAO(null);
  }

  public ParticipanteDAO(Connection connection) {
    this.conn = connection;
    serviceFileDAO = new ServiceFileDAO(connection);
  }

  public Connection getConn() {
    return conn;
  }

  public void setConnection(Connection conn) {
    this.conn = conn;
    serviceFileDAO.setConnection(conn);
  }

  public Optional<Participante> getParticipante(Participante participante) throws SQLException {
    String getParticipanteQuery = "SELECT id_participante,nome_participante,area_atuacao,bio,link_perfil,id_service_file FROM participante WHERE id_participante=?";
    PreparedStatement statement = conn.prepareStatement(getParticipanteQuery);

    try {

      statement.setInt(1, participante.getIdParticipante());

      ResultSet resultado = statement.executeQuery();

      if (resultado.next()) {
        ServiceFile resultFile = new ServiceFile(resultado.getInt("id_service_file"));

        participante.setIdParticipante(resultado.getInt("id_participante"));
        participante.setAreaDeAtuacao(resultado.getString("area_atuacao"));
        participante.setNome(resultado.getString("nome_participante"));
        participante.setBio(resultado.getString("bio"));
        participante.setLinkMapaDaCultura(resultado.getString("link_perfil"));
        if (serviceFileDAO.getArquivo(resultFile).isPresent()) {
          participante.setImagemCapa(serviceFileDAO.getArquivo(resultFile).get());
        }
        return Optional.of(participante);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      String nomeParticipanteCausa = participante.getNome() != null ? participante.getNome() : "";
      logException(e);
      throw new SQLException("erro buscando participante " + nomeParticipanteCausa, e);
    } finally {
      statement.close();
    }

  }

  public boolean inserirParticipante(Participante participante) throws SQLException {
    String inserirParticipanteQuery = "INSERT INTO participante (nome_participante, area_atuacao, bio, link_perfil, id_service_file) VALUES (?, ?, ?, ?, ?)";
    PreparedStatement statement = conn.prepareStatement(inserirParticipanteQuery, Statement.RETURN_GENERATED_KEYS);
    try {
      Integer serviceFileId = null;
      if (participante.getImagemCapa() != null) {
        serviceFileId = participante.getImagemCapa().getServiceFileId();
      }
      statement.setString(1, participante.getNome());
      statement.setString(2, participante.getAreaDeAtuacao());
      statement.setString(3, participante.getBio());
      statement.setString(4, participante.getLinkMapaDaCultura());
      statement.setObject(5, serviceFileId, Types.INTEGER);
      statement.executeUpdate();

      ResultSet rs = statement.getGeneratedKeys();
      if (rs.next()) {
        participante.setIdParticipante(rs.getInt("id_participante"));
        return true;
      }
      return false;
    } catch (SQLException e) {
      String nomeParticipanteCausa = participante.getNome() != null ? participante.getNome() : "";

      logException(e);
      throw new SQLException("falha inserindo participante " + nomeParticipanteCausa, e);
    } finally {
      statement.close();
    }
  }

  public boolean updateParticipante(Participante participante) throws SQLException {
    String atualizarParticipanteQuery = "UPDATE participante SET nome_participante=?, area_atuacao=?, bio=?, link_perfil=?, id_service_file =? WHERE id_participante=?";
    PreparedStatement statement = conn.prepareStatement(atualizarParticipanteQuery);

    try {
      Integer serviceFileId = null;
      if (participante.getImagemCapa() != null) {
        serviceFileId = participante.getImagemCapa().getServiceFileId();
      }

      statement.setString(1, participante.getNome());
      statement.setString(2, participante.getAreaDeAtuacao());
      statement.setString(3, participante.getBio());
      statement.setString(4, participante.getLinkMapaDaCultura());
      statement.setObject(5, serviceFileId, Types.INTEGER);
      statement.setInt(6, participante.getIdParticipante());

      int numAtualizacoes = statement.executeUpdate();
      return numAtualizacoes > 0;
    } catch (Exception e) {
      String nomeParticipanteCausa = participante.getNome() != null ? participante.getNome() : "";
      logException(e);
      throw new SQLException("erro atualizando participante " + nomeParticipanteCausa, e);
    } finally {
      statement.close();
    }
  }

  public boolean deletarParticipante(Participante participante) throws SQLException {
    String deletarParticipanteQuery = "DELETE FROM participante WHERE id_participante=?";
    PreparedStatement statement = conn.prepareStatement(deletarParticipanteQuery);
    try {
      statement.setInt(1, participante.getIdParticipante());

      int numRemocoes = statement.executeUpdate();
      statement.close();
      return numRemocoes > 0;
    } catch (SQLException e) {
      String nomeParticipanteCausa = participante.getNome() != null ? participante.getNome() : "";

      logException(e);
      throw new SQLException("falha inserindo participante " + nomeParticipanteCausa, e);
    } finally {
      statement.close();
    }
  }

  boolean vincularEvento(int idParticipante, int idEvento) throws SQLException {
    String sql = "insert into participante_evento (id_participante,id_evento)" + " values(?,?)";
    PreparedStatement statement = conn.prepareStatement(sql);
    try {
      statement.setInt(1, idParticipante);
      statement.setInt(2, idEvento);

      int numInsercoes = statement.executeUpdate();
      return numInsercoes > 0;
    } catch (SQLException e) {
      logException(e);
      throw new SQLException("falha vinculando participante a evento", e);
    } finally {
      statement.close();
    }
  }

  boolean desvincularEvento(Integer idParticipante, Integer idEvento) throws SQLException {
    String vincLocaisSql = "DELETE FROM participante_evento WHERE id_participante=? AND id_evento=?";
    PreparedStatement statement = conn.prepareStatement(vincLocaisSql);

    try {
      statement.setInt(1, idParticipante);
      statement.setInt(2, idEvento);

      int numRemocoes = statement.executeUpdate();
      return numRemocoes > 0;
    } catch (SQLException e) {
      logException(e);
      throw new SQLException("falha vinculando participante ao evento", e);
    } finally {
      statement.close();
    }
  }
}
