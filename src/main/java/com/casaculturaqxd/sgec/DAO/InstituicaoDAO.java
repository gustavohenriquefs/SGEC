package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.casaculturaqxd.sgec.models.Instituicao;

public class InstituicaoDAO {
  private Connection conn;
  
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
        instituicao.setDescricaoContribuicao(resultado.getString("descricao_contribuicao"));
        instituicao.setValorContribuicao(resultado.getInt("valor_contribuicao"));
      }

      statement.close();
    } catch (Exception e) {
      return Optional.empty();
    }

    return Optional.of(instituicao);
  }

  public boolean inserirInstituicao(Instituicao instituicao) throws SQLException {
    String inserirInstituicaoQuery = "INSERT INTO instituicao (nome_instituicao, descricao_contribuicao, valor_contribuicao) VALUES (?, ?, ?)";
    
    try {
      PreparedStatement statement = conn.prepareStatement(inserirInstituicaoQuery);
      
      statement.setString(1, instituicao.getNome());
      statement.setString(2, instituicao.getDescricaoContribuicao());
      statement.setFloat(3, instituicao.getValorContribuicao());

      statement.execute();
      statement.close();
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  public boolean atualizarInstituicao(Instituicao instituicao) throws SQLException {
    String atualizarInstituicaoQuery = "UPDATE instituicao SET nome_instituicao=?, descricao_contribuicao=?, valor_contribuicao=? WHERE id_instituicao=?";

    try {
      PreparedStatement statement = conn.prepareStatement(atualizarInstituicaoQuery);

      statement.setString(1, instituicao.getNome());
      statement.setString(2, instituicao.getDescricaoContribuicao());
      statement.setFloat(3, instituicao.getValorContribuicao());
      statement.setInt(4, instituicao.getIdInstituicao());

      statement.execute();
      statement.close();
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  public boolean removerInstituicao(Instituicao instituicao) throws SQLException {
    String removerInstituicaoQuery = "DELETE FROM instituicao WHERE id_instituicao=?";

    try {
      PreparedStatement statement = conn.prepareStatement(removerInstituicaoQuery);

      statement.setInt(1, instituicao.getIdInstituicao());

      statement.execute();
      statement.close();
    } catch (Exception e) {
      return false;
    }

    return true;
  }
}