package com.casaculturaqxd.builder;

import java.sql.Time;
import java.sql.Date;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.models.Evento;

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

  void setCertificavel(boolean certificavel){
    evento.setCertificavel(certificavel);
  }

  void setLocalizacoes(SortedSet<Integer> localizacoes){
    evento.setLocais(localizacoes);
  }

  void setParticipantes(SortedSet<Integer> participantes){
    evento.setListaParticipantes(participantes);
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
    evento.setPublicoEsperado(publicoEsperado);
  }

  @Override
  public void setPublicoAlcancado(int publicoAlcancado) {
    evento.setPublicoAlcancado(publicoAlcancado);
  }

  @Override
  public void setClassificacaoEtaria(String classificacaoEtaria) {
    evento.setClassificacaoEtaria(classificacaoEtaria);
  }

  @Override
  public void setColaboradores(SortedSet<Integer> colaboradores) {
    evento.setListaColaboradores(colaboradores);
  }

  @Override
  public void setOrganizadores(SortedSet<Integer> organizadores) {
    evento.setListaOrganizadores(organizadores);
  }
  
}
