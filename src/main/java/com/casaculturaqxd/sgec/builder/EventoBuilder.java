package com.casaculturaqxd.sgec.builder;

import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class EventoBuilder implements Builder {
  Evento evento;

  public EventoBuilder() {
    resetar();
  }

  public Evento getEvento() {
    return evento;
  }

  public void setCargaHoraria(Time cargaHoraria) {
    evento.setCargaHoraria(cargaHoraria);
  }

  public void setCertificavel(boolean certificavel) {
    evento.setCertificavel(certificavel);
  }

  public void setLocalizacoes(SortedSet<Integer> localizacoes) {
    evento.setLocais(localizacoes);
  }

  public void setParticipantes(SortedSet<Integer> participantes) {
    evento.setListaParticipantes(participantes);
  }

  public void setAcessivelEmLibras(boolean acessivel) {
    evento.setAcessivelEmLibras(acessivel);
  }

  public void setMunicipiosEsperado(int municipiosEsperado) {
    evento.setMunicipiosEsperado(municipiosEsperado);
  }

  public void setParticipantesEsperado(int participantesEsperado) {
    evento.setParticipantesEsperado(participantesEsperado);
  }

  public EventoBuilder setListaArquivos(ArrayList<ServiceFile> listaArquivos) {
    evento.setListaArquivos(listaArquivos);
    return this;
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
