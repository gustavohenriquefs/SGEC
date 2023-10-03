package com.casaculturaqxd.builder;

import java.sql.Time;
import java.util.Date;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Instituicao;

public class EventoBuilder implements Builder {
  Evento evento;

  public EventoBuilder(){
    resetar();
  }

  Evento getEvento(){
    return evento;
  }

  void setCargaHoraria(Time cargaHoraria){
    evento.setCargaHoraria(cargaHoraria);
  }

  @Override
  public void resetar() {
    evento = new Evento();
  }

  @Override
  public void setNome(String nome) {
    evento.setNome(nome);
  }

  @Override
  public void setDescricao(String descricao) {
    evento.setDescricao(descricao);
  }

  @Override
  public void setDataInicial(Date dataInicial) {
    evento.setDataInicial(dataInicial);
  }

  @Override
  public void setDataFinal(Date dataFinal) {
    evento.setDataFinal(dataFinal);
  }

  @Override
  public void setPublicoEsperado(int publicoEsperado) {
    evento.setPublico_esperado(publicoEsperado);
  }

  @Override
  public void setPublicoAlcancado(int publicoAlcancado) {
    evento.setPublico_alcancado(publicoAlcancado);
  }

  @Override
  public void setClassificacaoEtaria(String classificacaoEtaria) {
    evento.setClassificacaoEtaria(null);
  }

  @Override
  public void setColaboradores(SortedSet<Instituicao> colaboradores) {
    evento.setListaColaboradores(colaboradores);
  }

  @Override
  public void setOrganizadores(SortedSet<Instituicao> organizadores) {
    evento.setListaOrganizadores(organizadores);
  }
  
}
