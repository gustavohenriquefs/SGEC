package com.casaculturaqxd.sgec.builder;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public interface Builder {
  void resetar();

  Builder setId(int id);

  Builder setImagemCapa(ServiceFile serviceFile);

  Builder setNome(String nome);

  Builder setDescricao(String descricao);

  Builder setDataInicial(Date dataInicial);

  Builder setDataFinal(Date dataFinal);

  Builder setPublicoEsperado(int publicoEsperado);

  Builder setPublicoAlcancado(int publicoAlcancado);

  Builder setCargaHoraria(Time cargaHoraria);

  Builder setClassificacaoEtaria(String classificacaoEtaria);

  Builder setColaboradores(ArrayList<Instituicao> colaboradores);

  Builder setOrganizadores(ArrayList<Instituicao> organizadores);
}
