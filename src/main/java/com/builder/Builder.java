package com.builder;

import java.util.Date;

public interface Builder {
  void resetar();
  void setNome(String nome);
  void setDescricao(String descricao);
  void setDataInicial(Date dataInicial);
}
