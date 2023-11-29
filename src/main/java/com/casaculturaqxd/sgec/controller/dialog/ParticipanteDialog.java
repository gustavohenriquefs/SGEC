package com.casaculturaqxd.sgec.controller.dialog;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.ParticipanteDAO;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
import com.casaculturaqxd.sgec.controller.DialogParticipanteController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Participante;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.util.Callback;

public class ParticipanteDialog extends Dialog<Participante> {
  private DialogParticipanteController dialogParticipanteController;
  private ButtonType btnTypeCadastrarParticipante;
  private ButtonType btnTypeVincularParticipante;

  public ParticipanteDialog(Participante participante) {
    super();

    this.setTitle("Adicionar participante");

    dialogParticipanteController = new DialogParticipanteController();

    buildUI();
    setResultConverter();

  }

  private DialogPane createFieldParticipanteView() {
    String path = "view/Dialog/dialogParticipante.fxml";

    FXMLLoader fielParticipanteViewLoader = new FXMLLoader(App.class.getResource(path));

    DialogPane fieldParticipanteView = null;

    try {
      fieldParticipanteView = fielParticipanteViewLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.dialogParticipanteController = fielParticipanteViewLoader.getController();

    return fieldParticipanteView ;
  }

  private void setResultConverter() {
    Callback<ButtonType, Participante> participanteResult = new Callback<ButtonType, Participante>() {
      @Override
      public Participante call(ButtonType param) {
        if (param == btnTypeVincularParticipante) {
          return dialogParticipanteController.getParticipante();
        }

        return null;
      }
    };

    setResultConverter(participanteResult);
  }

  private void criarCadParticipanteBtnType() {
    this.btnTypeCadastrarParticipante = new ButtonType("Cadastrar participante", ButtonData.OK_DONE);
  }

  private void criarVincParticipanteBtnType() {
    this.btnTypeVincularParticipante = new ButtonType("Vincular participante", ButtonData.APPLY);
  }

  private void buildUI() {
    DialogPane pane = createFieldParticipanteView();

    this.criarCadParticipanteBtnType();
    this.criarVincParticipanteBtnType();

    setDialogPane(pane);
    getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, btnTypeCadastrarParticipante, btnTypeVincularParticipante);

    this.setarAcaoCadParticipante();
    this.setarAcaoVincParticipante();
    
  }

  private void setarAcaoVincParticipante() {
    Button btnVincParticipante = (Button) getDialogPane().lookupButton(btnTypeVincularParticipante);

    btnVincParticipante.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {

        if(dialogParticipanteController.getParticipante().getIdParticipante() == 0) {
          Alert alert = new Alert(Alert.AlertType.ERROR);

          alert.setTitle("Erro");
          alert.setContentText("Não foi possível vincular o participante informado! \n"
            + "É necessário quer o participante esteja cadastrado previamente antes de vinculá-lo.");

          alert.showAndWait();
          
          return;
        }
      }
    });
  }
  
  private void setarAcaoCadParticipante() {
    Button btnCadParticipante = (Button) getDialogPane().lookupButton(btnTypeCadastrarParticipante);
    
    btnCadParticipante.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        cadastrarParticipante();

        event.consume();
      }

      private void cadastrarParticipante() {
        Connection connection = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD")
          .getConnection();
        ParticipanteDAO participanteDAO = new ParticipanteDAO(connection);

        Participante participante = dialogParticipanteController.getParticipante();

        ServiceFileDAO serviceFileDAO = new ServiceFileDAO(connection);

        Optional<ServiceFile> optionalFile = Optional.empty();

        try {
          if(participante.getImagemCapa() != null) {
            optionalFile = serviceFileDAO.getArquivo(participante.getImagemCapa().getFileKey());
          }
        } catch (SQLException e) {
          Alert alert = new Alert(Alert.AlertType.ERROR);

          alert.setTitle("Erro");
          alert.setContentText("Não foi possível cadastrar o participante informado! Falha ao tentar buscar imagem de capa.");

          e.printStackTrace();
        }

        if(optionalFile.isPresent()){
          participante.setImagemCapa(optionalFile.get());

          try {
            participante.getImagemCapa().setContent(
              serviceFileDAO.getContent(optionalFile.get())
            );
          } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Erro");
            alert.setContentText("Não foi possível cadastrar o participante informado! Falha ao tentar buscar imagem de capa.");

            alert.showAndWait();

            e.printStackTrace();
          }
        } else {
          try {
            if(participante.getImagemCapa() != null) {
              serviceFileDAO.inserirArquivo(participante.getImagemCapa());
            }
          } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            
            alert.setTitle("Erro");
            alert.setContentText("Não foi possível cadastrar o participante informado! Falha ao tentar inserir imagem de capa.");

            alert.showAndWait();

            e.printStackTrace();
          }
        }

        try {
          participanteDAO.inserirParticipante(participante);

          dialogParticipanteController.addNome(participante.getNome());

          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

          alert.setTitle("Sucesso");
          alert.setContentText("Participante cadastrado com sucesso!");

          alert.showAndWait();

        } catch (Exception e) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Erro");
          alert.setContentText("Não foi possível cadastrar o participante informado");
          alert.showAndWait();
        }
      }
    });
  }
}
