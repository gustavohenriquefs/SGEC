package com.casaculturaqxd.sgec.controller.dialog;

import com.casaculturaqxd.sgec.models.Instituicao;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class DialogInstituicaoController {
  @FXML
  private TextField nomeInstituicao;

  @FXML
  private TextField contribuicoes;

  @FXML
  private TextField valorContribuicao;

  @FXML
  private ImageView imagem;

  Dialog<Instituicao> dialog;
  ObservableList<String> nomesInstituicoes;

}
