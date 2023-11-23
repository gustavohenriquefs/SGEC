package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.casaculturaqxd.sgec.models.Meta;

public class MetaDAO {
    private Connection connection;

    public MetaDAO(Connection connection) {
        this.connection = connection;
    }

    public Connection getConn() {
        return connection;
    }

    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    public Optional<Meta> getMeta(Meta meta) {
        String queryGet = "SELECT * FROM meta WHERE id_meta=?";
        try {
            PreparedStatement statement = connection.prepareStatement(queryGet);
            statement.setInt(1, meta.getIdMeta());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                meta.setNomeMeta(resultSet.getString("nome_meta"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            Logger erro = Logger.getLogger("erroSQL");
            erro.log(Level.SEVERE, "excecao levantada:", e);

            return Optional.empty();
        }
        return Optional.of(meta);
    }

    public Optional<Meta> getMetaPorNome(String nomeMeta) {
        String queryGet = "SELECT * FROM meta WHERE nome_meta like ?";
        Meta meta = new Meta(nomeMeta);
        try {
            PreparedStatement statement = connection.prepareStatement(queryGet);
            statement.setString(1, nomeMeta);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                meta.setIdMeta(resultSet.getInt("id_meta"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            Logger erro = Logger.getLogger("erroSQL");
            erro.log(Level.SEVERE, "excecao levantada:", e);

            return Optional.empty();
        }
        return Optional.of(meta);
    }

    public boolean inserirMeta(Meta meta) {
        String insertQuery = "INSERT INTO meta(nome_meta) VALUES(?)";
        try {
            PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, meta.getNomeMeta());
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                meta.setIdMeta(resultSet.getInt("id_meta"));
            }
            statement.close();
            return true;
        } catch (SQLException e) {
            Logger erro = Logger.getLogger("erroSQL");
            erro.log(Level.SEVERE, "excecao levantada:", e);

            return false;
        }

    }

    public boolean updateMeta(Meta meta) {
        String updateQuery = "UPDATE meta SET nome_meta=? WHERE id_meta=?";
        try {
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1, meta.getNomeMeta());
            statement.setInt(2, meta.getIdMeta());

            int numAtualizacoes = statement.executeUpdate();
            statement.close();
            return numAtualizacoes > 0;
        } catch (SQLException e) {
            Logger erro = Logger.getLogger("erroSQL");
            erro.log(Level.SEVERE, "excecao levantada:", e);

            return false;
        }

    }

    public boolean deleteMeta(Meta meta) {
        String deleteQuery = "DELETE FROM meta WHERE id_meta=?";
        try {
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, meta.getIdMeta());

            int numRemocoes = statement.executeUpdate();
            statement.close();
            return numRemocoes > 0;
        } catch (SQLException e) {
            Logger erro = Logger.getLogger("erroSQL");
            erro.log(Level.SEVERE, "excecao levantada:", e);

            return false;
        }

    }

    public ArrayList<Meta> listarMetas() {
        return this.listarMetas(null);
    }

    public ArrayList<Meta> listarMetas(String campoUsadoOrdenar) {
        String queryListarMetas = "SELECT * FROM meta ORDER BY ";
        queryListarMetas += campoUsadoOrdenar;

        ArrayList<Meta> listaMetas = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(queryListarMetas);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Meta meta = new Meta(resultSet.getInt("id_meta"), resultSet.getString("nome_meta"));
                listaMetas.add(meta);
            }
            statement.close();
        } catch (SQLException e) {
            Logger erro = Logger.getLogger("erroSQL");
            erro.log(Level.SEVERE, "excecao levantada:", e);

            return null;
        }
        return listaMetas;
    }

    public ArrayList<Meta> listarMetasEvento(Integer idEvento) {
        String queryListarMetas = "SELECT * FROM meta_evento INNER JOIN meta USING(id_meta) WHERE id_evento=?";

        ArrayList<Meta> listaMetas = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(queryListarMetas);
            statement.setInt(1, idEvento);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Meta meta = new Meta(resultSet.getInt("id_meta"), resultSet.getString("nome_meta"));
                listaMetas.add(meta);
            }
            statement.close();
        } catch (SQLException e) {
            Logger erro = Logger.getLogger("erroSQL");
            erro.log(Level.SEVERE, "excecao levantada:", e);

            return null;
        }
        return listaMetas;
    }

    public ArrayList<Meta> listarMetasGrupoEventos(Integer idGrupoEventos) throws SQLException {
        String queryListarMetas = "SELECT * FROM meta_grupo_eventos INNER JOIN meta USING(id_meta) WHERE id_grupo_eventos=?";
        PreparedStatement statement = connection.prepareStatement(queryListarMetas);

        ArrayList<Meta> listaMetas = new ArrayList<>();
        try {
            statement.setInt(1, idGrupoEventos);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Meta meta = new Meta(resultSet.getInt("id_meta"), resultSet.getString("nome_meta"));
                listaMetas.add(meta);
            }
            statement.close();
        } catch (Exception e) {
            throw new SQLException("falha listando metas do grupo de eventos", e);
        } finally {
            statement.close();
        }
        return listaMetas;
    }

    public boolean vincularEvento(Integer idMeta, Integer idEvento) {
        String queryVincularGrupo = "INSERT INTO meta_evento (id_meta,id_evento) VALUES(?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(queryVincularGrupo);
            statement.setInt(1, idMeta);
            statement.setInt(2, idEvento);
            int numRemocoes = statement.executeUpdate();
            statement.close();

            return numRemocoes > 0;

        } catch (SQLException e) {
            Logger erro = Logger.getLogger("erroSQL");
            erro.log(Level.SEVERE, "excecao levantada:", e);

            return false;
        }
    }

    public boolean vincularGrupoEventos(Integer idMeta, Integer idGrupoEventos) throws SQLException {
        String queryVincularGrupo = "INSERT INTO meta_grupo_eventos VALUES(?,?)";
        PreparedStatement statement = connection.prepareStatement(queryVincularGrupo);

        try {
            statement.setInt(1, idMeta);
            statement.setInt(2, idGrupoEventos);
            statement.execute();

            return true;
        } catch (Exception e) {
            throw new SQLException("falha vinculando meta ao grupo de eventos", e);
        } finally {
            statement.close();
        }
    }

    public boolean desvincularEvento(Integer idMeta, Integer idEvento) {
        String queryVincularGrupo = "DELETE FROM meta_evento WHERE id_meta=? AND id_evento=?";
        try {
            PreparedStatement statement = connection.prepareStatement(queryVincularGrupo);
            statement.setInt(1, idMeta);
            statement.setInt(2, idEvento);
            int numRemocoes = statement.executeUpdate();
            statement.close();

            return numRemocoes > 0;

        } catch (SQLException e) {
            Logger erro = Logger.getLogger("erroSQL");
            erro.log(Level.SEVERE, "excecao levantada:", e);

            return false;
        }

    }

    public boolean desvincularGrupoEventos(Integer idMeta, Integer idGrupoEventos) throws SQLException {
        String queryVincularGrupo = "DELETE FROM meta_grupo_eventos WHERE id_meta=? AND id_grupo_eventos=?";
        PreparedStatement statement = connection.prepareStatement(queryVincularGrupo);
        try {
            statement.setInt(1, idMeta);
            statement.setInt(2, idGrupoEventos);
            int numRemocoes = statement.executeUpdate();

            return numRemocoes > 0;

        } catch (Exception e) {
            throw new SQLException("falha desvinculando meta do grupo de eventos", e);
        } finally {
            statement.close();
        }
    }
}
