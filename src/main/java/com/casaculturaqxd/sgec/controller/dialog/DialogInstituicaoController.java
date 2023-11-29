package com.casaculturaqxd.sgec.controller.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import com.casaculturaqxd.sgec.DAO.InstituicaoDAO;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
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
  ServiceFileDAO serviceFileDAO = new ServiceFileDAO(db.getConnection());

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

  AutoCompletionBinding<String> binding;
  ArrayList<String> listaNomes;

  public void initialize() throws IOException {
    carregaListaNomes();
    binding = TextFields.bindAutoCompletion(nomeInstituicao, listaNomes);
    bindingSetOnAutoCompleted();
    nomeInstituicaoSetOnMouseClicked();
    instituicao = new Instituicao();
  }

  private void updateInstituicao() {
    instituicao.setNome(nomeInstituicao.getText());
    instituicao.setDescricaoContribuicao(contribuicoes.getText());
    instituicao.setValorContribuicao(valorContribuicao.getText());

    if (file != null) {
      instituicao.setImagemCapa(new ServiceFile(file));
    }
  }

  public Instituicao obterInstituicao() {
    this.updateInstituicao();
    
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
    Optional<Instituicao> temp = null;
    try {
      temp = instituicaoDAO.getInstituicao(nomeInstituicao.getText());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return temp;
  }

  public void carregaListaNomes() {
    try {
      listaNomes = instituicaoDAO.listarInstituicoes();
    } catch (SQLException e) {
      listaNomes = new ArrayList<>();
    }
  }

  public boolean cadastrarInstituicao() {
    if (nomeInstituicao.getText() != "") {
      ServiceFile serviceFileTemp = file != null ? new ServiceFile(file) : null;
      if (serviceFileTemp != null) {
        try {
          if (serviceFileDAO.getArquivo(serviceFileTemp.getFileKey()).isEmpty()) {
            serviceFileDAO.inserirArquivo(serviceFileTemp);
          } else {
            serviceFileTemp = serviceFileDAO.getArquivo(serviceFileTemp.getFileKey()).get();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      Instituicao instituicao = new Instituicao(nomeInstituicao.getText(), contribuicoes.getText(),
          valorContribuicao.getText(), serviceFileTemp);
      try {
        instituicaoDAO.inserirInstituicao(instituicao);
        this.carregaListaNomes();
        this.binding = TextFields.bindAutoCompletion(nomeInstituicao, listaNomes);
        bindingSetOnAutoCompleted();
        nomeInstituicaoSetOnMouseClicked();
        return true;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public void carregarImagem() {
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

  public void bindingSetOnAutoCompleted() {
    binding.setOnAutoCompleted(autoCompletionEvent -> {
      String selected = autoCompletionEvent.getCompletion();
      try {
        instituicao = instituicaoDAO.getInstituicao(selected).get();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      if (instituicao.getImagemCapa() != null) {
        InputStream fileAsStream;
        try {
          file = serviceFileDAO.getContent(instituicao.getImagemCapa());
          fileAsStream = new FileInputStream(file);
          this.imagem.setImage(new Image(fileAsStream));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (NullPointerException e) {
          e.printStackTrace();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      nomeInstituicao.setEditable(false);
    });
  }

  public void nomeInstituicaoSetOnMouseClicked() {
    nomeInstituicao.setOnMouseClicked(mousePressed -> {
      nomeInstituicao.setEditable(true);
    });
  }

}
