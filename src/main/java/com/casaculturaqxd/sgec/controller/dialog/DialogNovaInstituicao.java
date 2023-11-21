package com.casaculturaqxd.sgec.controller.dialog;

import java.io.IOException;
import java.util.Optional;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.models.Instituicao;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class DialogNovaInstituicao extends Dialog<Instituicao> {
  DialogInstituicaoController dialogInstituicaoController = new DialogInstituicaoController();
  private Alert mensagem = new Alert(AlertType.NONE);

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
        if(instituicao.isPresent()){
          mensagem.setAlertType(AlertType.INFORMATION);
          mensagem.setContentText("Vinculação realizada com sucesso!");
          mensagem.show();
          return instituicao.get();
        }
        return null;
      } else if(dialogButton == buttonTypeCadastrar){
          Optional<Instituicao> instituicao = dialogInstituicaoController.getInstituicao();
          if(instituicao.isEmpty()) {
            if(dialogInstituicaoController.cadastrarInstituicao() == false){
              mensagem.setAlertType(AlertType.ERROR);
              mensagem.setContentText("Não foi possivel realizar o cadastro: Nome da Instituição inválido");
              mensagem.show();
            }
            mensagem.setAlertType(AlertType.INFORMATION);
            mensagem.setContentText("Instituição cadastrada");
            mensagem.show();
          } else {
              mensagem.setAlertType(AlertType.ERROR);
              mensagem.setContentText("Não foi possivel realizar o cadastro: Instituição já existe");
              mensagem.show();
          }
      }
      return null;
  });
  
  }

}
