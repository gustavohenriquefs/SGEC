package com.casaculturaqxd.builder;

import java.util.Date;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.models.Instituicao;

public interface Builder {
  void resetar();
  void setNome(String nome);
  void setDescricao(String descricao);
  void setDataInicial(Date dataInicial);
  void setDataFinal(Date dataFinal);
  void setPublicoEsperado(int publicoEsperado);
  void setPublicoAlcancado(int publicoAlcancado);
  void setClassificacaoEtaria(String classificacaoEtaria);
  void setColaboradores(SortedSet<Instituicao> colaboradores);
  void setOrganizadores(SortedSet<Instituicao> organizadores);
}
