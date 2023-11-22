package com.casaculturaqxd.sgec.controller.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Optional;

import com.casaculturaqxd.sgec.DAO.InstituicaoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

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

  private Stage stage;
  private File file;

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

  public boolean cadastrarInstituicao() {
    if(nomeInstituicao.getText() != ""){
      Instituicao instituicao = new Instituicao(nomeInstituicao.getText());
      try {
        instituicaoDAO.inserirInstituicao(instituicao);
        return true;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } 
    return false;
  }
  
  public void carregarImagem(){
    InputStream fileAsStream;
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Alterar foto da instituição");
    ExtensionFilter filterImagens = new ExtensionFilter("imagem", "*.jpeg", "*.jpg", "*.png", "*.bmp");
    fileChooser.getExtensionFilters().add(filterImagens);
    try {
      file = fileChooser.showOpenDialog(stage);
      fileAsStream = new FileInputStream(file);
      imagem.setImage(new Image(fileAsStream));
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (NullPointerException e) {
        e.printStackTrace();
    }
  }

  public File getFile() {
    return file;
  }

}
