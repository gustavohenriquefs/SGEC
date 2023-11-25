package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import com.casaculturaqxd.sgec.models.Meta;

public class MetaDAO extends DAO {
    private Connection connection;

    public MetaDAO(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Optional<Meta> getMeta(Meta meta) throws SQLException {
        String queryGet = "SELECT nome_meta FROM meta WHERE id_meta=?";
        PreparedStatement statement = connection.prepareStatement(queryGet);
        try {
            statement.setInt(1, meta.getIdMeta());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                meta.setNomeMeta(resultSet.getString("nome_meta"));
                return Optional.of(meta);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            String nomeMetaCausa = "";
            if (meta.getNomeMeta() != null) {
                nomeMetaCausa = meta.getNomeMeta();
            }
            logException(e);
            throw new SQLException("falha buscando meta " + nomeMetaCausa, e);
        } finally {
            statement.close();
        }
    }

    public Optional<Meta> getMeta(String nomeMeta) throws SQLException {
        String queryGet = "SELECT id_meta,nome_meta FROM meta WHERE nome_meta ILIKE ?";
        Meta meta = new Meta(nomeMeta);
        PreparedStatement statement = connection.prepareStatement(queryGet);
        try {
            statement.setString(1, nomeMeta);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                meta.setIdMeta(resultSet.getInt("id_meta"));
                return Optional.of(meta);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            String nomeMetaCausa = "";
            if (meta.getNomeMeta() != null) {
                nomeMetaCausa = meta.getNomeMeta();
            }
            logException(e);
            throw new SQLException("falha buscando meta " + nomeMetaCausa, e);
        } finally {
            statement.close();
        }
    }

    public boolean inserirMeta(Meta meta) throws SQLException {
        String insertQuery = "INSERT INTO meta(nome_meta) VALUES(?)";
        PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        try {
            statement.setString(1, meta.getNomeMeta());
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                meta.setIdMeta(resultSet.getInt("id_meta"));
            }
            return true;
        } catch (Exception e) {
            String nomeMetaCausa = "";
            if (meta.getNomeMeta() != null) {
                nomeMetaCausa = meta.getNomeMeta();
            }
            logException(e);
            throw new SQLException("falha inserindo meta " + nomeMetaCausa, e);
        } finally {
            statement.close();
        }
    }

    public boolean updateMeta(Meta meta) throws SQLException {
        String updateQuery = "UPDATE meta SET nome_meta=? WHERE id_meta=?";
        PreparedStatement statement = connection.prepareStatement(updateQuery);

        try {
            statement.setString(1, meta.getNomeMeta());
            statement.setInt(2, meta.getIdMeta());

            int numAtualizacoes = statement.executeUpdate();
            return numAtualizacoes > 0;
        } catch (Exception e) {
            String nomeMetaCausa = "";
            if (meta.getNomeMeta() != null) {
                nomeMetaCausa = meta.getNomeMeta();
            }
            logException(e);
            throw new SQLException("falha atualizando meta " + nomeMetaCausa, e);
        } finally {
            statement.close();
        }

    }

    public boolean deleteMeta(Meta meta) throws SQLException {
        String deleteQuery = "DELETE FROM meta WHERE id_meta=?";
        PreparedStatement statement = connection.prepareStatement(deleteQuery);
        try {
            statement.setInt(1, meta.getIdMeta());

            int numRemocoes = statement.executeUpdate();
            return numRemocoes > 0;
        } catch (Exception e) {
            String nomeMetaCausa = "";
            if (meta.getNomeMeta() != null) {
                nomeMetaCausa = meta.getNomeMeta();
            }
            logException(e);
            throw new SQLException("falha deletando meta " + nomeMetaCausa, e);
        } finally {
            statement.close();
        }

    }

    public ArrayList<Meta> listarMetas() throws SQLException {
        return this.listarMetas(null);
    }

    public ArrayList<Meta> listarMetas(String campoUsadoOrdenar) throws SQLException {
        String queryListarMetas = "SELECT id_meta,nome_meta FROM meta ";
        queryListarMetas += campoUsadoOrdenar != null ? "ORDER BY " + campoUsadoOrdenar : "";
        PreparedStatement statement = connection.prepareStatement(queryListarMetas);

        ArrayList<Meta> listaMetas = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Meta meta = new Meta(resultSet.getInt("id_meta"), resultSet.getString("nome_meta"));
                listaMetas.add(meta);
            }
        } catch (Exception e) {
            logException(e);
            throw new SQLException("falha listando metas ordenadas por " + campoUsadoOrdenar, e);
        } finally {
            statement.close();
        }
        return listaMetas;
    }

    public ArrayList<Meta> listarMetasEvento(Integer idEvento) throws SQLException {
        String queryListarMetas = "SELECT id_meta,nome_meta FROM meta_evento INNER JOIN meta USING(id_meta) WHERE id_evento=?";
        PreparedStatement statement = connection.prepareStatement(queryListarMetas);

        ArrayList<Meta> listaMetas = new ArrayList<>();
        try {
            statement.setInt(1, idEvento);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Meta meta = new Meta(resultSet.getInt("id_meta"), resultSet.getString("nome_meta"));
                listaMetas.add(meta);
            }
        } catch (Exception e) {
            logException(e);
            throw new SQLException("erro listando metas de um evento" + idEvento, e);
        } finally {
            statement.close();
        }
        return listaMetas;
    }

    public ArrayList<Meta> listarMetasGrupoEventos(Integer idGrupoEventos) throws SQLException {
        String queryListarMetas = "SELECT id_meta,nome_meta FROM meta_grupo_eventos INNER JOIN meta USING(id_meta) WHERE id_grupo_eventos=?";
        PreparedStatement statement = connection.prepareStatement(queryListarMetas);

        ArrayList<Meta> listaMetas = new ArrayList<>();
        try {
            statement.setInt(1, idGrupoEventos);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Meta meta = new Meta(resultSet.getInt("id_meta"), resultSet.getString("nome_meta"));
                listaMetas.add(meta);
            }
        } catch (Exception e) {
            throw new SQLException("falha listando metas do grupo de eventos", e);
        } finally {
            statement.close();
        }
        return listaMetas;
    }

    public boolean vincularEvento(Integer idMeta, Integer idEvento) throws SQLException {
        String queryVincularGrupo = "INSERT INTO meta_evento (id_meta,id_evento) VALUES(?,?)";
        PreparedStatement statement = connection.prepareStatement(queryVincularGrupo);
        try {
            statement.setInt(1, idMeta);
            statement.setInt(2, idEvento);
            int numRemocoes = statement.executeUpdate();
            statement.close();

            return numRemocoes > 0;

        } catch (Exception e) {
            logException(e);
            throw new SQLException("falha vinculando meta a evento", e);
        } finally {
            statement.close();
        }
    }

    public boolean vincularGrupoEventos(Integer idMeta, Integer idGrupoEventos) throws SQLException {
        String queryVincularGrupo = "INSERT INTO meta_grupo_eventos(id_meta,id_grupo_eventos) VALUES(?,?)";
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

    public boolean desvincularEvento(Integer idMeta, Integer idEvento) throws SQLException {
        String queryDesvincularEventoMeta = "DELETE FROM meta_evento WHERE id_meta=? AND id_evento=?";
        PreparedStatement statement = connection.prepareStatement(queryDesvincularEventoMeta);
        try {
            statement.setInt(1, idMeta);
            statement.setInt(2, idEvento);
            int numRemocoes = statement.executeUpdate();

            return numRemocoes > 0;
        } catch (Exception e) {
            logException(e);
            throw new SQLException("falha desvinculando meta do evento", e);
        } finally {
            statement.close();
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
