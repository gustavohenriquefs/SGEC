package com.casaculturaqxd.builder;

import java.util.Date;
import java.util.Set;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Instituicao;

public class EventoBuilder implements Builder {
  Evento evento;

  public EventoBuilder(){
    resetar();
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
    // TODO Auto-generated method stub
  }

  @Override
  public void setDataInicial(Date dataInicial) {
    // TODO Auto-generated method stub
  }

  @Override
  public void setDataFinal(Date dataFinal) {
    // TODO Auto-generated method stub
  }

  @Override
  public void setPublicoEsperado(int publicoEsperado) {
    // TODO Auto-generated method stub
  }

  @Override
  public void setPublicoAlcancado(int publicoAlcancado) {
    // TODO Auto-generated method stub
  }

  @Override
  public void setClassificacaoEtaria(String classificacaoEtaria) {
    // TODO Auto-generated method stub
  }

  @Override
  public void setColaboradores(Set<Instituicao> colaboradores) {
    // TODO Auto-generated method stub
  }

  @Override
  public void setOrganizadores(Set<Instituicao> organizadores) {
    // TODO Auto-generated method stub
  }
  
}
