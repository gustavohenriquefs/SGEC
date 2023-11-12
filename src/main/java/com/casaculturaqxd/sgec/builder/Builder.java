package com.casaculturaqxd.sgec.builder;

import java.sql.Date;
import java.util.ArrayList;
import com.casaculturaqxd.sgec.models.Instituicao;

public interface Builder {
  void resetar();

  Builder setNome(String nome);

  Builder setDescricao(String descricao);

  Builder setDataInicial(Date dataInicial);

  Builder setDataFinal(Date dataFinal);

  Builder setPublicoEsperado(int publicoEsperado);

  Builder setPublicoAlcancado(int publicoAlcancado);

  Builder setClassificacaoEtaria(String classificacaoEtaria);

  Builder setColaboradores(ArrayList<Instituicao> colaboradores);

  Builder setOrganizadores(ArrayList<Instituicao> organizadores);
}
