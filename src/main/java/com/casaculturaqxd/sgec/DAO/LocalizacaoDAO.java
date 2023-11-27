package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import com.casaculturaqxd.sgec.models.Localizacao;

public class LocalizacaoDAO extends DAO {
  private Connection connection;

  public LocalizacaoDAO() {

  }

  public LocalizacaoDAO(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() {
    return connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public Optional<Localizacao> getLocalizacao(Localizacao localizacao) throws SQLException {
    String sql = "SELECT id_localizacao,nome_localizacao,rua,numero_rua,bairro,cep,cidade,estado,pais FROM localizacao WHERE id_localizacao=?";
    PreparedStatement statement = connection.prepareStatement(sql);

    try {
      statement.setInt(1, localizacao.getIdLocalizacao());
      ResultSet resultado = statement.executeQuery();
      if (resultado.next()) {
        localizacao.setIdLocalizacao(resultado.getInt("id_localizacao"));
        localizacao.setNome(resultado.getString("nome_localizacao"));
        localizacao.setRua(resultado.getString("rua"));
        localizacao.setNumeroRua(resultado.getInt("numero_rua"));
        localizacao.setBairro(resultado.getString("bairro"));
        localizacao.setCep(resultado.getString("cep"));
        localizacao.setCidade(resultado.getString("cidade"));
        localizacao.setEstado(resultado.getString("estado"));
        localizacao.setPais(resultado.getString("pais"));
        return Optional.of(localizacao);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      String nomeLocalCausa = localizacao != null && localizacao.getNome() != null ? localizacao.getNome() : "";
      logException(e);
      throw new SQLException("falha buscando local " + nomeLocalCausa, e);
    } finally {
      statement.close();
    }
  }

  public boolean inserirLocalizacao(Localizacao localizacao) throws SQLException {
    String sql = "insert into localizacao (nome_localizacao,rua,numero_rua,bairro,cep,cidade,estado,pais)"
        + " values(?,?,?,?,?,?,?,?)";
    PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    try {

      statement.setString(1, localizacao.getNome());
      statement.setString(2, localizacao.getRua());
      statement.setInt(3, localizacao.getNumeroRua());
      statement.setString(4, localizacao.getBairro());
      statement.setString(5, localizacao.getCep());
      statement.setString(6, localizacao.getCidade());
      statement.setString(7, localizacao.getEstado());
      statement.setString(8, localizacao.getPais());
      int numInsercoes = statement.executeUpdate();

      ResultSet rs = statement.getGeneratedKeys();
      if (rs.next()) {
        localizacao.setIdLocalizacao(rs.getInt("id_localizacao"));
      }

      return numInsercoes > 0;
    } catch (Exception e) {
      String nomeLocalCausa = localizacao != null && localizacao.getNome() != null ? localizacao.getNome() : "";
      logException(e);
      throw new SQLException("falha inserindo local " + nomeLocalCausa, e);
    } finally {
      statement.close();
    }
  }

  boolean updateLocalizacao(Localizacao localizacao) throws SQLException {
    String sql = "UPDATE localizacao SET nome_localizacao=?,rua=?,numero_rua=?,bairro=?,cep=?,cidade=?,estado=?,pais=? WHERE id_localizacao=?";
    PreparedStatement statement = connection.prepareStatement(sql);

    try {
      statement.setString(1, localizacao.getNome());
      statement.setString(2, localizacao.getRua());
      statement.setInt(3, localizacao.getNumeroRua());
      statement.setString(4, localizacao.getBairro());
      statement.setString(5, localizacao.getCep());
      statement.setString(6, localizacao.getCidade());
      statement.setString(7, localizacao.getEstado());
      statement.setString(8, localizacao.getPais());
      statement.setInt(9, localizacao.getIdLocalizacao());

      int numModificacoes = statement.executeUpdate();
      statement.close();
      return numModificacoes > 0;

    } catch (Exception e) {
      String nomeLocalCausa = localizacao != null && localizacao.getNome() != null ? localizacao.getNome() : "";
      logException(e);
      throw new SQLException("falha atualizando local " + nomeLocalCausa, e);
    } finally {
      statement.close();
    }
  }

  boolean deletarLocalizacao(Localizacao localizacao) throws SQLException {
    String sql = "DELETE FROM localizacao WHERE id_localizacao=?";
    PreparedStatement statement = connection.prepareStatement(sql);

    try {
      statement.setInt(1, localizacao.getIdLocalizacao());

      int numRemocoes = statement.executeUpdate();
      statement.close();
      return numRemocoes > 0;
    } catch (Exception e) {
      String nomeLocalCausa = localizacao != null && localizacao.getNome() != null ? localizacao.getNome() : "";
      logException(e);
      throw new SQLException("falha deletando local " + nomeLocalCausa, e);
    } finally {
      statement.close();
    }
  }

  boolean vincularEvento(int idLocalizacao, int idEvento) throws SQLException {
    String sql = "insert into localizacao_evento (id_localizacao,id_evento)" + " values(?,?)";
    PreparedStatement stmt = connection.prepareStatement(sql);
    try {

      stmt.setInt(1, idLocalizacao);
      stmt.setInt(2, idEvento);
      int numInsercoes = stmt.executeUpdate();
      return numInsercoes > 0;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha vinculando local a evento ", e);
    } finally {
      stmt.close();
    }
  }

  public ArrayList<Localizacao> pesquisarLocalizacao(String nomeCidade) throws SQLException {
    String sql = "select id_localizacao,nome_localizacao,rua,numero_rua,bairro,cep,cidade,estado,pais from localizacao where cidade like ?";
    PreparedStatement statement = connection.prepareStatement(sql);

    ArrayList<Localizacao> localizacoes = new ArrayList<>();
    try {
      statement.setString(1, "%" + nomeCidade + "%");
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Localizacao localizacao = new Localizacao();
        localizacao.setIdLocalizacao(resultSet.getInt("id_localizacao"));
        localizacao.setEstado(resultSet.getString("estado"));
        localizacao.setRua(resultSet.getString("rua"));
        localizacao.setBairro(resultSet.getString("bairro"));
        localizacao.setNumeroRua(resultSet.getInt("numero_rua"));
        localizacao.setPais(resultSet.getString("pais"));
        localizacao.setCidade(resultSet.getString("cidade"));
        localizacao.setCep(resultSet.getString("cep"));
        localizacao.setNome(resultSet.getString("nome_localizacao"));
        localizacoes.add(localizacao);
      }
      return localizacoes;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha pesquisando locais por nome de cidade " + nomeCidade != null ? nomeCidade : "", e);
    } finally {
      statement.close();
    }
  }

  boolean desvincularEvento(Integer idLocalizacao, Integer idEvento) throws SQLException {
    String vincLocaisSql = "DELETE FROM localizacao_evento WHERE id_localizacao=? AND id_evento=?";
    PreparedStatement stmt = connection.prepareStatement(vincLocaisSql);

    try {
      stmt.setInt(1, idLocalizacao);
      stmt.setInt(2, idEvento);

      int numRemocoes = stmt.executeUpdate();
      return numRemocoes > 0;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha desvinculando local de evento ", e);
    } finally {
      stmt.close();
    }
  }

}
