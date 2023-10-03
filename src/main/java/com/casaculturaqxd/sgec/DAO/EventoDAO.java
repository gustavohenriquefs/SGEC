package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import com.casaculturaqxd.sgec.enums.ClassificacaoEtaria;
import com.casaculturaqxd.sgec.models.Evento;

public class EventoDAO {
  private Connection connection;

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public boolean inserirEvento(Evento evento) {
    
    if(evento.getLocais() == null || evento.getLocais().size() == 0) {
      return false;
    }

    try {
      String sql = "INSERT INTO evento (nome_evento, publico_esperado, publico_alcancado, descricao, data_inicial, data_final, horario, classificacao_etaria, certificavel, carga_horaria, acessivel_em_libras) VALUES (?, ?, ?, ?, ?, ?, ?, ?::faixa_etaria, ?, ?, ?) RETURNING id_evento";
      
      PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      String classificacaoEtaria = evento.getClassificacaoEtaria().getClassificacao();

      stmt.setString( 1, evento.getNome());
      stmt.setInt(    2, evento.getPublicoEsperado());
      stmt.setInt(    3, evento.getPublicoAlcancado());
      stmt.setString( 4, evento.getDescricao());
      stmt.setDate(   5, evento.getDataInicial());
      stmt.setDate(   6, evento.getDataFinal());
      stmt.setTime(   7, Time.valueOf(evento.getHorario()));
      stmt.setString( 8, classificacaoEtaria);
      stmt.setBoolean(9, evento.isCertificavel());
      stmt.setTime(   10, Time.valueOf(evento.getCargaHoraria()));
      stmt.setBoolean(11, evento.isAcessivelEmLibras());
      stmt.executeUpdate();
      
      ResultSet rs = stmt.getGeneratedKeys();

      if (rs.next()) {
          evento.setIdEvento(rs.getInt("id_evento"));
      }

      stmt.close();
      
    } catch (SQLException e) {
      return false;
    }
    
    if(evento.getLocais() != null) {
      boolean vinculoLocais = this.vincularLocais(evento.getLocais(), evento.getIdEvento());

      if(vinculoLocais == false) {
        return false;
      }
    }

    if(evento.getListaOrganizadores() != null) {
      boolean vinculoOrganizadores = this.vincularOrganizadores(evento.getListaOrganizadores(), evento.getIdEvento());

      if(vinculoOrganizadores == false) {
        return false;
      }
    }

    if(evento.getListaColaboradores() != null) {
      boolean vinculoColaboradores = this.vincularColaboradores(evento.getListaColaboradores(), evento.getIdEvento());

      if(vinculoColaboradores == false) {
        return false;
      }
    }

    if(evento.getListaParticipantes() != null) {
      boolean vinculoParticipantes = this.vincularParticipantes(evento.getListaParticipantes(), evento.getIdEvento());

      if(vinculoParticipantes == false) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularLocais(SortedSet<Integer> locais, Integer idEvento) {
    for(Integer local: locais) {
      if(!this.vincularLocal(local, idEvento)) {
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
    for(Integer organizador: organizadores) {
      if(!this.vincularOrganizador(organizador, idEvento)) {
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
    for(Integer colaborador: colaboradores) {
      if(!this.vincularColaborador(colaborador, idEvento)) {
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
    for(Integer participante: participantes) {
      if(!this.vincularParticipante(participante, idEvento)) {
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
        eventoRetorno.setHorario(resultSet.getTime("horario").toLocalTime());
        eventoRetorno.setClassificacaoEtaria(ClassificacaoEtaria.valueOf(resultSet.getString("classificacao_etaria")));
        eventoRetorno.setCertificavel(resultSet.getBoolean("certificavel"));
        eventoRetorno.setCargaHoraria(resultSet.getTime("carga_horaria").toLocalTime());
        eventoRetorno.setAcessivelEmLibras(resultSet.getBoolean("acessivel_em_libras"));
        eventoRetorno.setLocais(this.buscarLocaisPorEvento(eventoRetorno.getIdEvento()));
        eventoRetorno.setListaOrganizadores(this.buscarOrganizadoresPorEvento(eventoRetorno.getIdEvento()));
        eventoRetorno.setListaColaboradores(this.buscarColaboradoresPorEvento(eventoRetorno.getIdEvento()));
        eventoRetorno.setListaParticipantes(this.buscarLocaisPorEvento(eventoRetorno.getIdEvento()));
      }
      stmt.close();
      return Optional.ofNullable(eventoRetorno);
    } catch (SQLException e) {
      return Optional.empty();
    }
  }

  private SortedSet<Integer> buscarColaboradoresPorEvento(int idEvento) {
    String sql = "select id_instituicao from colaborador_evento where id_evento=?";

    SortedSet<Integer> colaboradores = new TreeSet<>();

    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      
      stmt.setInt(1, idEvento);
      
      ResultSet resultSet = stmt.executeQuery();

      while (resultSet.next()) {
        colaboradores.add(resultSet.getInt("id_instituicao"));
      }
      stmt.close();
    } catch (SQLException e) {
      return null;
    }

    return colaboradores;
  }

  private SortedSet<Integer> buscarOrganizadoresPorEvento(int idEvento) {
    String sql = "select id_instituicao from organizador_evento where id_evento=?";

    SortedSet<Integer> organizadores = new TreeSet<>();

    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      
      stmt.setInt(1, idEvento);
      
      ResultSet resultSet = stmt.executeQuery();

      while (resultSet.next()) {
        organizadores.add(resultSet.getInt("id_instituicao"));
      }
      stmt.close();
    } catch (SQLException e) {
      return null;
    }

    return organizadores;
  }

  private SortedSet<Integer> buscarLocaisPorEvento(Integer idEvento) {
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
      String sql = "update evento set nome_evento=?, publico_esperado=?, publico_alcancado=?, descricao=?, data_inicial=?, data_final=?, horario=?, classificacao_etaria=?, imagem_preview=?, certificavel=?, carga_horaria=?, acessivel_em_libras=?, localizacao_id_localizacao=? where id_evento=?";
      
      PreparedStatement stmt = connection.prepareStatement(sql);
      
      stmt.setString(1, evento.getNome());
      stmt.setString(2, evento.getDescricao());
      stmt.setDate(3, evento.getDataInicial());
      stmt.setDate(4, evento.getDataFinal());
      stmt.setTime(5, Time.valueOf(evento.getHorario()));
      stmt.setInt(6, evento.getIdEvento());
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    boolean sincLocais = this.sincronizarLocais(evento);

    if(!sincLocais) {
      return false;
    }
    
    boolean sincOrganizadores = this.sincronizarOrganizadores(evento);

    if(!sincOrganizadores) {
      return false;
    }

    boolean sincColaboradores = this.sincronizarColaboradores(evento);

    if(!sincColaboradores) {
      return false;
    }

    boolean sincParticipantes = this.sincronizarParticipantes(evento);

    if(!sincParticipantes) {
      return false;
    }

    return true;
  }

  private boolean sincronizarParticipantes(Evento evento) {
    SortedSet<Integer> participantesEventoIds = this.buscarParticipantesPorEvento(evento.getIdEvento());

    for(Integer participanteId: participantesEventoIds) {
      if(!evento.getListaParticipantes().contains(participanteId)) {
        boolean participanteFoiDesvinculado = this.desvincularParticipante(participanteId, evento.getIdEvento()); 

        if(!participanteFoiDesvinculado) {
          return false;
        }
      }
    }

    for(Integer participanteId: evento.getListaParticipantes()) {
      boolean participanteFoiVinculado = this.vincularParticipante(participanteId, evento.getIdEvento());

      if(!participanteFoiVinculado) {
        return false;
      }
    }

    return true;
  }

  private boolean sincronizarColaboradores(Evento evento) {
    SortedSet<Integer> colaboradoresEventoIds = this.buscarColaboradoresPorEvento(evento.getIdEvento());

    for(Integer colaboradorId: colaboradoresEventoIds) {
      if(!evento.getListaColaboradores().contains(colaboradorId)) {
        boolean colaboradorFoiDesvinculado = this.desvincularColaborador(colaboradorId, evento.getIdEvento()); 

        if(!colaboradorFoiDesvinculado) {
          return false;
        }
      }
    }

    for(Integer colaboradorId: evento.getListaColaboradores()) {
      boolean colaboradorFoiVinculado = this.vincularColaborador(colaboradorId, evento.getIdEvento());

      if(!colaboradorFoiVinculado) {
        return false;
      }
    }

    return true;
  }

  private boolean sincronizarOrganizadores(Evento evento) {
    SortedSet<Integer> organizadoresEventoIds = this.buscarOrganizadoresPorEvento(evento.getIdEvento());

    for(Integer organizadorId: organizadoresEventoIds) {
      if(!evento.getListaOrganizadores().contains(organizadorId)) {
        boolean organizadorFoiDesvinculado = this.desvincularOrganizador(organizadorId, evento.getIdEvento()); 

        if(!organizadorFoiDesvinculado) {
          return false;
        }
      }
    }

    for(Integer organizadorId: evento.getListaOrganizadores()) {
      boolean organizadorFoiVinculado = this.vincularOrganizador(organizadorId, evento.getIdEvento());

      if(!organizadorFoiVinculado) {
        return false;
      }
    }

    return true;
  }

  private boolean sincronizarLocais(Evento evento) {
    SortedSet<Integer> locaisEventoIds = this.buscarLocaisPorEvento(evento.getIdEvento());
    
    for(Integer localId: locaisEventoIds) {
      if(!evento.getLocais().contains(localId)) {
        boolean localFoiDesvinculado = this.desvincularLocal(localId, evento.getIdEvento()); 

        if(!localFoiDesvinculado) {
          return false;
        }
      }
    }
    
    for(Integer localId: evento.getLocais()) {
      boolean localFoiVinculado = this.vincularLocal(localId, evento.getIdEvento());

      if(!localFoiVinculado) {
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

  private boolean  desvincularLocal(Integer localId, Integer idEvento) {
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
}
