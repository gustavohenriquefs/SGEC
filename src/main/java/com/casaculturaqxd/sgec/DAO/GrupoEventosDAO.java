package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public Optional<GrupoEventos> getGrupoEventos(GrupoEventos grupoEventos) {
        String sql = "SELECT * FROM GRUPO_EVENTOS WHERE id_grupo_eventos = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, grupoEventos.getIdGrupoEventos());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int idImagemCapa = resultSet.getInt("id_service_file");
                GrupoEventosBuilder grupoEventosBuilder = new GrupoEventosBuilder();
                grupoEventosBuilder.setNome(resultSet.getString("nome_grupo_eventos"))
                        .setImagemCapa(idImagemCapa != 0 ? new ServiceFile(idImagemCapa) : null)
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

                preparedStatement.close();
                return Optional.of(grupoEventosBuilder.getGrupoEventos());
            } else {
                return Optional.empty();
            }
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public Optional<GrupoEventos> getGrupoEventosPreview(GrupoEventos grupoEventos) {
        String sql = "SELECT * FROM GRUPO_EVENTOS WHERE id_grupo_eventos = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, grupoEventos.getIdGrupoEventos());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                GrupoEventosBuilder grupoEventosBuilder = new GrupoEventosBuilder();
                grupoEventosBuilder.setNome(resultSet.getString("nome_grupo_eventos"))
                        .setImagemCapa(new ServiceFile(resultSet.getInt("id_service_file")))
                        .setDataInicial(resultSet.getDate("data_inicial"))
                        .setDataFinal(resultSet.getDate("data_final"));

                preparedStatement.close();
                return Optional.of(grupoEventosBuilder.getGrupoEventos());
            } else {
                return Optional.empty();
            }
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    /**
     * chama o metodo de listar preview com um limite padrao de 5 grupos de eventos
     *
     * @return lista de grupos de eventos somente com os atributos necessarios para
     *         preview
     */
    public ArrayList<GrupoEventos> listUltimosGrupoEventos() {
        return listPreviewGrupoEventos(null, false, 5);
    }

    private ArrayList<GrupoEventos> listPreviewGrupoEventos(String colunaOrdenacao, boolean ascending, int limit) {
        if (colunaOrdenacao == null) {
            colunaOrdenacao = "cadastrado_em";
        }
        String sql = "SELECT * FROM grupo_eventos";

        String ordering = colunaOrdenacao == null ? " ORDER BY " + colunaOrdenacao : " ";
        String limitClause = limit > 0 ? "LIMIT" + String.valueOf(limit) : " ";
        sql += ordering += limitClause;
        ArrayList<GrupoEventos> listaPreviewGrupoEventos = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                GrupoEventosBuilder grupoEventosBuilder = new GrupoEventosBuilder();
                grupoEventosBuilder.setNome(resultSet.getString("nome_grupo_eventos"))
                        .setImagemCapa(new ServiceFile(resultSet.getInt("id_service_file")))
                        .setDataInicial(resultSet.getDate("data_inicial"))
                        .setDataFinal(resultSet.getDate("data_final"));

                preparedStatement.close();
                listaPreviewGrupoEventos.add(grupoEventosBuilder.getGrupoEventos());
            }
        } catch (Exception exception) {
            return null;
        }
        return listaPreviewGrupoEventos;
    }

    public ArrayList<GrupoEventos> pesquisaPreviewGrupoEventos(String nome, String cidade, String classficacaoEtaria,
            Date dataInicio, Date dataFim, boolean acessivelLibras, ArrayList<Meta> metas) {
        String sql = "SELECT FROM grupo_eventos";
        String conditions = null;
        sql += conditions != null ? " WHERE " + conditions : " ";
        return null;
    }

    public boolean updateGrupoEventos(GrupoEventos grupoEventos) {
        String sql = "UPDATE grupo_eventos  SET  WHERE id_grupo_eventos = ?";
        return false;
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