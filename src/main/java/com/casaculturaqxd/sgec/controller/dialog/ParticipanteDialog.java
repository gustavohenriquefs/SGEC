package com.casaculturaqxd.sgec.controller.dialog;

import com.casaculturaqxd.sgec.models.Participante;

import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ParticipanteDialog extends Dialog<Participante> {
  private Participante participante;

  private TextField nome;
  private TextField areaDeAtuacao;
  private TextField bio;
  private TextField linkMapaDaCultura;

  public ParticipanteDialog(Participante participante) {
    super();

    this.setTitle("Adicionar participante");
    this.participante = participante;

    buildUI();
    setPropertyBindings();
    setResultConverter();
  }

  private Pane createGridPane() {
    VBox content = new VBox();

    Label labelNome = new Label("Nome"); // TODO: add auto complete
    Label labelAreaDeAtuacao = new Label("Área de atuação");
    Label labelBio = new Label("Bio");  
    Label labelLinkMapaDaCultura = new Label("Link do Mapa da Cultura");

    GridPane gridPane = new GridPane();

    gridPane.setHgap(10);
    gridPane.setVgap(5);
    
    gridPane.add(labelNome, 0, 0);
    gridPane.add(nome, 1, 0);
    gridPane.add(labelAreaDeAtuacao, 0, 1);
    gridPane.add(areaDeAtuacao, 1, 1);
    gridPane.add(labelBio, 0, 2);
    gridPane.add(bio, 1, 2);
    gridPane.add(labelLinkMapaDaCultura, 0, 3);
    gridPane.add(linkMapaDaCultura, 1, 3);
    
    content.getChildren().add(gridPane);

    return content;
  }

  private void setPropertyBindings() {
  }

  private void setResultConverter() {
  }

  private void buildUI() {
    Pane pane = createGridPane();
    getDialogPane().setContent(pane); 
  }
}
