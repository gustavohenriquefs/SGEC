package com.casaculturaqxd.sgec.builder;

import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.GrupoEventos;
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

  public EventoBuilder setCargaHoraria(Time cargaHoraria) {
    evento.setCargaHoraria(cargaHoraria);
    return this;
  }

  public EventoBuilder setCertificavel(boolean certificavel) {
    evento.setCertificavel(certificavel);
    return this;
  }

  public EventoBuilder setLocalizacoes(SortedSet<Integer> localizacoes) {
    evento.setLocais(localizacoes);
    return this;
  }

  public EventoBuilder setParticipantes(SortedSet<Integer> participantes) {
    evento.setListaParticipantes(participantes);
    return this;
  }

  public EventoBuilder setAcessivelEmLibras(boolean acessivel) {
    evento.setAcessivelEmLibras(acessivel);
    return this;
  }

  public EventoBuilder setMunicipiosEsperado(int municipiosEsperado) {
    evento.setNumMunicipiosEsperado(municipiosEsperado);
    return this;
  }

  public EventoBuilder setNumMunicipiosAlcancado(int numMunicipiosAlcancado) {
    evento.setNumMunicipiosAlcancado(numMunicipiosAlcancado);
    return this;
  }

  public EventoBuilder setNumParticipantesEsperado(int participantesEsperado) {
    evento.setNumParticipantesEsperado(participantesEsperado);
    return this;
  }

  public EventoBuilder setNumParticipantesAlcancado(int numParticipantesAlcancado) {
    evento.setNumParticipantesAlcancado(numParticipantesAlcancado);
    return this;
  }

  public EventoBuilder setNumColaboradoresEsperado(int numColaboradoresEsperado) {
    evento.setNumColaboradoresEsperado(numColaboradoresEsperado);
    return this;
  }

  public EventoBuilder setNumColaboradoresAlcancado(int numColaboradoresAlcancado) {
    evento.setNumColaboradoresAlcancado(numColaboradoresAlcancado);
    return this;
  }

  public EventoBuilder setGrupoEventos(GrupoEventos grupoEventos) {
    evento.setGrupoEventos(grupoEventos);
    return this;
  }

  public EventoBuilder setListaArquivos(ArrayList<ServiceFile> listaArquivos) {
    evento.setListaArquivos(listaArquivos);
    return this;
  }

  public EventoBuilder setListaMetas(ArrayList<Meta> metas) {
    evento.setListaMetas(metas);
    return this;

  }

  @Override
  public void resetar() {
    evento = new Evento();
  }

  @Override
  public Builder setId(int idEvento) {
    evento.setIdEvento(idEvento);
    return this;
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
  public Builder setImagemCapa(ServiceFile serviceFile) {
    evento.setImagemCapa(serviceFile);
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
    evento.setListaColaboradores(colaboradores);
    return this;
  }

  @Override
  public Builder setOrganizadores(ArrayList<Instituicao> organizadores) {
    evento.setListaOrganizadores(organizadores);
    return this;
  }

  public EventoBuilder setHorario(Time horario) {
    evento.setHorario(horario);
    return this;
  }
}
