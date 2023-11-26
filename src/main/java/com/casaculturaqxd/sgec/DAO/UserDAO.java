
package com.casaculturaqxd.sgec.DAO;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import com.casaculturaqxd.sgec.models.User;

public class UserDAO extends DAO {
  private Connection connection;

  public UserDAO(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() {
    return connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public boolean inserir(User user) throws SQLException {
    String sql = "insert into usuario (nome_usuario,email,senha,editor)" + " values(?,?,?,?)";
    PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

    try {
      statement.setString(1, user.getNomeUsuario());
      statement.setString(2, user.getEmail());
      statement.setString(3, encriptar(user.getSenha()));
      statement.setBoolean(4, user.isEditor());

      int numInsercoes = statement.executeUpdate();

      ResultSet rs = statement.getGeneratedKeys();
      if (rs.next()) {
        user.setIdUsuario(rs.getInt("id_usuario"));
      }
      return numInsercoes > 0;
    } catch (Exception e) {
      String nomeUsuarioCausa = user.getNomeUsuario() != null ? user.getNomeUsuario() : "";
      logException(e);
      throw new SQLException("falha inserindo usuario " + nomeUsuarioCausa, e);
    } finally {
      statement.close();
    }
  }

  public Optional<User> getUsuario(User user) throws SQLException {
    String sql = "SELECT id_usuario,nome_usuario,email,senha,editor FROM usuario WHERE id_usuario=?";
    PreparedStatement statement = connection.prepareStatement(sql);
    try {
      statement.setInt(1, user.getIdUsuario());
      ResultSet resultado = statement.executeQuery();
      if (resultado.next()) {
        user.setIdUsuario(resultado.getInt("id_usuario"));
        user.setNomeUsuario(resultado.getString("nome_usuario"));
        user.setEmail(resultado.getString("email"));
        user.setSenha(resultado.getString("senha"));
        user.setEditor(resultado.getBoolean("editor"));
        return Optional.of(user);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      logException(e);
      throw new SQLException("", e);
    } finally {
      statement.close();
    }

  }

  /**
   * Encripta a string passada como senha
   * 
   * @param senha
   * @return
   * @throws NoSuchAlgorithmException
   */
  private String encriptar(String senha) throws NoSuchAlgorithmException {
    if (senha == null) {
      return null;
    }
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] asHex = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
    BigInteger number = new BigInteger(1, asHex);
    StringBuilder hexBuilder = new StringBuilder(number.toString(16));
    return hexBuilder.toString();
  }

  public boolean usuarioExists(String email) throws SQLException {
    String sql = "SELECT email FROM usuario WHERE email = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    try {
      preparedStatement.setString(1, email);
      ResultSet resultSet = preparedStatement.executeQuery();
      return resultSet.next();
    } catch (Exception e) {
      throw new SQLException("falha buscando nome de usuario", e);
    } finally {
      preparedStatement.close();
    }
  }

<<<<<<< HEAD
  public boolean usuarioExists(String email) throws SQLException {
    String sql = "SELECT email FROM usuario WHERE email = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    try {
      preparedStatement.setString(1, email);
      ResultSet resultSet = preparedStatement.executeQuery();
      return resultSet.next();
    } catch (Exception e) {
      throw new SQLException("falha buscando nome de usuario", e);
    } finally {
      preparedStatement.close();
    }
  }

  public boolean validar(User obj) throws NoSuchAlgorithmException {
    String sql = "SELECT * FROM usuario WHERE email=? and senha=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, obj.getEmail());
      stmt.setString(2, encriptar(obj.getSenha()));
      ResultSet resultado = stmt.executeQuery();
      if (resultado.next()) {
        obj.setIdUsuario(resultado.getInt("id_usuario"));
        obj.setNomeUsuario(resultado.getString("nome_usuario"));
        obj.setEmail(resultado.getString("email"));
        obj.setSenha(resultado.getString("senha"));
        obj.setEditor(resultado.getBoolean("editor"));
=======
  public boolean validar(User user) throws SQLException {
    String sql = "SELECT id_usuario,nome_usuario,email,senha,editor FROM usuario WHERE email=? and senha=?";
    PreparedStatement statement = connection.prepareStatement(sql);
>>>>>>> 285285cfa2ec5e334c6b770f0fd8e22658029a50

    try {
      statement.setString(1, user.getEmail());
      statement.setString(2, encriptar(user.getSenha()));
      ResultSet resultado = statement.executeQuery();
      if (resultado.next()) {
        user.setIdUsuario(resultado.getInt("id_usuario"));
        user.setNomeUsuario(resultado.getString("nome_usuario"));
        user.setEmail(resultado.getString("email"));
        user.setSenha(resultado.getString("senha"));
        user.setEditor(resultado.getBoolean("editor"));

        this.connection.setReadOnly(!user.isEditor());
        return true;
      }
      return false;
    } catch (Exception e) {
      throw new SQLException("falha validando usuario", e);
    } finally {
      statement.close();
    }
  }

  public boolean update(User user) throws SQLException {
    String sql = "UPDATE usuario SET nome_usuario=?, email=?, senha=?, editor=? WHERE id_usuario=?";
    PreparedStatement stmt = connection.prepareStatement(sql);

    try {
      stmt.setString(1, user.getNomeUsuario());
      stmt.setString(2, user.getEmail());
      stmt.setString(3, encriptar(user.getSenha()));
      stmt.setBoolean(4, user.isEditor());
      stmt.setInt(5, user.getIdUsuario());

      int numModificacoes = stmt.executeUpdate();
      return numModificacoes > 0;

    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha atualizando usuario", e);
    }
  }

  public boolean deletar(User user) throws SQLException {
    String sql = "DELETE FROM usuario WHERE id_usuario=?";
    PreparedStatement statement = connection.prepareStatement(sql);

    try {
      statement.setInt(1, user.getIdUsuario());
      int numRemocoes = statement.executeUpdate();
      return numRemocoes > 0;
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha deletando usuario", e);
    } finally {
      statement.close();
    }
  }
}
