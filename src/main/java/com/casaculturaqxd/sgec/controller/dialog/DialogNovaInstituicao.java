package com.casaculturaqxd.sgec.controller.dialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.InstituicaoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class DialogNovaInstituicao extends Dialog<Instituicao> {
  DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
  DialogInstituicaoController dialogInstituicaoController = new DialogInstituicaoController();
  InstituicaoDAO instituicaoDAO = new InstituicaoDAO(db.getConnection());
  Instituicao instituicao = null;
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
      instituicao = dialogInstituicaoController.obterInstituicao();
      if (dialogButton == okButtonType) {
        if(instituicao != null){

          if(!dialogInstituicaoController.getContribuicoes().getText().isEmpty()){
            instituicao.setDescricaoContribuicao(dialogInstituicaoController.getContribuicoes().getText());
          } else {
            mensagem.setAlertType(AlertType.ERROR);
            mensagem.setContentText("Não foi possivel realizar a vinculação: A descrição não pode ser vazia");
            mensagem.show();
            return null;
          }
            
          if(!dialogInstituicaoController.getValorContribuicao().getText().isEmpty()) {
            instituicao.setValorContribuicao(dialogInstituicaoController.getValorContribuicao().getText());
          } else{
            instituicao.setValorContribuicao("Valor das contribuições"); 
          }

          if(dialogInstituicaoController.getFile() != null){
            FileChooser fileChooser = new FileChooser();
            ExtensionFilter filterImagens = new ExtensionFilter("imagem", "*.jpeg", "*.jpg", "*.png", "*.bmp");
            fileChooser.getExtensionFilters().add(filterImagens);
            instituicao.setImagemCapa(new ServiceFile(dialogInstituicaoController.getFile()));
          }

          return instituicao;

        } else {
          mensagem.setAlertType(AlertType.ERROR);
          mensagem.setContentText("Instituição não encontrada!");
          mensagem.show();
        }
        
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
