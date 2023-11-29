package com.casaculturaqxd.sgec.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import com.casaculturaqxd.sgec.DAO.ParticipanteDAO;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Participante;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DialogParticipanteController {
  private Participante participante;

  private ArrayList<String> nomes;

  private File lastDirectoryOpen;

  Stage stage;
  
  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button adicionarImagem;

  @FXML
  private TextField areaAtuacao;

  @FXML 
  private TextField linkMapaCultura;

  @FXML
  private ImageView imagem;

  @FXML
  private TextArea minibioArtista;

  @FXML
  private TextField nomeArtista;

  @FXML
  void initialize() {
    this.participante = new Participante(0);
    this.nomes = new ArrayList<>();

    initAutoComplete();

    adicionarImagem.setOnAction(event -> {
      selecionarImagemCapa();
    });

  }

  private void selecionarImagemCapa() {
    FileChooser fileChooser = new FileChooser();

    if (lastDirectoryOpen != null) {
        fileChooser.setInitialDirectory(lastDirectoryOpen);
    }

    File arquivoSelecionado = fileChooser.showOpenDialog(stage);

    lastDirectoryOpen = arquivoSelecionado.getParentFile();

    if(arquivoSelecionado != null) {
      this.participante.setImagemCapa(new ServiceFile(arquivoSelecionado));

      InputStream fileAsStream = null;
      
      try {
        fileAsStream = new FileInputStream(this.participante.getImagemCapa().getContent());
      } catch (IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Erro");
        alert.setContentText("Não foi possível carregar a imagem selecionada!");

        alert.showAndWait();
      }

      this.imagem.setImage(new Image(fileAsStream));
    }
  }

  private void updateParticipanteData() {
    participante.setNome(nomeArtista.getText());
    participante.setAreaDeAtuacao(areaAtuacao.getText());
    participante.setBio(minibioArtista.getText());
    participante.setLinkMapaDaCultura(linkMapaCultura.getText());
  }

  public Participante getParticipante() {
    this.updateParticipanteData();

    return participante;
  }

  public void addNome(String nome) {
    nomes.add(nome);
  }

  private void initAutoComplete() {
    Connection connection = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD").getConnection();
    ParticipanteDAO participanteDAO = new ParticipanteDAO(connection);

    try {
      nomes = participanteDAO.obterListaNomesParticipantes();
    } catch (SQLException e) {
      nomes = new ArrayList<>();
    }

    AutoCompletionBinding<String> autoCompletionBinding = TextFields.bindAutoCompletion(nomeArtista, nomes);

    autoCompletionBinding.setOnAutoCompleted(event -> {
      if (event.getCompletion() != null) {
        try {
          Optional<Participante> participanteOp = participanteDAO.getParticipantePorNome(event.getCompletion());
          
          if (participanteOp.isPresent()) {
            participante = participanteOp.get();
          }

          nomeArtista.setText(participante.getNome());
          areaAtuacao.setText(participante.getAreaDeAtuacao());
          minibioArtista.setText(participante.getBio());

          ServiceFileDAO serviceFileDAO = new ServiceFileDAO(connection);
          
          File file = serviceFileDAO.getContent(participante.getImagemCapa());

          InputStream fileAsStream;
          
          try {
            fileAsStream = new FileInputStream(file);
            this.imagem.setImage(new Image(fileAsStream));

            imagem.setImage(new Image(file.toURI().toString()));
          } catch (Exception e) {
            imagem.setImage(null);

            e.printStackTrace();
          }

        } catch (SQLException e) {
          e.printStackTrace();
        }
    }});
  }

}
