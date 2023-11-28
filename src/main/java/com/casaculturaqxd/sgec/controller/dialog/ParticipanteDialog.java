package com.casaculturaqxd.sgec.controller.dialog;

import java.io.IOException;

import com.casaculturaqxd.sgec.models.Participante;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.util.Callback;

public class ParticipanteDialog extends Dialog<Participante> {

  private Participante participante;

  public ParticipanteDialog(Participante participante) {
    super();

    this.participante = participante;

    this.setTitle("Adicionar participante");

    buildUI();
    setResultConverter();
    removeErrorStyle();

  }

  private void removeErrorStyle() {
    // nome.textProperty().addListener((observable, oldValue, newValue) -> {
    //   nome.setStyle("-fx-border-color: none;");
    // });

    // areaDeAtuacao.textProperty().addListener((observable, oldValue, newValue) -> {
    //   areaDeAtuacao.setStyle("-fx-border-color: none;");
    // });

    // bio.textProperty().addListener((observable, oldValue, newValue) -> {
    //   bio.setStyle("-fx-border-color: none;");
    // });

    // linkMapaDaCultura.textProperty().addListener((observable, oldValue, newValue) -> {
    //   linkMapaDaCultura.setStyle("-fx-border-color: none;");
    // });
  }

  private Parent createFieldParticipanteView() {
    String path = "view/fields/fieldParticipante.fxml";

    FXMLLoader fielParticipanteViewLoader = new FXMLLoader(getClass().getResource(path));

    try {
      return fielParticipanteViewLoader.load();
    } catch (IOException e) {
      e.printStackTrace();

      return null;
    }
  }

  private void setPropertyValues() {
  }

  private void setResultConverter() {
    Callback<ButtonType, Participante> participanteResult = new Callback<ButtonType, Participante>() {
      @Override
      public Participante call(ButtonType param) {
        if (param == ButtonType.OK) {
          setPropertyValues();

          return participante;
        }

        return null;
      }
    };
    setResultConverter(participanteResult);
  }

  private void buildUI() {
    Parent pane = createFieldParticipanteView();
    getDialogPane().setContent(pane); 
    getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    Button btn = (Button) getDialogPane().lookupButton(ButtonType.OK);

    btn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (!validateDialog()) {
          event.consume();
        }
      }

      private boolean validateDialog() {
        return true;
      }
    });
  }
}
