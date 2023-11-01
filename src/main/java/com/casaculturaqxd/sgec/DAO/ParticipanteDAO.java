package com.casaculturaqxd.sgec.DAO;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.casaculturaqxd.sgec.models.Participante;

public class ParticipanteDAO {
  private Connection conn;

  public void setConnection(Connection conn) {
    this.conn = conn;
  }

  public Optional<Participante> getParticipante(Participante participante) throws SQLException {
    String getParticipanteQuery = "SELECT * FROM participante WHERE id_participante=?";

    try {
      PreparedStatement statement = conn.prepareStatement(getParticipanteQuery);

      statement.setInt(1, participante.getIdParticipante());

      ResultSet resultado = statement.executeQuery();

      if (resultado.next()) {
        participante.setIdParticipante(resultado.getInt("id_participante"));
        participante.setAreaDeAtuacao(resultado.getString("area_atuacao"));
        participante.setNome(resultado.getString("nome"));
        participante.setLinkMapaDaCultura(resultado.getString("link_perfil"));
        participante.setImagemParticipante(
            (ByteArrayInputStream) resultado.getBinaryStream("imagem_preview"));
      }

      statement.close();
    } catch (Exception e) {
      return Optional.empty();
    }

    return Optional.of(participante);
  }

  public boolean inserirParticipante(Participante participante) throws SQLException {
    String inserirParticipanteQuery =
        "INSERT INTO participante (nome, area_atuacao, link_perfil, imagem_preview) VALUES (?, ?, ?, ?)";

    try {
      PreparedStatement statement = conn.prepareStatement(inserirParticipanteQuery);

      statement.setString(1, participante.getNome());
      statement.setString(2, participante.getAreaDeAtuacao());
      statement.setString(3, participante.getLinkMapaDaCultura());
      statement.setBlob(4, participante.getImagemParticipante());

      statement.execute();
      statement.close();
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      conn.rollback();

      return false;

    } finally {
      conn.commit();
    }

    return true;
  }

  public boolean updateParticipante(Participante participante) throws SQLException {
    String atualizarParticipanteQuery =
        "UPDATE participante SET nome=?, area_atuacao=?, imagem_preview=?, link_perfil=? WHERE id_participante=?";

    try {
      PreparedStatement statement = conn.prepareStatement(atualizarParticipanteQuery);

      statement.setString(1, participante.getNome());
      statement.setString(2, participante.getAreaDeAtuacao());
      statement.setString(3, participante.getLinkMapaDaCultura());
      statement.setBlob(4, participante.getImagemParticipante());
      statement.setInt(5, participante.getIdParticipante());

      statement.execute();
      statement.close();
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      conn.rollback();

      return false;
    } finally {
      conn.commit();
    }

    return true;
  }

  public boolean deletarParticipante(Participante participante) throws SQLException {
    String deletarParticipanteQuery = "DELETE FROM participante WHERE id_participante=?";

    try {
      PreparedStatement statement = conn.prepareStatement(deletarParticipanteQuery);

      statement.setInt(1, participante.getIdParticipante());

      statement.execute();
      statement.close();
    } catch (SQLException e) {
      Logger erro = Logger.getLogger("erroSQl");
      erro.log(Level.SEVERE, "excecao levantada:", e);
      conn.rollback();

      return false;
    } finally {
      conn.commit();
    }

    return true;
  }
}
