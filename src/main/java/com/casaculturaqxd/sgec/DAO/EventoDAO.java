package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.SortedSet;

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
      stmt.setInt(    2, evento.getPublico_esperado());
      stmt.setInt(    3, evento.getPublico_alcancado());
      stmt.setString( 4, evento.getDescricao());
      stmt.setDate(   5, java.sql.Date.valueOf(evento.getDataInicial()));
      stmt.setDate(   6, java.sql.Date.valueOf(evento.getDataFinal()));
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

  public boolean alterarEvento(Evento evento) {
    try {
      String sql = "update evento set nome_evento=?, publico_esperado=?, publico_alcancado=?, descricao=?, data_inicial=?, data_final=?, horario=?, classificacao_etaria=?, imagem_preview=?, certificavel=?, carga_horaria=?, acessivel_em_libras=?, localizacao_id_localizacao=? where id_evento=?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString( 1, evento.getNome());
      stmt.setString( 2, evento.getDescricao());
      stmt.setDate(   3, java.sql.Date.valueOf(evento.getDataInicial()));
      stmt.setDate(   4, java.sql.Date.valueOf(evento.getDataFinal()));
      stmt.setTime(   5, Time.valueOf(evento.getHorario()));
      stmt.setInt(    6, evento.getIdEvento());
      stmt.execute();
      stmt.close();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }
}
