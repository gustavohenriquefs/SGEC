package com.casaculturaqxd.sgec.builder;

import java.sql.Date;
import java.util.SortedSet;

public interface Builder {
  void resetar();
  void setNome(String nome);
  void setDescricao(String descricao);
  void setDataInicial(Date dataInicial);
  void setDataFinal(Date dataFinal);
  void setPublicoEsperado(int publicoEsperado);
  void setPublicoAlcancado(int publicoAlcancado);
  void setClassificacaoEtaria(String classificacaoEtaria);
  void setColaboradores(SortedSet<Integer> colaboradores);
  void setOrganizadores(SortedSet<Integer> organizadores);
}
