package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.UserDAO;
import com.casaculturaqxd.sgec.models.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ConfiguracoesController {
    @FXML
    private VBox root;

    @FXML
    private TextField nome;

    @FXML
    private TextField senhaAntiga;

    @FXML
    private TextField novaSenha;

    @FXML
    private TextField confirmarSenha;

    @FXML 
    private Button salvar, trocarSenha;

    @FXML
    private TextField email;

    private User usuario;

    private UserDAO userDAO;

    private Alert alert;

    public void initialize() {
        try {
            loadMenu();
            App.setUsuario(usuario);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu =
                new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }

    public void alterarEmailESenha() throws SQLException{
        /*
         * que seta o autocommit para false, chama a operação de atualizar de UserDAO e realiza rollback em caso de SQLException. 
         * Setar autocommit para true.  Verificar se o nome e o email nos textfields não é vazio
         */
        try {
            if(!(nome.getText().isEmpty()) && !(email.getText().isEmpty())){
                usuario.setEmail(email.getText());
                usuario.setSenha(novaSenha.getText());
                userDAO.update(usuario);
                alert = new Alert(AlertType.CONFIRMATION, "Alterações salvas com sucesso");
                alert.showAndWait();
            }else{
                alert = new Alert(AlertType.CONFIRMATION, "Nem todos os campos foram preenchidos");
                alert.showAndWait();
            }
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    public void alterarSenha(){
        /*
         * que seta o autocommit para false, chama a operação de atualizar de UserDAO e realiza rollback em caso de SQLException. 
         * Setar autocommit para true. Verificar se a senha antiga é válida para o usuário, 
         * e separadamente, se a senha nova foi inserida corretamente nos campos de 'nova senha' e 'confirmar senha'
         */
        try {
            if(!usuario.getSenha().isEmpty()){
                if(novaSenha.getText().equals(confirmarSenha.getText())){
                    usuario.setSenha(novaSenha.getText());
                    userDAO.update(usuario);
                    alert = new Alert(AlertType.CONFIRMATION, "Alterações salvas com sucesso");
                    alert.showAndWait();
                }else{
                    alert = new Alert(AlertType.CONFIRMATION, "Senha de confirmação deve ser igual ao campo de nova senha");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            new SQLException();
        }
    }
}
