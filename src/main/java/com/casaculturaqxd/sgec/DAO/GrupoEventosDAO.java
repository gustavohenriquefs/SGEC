package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
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

    public boolean insertGrupoEventos(GrupoEventos grupoEventos) throws SQLException {
        String sql = """
            INSERT INTO grupo_eventos(
            nome_grupo_eventos,
            descricao,
            classificacao_etaria,
            publico_esperado,
            publico_alcancado,
            num_acoes_esperado,
            num_municipios_esperado,
            num_participantes_esperado,
            num_colaboradores_esperado,
            id_service_file,
            data_inicial,
            data_final
            )
            VALUES(?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?);
            """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        try {
            int idImagemCapa = 0;
            if (grupoEventos.getImagemCapa() != null) {
                idImagemCapa = grupoEventos.getImagemCapa().getServiceFileId();
            }
            preparedStatement.setString(1, grupoEventos.getNome());
            preparedStatement.setString(2, grupoEventos.getDescricao());
            preparedStatement.setObject(3, grupoEventos.getClassificacaoEtaria(), Types.OTHER);
            preparedStatement.setInt(4, grupoEventos.getPublicoEsperado());
            preparedStatement.setInt(5, grupoEventos.getPublicoAlcancado());
            preparedStatement.setInt(6, grupoEventos.getNumAcoesEsperado());
            preparedStatement.setInt(7, grupoEventos.getNumMunicipiosEsperado());
            preparedStatement.setInt(8, grupoEventos.getNumParticipantesEsperado());
            preparedStatement.setInt(9, grupoEventos.getNumColaboradoresEsperado());
            preparedStatement.setObject(10, idImagemCapa != 0 ? idImagemCapa : null, Types.INTEGER);
            preparedStatement.setDate(11, grupoEventos.getDataInicial());
            preparedStatement.setDate(12, grupoEventos.getDataFinal());

            preparedStatement.execute();
            return true;
        } catch (Exception exception) {
            String nomeGrupoEventosCausa = "";
            if (grupoEventos != null) {
                nomeGrupoEventosCausa = grupoEventos.getNome() != null ? grupoEventos.getNome() : " ";
            }
            throw new SQLException("falha ao inserir grupo de eventos " + nomeGrupoEventosCausa, exception);
        } finally {
            preparedStatement.close();
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
                Optional<ServiceFile> resultFile = serviceFileDAO
                    .getArquivo(new ServiceFile(resultSet.getInt("id_service_file")));
                GrupoEventosBuilder grupoEventosBuilder = new GrupoEventosBuilder();
                grupoEventosBuilder.setId(resultSet.getInt("id_grupo_eventos"))
                    .setNome(resultSet.getString("nome_grupo_eventos"))
                    .setDescricao(resultSet.getString("descricao"))
                    .setClassificacaoEtaria(resultSet.getString("classificacao_etaria"))
                    .setCargaHoraria(resultSet.getTime("carga_horaria"))
                    .setDataInicial(resultSet.getDate("data_inicial")).setDataFinal(resultSet.getDate("data_final"))
                    .setNumAcoesEsperado(resultSet.getInt("num_acoes_esperado"))
                    .setNumAcoesAlcancado(resultSet.getInt("num_acoes_alcancado"))
                    .setPublicoEsperado(resultSet.getInt("publico_esperado"))
                    .setPublicoAlcancado(resultSet.getInt("publico_alcancado"))
                    .setNumMunicipiosEsperado(resultSet.getInt("num_municipios_esperado"))
                    .setNumMunicipiosAlcancado(resultSet.getInt("num_municipios_alcancado"))
                    .setNumParticipantesEsperado(resultSet.getInt("num_participantes_esperado"))
                    .setNumParticipantesAlcancado(resultSet.getInt("num_participantes_alcancado"))
                    .setNumColaboradoresEsperado(resultSet.getInt("num_colaboradores_esperado"))
                    .setNumColaboradoresAlcancado(resultSet.getInt("num_colaboradores_alcancado"))
                    .setMetas(listMetas(grupoEventos))
                    .setColaboradores(listColaboradores(grupoEventos))
                    .setOrganizadores(listOrganizadores(grupoEventos))
                    .setEventos(listEventos(grupoEventos));
                if (resultFile.isPresent()) {
                    grupoEventosBuilder.setImagemCapa(resultFile.get());
                }
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
        String sql = "SELECT id_grupo_eventos, nome_grupo_eventos,id_service_file,data_inicial,data_final FROM GRUPO_EVENTOS WHERE id_grupo_eventos = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        try {
            preparedStatement.setInt(1, grupoEventos.getIdGrupoEventos());
            ResultSet resultSet = preparedStatement.executeQuery();
            ServiceFileDAO serviceFileDAO = new ServiceFileDAO(connection);
            if (resultSet.next()) {
                Optional<ServiceFile> resultFile = serviceFileDAO
                    .getArquivo(new ServiceFile(resultSet.getInt("id_service_file")));
                GrupoEventosBuilder grupoEventosBuilder = new GrupoEventosBuilder();
                grupoEventosBuilder.setId(resultSet.getInt("id_grupo_eventos"))
                    .setNome(resultSet.getString("nome_grupo_eventos"))
                    .setDataInicial(resultSet.getDate("data_inicial"))
                    .setDataFinal(resultSet.getDate("data_final"));
                if (resultFile.isPresent()) {
                    grupoEventosBuilder.setImagemCapa(resultFile.get());
                }
                return Optional.of(grupoEventosBuilder.getGrupoEventos());
            } else {
                return Optional.empty();
            }
        } catch (NullPointerException e) {
            return Optional.empty();
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
                ServiceFileDAO serviceFileDAO = new ServiceFileDAO(connection);
                Optional<ServiceFile> optionaImagemCapa = serviceFileDAO
                    .getArquivo(new ServiceFile(resultSet.getInt("id_service_file")));

                int idGrupoEventos = resultSet.getInt("id_grupo_eventos");

                ArrayList<Meta> metas = listMetas(new GrupoEventos(idGrupoEventos));

                GrupoEventosBuilder grupoEventosBuilder = new GrupoEventosBuilder();
                grupoEventosBuilder.setId(idGrupoEventos).setNome(resultSet.getString("nome_grupo_eventos"))
                    .setDataInicial(resultSet.getDate("data_inicial")).setDataFinal(resultSet.getDate("data_final"))
                    .setMetas(metas);

                listaPreviewGrupoEventos.add(grupoEventosBuilder.getGrupoEventos());

                if (optionaImagemCapa.isPresent()) {
                    ServiceFile imagemCapa = optionaImagemCapa.get();
                    imagemCapa.setContent(serviceFileDAO.getContent(imagemCapa));
                    grupoEventosBuilder.setImagemCapa(imagemCapa);
                }
            }
            return listaPreviewGrupoEventos;
        } catch (Exception exception) {
            throw new SQLException("falha ao listar grupos de eventos ", exception);
        } finally {
            preparedStatement.close();
        }
    }

    /**
     * busca o resultado de todos os grupos de eventos contendo a string
     * nomeGrupoEventos no nome, com a classificacao etaria selecionada, no
     * intervalo das datas inicial e final e que atendam a pelo menos uma das metas
     * passadas
     * 
     * @param nomeGrupoEventos
     * @param classificacaoEtaria
     * @param dataInicio
     * @param dataFim
     * @param metas
     * @return lista de grupos de eventos com atributos utilizados para preview
     * @throws SQLException
     */
    public ArrayList<GrupoEventos> pesquisaPreviewGrupoEventos(String nomeGrupoEventos, String classificacaoEtaria,
        Date dataInicio, Date dataFim, ArrayList<Meta> metas) throws SQLException {
        String sql = "SELECT id_grupo_eventos,nome_grupo_eventos,data_inicial,data_final,id_service_file FROM grupo_eventos WHERE nome_grupo_eventos ILIKE ?";
        if (classificacaoEtaria != null) {
            sql += " AND classificacao_etaria = '" + classificacaoEtaria + "' ";
        }
        if (dataInicio != null) {
            sql += "AND data_inicial >= '" + dataInicio.toString() + "' ";
        }
        if (dataFim != null) {
            sql += "AND data_final <= '" + dataFim.toString() + "' ";
        }
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ArrayList<GrupoEventos> listaPreviewGrupoEventos = new ArrayList<>();
        try {
            preparedStatement.setString(1, "%" + nomeGrupoEventos + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                GrupoEventosBuilder grupoEventosBuilder = new GrupoEventosBuilder();
                grupoEventosBuilder.setId(resultSet.getInt("id_grupo_eventos"))
                    .setNome(resultSet.getString("nome_grupo_eventos"))
                    .setImagemCapa(new ServiceFile(resultSet.getInt("id_service_file")))
                    .setDataInicial(resultSet.getDate("data_inicial"))
                    .setDataFinal(resultSet.getDate("data_final"));
                GrupoEventos result = grupoEventosBuilder.getGrupoEventos();
                // se alguma das metas do grupo de eventos estiver nas metas pesquisadas
                if (listMetas(result).stream().anyMatch(meta -> metas.contains(meta)))
                    listaPreviewGrupoEventos.add(grupoEventosBuilder.getGrupoEventos());
            }
            return listaPreviewGrupoEventos;
        } catch (Exception e) {
            throw new SQLException("falha pesquisando grupos de eventos", e);
        } finally {
            preparedStatement.close();
        }
    }

    /**
     * atualiza todas as colunas, exceto as de valores alcancados que sao calculadas
     * por triggers
     * (num_acoes_alcancado,num_municipios_alcancado,num_participantes_alcancado,
     * num_colaboradores_alcancado e num_organizadores_alcancado), do grupo de
     * eventos especificado
     *
     * @param grupoEventos
     * @return true se alguma alteracao for realizada
     * @throws SQLException
     */
    public boolean updateGrupoEventos(GrupoEventos grupoEventos) throws SQLException {
        String sql = """
            UPDATE grupo_eventos
            SET
            nome_grupo_eventos = ?,
            descricao = ?,
            classificacao_etaria = ?,
            publico_esperado = ?,
            publico_alcancado = ?,
            num_acoes_esperado = ?,
            num_municipios_esperado = ?,
            num_participantes_esperado = ?,
            num_colaboradores_esperado = ?,
            id_service_file = ?,
            data_inicial = ?,
            data_final = ?
            WHERE
            id_grupo_eventos = ?
            """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        try {
            int idImagemCapa = 0;
            if (grupoEventos.getImagemCapa() != null) {
                idImagemCapa = grupoEventos.getImagemCapa().getServiceFileId();
            }
            preparedStatement.setString(1, grupoEventos.getNome());
            preparedStatement.setString(2, grupoEventos.getDescricao());
            preparedStatement.setObject(3, grupoEventos.getClassificacaoEtaria(), Types.OTHER);
            preparedStatement.setInt(4, grupoEventos.getPublicoEsperado());
            preparedStatement.setInt(5, grupoEventos.getPublicoAlcancado());
            preparedStatement.setInt(6, grupoEventos.getNumAcoesEsperado());
            preparedStatement.setInt(7, grupoEventos.getNumMunicipiosEsperado());
            preparedStatement.setInt(8, grupoEventos.getNumParticipantesEsperado());
            preparedStatement.setInt(9, grupoEventos.getNumColaboradoresEsperado());
            preparedStatement.setObject(10, idImagemCapa != 0 ? idImagemCapa : null, Types.OTHER);
            preparedStatement.setDate(11, grupoEventos.getDataInicial());
            preparedStatement.setDate(12, grupoEventos.getDataFinal());

            preparedStatement.setInt(13, grupoEventos.getIdGrupoEventos());
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

    public boolean deleteGrupoEventos(GrupoEventos grupoEventos) throws SQLException {
        String sql = "DELETE FROM grupo_eventos WHERE id_grupo_eventos = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        try {
            statement.setInt(1, grupoEventos.getIdGrupoEventos());

            int numRemocoes = statement.executeUpdate();
            statement.close();
            return numRemocoes > 0;
        } catch (Exception e) {
            String nomeGrupoEventosCausa = "";
            if (grupoEventos != null) {
                nomeGrupoEventosCausa = grupoEventos.getNome() != null ? grupoEventos.getNome() : " ";
            }
            throw new SQLException("falha deletando grupo de eventos " + nomeGrupoEventosCausa, e);
        } finally {
            statement.close();
        }
    }

    public ArrayList<Evento> listEventos(GrupoEventos grupoEventos) throws SQLException {
        EventoDAO eventoDAO = new EventoDAO(connection);
        return (ArrayList<Evento>) eventoDAO.listarEventosGrupoEventos(grupoEventos);
    }

    public ArrayList<Meta> listMetas(GrupoEventos grupoEventos) throws SQLException {
        MetaDAO metaDAO = new MetaDAO(connection);
        return metaDAO.listarMetasGrupoEventos(grupoEventos.getIdGrupoEventos());
    }

    public ArrayList<Instituicao> listOrganizadores(GrupoEventos grupoEventos) throws SQLException {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
        return instituicaoDAO.listarOrganizadoresGrupoEventos(grupoEventos.getIdGrupoEventos());
    }

    public ArrayList<Instituicao> listColaboradores(GrupoEventos grupoEventos) throws SQLException {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
        return instituicaoDAO.listarColaboradoresGrupoEventos(grupoEventos.getIdGrupoEventos());
    }

    public boolean vincularEvento(GrupoEventos grupoEventos, Evento evento) throws SQLException {
        EventoDAO eventoDAO = new EventoDAO(connection);
        return eventoDAO.vincularGrupoEventos(grupoEventos.getIdGrupoEventos(), evento.getIdEvento());
    }

    public boolean desvincularEvento(GrupoEventos grupoEventos, Evento evento) throws SQLException {
        EventoDAO eventoDAO = new EventoDAO(connection);
        return eventoDAO.desvincularGrupoEventos(grupoEventos.getIdGrupoEventos(), evento.getIdEvento());
    }

    public boolean vincularAllOrganizadores(GrupoEventos grupoEventos, List<Instituicao> organizadores)
        throws SQLException {
        try {
            for (Instituicao instituicao : organizadores) {
                vincularOrganizador(grupoEventos, instituicao);
            }
            return true;
        } catch (Exception e) {
            throw new SQLException("falha vinculando conjunto de organizadores", e);
        }
    }

    public boolean vincularOrganizador(GrupoEventos grupoEventos, Instituicao organizador) throws SQLException {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
        return instituicaoDAO.vincularOrganizadorGrupoEventos(grupoEventos.getIdGrupoEventos(),
            organizador.getIdInstituicao());
    }

    public boolean desvincularOrganizador(GrupoEventos grupoEventos, Instituicao organizador) throws SQLException {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
        return instituicaoDAO.desvincularOrganizadorGrupoEventos(grupoEventos.getIdGrupoEventos(),
            organizador.getIdInstituicao());
    }

    public boolean vincularAllColaboradores(GrupoEventos grupoEventos, List<Instituicao> colaboradores)
        throws SQLException {
        try {
            for (Instituicao instituicao : colaboradores) {
                vincularColaborador(grupoEventos, instituicao);
            }
            return true;
        } catch (Exception e) {
            throw new SQLException("falha vinculando conjunto de colaboradores", e);
        }
    }

    public boolean vincularColaborador(GrupoEventos grupoEventos, Instituicao colaborador) throws SQLException {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
        return instituicaoDAO.vincularColaboradorGrupoEventos(grupoEventos.getIdGrupoEventos(),
            colaborador.getIdInstituicao());
    }

    public boolean desvincularColaborador(GrupoEventos grupoEventos, Instituicao colaborador) throws SQLException {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
        return instituicaoDAO.desvincularColaboradorGrupoEventos(grupoEventos.getIdGrupoEventos(),
            colaborador.getIdInstituicao());
    }

    public boolean vincularMeta(Meta meta, GrupoEventos grupoEventos) throws SQLException {
        MetaDAO metaDAO = new MetaDAO(connection);
        return metaDAO.vincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos());
    }

    public boolean desvincularMeta(Meta meta, GrupoEventos grupoEventos) throws SQLException {
        MetaDAO metaDAO = new MetaDAO(connection);
        return metaDAO.desvincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos());
    }
}
