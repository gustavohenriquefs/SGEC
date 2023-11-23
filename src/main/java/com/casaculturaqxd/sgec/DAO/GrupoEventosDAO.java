package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import com.casaculturaqxd.sgec.builder.GrupoEventosBuilder;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Meta;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class GrupoEventosDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public GrupoEventosDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean inserirGrupoEventos(GrupoEventos grupoEventos) throws SQLException {
        String sql = "INSERT INTO grupo_eventos VALUES()";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {

            return true;
        } catch (Exception exception) {
            String nomeGrupoEventosCausa = "";
            if (grupoEventos != null) {
                nomeGrupoEventosCausa = grupoEventos.getNome() != null ? grupoEventos.getNome() : " ";
            }
            throw new SQLException("falha ao inserir grupo de eventos " + nomeGrupoEventosCausa, exception);

        }

        finally {
            statement.close();
        }
    }

    public Optional<GrupoEventos> getGrupoEventos(GrupoEventos grupoEventos) throws SQLException {
        String sql = "SELECT * FROM GRUPO_EVENTOS WHERE id_grupo_eventos = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        try {
            preparedStatement.setInt(1, grupoEventos.getIdGrupoEventos());
            ResultSet resultSet = preparedStatement.executeQuery();
            ServiceFileDAO serviceFileDAO = new ServiceFileDAO(connection);
            if (resultSet.next()) {
                ServiceFile resultFile = new ServiceFile(resultSet.getInt("id_service_file"));
                GrupoEventosBuilder grupoEventosBuilder = new GrupoEventosBuilder();
                grupoEventosBuilder.setIdGrupoEventos(resultSet.getInt("id_grupo_eventos"))
                        .setNome(resultSet.getString("nome_grupo_eventos"))
                        .setImagemCapa(serviceFileDAO.getArquivo(resultFile))
                        .setDescricao(resultSet.getString("descricao"))
                        .setClassificacaoEtaria(resultSet.getString("classificacao_etaria"))
                        .setDataInicial(resultSet.getDate("data_inicial")).setDataFinal(resultSet.getDate("data_final"))
                        .setNumAcoesEsperado(resultSet.getInt("num_acoes_esperado"))
                        .setNumAcoesAlcancado(resultSet.getInt("num_acoes_alcancado"))
                        .setPublicoEsperado(resultSet.getInt("publico_esperado"))
                        .setPublicoAlcancado(resultSet.getInt("publico_alcancado"))
                        .setNumMunicipiosEsperado(resultSet.getInt("num_municipios_esperado"))
                        .setNumMunicipiosAlcancado(resultSet.getInt("num_municipios_alcancado"))
                        .setNumParticipantesEsperado(resultSet.getInt("num_participantes_esperado"))
                        .setNumParticipantesAlcancado(resultSet.getInt("num_participantes_alcancado"));

                return Optional.of(grupoEventosBuilder.getGrupoEventos());
            } else {
                return Optional.empty();
            }
        } catch (Exception exception) {
            String nomeGrupoEventosCausa = "";
            if (grupoEventos != null) {
                nomeGrupoEventosCausa = grupoEventos.getNome() != null ? grupoEventos.getNome() : " ";
            }
            throw new SQLException("falha ao buscar grupo de eventos " + nomeGrupoEventosCausa, exception);

        } finally {
            preparedStatement.close();
        }
    }

    public Optional<GrupoEventos> getPreviewGrupoEventos(GrupoEventos grupoEventos) throws SQLException {
        String sql = "SELECT * FROM GRUPO_EVENTOS WHERE id_grupo_eventos = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        try {
            preparedStatement.setInt(1, grupoEventos.getIdGrupoEventos());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                GrupoEventosBuilder grupoEventosBuilder = new GrupoEventosBuilder();
                grupoEventosBuilder.setIdGrupoEventos(resultSet.getInt("id_grupo_eventos"))
                        .setNome(resultSet.getString("nome_grupo_eventos"))
                        .setImagemCapa(new ServiceFile(resultSet.getInt("id_service_file")))
                        .setDataInicial(resultSet.getDate("data_inicial"))
                        .setDataFinal(resultSet.getDate("data_final"));

                return Optional.of(grupoEventosBuilder.getGrupoEventos());
            } else {
                return Optional.empty();
            }
        } catch (Exception exception) {
            String nomeGrupoEventosCausa = "";
            if (grupoEventos != null) {
                nomeGrupoEventosCausa = grupoEventos.getNome() != null ? grupoEventos.getNome() : " ";
            }
            throw new SQLException("falha ao buscar grupo de eventos " + nomeGrupoEventosCausa, exception);
        } finally {
            preparedStatement.close();
        }
    }

    /**
     * chama o metodo de listar preview com um limite padrao de 5 grupos de eventos
     *
     * @return lista de grupos de eventos somente com os atributos necessarios para
     *         preview
     * @throws SQLException
     */
    public ArrayList<GrupoEventos> listUltimosGrupoEventos() throws SQLException {
        return listPreviewGrupoEventos(null, false, 5);
    }

    private ArrayList<GrupoEventos> listPreviewGrupoEventos(String colunaOrdenacao, boolean ascending, int limit)
            throws SQLException {
        if (colunaOrdenacao == null) {
            colunaOrdenacao = "cadastrado_em";
        }
        String sql = "SELECT id_grupo_eventos,nome_grupo_eventos,data_inicial,data_final,id_service_file FROM grupo_eventos";

        String ordering = colunaOrdenacao == null ? " ORDER BY " + colunaOrdenacao : " ";
        String limitClause = limit > 0 ? " LIMIT " + String.valueOf(limit) : " ";
        sql += ordering += limitClause;
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ArrayList<GrupoEventos> listaPreviewGrupoEventos = new ArrayList<>();
        try {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                GrupoEventosBuilder grupoEventosBuilder = new GrupoEventosBuilder();
                grupoEventosBuilder.setIdGrupoEventos(resultSet.getInt("id_grupo_eventos"))
                        .setNome(resultSet.getString("nome_grupo_eventos"))
                        .setImagemCapa(new ServiceFile(resultSet.getInt("id_service_file")))
                        .setDataInicial(resultSet.getDate("data_inicial"))
                        .setDataFinal(resultSet.getDate("data_final"));

                listaPreviewGrupoEventos.add(grupoEventosBuilder.getGrupoEventos());
            }
            return listaPreviewGrupoEventos;
        } catch (Exception exception) {
            throw new SQLException("falha ao listar grupos de eventos ", exception);
        } finally {
            preparedStatement.close();
        }
    }

    public ArrayList<GrupoEventos> pesquisaPreviewGrupoEventos(String nome, String cidade, String classficacaoEtaria,
            Date dataInicio, Date dataFim, boolean acessivelLibras, ArrayList<Meta> metas) {
        String sql = "SELECT FROM grupo_eventos";
        String conditions = null;
        sql += conditions != null ? " WHERE " + conditions : " ";
        return null;
    }

    public boolean updateGrupoEventos(GrupoEventos grupoEventos) throws SQLException {
        String sql = """
                    UPDATE grupo_eventos
                    SET
                    nome_grupo_eventos = ?,
                    descricao = ?,
                    classificacao_etaria = ?,
                    publico_esperado = ?,
                    publico_alcancado = ?
                    num_acoes_alcancado = ?
                    num_municipios_alcancado = ?
                    num_participantes_alcancado = ?
                    num_acoes_esperado = ?
                    num_municipios_esperado = ?
                    num_participantes_esperado = ?
                    id_service_file = ?
                    data_inicial = ?
                    data_final = ?
                WHERE
                id_grupo_eventos = ?
                    """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        try {
            preparedStatement.setString(1, grupoEventos.getNome());

            preparedStatement.setInt(14, grupoEventos.getIdGrupoEventos());
            int numAtualizacoes = preparedStatement.executeUpdate();
            return numAtualizacoes > 0;
        } catch (Exception e) {
            String nomeGrupoEventosCausa = "";
            if (grupoEventos != null) {
                nomeGrupoEventosCausa = grupoEventos.getNome() != null ? grupoEventos.getNome() : " ";
            }
            throw new SQLException("falha ao atualizar grupo de eventos " + nomeGrupoEventosCausa, e);
        } finally {
            preparedStatement.close();
        }
    }

    public boolean deleteGrupoEventos(GrupoEventos grupoEventos) {
        String sql = "DELETE FROM grupo_eventos WHERE id_grupo_eventos = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, grupoEventos.getIdGrupoEventos());

            int numRemocoes = statement.executeUpdate();
            statement.close();
            return numRemocoes > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<Evento> listEventos(GrupoEventos grupoEventos) {
        EventoDAO eventoDAO = new EventoDAO(connection);
        return null;
    }

    public ArrayList<Meta> listMetas(GrupoEventos grupoEventos) {
        MetaDAO metaDAO = new MetaDAO(connection);
        return metaDAO.listarMetasGrupoEventos(grupoEventos.getIdGrupoEventos());
    }

    public boolean vincularEvento(GrupoEventos grupoEventos, Evento evento) {
        EventoDAO eventoDAO = new EventoDAO(connection);
        return false;
    }

    public boolean desvincularEvento(GrupoEventos grupoEventos, Evento evento) {
        EventoDAO eventoDAO = new EventoDAO(connection);
        return false;
    }

    public boolean vincularOrganizador(GrupoEventos grupoEventos, Instituicao organizador) {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
        return false;
    }

    public boolean desvincularOrganizador(GrupoEventos grupoEventos, Instituicao organizador) {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
        return false;
    }

    public boolean vincularColaborador(GrupoEventos grupoEventos, Instituicao colaborador) {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
        return false;
    }

    public boolean desvincularColaborador(GrupoEventos grupoEventos, Instituicao colaborador) {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
        return false;
    }

    public boolean vincularMeta(Meta meta, GrupoEventos grupoEventos) {
        MetaDAO metaDAO = new MetaDAO(connection);
        return metaDAO.vincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos());
    }

    public boolean desvincularMeta(Meta meta, GrupoEventos grupoEventos) {
        MetaDAO metaDAO = new MetaDAO(connection);
        return metaDAO.desvincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos());
    }
}
