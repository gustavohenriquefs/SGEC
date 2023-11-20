package com.casaculturaqxd.sgec.controller.dialog;

import java.util.Optional;

import com.casaculturaqxd.sgec.DAO.InstituicaoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Instituicao;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class DialogInstituicaoController {
  DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
  InstituicaoDAO instituicaoDAO = new InstituicaoDAO(db.getConnection());
  
  @FXML
  private TextField nomeInstituicao;

  @FXML
  private TextArea contribuicoes;

  @FXML
  private TextField valorContribuicao;

  @FXML
  private ImageView imagem;

  public TextField getNomeInstituicao() {
    return nomeInstituicao;
  }

  public TextArea getContribuicoes() {
    return contribuicoes;
  }

  public ImageView getImagem() {
    return imagem;
  }

  public TextField getValorContribuicao() {
    return valorContribuicao;
  }

  public Optional<Instituicao> getInstituicao() {
    Optional<Instituicao> temp = instituicaoDAO.getInstituicao(nomeInstituicao.getText());
    return temp;
  }
  
}
