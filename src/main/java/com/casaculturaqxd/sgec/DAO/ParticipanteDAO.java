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

    try {
      PreparedStatement statement = conn.prepareStatement(getParticipanteQuery);

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
      } else {
        return Optional.empty();
      }
      statement.close();
    } catch (Exception e) {
      return Optional.empty();
    }

    return Optional.of(participante);
  }

  public boolean inserirParticipante(Participante participante) throws SQLException {
    String inserirParticipanteQuery = "INSERT INTO participante (nome_participante, area_atuacao, bio, link_perfil, id_service_file) VALUES (?, ?, ?, ?, ?)";
    Integer serviceFileId = null;
    if (participante.getImagemCapa() != null)
      serviceFileId = participante.getImagemCapa().getServiceFileId();
    try {
      PreparedStatement statement = conn.prepareStatement(inserirParticipanteQuery, Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, participante.getNome());
      statement.setString(2, participante.getAreaDeAtuacao());
      statement.setString(3, participante.getBio());
      statement.setString(4, participante.getLinkMapaDaCultura());
      statement.setObject(5, serviceFileId, Types.INTEGER);
      statement.executeUpdate();

      ResultSet rs = statement.getGeneratedKeys();
      if (rs.next()) {
        participante.setIdParticipante(rs.getInt("id_participante"));
      }
      statement.close();
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);

      return false;

    }

    return true;
  }

  public boolean updateParticipante(Participante participante) throws SQLException {
    String atualizarParticipanteQuery = "UPDATE participante SET nome_participante=?, area_atuacao=?, bio=?, link_perfil=?, id_service_file =? WHERE id_participante=?";
    Integer serviceFileId = null;
    if (participante.getImagemCapa() != null)
      serviceFileId = participante.getImagemCapa().getServiceFileId();
    try {
      PreparedStatement statement = conn.prepareStatement(atualizarParticipanteQuery);

      statement.setString(1, participante.getNome());
      statement.setString(2, participante.getAreaDeAtuacao());
      statement.setString(3, participante.getBio());
      statement.setString(4, participante.getLinkMapaDaCultura());
      statement.setObject(5, serviceFileId, Types.INTEGER);
      statement.setInt(6, participante.getIdParticipante());

      int numAtualizacoes = statement.executeUpdate();
      statement.close();

      return numAtualizacoes > 0;
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQL");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  public boolean deletarParticipante(Participante participante) throws SQLException {
    String deletarParticipanteQuery = "DELETE FROM participante WHERE id_participante=?";

    try {
      PreparedStatement statement = conn.prepareStatement(deletarParticipanteQuery);

      statement.setInt(1, participante.getIdParticipante());

      int numRemocoes = statement.executeUpdate();
      statement.close();
      return numRemocoes > 0;
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }

  boolean vincularEvento(int idParticipante, int idEvento) {
    try {
      String sql = "insert into participante_evento (id_participante,id_evento)" + " values(?,?)";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, idParticipante);
      stmt.setInt(2, idEvento);
      stmt.execute();
      stmt.close();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  boolean desvincularEvento(Integer idParticipante, Integer idEvento) {
    String vincLocaisSql = "DELETE FROM participante_evento WHERE id_participante=? AND id_evento=?";

    try {
      PreparedStatement stmt = conn.prepareStatement(vincLocaisSql);
      stmt.setInt(1, idParticipante);
      stmt.setInt(2, idEvento);

      int numRemocoes = stmt.executeUpdate();
      stmt.close();
      return numRemocoes > 0;

    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      return false;
    }
  }
}
