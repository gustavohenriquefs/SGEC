package com.casaculturaqxd.sgec.controller.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

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
  private File file = null;
  private Instituicao instituicao = null;

  ArrayList<String> listaNomes = instituicaoDAO.listarInstituicoess();
  AutoCompletionBinding<String> binding;

  public void initialize() throws IOException {
    binding = TextFields.bindAutoCompletion(nomeInstituicao, listaNomes);
    binding.setOnAutoCompleted(autoCompletionEvent -> {
      String selected = autoCompletionEvent.getCompletion();
      instituicao = instituicaoDAO.getInstituicao(selected).get();
      nomeInstituicao.setEditable(false);
    });
    nomeInstituicao.setOnMouseClicked(mousePressed -> {
      nomeInstituicao.setEditable(true);
    });
  }

  public Instituicao obterInstituicao(){
    return instituicao;
  }

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
      if(file == null){
        Instituicao instituicao = new Instituicao(nomeInstituicao.getText(), contribuicoes.getText(), 
        valorContribuicao.getText(), null);
        try {
          instituicaoDAO.inserirInstituicao(instituicao);
          return true;
        } catch (SQLException e) {
          e.printStackTrace();
        }
      } else {
        Instituicao instituicao = new Instituicao(nomeInstituicao.getText(), contribuicoes.getText(), 
        valorContribuicao.getText(), new ServiceFile(file));
        try {
          instituicaoDAO.inserirInstituicao(instituicao);
          return true;
        } catch (SQLException e) {
          e.printStackTrace();
        }
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
