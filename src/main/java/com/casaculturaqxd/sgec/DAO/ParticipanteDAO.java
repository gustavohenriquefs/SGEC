package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.io.ByteArrayInputStream;

import com.casaculturaqxd.sgec.models.Participante;

public class ParticipanteDAO {
  private Connection conn;    

  public ParticipanteDAO(Connection conn) {
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
        participante.setAreaDeAtuacao(resultado.getString("area_de_atuacao"));
        participante.setNome(resultado.getString("nome"));
        participante.setLinkMapaDaCultura(resultado.getString("link_perfil"));
      }
    } catch (Exception e) {
      return Optional.empty();
    }

    return Optional.of(participante);
  }
}
