package com.casaculturaqxd.sgec.controller.dialog;

import java.io.IOException;
import java.util.Optional;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.models.Instituicao;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class DialogNovaInstituicao extends Dialog<Instituicao> {
  DialogInstituicaoController dialogInstituicaoController = new DialogInstituicaoController();

  public DialogNovaInstituicao(ButtonType okButtonType) throws IOException{
    super();
    this.setTitle("Dialog Instituição");
    ButtonType buttonTypeCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    ButtonType buttonTypeCadastrar = new ButtonType("Cadastrar nova instituição", ButtonBar.ButtonData.APPLY);
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/Dialog/dialogInstituicoes.fxml"));
    this.setDialogPane(fxmlLoader.load());
    dialogInstituicaoController = fxmlLoader.getController();
    this.getDialogPane().getButtonTypes().addAll(buttonTypeCancelar, buttonTypeCadastrar, okButtonType);
    this.setResultConverter(dialogButton -> {
      if (dialogButton == okButtonType) {
        Optional<Instituicao> instituicao = dialogInstituicaoController.getInstituicao();
        System.out.println(instituicao.get());
        return instituicao.orElse(null);
      }
      return null;
  });
  
  }

}
