package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Meta;

public class EventoDAO {
  private Connection connection;

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public boolean inserirEvento(Evento evento) {

    if (evento.getLocais() == null || evento.getLocais().size() == 0) {
      return false;
    }

    try {
      String sql = "INSERT INTO evento (nome_evento, publico_esperado, publico_alcancado, descricao, data_inicial, data_final, horario, classificacao_etaria, certificavel, carga_horaria, acessivel_em_libras, num_participantes_esperado, num_municipios_esperado) VALUES (?, ?, ?, ?, ?, ?, ?, ?::faixa_etaria, ?, ?, ?, ?, ?) RETURNING id_evento";

      PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

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
      stmt.setInt(12, evento.getParticipantesEsperado());
      stmt.setInt(13, evento.getMunicipiosEsperado());

      stmt.executeUpdate();

      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        evento.setIdEvento(rs.getInt("id_evento"));
      }

      stmt.close();

    } catch (SQLException e) {
      return false;
    }

    if (evento.getLocais() != null) {
      boolean vinculoLocais = this.vincularLocais(evento.getLocais(), evento.getIdEvento());

      if (vinculoLocais == false) {
        return false;
      }
    }
    /*
     * if(evento.getListaOrganizadores() != null) { boolean vinculoOrganizadores =
     * this.vincularOrganizadores(evento.getListaOrganizadores(),
     * evento.getIdEvento());
     * if(vinculoOrganizadores == false) { return false; } }
     */

    /*
     * if(evento.getListaColaboradores() != null) { boolean vinculoColaboradores =
     * this.vincularColaboradores(evento.getListaColaboradores(),
     * evento.getIdEvento());
     * if(vinculoColaboradores == false) { return false; } }
     * if(evento.getListaParticipantes() !=
     * null) { boolean vinculoParticipantes =
     * this.vincularParticipantes(evento.getListaParticipantes(),
     * evento.getIdEvento());
     * if(vinculoParticipantes == false) { return false; } }
     */

    return true;
  }

  public boolean vincularMetas(List<Meta> metas, Integer idEvento) {
    MetaDAO metaDAO = new MetaDAO(connection);
    List<Boolean> vinculos = new ArrayList<>();
    for (Meta meta : metas) {
      vinculos.add(metaDAO.vincularEvento(meta.getIdMeta(), idEvento));
    }
    return vinculos.contains(false);
  }

  private boolean vincularLocais(SortedSet<Integer> locais, Integer idEvento) {
    for (Integer local : locais) {
      if (!this.vincularLocal(local, idEvento)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularLocal(Integer local, Integer idEvento) {
    String vincLocaisSql = "INSERT INTO localizacao_evento(id_localizacao, id_evento) VALUES (?, ?)";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincLocaisSql);
      stmt.setInt(1, local);
      stmt.setInt(2, idEvento);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  private boolean vincularOrganizadores(SortedSet<Integer> organizadores, Integer idEvento) {
    for (Integer organizador : organizadores) {
      if (!this.vincularOrganizador(organizador, idEvento)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularOrganizador(Integer organizador, Integer idEvento) {
    String vincOrganizadoresSql = "INSERT INTO organizador_evento(id_evento, id_instituicao) VALUES (?, ?);";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincOrganizadoresSql);
      stmt.setInt(1, idEvento);
      stmt.setInt(2, organizador);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  private boolean vincularColaboradores(SortedSet<Integer> colaboradores, Integer idEvento) {
    for (Integer colaborador : colaboradores) {
      if (!this.vincularColaborador(colaborador, idEvento)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularColaborador(Integer colaborador, Integer idEvento) {
    String vincColaboradoresSql = "INSERT INTO colaborador_evento(id_evento, id_instituicao) VALUES (?, ?);";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincColaboradoresSql);
      stmt.setInt(1, idEvento);
      stmt.setInt(2, colaborador);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  private boolean vincularParticipantes(SortedSet<Integer> participantes, Integer idEvento) {
    for (Integer participante : participantes) {
      if (!this.vincularParticipante(participante, idEvento)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularParticipante(Integer participante, Integer idEvento) {
    String vincParticipantesSql = "INSERT INTO participante_evento(id_participante, id_evento) VALUES (?, ?);";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincParticipantesSql);
      stmt.setInt(1, participante);
      stmt.setInt(2, idEvento);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
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

  public ArrayList<Evento> listarUltimosEventos() {
    String sql = "select * from evento where nome_evento <> '' order by data_inicial desc limit 5";
    ArrayList<Evento> eventos = new ArrayList<>();

    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
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
        evento.setParticipantesEsperado(resultSet.getInt("num_participantes_esperado"));
        evento.setMunicipiosEsperado(resultSet.getInt("num_municipios_esperado"));
        evento.setLocais(this.buscarLocaisPorEvento(evento.getIdEvento()));
        evento.setListaOrganizadores(this.buscarOrganizadoresPorEvento(evento.getIdEvento()));
        evento.setListaColaboradores(this.buscarColaboradoresPorEvento(evento.getIdEvento()));
        evento.setListaParticipantes(this.buscarLocaisPorEvento(evento.getIdEvento()));
        eventos.add(evento);
      }
      stmt.close();
    } catch (SQLException e) {
      return null;
    }

    return eventos;
  }

  public ArrayList<Evento> pesquisarEvento(String nome, Date inicioDate, Date fimDate){
    String sql = "select * from evento where nome_evento ilike ? ";
    if(inicioDate != null)
      sql += "and data_inicial >= '" + inicioDate.toString() + "' ";

    if(fimDate != null)
      sql += "and data_final <= '" + fimDate.toString() + "' ";

    if(nome == "" && inicioDate == null && fimDate == null)
      sql += "limit 30";

    try {
      ArrayList<Evento> eventos = new ArrayList<>();
      
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, "%"+nome+"%");
      ResultSet resultSet = stmt.executeQuery();
      while(resultSet.next()){
        Evento evento = new Evento();
        evento.setIdEvento(resultSet.getInt("id_evento"));
        evento.setNome(resultSet.getString("nome_evento"));
        evento.setDataFinal(resultSet.getDate("data_final"));
        evento.setHorario(resultSet.getTime("horario"));
        eventos.add(evento);
      }     
      return eventos;
    } catch (SQLException e) {
      return new ArrayList<Evento>();
    } 
  }

  public Optional<Evento> buscarEvento(Evento evento) {
    try {
      String sql = "select * from evento where id_evento=?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, evento.getIdEvento());
      ResultSet resultSet = stmt.executeQuery();
      Evento eventoRetorno = null;

      if (resultSet.next()) {
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
        eventoRetorno.setParticipantesEsperado(resultSet.getInt("num_participantes_esperado"));
        eventoRetorno.setMunicipiosEsperado(resultSet.getInt("num_municipios_esperado"));
        eventoRetorno.setLocais(this.buscarLocaisPorEvento(eventoRetorno.getIdEvento()));
        eventoRetorno
            .setListaOrganizadores(this.buscarOrganizadoresPorEvento(eventoRetorno.getIdEvento()));
        eventoRetorno
            .setListaColaboradores(this.buscarColaboradoresPorEvento(eventoRetorno.getIdEvento()));
        eventoRetorno
            .setListaParticipantes(this.buscarLocaisPorEvento(eventoRetorno.getIdEvento()));
        eventoRetorno.setListaMetas(this.listarMetasEvento(eventoRetorno));
      }
      stmt.close();
      return Optional.ofNullable(eventoRetorno);
    } catch (SQLException e) {
      return Optional.empty();
    }
  }

  private ArrayList<Instituicao> buscarColaboradoresPorEvento(int idEvento) {
    InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
    return instituicaoDAO.listarColaboradoresEvento(idEvento);
  }

  private ArrayList<Instituicao> buscarOrganizadoresPorEvento(int idEvento) {
    InstituicaoDAO instituicaoDAO = new InstituicaoDAO(connection);
    return instituicaoDAO.listarOrganizadoresEvento(idEvento);
  }

  public int getNumeroMunicipiosDiferentes(Integer idEvento) {
    String sql = "SELECT DISTINCT count(l.cidade) as num_municipios_distintos FROM localizacao_evento le LEFT JOIN localizacao l on l.id_localizacao = le.id_localizacao WHERE le.id_evento = ?";
    int numMunicipiosDistintos = 0;

    try {
      PreparedStatement stmt = connection.prepareStatement(sql);

      stmt.setInt(1, 1);

      ResultSet resultSet = stmt.executeQuery();

      if (resultSet.next()) {
        numMunicipiosDistintos = resultSet.getInt("num_municipios_distintos");
      }

      stmt.close();
    } catch (SQLException e) {
      return 0;
    }

    return numMunicipiosDistintos;
  }

  public SortedSet<Integer> buscarLocaisPorEvento(Integer idEvento) {
    String sql = "select id_localizacao from localizacao_evento where id_evento=?";

    SortedSet<Integer> locais = new TreeSet<>();

    try {
      PreparedStatement stmt = connection.prepareStatement(sql);

      stmt.setInt(1, idEvento);

      ResultSet resultSet = stmt.executeQuery();

      while (resultSet.next()) {
        locais.add(resultSet.getInt("id_localizacao"));
      }
      stmt.close();
    } catch (SQLException e) {
      return null;
    }

    return locais;
  }

  public boolean alterarEvento(Evento evento) {
    try {
      String sql = "update evento set nome_evento=?, publico_esperado=?, publico_alcancado=?, descricao=?, data_inicial=?, data_final=?, horario=?, classificacao_etaria=?::faixa_etaria, certificavel=?, carga_horaria=?, acessivel_em_libras=?, num_participantes_esperado = ?, num_municipios_esperado = ? where id_evento=?";

      PreparedStatement stmt = connection.prepareStatement(sql);

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
      stmt.setInt(12, evento.getParticipantesEsperado());
      stmt.setInt(13, evento.getMunicipiosEsperado());
      stmt.setInt(14, evento.getIdEvento());
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    boolean sincLocais = this.sincronizarLocais(evento);

    if (!sincLocais) {
      return false;
    }

    boolean sincOrganizadores = this.sincronizarOrganizadores(evento);

    if (!sincOrganizadores) {
      return false;
    }

    boolean sincColaboradores = this.sincronizarColaboradores(evento);

    if (!sincColaboradores) {
      return false;
    }

    boolean sincParticipantes = this.sincronizarParticipantes(evento);

    if (!sincParticipantes) {
      return false;
    }

    return true;
  }

  private boolean sincronizarParticipantes(Evento evento) {
    SortedSet<Integer> participantesEventoIds = this.buscarParticipantesPorEvento(evento.getIdEvento());

    for (Integer participanteId : participantesEventoIds) {
      if (!evento.getListaParticipantes().contains(participanteId)) {
        boolean participanteFoiDesvinculado = this.desvincularParticipante(participanteId, evento.getIdEvento());

        if (!participanteFoiDesvinculado) {
          return false;
        }
      }
    }

    for (Integer participanteId : evento.getListaParticipantes()) {
      boolean participanteFoiVinculado = this.vincularParticipante(participanteId, evento.getIdEvento());

      if (!participanteFoiVinculado) {
        return false;
      }
    }

    return true;
  }

  private boolean sincronizarColaboradores(Evento evento) {
    ArrayList<Instituicao> colaboradoresEvento = this.buscarColaboradoresPorEvento(evento.getIdEvento());

    for (Instituicao colaborador : colaboradoresEvento) {
      if (!evento.getListaColaboradores().contains(colaborador)) {
        boolean colaboradorFoiDesvinculado = this.desvincularColaborador(colaborador.getIdInstituicao(),
            evento.getIdEvento());

        if (!colaboradorFoiDesvinculado) {
          return false;
        }
      }
    }

    for (Instituicao colaborador : evento.getListaColaboradores()) {
      boolean colaboradorFoiVinculado = this.vincularColaborador(colaborador.getIdInstituicao(),
          evento.getIdEvento());

      if (!colaboradorFoiVinculado) {
        return false;
      }
    }

    return true;
  }

  private boolean sincronizarOrganizadores(Evento evento) {
    ArrayList<Instituicao> organizadoresEvento = this.buscarOrganizadoresPorEvento(evento.getIdEvento());

    for (Instituicao organizador : organizadoresEvento) {
      if (!evento.getListaOrganizadores().contains(organizador)) {
        boolean organizadorFoiDesvinculado = this.desvincularOrganizador(organizador.getIdInstituicao(),
            evento.getIdEvento());

        if (!organizadorFoiDesvinculado) {
          return false;
        }
      }
    }

    for (Instituicao organizador : evento.getListaOrganizadores()) {
      boolean organizadorFoiVinculado = this.vincularOrganizador(organizador.getIdInstituicao(),
          evento.getIdEvento());

      if (!organizadorFoiVinculado) {
        return false;
      }
    }

    return true;
  }

  private boolean sincronizarLocais(Evento evento) {
    SortedSet<Integer> locaisEventoIds = this.buscarLocaisPorEvento(evento.getIdEvento());

    for (Integer localId : locaisEventoIds) {
      if (!evento.getLocais().contains(localId)) {
        boolean localFoiDesvinculado = this.desvincularLocal(localId, evento.getIdEvento());

        if (!localFoiDesvinculado) {
          return false;
        }
      }
    }

    for (Integer localId : evento.getLocais()) {
      boolean localFoiVinculado = this.vincularLocal(localId, evento.getIdEvento());

      if (!localFoiVinculado) {
        return false;
      }
    }

    return true;
  }

  private boolean desvincularParticipante(Integer participanteId, Integer idEvento) {
    String vincParticipantesSql = "DELETE FROM participante_evento WHERE id_participante=? AND id_evento=?";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincParticipantesSql);
      stmt.setInt(1, participanteId);
      stmt.setInt(2, idEvento);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  private SortedSet<Integer> buscarParticipantesPorEvento(Integer idEvento) {
    String sql = "select id_participante from participante_evento where id_evento=?";

    SortedSet<Integer> participantes = new TreeSet<>();

    try {
      PreparedStatement stmt = connection.prepareStatement(sql);

      stmt.setInt(1, idEvento);

      ResultSet resultSet = stmt.executeQuery();

      while (resultSet.next()) {
        participantes.add(resultSet.getInt("id_participante"));
      }
      stmt.close();
    } catch (SQLException e) {
      return null;
    }

    return participantes;
  }

  public ArrayList<Meta> listarMetasEvento(Evento evento) {
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

  private boolean desvincularOrganizador(Integer organizadorId, Integer idEvento) {
    String vincOrganizadoresSql = "DELETE FROM organizador_evento WHERE id_instituicao=? AND id_evento=?";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincOrganizadoresSql);
      stmt.setInt(1, organizadorId);
      stmt.setInt(2, idEvento);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
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

  private boolean desvincularMeta(Integer meta, Integer idEvento) {
    MetaDAO metaDAO = new MetaDAO(connection);
    return metaDAO.desvincularEvento(meta, idEvento);
  }

  public ArrayList<Evento> obterEventos() {
    return this.obterEventos(null, true, 5);
  }

  public ArrayList<Evento> obterEventos(String campoUsadoOrdenar, boolean ehAscendente) {
    return this.obterEventos(campoUsadoOrdenar, ehAscendente, 5);
  }

  public ArrayList<Evento> obterEventos(String campoUsadoOrdenar, boolean ehAscendente,
      Integer limite) {
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
        evento.setParticipantesEsperado(resultSet.getInt("num_participantes_esperado"));
        evento.setMunicipiosEsperado(resultSet.getInt("num_municipios_esperado"));
        evento.setCadastradoEm(resultSet.getDate("cadastrado_em"));

        evento.setLocais(this.buscarLocaisPorEvento(evento.getIdEvento()));
        evento.setListaOrganizadores(this.buscarOrganizadoresPorEvento(evento.getIdEvento()));
        evento.setListaColaboradores(this.buscarColaboradoresPorEvento(evento.getIdEvento()));
        evento.setListaParticipantes(this.buscarLocaisPorEvento(evento.getIdEvento()));
        eventos.add(evento);
      }
      stmt.close();
    } catch (SQLException e) {
      return null;
    }

    return eventos;
  }
}
