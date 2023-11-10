package com.casaculturaqxd.sgec.builder;

import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Meta;

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

  public void setListaMetas(ArrayList<Meta> metas) {
    evento.setListaMetas(metas);

  }

  @Override
  public void resetar() {
    evento = new Evento();
  }

  @Override
  public Builder setNome(String nome) {
    evento.setNome(nome);
    return this;
  }

  @Override
  public Builder setDescricao(String descricao) {
    evento.setDescricao(descricao);
    return this;
  }

  @Override
  public Builder setDataInicial(Date dataInicial) {
    evento.setDataInicial(dataInicial);
    return this;
  }

  @Override
  public Builder setDataFinal(Date dataFinal) {
    evento.setDataFinal(dataFinal);
    return this;
  }

  @Override
  public Builder setPublicoEsperado(int publicoEsperado) {
    evento.setPublicoEsperado(publicoEsperado);
    return this;
  }

  @Override
  public Builder setPublicoAlcancado(int publicoAlcancado) {
    evento.setPublicoAlcancado(publicoAlcancado);
    return this;
  }

  @Override
  public Builder setClassificacaoEtaria(String classificacaoEtaria) {
    evento.setClassificacaoEtaria(classificacaoEtaria);
    return this;
  }

  @Override
  public Builder setColaboradores(ArrayList<Instituicao> colaboradores) {
    evento.setListaOrganizadores(colaboradores);
    return this;
  }

  @Override
  public Builder setOrganizadores(ArrayList<Instituicao> organizadores) {
    evento.setListaColaboradores(organizadores);
    return this;
  }

}
