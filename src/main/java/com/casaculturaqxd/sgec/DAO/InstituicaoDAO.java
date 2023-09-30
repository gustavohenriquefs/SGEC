package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.casaculturaqxd.sgec.enums.Atribuicao;
import com.casaculturaqxd.sgec.models.Instituicao;

public class InstituicaoDAO {
  private Connection conn;
  
  public void setConnection(Connection conn) {
    this.conn = conn;
  }

  public Optional<Instituicao> getInstituicao(Instituicao instituicao) {
    String getInstituicaoQuery = "SELECT * FROM instituicao WHERE id_instituicao=?";

    try {
      PreparedStatement statement = conn.prepareStatement(getInstituicaoQuery);

      statement.setInt(1, instituicao.getIdInstituicao());

      ResultSet resultado = statement.executeQuery();
      
      if (resultado.next()) {
        instituicao.setIdInstituicao(resultado.getInt("id_instituicao"));
        instituicao.setAtribuicao(Atribuicao.valueOf(resultado.getString("atribuicao")));
        instituicao.setNome(resultado.getString("nome"));
        instituicao.setDescricaoContribuicao(resultado.getString("descricao_contribuicao"));
        instituicao.setValorContribuicao(resultado.getInt("valor_contribuicao"));
      }

      statement.close();
    } catch (Exception e) {
      return Optional.empty();
    }

    return Optional.of(instituicao);
  }

}