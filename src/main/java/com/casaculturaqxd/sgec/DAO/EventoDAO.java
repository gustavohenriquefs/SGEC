package com.casaculturaqxd.sgec.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Localizacao;

public class EventoDAO {
  private Connection connection;

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public boolean inserirEvento(Evento evento) {
    
    if(evento.getLocais().size() == 0) {
      return false;
    } 

    if(evento.getListaOrganizadores().size() == 0) {
      return false;
    }

    evento.getListaColaboradores();

    // evento.getListaArquivos();

    // evento.getListaParticipantes();

    try {
      String sql = "insert into evento(id_evento,nome_evento,publico_esperado,publico_alcancado,descricao,data_inicial,data_final,horario,classificacao_etaria,imagem_preview,certificavel,carga_horaria,acessivel_em_libras) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
      
      PreparedStatement stmt = connection.prepareStatement(sql);

      stmt.setInt(    1, evento.getIdEvento());
      stmt.setString( 2, evento.getNome());
      stmt.setInt(    3, evento.getPublico_esperado());
      stmt.setInt(    4, evento.getPublico_alcancado());
      stmt.setString( 5, evento.getDescricao());
      stmt.setDate(   6, java.sql.Date.valueOf(evento.getDataInicial()));
      stmt.setDate(   7, java.sql.Date.valueOf(evento.getDataFinal()));
      stmt.setTime(   8, Time.valueOf(evento.getHorario()));
      stmt.setString( 9, evento.getClassificacaoEtaria().toString());
      stmt.setString(10, null);
      stmt.setBoolean(11, evento.isCertificavel());
      stmt.setTime(   12, Time.valueOf(evento.getCargaHoraria()));
      stmt.setBoolean(13, evento.isAcessivelEmLibras());

      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }
    
    if(this.vincularLocais(evento.getLocais()) == false) {
      return false;
    }

    if(this.vincularOrganizadores(evento.getListaOrganizadores()) == false) {
      return false;
    }

    if(this.vincularColaboradores(evento.getListaColaboradores()) == false) {
      return false;
    }

    if(this.vincularParticipantes(evento.getListaParticipantes()) == false) {
      return false;
    }
    
    return true;
  }

  private boolean vincularLocais(SortedSet<Integer> locais) {
    for(Integer local: locais) {
      if(!this.vincularLocal(local)) {
        return false;
      }
    }

    return true;
  }
  
  private boolean vincularLocal(Integer local) {
    String vincLocaisSql = "INSERT INTO localizacao_evento(id_localizacao, id_evento) VALUES (?, ?)";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincLocaisSql);
      stmt.setInt(1, local);
      stmt.setInt(2, 1);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  private boolean vincularOrganizadores(SortedSet<Integer> organizadores) {
    for(Integer organizador: organizadores) {
      if(!this.vincularOrganizador(organizador)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularOrganizador(Integer organizador) {
    String vincOrganizadoresSql = "INSERT INTO organizador_evento(id_evento, id_instituicao) VALUES (?, ?);";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincOrganizadoresSql);
      stmt.setInt(1, organizador);
      stmt.setInt(2, 1);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  private boolean vincularColaboradores(SortedSet<Integer> colaboradores) {
    for(Integer colaborador: colaboradores) {
      if(!this.vincularColaborador(colaborador)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularColaborador(Integer colaborador) {
    String vincColaboradoresSql = "INSERT INTO colaborador_evento(id_evento, id_instituicao) VALUES (?, ?);";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincColaboradoresSql);
      stmt.setInt(1, colaborador);
      stmt.setInt(2, 1);
      stmt.execute();
      stmt.close();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  private boolean vincularParticipantes(SortedSet<Integer> participantes) {
    for(Integer participante: participantes) {
      if(!this.vincularParticipante(participante)) {
        return false;
      }
    }

    return true;
  }

  private boolean vincularParticipante(Integer participante) {
    String vincParticipantesSql = "INSERT INTO participante_evento(id_participante, id_evento) VALUES (?, ?);";

    try {
      PreparedStatement stmt = connection.prepareStatement(vincParticipantesSql);
      stmt.setInt(1, participante);
      stmt.setInt(2, 1);
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
