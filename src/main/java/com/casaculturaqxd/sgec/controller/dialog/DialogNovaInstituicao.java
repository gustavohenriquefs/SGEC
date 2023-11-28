package com.casaculturaqxd.sgec.controller.dialog;

import java.io.IOException;
import java.util.Optional;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.InstituicaoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class DialogNovaInstituicao extends Dialog<Instituicao> {
  DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
  DialogInstituicaoController dialogInstituicaoController = new DialogInstituicaoController();
  InstituicaoDAO instituicaoDAO = new InstituicaoDAO(db.getConnection());

  Instituicao instituicao = null;
  private Alert mensagem = new Alert(AlertType.NONE);
  ButtonType buttonTypeCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
  ButtonType buttonTypeCadastrar = new ButtonType("Cadastrar nova instituição", ButtonBar.ButtonData.APPLY);
  ButtonType okButtonType;
  

  public DialogNovaInstituicao(ButtonType okButtonType) throws IOException{
    super();
    this.okButtonType = okButtonType;
    carregaDialog();
    
    this.setResultConverter(dialogButton -> {
      if(dialogButton == okButtonType){
        return instituicao;
      }
      return null;
  });

  this.getDialogPane().lookupButton(okButtonType).addEventFilter(ActionEvent.ACTION, event -> {
    instituicao = dialogInstituicaoController.obterInstituicao();
    if(instituicao != null){
      if(!dialogInstituicaoController.getContribuicoes().getText().isEmpty()){
        instituicao.setDescricaoContribuicao(dialogInstituicaoController.getContribuicoes().getText());
      } else {
        mensagem.setAlertType(AlertType.ERROR);
        mensagem.setContentText("Não foi possivel realizar a vinculação: A descrição não pode ser vazia");
        mensagem.show();
        event.consume();
      }
        
      instituicao.setValorContribuicao(dialogInstituicaoController.getValorContribuicao().getText());

      if(dialogInstituicaoController.getFile() != null){
        instituicao.setImagemCapa(new ServiceFile(dialogInstituicaoController.getFile()));
      }
    } 
    else {
      mensagem.setAlertType(AlertType.ERROR);
      mensagem.setContentText("Instituição não encontrada!");
      mensagem.show();
      event.consume();
    }
  });

  this.getDialogPane().lookupButton(buttonTypeCadastrar).addEventFilter(ActionEvent.ACTION, event -> {
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
    event.consume();
  });

  }

  void carregaDialog() throws IOException {
    this.setTitle("Dialog Instituição");
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/Dialog/dialogInstituicoes.fxml"));
    this.setDialogPane(fxmlLoader.load());
    dialogInstituicaoController = fxmlLoader.getController();
    this.getDialogPane().getButtonTypes().addAll(buttonTypeCancelar, buttonTypeCadastrar, okButtonType);
  }

}
