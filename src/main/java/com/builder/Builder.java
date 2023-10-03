package com.builder;

import java.util.Date;

public interface Builder {
  void resetar();
  void setNome(String nome);
  void setDescricao(String descricao);
  void setDataInicial(Date dataInicial);
  void setDataFinal(Date dataFinal);
  void setPublicoEsperado(int publicoEsperado);
  void setPublicoAlcancado(int publicoAlcancado);
  void setClassificacaoEtaria(String classificacaoEtaria);
}
