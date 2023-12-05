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

import com.casaculturaqxd.sgec.builder.EventoBuilder;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Localizacao;
import com.casaculturaqxd.sgec.models.Meta;
import com.casaculturaqxd.sgec.models.Participante;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class EventoDAO extends DAO {
  private Connection connection;

  public EventoDAO() {

  }

  public EventoDAO(Connection connection) {
    this.connection = connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() {
    return this.connection;
  }

  public boolean inserirEvento(Evento evento) {

    if (evento.getLocais() == null || evento.getLocais().size() == 0) {
      return false;
    }

    try {
      String sql = "INSERT INTO evento (nome_evento, publico_esperado, publico_alcancado, descricao, data_inicial, data_final, horario, classificacao_etaria, certificavel, carga_horaria, acessivel_em_libras, num_participantes_esperado, num_municipios_esperado, id_service_file) VALUES (?, ?, ?, ?, ?, ?, ?, ?::faixa_etaria, ?, ?, ?, ?, ?, ?) RETURNING id_evento";

      PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      Integer idServiceFile = null;
      if (evento.getImagemCapa() != null) {
        idServiceFile = evento.getImagemCapa().getServiceFileId();
      }

      stmt.setString(1, evento.getNome());
      stmt.setInt(2, evento.getPublicoEsperado());
      stmt.setInt(3, evento.getPublicoAlcancado());
      stmt.setString(4, evento.getDescricao());
      stmt.setDate(5, evento.getDataInicial());
      stmt.setDate(6, evento.getDataFinal());
      stmt.setTime(7, evento.getHorario());
      stmt.setString(8, evento.getClassificacaoEtaria());
      stmt.setBoolean(9, evento.isCertificavel());
      stmt.setTime(10, evento.getCargaHoraria());
      stmt.setBoolean(11, evento.isAcessivelEmLibras());
      stmt.setInt(12, evento.getNumParticipantesEsperado());
      stmt.setInt(13, evento.getNumMunicipiosEsperado());
      stmt.setObject(14, idServiceFile, Types.INTEGER);

      stmt.executeUpdate();

      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        evento.setIdEvento(rs.getInt("id_evento"));
      }

      stmt.close();

    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  public boolean vincularMetas(List<Meta> metas, Integer idEvento) throws SQLException {
    MetaDAO metaDAO = new MetaDAO(connection);
    try {
      for (Meta meta : metas) {
        boolean temp = metaDAO.vincularEvento(meta.getIdMeta(), idEvento);
        if (temp == false) {
          return false;
        }
      }
    } catch (Exception e) {
      throw new SQLException("falha vinculando conjunto de metas", e);
    }

    return true;
  }

  public boolean vincularMeta(Meta meta, Evento evento) throws SQLException {
    MetaDAO metaDAO = new MetaDAO(connection);
    return metaDAO.vincularEvento(meta.getIdMeta(), evento.getIdEvento());
  }

  public boolean vincularArquivos(Evento evento) throws SQLException {
    ServiceFileDAO serviceFileDAO = new ServiceFileDAO(getConnection());
    return serviceFileDAO.vincularAllArquivos(evento);
  }

  public boolean vincularLocais(List<Localizacao> locais, Integer idEvento) throws SQLException {
    for (Localizacao local : locais) {
      if (!this.vincularLocal(local.getIdLocalizacao(), idEvento)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularLocal(Integer idLocal, Integer idEvento) throws SQLException {
    LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(connection);

    if (!localizacaoDAO.vincularEvento(idLocal, idEvento)) {
      return false;
    }
    return true;
  }

  public boolean vincularOrganizadores(ArrayList<Instituicao> organizadores, Evento evento) throws SQLException {
    for (Instituicao organizador : organizadores) {
      if (!this.vincularOrganizador(organizador, evento)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularOrganizador(Instituicao organizador, Evento evento) throws SQLException {
    InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
    return instituicaoDAO.vincularOrganizadorEvento(organizador, evento.getIdEvento());
  }

  public boolean vincularColaboradores(ArrayList<Instituicao> colaboradores, Evento evento) throws SQLException {
    for (Instituicao colaborador : colaboradores) {
      if (!this.vincularColaborador(colaborador, evento)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularColaborador(Instituicao colaborador, Evento evento) throws SQLException {
    InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
    return instituicaoDAO.vincularColaboradorEvento(colaborador, evento.getIdEvento());
  }

  public boolean vincularParticipantes(ArrayList<Participante> participantes, Evento evento) throws SQLException {
    for (Participante participante : participantes) {
      if (!this.vincularParticipante(participante, evento)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularParticipante(Participante participante, Evento evento) throws SQLException {
    ParticipanteDAO participanteDAO = new ParticipanteDAO(connection);
    return participanteDAO.vincularEvento(participante.getIdParticipante(), evento.getIdEvento());
  }

  public boolean removerEvento(Evento evento) {
    try {
      String sql = "delete from evento where id_evento=?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, evento.getIdEvento());
      stmt.execute();
      stmt.close();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  public ArrayList<Evento> listarUltimosEventos() throws SQLException {
    String sql = "select id_evento,nome_evento,data_inicial, horario, id_service_file from evento where nome_evento <> '' order by data_inicial desc limit 5";
    PreparedStatement stmt = connection.prepareStatement(sql);
    ArrayList<Evento> eventos = new ArrayList<>();
    try {
      ResultSet resultSet = stmt.executeQuery();

      while (resultSet.next()) {
        Evento evento = new Evento(resultSet.getInt("id_evento"));

        eventos.add(getPreviewEvento(evento).get());
      }
    } catch (SQLException e) {
      logException(e);
      throw new SQLException("falha pesquisando ultimos eventos", e);
    } finally {
      stmt.close();
    }
    return eventos;
  }

  public ArrayList<Evento> pesquisarEvento(String nome, Date inicioDate, Date fimDate) {
    String sql = "select id_evento, nome_evento, inicio, fim from pesquisar_evento where nome_evento ilike ? ";
    if (inicioDate != null)
      sql += "and inicio >= '" + inicioDate.toString() + "' ";

    if (fimDate != null)
      sql += "and fim <= '" + fimDate.toString() + "' ";

    if (nome == "" && inicioDate == null && fimDate == null)
      sql += "limit 30";

    try {
      ArrayList<Evento> eventos = new ArrayList<>();

      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, "%" + nome + "%");
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        Evento evento = new Evento(resultSet.getInt("id_evento"));
        eventos.add(getPreviewEvento(evento).get());
      }
      return eventos;
    } catch (SQLException e) {
      return new ArrayList<Evento>();
    }
  }

  public Optional<Evento> getEvento(Evento evento) throws SQLException {
    String sql = """
        SELECT
        id_evento,
        nome_evento,
        publico_esperado,
        publico_alcancado,
        descricao,
        data_inicial,
        data_final,
        horario,
        classificacao_etaria,
        certificavel,
        carga_horaria,
        acessivel_em_libras,
        num_participantes_esperado,
        num_municipios_esperado,
        id_grupo_eventos,
        num_municipios_alcancado,
        num_participantes_alcancado,
        num_colaboradores_alcancado,
        num_colaboradores_esperado,
        id_service_file
        FROM evento WHERE id_evento = ?
          """;
    PreparedStatement stmt = connection.prepareStatement(sql);
    try {
      stmt.setInt(1, evento.getIdEvento());
      ResultSet resultSet = stmt.executeQuery();
      Evento eventoRetorno = null;

      if (resultSet.next()) {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(connection);
        ServiceFileDAO serviceFileDAO = new ServiceFileDAO(connection);
        Optional<ServiceFile> optionalImagemCapa = serviceFileDAO
            .getArquivo(new ServiceFile(resultSet.getInt("id_service_file")));
        Optional<GrupoEventos> optionalGrupoEventos = grupoEventosDAO
            .getPreviewGrupoEventos(new GrupoEventos(resultSet.getInt("id_grupo_eventos")));
        eventoRetorno = new Evento();
        eventoRetorno.setIdEvento(resultSet.getInt("id_evento"));
        eventoRetorno.setNome(resultSet.getString("nome_evento"));
        eventoRetorno.setPublicoEsperado(resultSet.getInt("publico_esperado"));
        eventoRetorno.setPublicoAlcancado(resultSet.getInt("publico_alcancado"));
        eventoRetorno.setDescricao(resultSet.getString("descricao"));
        eventoRetorno.setDataInicial(resultSet.getDate("data_inicial"));
        eventoRetorno.setDataFinal(resultSet.getDate("data_final"));
        eventoRetorno.setHorario(resultSet.getTime("horario"));
        eventoRetorno.setClassificacaoEtaria(resultSet.getString("classificacao_etaria"));
        eventoRetorno.setCertificavel(resultSet.getBoolean("certificavel"));
        eventoRetorno.setCargaHoraria(resultSet.getTime("carga_horaria"));
        eventoRetorno.setAcessivelEmLibras(resultSet.getBoolean("acessivel_em_libras"));
        eventoRetorno.setNumParticipantesEsperado(resultSet.getInt("num_participantes_esperado"));
        eventoRetorno.setNumParticipantesAlcancado(resultSet.getInt("num_participantes_alcancado"));
        eventoRetorno.setNumMunicipiosEsperado(resultSet.getInt("num_municipios_esperado"));
        eventoRetorno.setNumMunicipiosAlcancado(resultSet.getInt("num_municipios_alcancado"));
        eventoRetorno.setNumColaboradoresEsperado(resultSet.getInt("num_colaboradores_esperado"));
        eventoRetorno.setNumColaboradoresAlcancado(resultSet.getInt("num_colaboradores_alcancado"));
        eventoRetorno.setLocais(this.buscarLocaisPorEvento(eventoRetorno.getIdEvento()));
        eventoRetorno.setListaOrganizadores(this.buscarOrganizadoresPorEvento(eventoRetorno.getIdEvento()));
        eventoRetorno.setListaColaboradores(this.buscarColaboradoresPorEvento(eventoRetorno.getIdEvento()));
        eventoRetorno.setListaParticipantes(this.buscarParticipantesPorEvento(eventoRetorno.getIdEvento()));
        eventoRetorno.setListaArquivos(this.buscarArquivosPorEvento(eventoRetorno));
        eventoRetorno.setListaMetas(this.listarMetasEvento(eventoRetorno));

        if (optionalImagemCapa.isPresent()) {
          eventoRetorno.setImagemCapa(optionalImagemCapa.get());
        }
        if (optionalGrupoEventos.isPresent()) {
          eventoRetorno.setGrupoEventos(optionalGrupoEventos.get());
        }
      }
      return Optional.ofNullable(eventoRetorno);
    } catch (Exception e) {
      String nomeEventoCausa = evento != null && evento.getNome() != null ? evento.getNome() : " ";
      logException(e);
      throw new SQLException("falha buscando evento " + nomeEventoCausa, e);
    } finally {
      stmt.close();
    }
  }

  public Optional<Evento> getEvento(String nomeEvento) throws SQLException {
    String sql = " SELECT id_evento FROM evento WHERE nome_evento ILIKE ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);

    try {
      preparedStatement.setString(1, nomeEvento);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Evento evento = new Evento(resultSet.getInt("id_evento"));

        return getEvento(evento);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha buscando evento com nome" + nomeEvento, e);
    } finally {
      preparedStatement.close();
    }
  }

  public Optional<Evento> getPreviewEvento(Evento evento) throws SQLException {
    String sql = "SELECT id_evento,nome_evento,data_inicial, horario, id_service_file FROM evento WHERE id_evento = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);

    try {
      preparedStatement.setInt(1, evento.getIdEvento());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        ServiceFileDAO serviceFileDAO = new ServiceFileDAO(connection);
        ServiceFile imagemCapa = new ServiceFile(resultSet.getInt("id_service_file"));
        Optional<ServiceFile> resultOptional = serviceFileDAO.getArquivo(imagemCapa);
        if (resultOptional.isPresent()) {
          imagemCapa = serviceFileDAO.getArquivo(imagemCapa).get();
        } else {
          imagemCapa = null;
        }
        EventoBuilder eventoBuilder = new EventoBuilder();

        eventoBuilder.setHorario(resultSet.getTime("horario")).setId(resultSet.getInt("id_evento"))
            .setNome(resultSet.getString("nome_evento")).setDataInicial(resultSet.getDate("data_inicial"))
            .setImagemCapa(imagemCapa);

        return Optional.ofNullable(eventoBuilder.getEvento());
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      String nomeEventoCausa = evento != null && evento.getNome() != null ? evento.getNome() : "";
      logException(e);
      throw new SQLException("falha buscando preview de evento " + nomeEventoCausa, e);
    } finally {
      preparedStatement.close();
    }
  }

  public Optional<Evento> getPreviewEvento(String nomeEvento) throws SQLException {
    String sql = "SELECT id_evento FROM evento WHERE nome_evento ILIKE ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);

    try {
      preparedStatement.setString(1, nomeEvento);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Evento evento = new Evento(resultSet.getInt("id_evento"));

        return getPreviewEvento(evento);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha buscando evento com nome" + nomeEvento, e);
    } finally {
      preparedStatement.close();
    }
  }

  private ArrayList<ServiceFile> buscarArquivosPorEvento(Evento evento) throws SQLException {
    ServiceFileDAO serviceFileDAO = new ServiceFileDAO(connection);
    return serviceFileDAO.listarArquivosEvento(evento);
  }

  private ArrayList<Instituicao> buscarColaboradoresPorEvento(int idEvento) throws SQLException {
    InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
    return instituicaoDAO.listarColaboradoresEvento(idEvento);
  }

  private ArrayList<Instituicao> buscarOrganizadoresPorEvento(int idEvento) throws SQLException {
    InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
    return instituicaoDAO.listarOrganizadoresEvento(idEvento);
  }

  public ArrayList<Localizacao> buscarLocaisPorEvento(Integer idEvento) throws SQLException {
    LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(connection);

    ArrayList<Localizacao> locais = localizacaoDAO.listarLocaisPorEvento(idEvento);

    return locais;
  }

  /**
   * seta todos os parametros passados utilizando o model de um evento, a unica
   * coluna de valor alcancado editavel manualmente e o publico alcancado, todas
   * as outras tem o valor controlado por triggers
   * 
   * @param evento
   * @return true se o numero de modificacoes for maior que zero
   * @throws SQLException
   */
  public boolean alterarEvento(Evento evento) throws SQLException {
    String sql = """
        update evento
        set nome_evento=?,
        publico_esperado=?,
        publico_alcancado=?,
        descricao=?,
        data_inicial=?,
        data_final=?,
        horario=?,
        classificacao_etaria=?::faixa_etaria,
        certificavel=?,
        carga_horaria=?,
        acessivel_em_libras=?,
        num_participantes_esperado = ?,
        num_municipios_esperado = ?,
        num_colaboradores_esperado = ?,
        id_grupo_eventos = ?,
        id_service_file = ?

        WHERE id_evento=?
        """;

    PreparedStatement stmt = connection.prepareStatement(sql);
    try {
      ServiceFileDAO serviceFileDAO = new ServiceFileDAO(connection);
      GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(connection);
      Optional<GrupoEventos> optionalGrupoEventos = grupoEventosDAO.getPreviewGrupoEventos(evento.getGrupoEventos());
      Integer idGrupoEventos = null, idServiceFile = null;
      if (optionalGrupoEventos.isPresent()) {
        idGrupoEventos = optionalGrupoEventos.get().getIdGrupoEventos();
      }
      Optional<ServiceFile> optionalFile = serviceFileDAO.getArquivo(evento.getImagemCapa());
      if (optionalFile.isPresent()) {
        idServiceFile = optionalFile.get().getServiceFileId();
      }
      stmt.setString(1, evento.getNome());
      stmt.setInt(2, evento.getPublicoEsperado());
      stmt.setInt(3, evento.getPublicoAlcancado());
      stmt.setString(4, evento.getDescricao());
      stmt.setDate(5, evento.getDataInicial());
      stmt.setDate(6, evento.getDataFinal());
      stmt.setTime(7, evento.getHorario());
      stmt.setString(8, evento.getClassificacaoEtaria());
      stmt.setBoolean(9, evento.isCertificavel());
      stmt.setTime(10, evento.getCargaHoraria());
      stmt.setBoolean(11, evento.isAcessivelEmLibras());
      stmt.setInt(12, evento.getNumParticipantesEsperado());
      stmt.setInt(13, evento.getNumMunicipiosEsperado());
      stmt.setInt(14, evento.getNumColaboradoresEsperado());
      stmt.setObject(15, idGrupoEventos, Types.INTEGER);
      stmt.setObject(16, idServiceFile, Types.INTEGER);

      stmt.setInt(17, evento.getIdEvento());

      int numAtualizacoes = stmt.executeUpdate();
      return numAtualizacoes > 0;
    } catch (Exception e) {
      String nomeEventoCausa = evento != null && evento.getNome() != null ? evento.getNome() : " ";
      logException(e);
      throw new SQLException("falha atualizando evento " + nomeEventoCausa, e);
    } finally {
      stmt.close();
    }
  }

  private boolean desvincularParticipante(Participante participante, Evento evento) throws SQLException {
    ParticipanteDAO participanteDAO = new ParticipanteDAO(connection);
    return participanteDAO.desvincularEvento(participante.getIdParticipante(), evento.getIdEvento());
  }

  private ArrayList<Participante> buscarParticipantesPorEvento(Integer idEvento) throws SQLException {
    String sql = "select id_participante from participante_evento where id_evento=?";
    PreparedStatement stmt = connection.prepareStatement(sql);

    ParticipanteDAO participanteDAO = new ParticipanteDAO(connection);
    ArrayList<Participante> participantes = new ArrayList<>();
    try {
      stmt.setInt(1, idEvento);

      ResultSet resultSet = stmt.executeQuery();

      while (resultSet.next()) {
        Participante participante = new Participante(resultSet.getInt("id_participante"));
        Optional<Participante> optionalParticipante = participanteDAO.getParticipante(participante);
        if (optionalParticipante.isPresent()) {
          participantes.add(participanteDAO.getParticipante(participante).get());
        }
      }
    } catch (Exception e) {
      logException(e);
      throw new SQLException("falha listando participantes do evento", e);
    } finally {
      stmt.close();
    }
    return participantes;
  }

  /**
   * Adiciona os participantes que nao estao na lista atual, mas estao na lista
   * passada. Remove os participantes que estam na lista atual mas nao estao na
   * lista passada
   * 
   * @param participantes
   * @param evento
   * @return true se todas as operacoes forem realizadas
   * @throws SQLException
   */
  public boolean atualizarParticipantesEvento(ArrayList<Participante> participantes, Evento evento)
      throws SQLException {
    ParticipanteDAO participanteDAO = new ParticipanteDAO(connection);
    ArrayList<Participante> participantesAtuais = buscarParticipantesPorEvento(evento.getIdEvento());
    try {
      for (Participante participante : participantes) {
        if (!participantesAtuais.contains(participanteDAO.getParticipante(participante).get())) {
          boolean check = vincularParticipante(participante, evento);
          if (!check) {
            return false;
          }
        }
      }
      for (Participante participante : participantesAtuais) {
        if (!participantes.contains(participante)) {
          boolean check = desvincularParticipante(participante, evento);
          if (!check) {
            return false;
          }
        }
      }
      return true;
    } catch (Exception e) {
      String nomeEventoCausa = evento != null && evento.getNome() != null ? evento.getNome() : " ";
      logException(e);
      throw new SQLException("falha atualizando participantes do evento " + nomeEventoCausa, e);
    }
  }

  /**
   * adiciona os organizadores que estao na lista passada, mas nao na lista atual.
   * Remove os organizadores que estao na lista atual, mas nao na lista passada
   * 
   * @param organizadores
   * @param evento
   * @return true se todas as alteracoes forem sucedidas
   * @throws SQLException
   */
  public boolean atualizarOrganizadoresEvento(ArrayList<Instituicao> organizadores, Evento evento) throws SQLException {
    ArrayList<Instituicao> organizadoresAtuais = buscarOrganizadoresPorEvento(evento.getIdEvento());
    try {
      for (Instituicao organizador : organizadores) {
        if (!organizadoresAtuais.contains(organizador)) {
          boolean check = vincularOrganizador(organizador, evento);
          if (!check) {
            return false;
          }
        }
      }

      for (Instituicao organizador : organizadoresAtuais) {
        if (!organizadores.contains(organizador)) {
          boolean check = desvincularOrganizador(organizador, evento);
          if (!check) {
            return false;
          }
        }
      }
      return true;
    } catch (Exception e) {
      String nomeEventoCausa = evento != null && evento.getNome() != null ? evento.getNome() : " ";
      logException(e);
      throw new SQLException("falha atualizando metas do evento " + nomeEventoCausa, e);
    }
  }

  /**
   * Adiciona colaboradores que estao na lista passada, mas nao na lista atual.
   * Remove os colaboradores que estao na lista de colaboradores atual mas nao na
   * lista passada
   * 
   * @param colaboradores
   * @param evento
   * @return true se todas as alteracoes forem sucedidas
   * @throws SQLException
   */
  public boolean atualizarColaboradoresEvento(ArrayList<Instituicao> colaboradores, Evento evento) throws SQLException {
    ArrayList<Instituicao> colaboradoresAtuais = buscarColaboradoresPorEvento(evento.getIdEvento());
    try {
      for (Instituicao colaborador : colaboradores) {
        // se o colaborador nao estiver na lista atual
        if (!colaboradoresAtuais.contains(colaborador)) {
          boolean check = vincularColaborador(colaborador, evento);
          if (!check) {
            return false;
          }
        }
      }
      for (Instituicao colaborador : colaboradoresAtuais) {
        if (!colaboradores.contains(colaborador)) {
          boolean check = desvincularColaborador(colaborador.getIdInstituicao(), evento.getIdEvento());
          if (!check) {
            return false;
          }
        }
      }

      return true;
    } catch (Exception e) {
      String nomeEventoCausa = evento != null && evento.getNome() != null ? evento.getNome() : " ";
      logException(e);
      throw new SQLException("falha atualizando metas do evento " + nomeEventoCausa, e);
    }
  }

  /**
   * adiciona ao evento as metas que estao na lista passada, mas nao estam
   * vinculadas atualmente. Remove metas que estao na lista de metas atual, mas
   * nao estao na lista passada
   * 
   * @return true se o numero de modificacoes for maior que 0
   * @throws SQLException
   */
  public boolean atualizarMetasEvento(ArrayList<Meta> metas, Evento evento) throws SQLException {
    MetaDAO metaDAO = new MetaDAO(connection);
    ArrayList<Meta> metasAtuais = listarMetasEvento(evento);
    try {
      for (Meta meta : metas) {
        // adicionando metas que nao estao na lista atual
        if (!metasAtuais.contains(metaDAO.getMeta(meta).get())) {
          boolean check = vincularMeta(meta, evento);
          if (!check) {
            return false;
          }
        }
      }
      for (Meta meta : metasAtuais) {
        // removendo metas que estao na lista atual, mas nao na lista passada
        if (!metas.contains(metaDAO.getMeta(meta).get())) {
          boolean check = desvincularMeta(meta, evento);
          if (!check) {
            return false;
          }
        }
      }
      return true;
    } catch (Exception e) {
      String nomeEventoCausa = evento != null && evento.getNome() != null ? evento.getNome() : " ";
      logException(e);
      throw new SQLException("falha atualizando metas do evento " + nomeEventoCausa, e);
    }

  }

  public ArrayList<Meta> listarMetasEvento(Evento evento) throws SQLException {
    MetaDAO metaDAO = new MetaDAO(connection);
    return metaDAO.listarMetasEvento(evento.getIdEvento());
  }

  private boolean desvincularColaborador(Integer colaboradorId, Integer idEvento) {
    String vincColaboradoresSql = "DELETE FROM colaborador_evento WHERE id_instituicao=? AND id_evento=?";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincColaboradoresSql);
      stmt.setInt(1, colaboradorId);
      stmt.setInt(2, idEvento);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  private boolean desvincularOrganizador(Instituicao organizador, Evento evento) throws SQLException {
    InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
    return instituicaoDAO.desvincularOrganizadorEvento(organizador.getIdInstituicao(), evento.getIdEvento());
  }

  private boolean desvincularLocal(Integer localId, Integer idEvento) {
    String vincLocaisSql = "DELETE FROM localizacao_evento WHERE id_localizacao=? AND id_evento=?";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincLocaisSql);
      stmt.setInt(1, localId);
      stmt.setInt(2, idEvento);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  private boolean desvincularMeta(Meta meta, Evento evento) throws SQLException {
    MetaDAO metaDAO = new MetaDAO(connection);
    return metaDAO.desvincularEvento(meta.getIdMeta(), evento.getIdEvento());
  }

  public ArrayList<Evento> obterEventos() {
    return this.obterEventos(null, true, 5);
  }

  public ArrayList<Evento> obterEventos(String campoUsadoOrdenar, boolean ehAscendente) {
    return this.obterEventos(campoUsadoOrdenar, ehAscendente, 5);
  }

  public ArrayList<Evento> obterEventos(String campoUsadoOrdenar, boolean ehAscendente, Integer limite) {
    if (campoUsadoOrdenar == null) {
      campoUsadoOrdenar = "cadastrado_em";
    }

    String sql = "select * from evento order by ";
    sql += campoUsadoOrdenar;
    sql += ehAscendente ? " asc" : " desc";
    sql += " limit ?";
    // qual o outro tipo de ordenação além do desc?
    ArrayList<Evento> eventos = new ArrayList<>();

    try {

      PreparedStatement stmt = connection.prepareStatement(sql);

      stmt.setInt(1, limite);

      ResultSet resultSet = stmt.executeQuery();

      while (resultSet.next()) {
        Evento evento = new Evento();

        evento.setIdEvento(resultSet.getInt("id_evento"));
        evento.setNome(resultSet.getString("nome_evento"));
        evento.setPublicoEsperado(resultSet.getInt("publico_esperado"));
        evento.setPublicoAlcancado(resultSet.getInt("publico_alcancado"));
        evento.setDescricao(resultSet.getString("descricao"));
        evento.setDataInicial(resultSet.getDate("data_inicial"));
        evento.setDataFinal(resultSet.getDate("data_final"));
        evento.setHorario(resultSet.getTime("horario"));
        evento.setClassificacaoEtaria(resultSet.getString("classificacao_etaria"));
        evento.setCertificavel(resultSet.getBoolean("certificavel"));
        evento.setCargaHoraria(resultSet.getTime("carga_horaria"));
        evento.setAcessivelEmLibras(resultSet.getBoolean("acessivel_em_libras"));
        evento.setNumParticipantesEsperado(resultSet.getInt("num_participantes_esperado"));
        evento.setNumMunicipiosEsperado(resultSet.getInt("num_municipios_esperado"));
        evento.setCadastradoEm(resultSet.getDate("cadastrado_em"));

        evento.setLocais(this.buscarLocaisPorEvento(evento.getIdEvento()));
        evento.setListaOrganizadores(this.buscarOrganizadoresPorEvento(evento.getIdEvento()));
        evento.setListaColaboradores(this.buscarColaboradoresPorEvento(evento.getIdEvento()));
        evento.setListaParticipantes(this.buscarParticipantesPorEvento(evento.getIdEvento()));
        eventos.add(evento);
      }
      stmt.close();
    } catch (SQLException e) {
      return null;
    }

    return eventos;
  }

  public List<Evento> listarEventosGrupoEventos(GrupoEventos grupoEventos) throws SQLException {
    String sql = "SELECT id_evento FROM evento WHERE id_grupo_eventos = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    List<Evento> listaEventos = new ArrayList<>();
    try {
      preparedStatement.setInt(1, grupoEventos.getIdGrupoEventos());
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Evento evento = new Evento(resultSet.getInt("id_evento"));

        listaEventos.add(getPreviewEvento(evento).get());
      }
      return listaEventos;
    } catch (Exception e) {
      throw new SQLException("falha listando eventos do grupo de eventos", e);
    } finally {
      preparedStatement.close();
    }
  }

  public boolean vincularGrupoEventos(int idGrupoEventos, int idEvento) throws SQLException {
    String sql = "UPDATE evento SET id_grupo_eventos = ? WHERE id_evento = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);

    try {
      preparedStatement.setInt(1, idGrupoEventos);

      preparedStatement.setInt(2, idEvento);
      int numAlteracoes = preparedStatement.executeUpdate();
      return numAlteracoes == 1;
    } catch (Exception e) {
      throw new SQLException("falha adicionando grupo de eventos ao evento", e);
    } finally {
      preparedStatement.close();
    }
  }

  public boolean desvincularGrupoEventos(int idGrupoEventos, int idEvento) throws SQLException {
    String sql = "UPDATE evento SET id_grupo_eventos = NULL WHERE idGrupoEventos = ? AND id_evento = ? ";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);

    try {
      preparedStatement.setInt(1, idGrupoEventos);
      preparedStatement.setInt(2, idEvento);

      int numAlteracoes = preparedStatement.executeUpdate();
      return numAlteracoes == 1;
    } catch (Exception e) {
      throw new SQLException("falha removendo grupo de eventos ao evento", e);
    } finally {
      preparedStatement.close();
    }
  }
}
