package com.casaculturaqxd.sgec.controller;

import java.io.IOException;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.UserDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;

public class LoginController {
    @FXML
    private TextField email;
    @FXML
    private PasswordField senha;
    @FXML 
    private Button loginButton;

    private Alert mensagemErro = new Alert(AlertType.NONE);

    private User usuario;
    private UserDAO userDAO = new UserDAO();
    private final DatabasePostgres userConnection = DatabasePostgres.getInstance("URL_TEST","USER_NAME_TEST","PASSWORD_TEST");

    /**
     * Carrega a página com o botão de login desabilitado
     */
    public void initialize(){
        loginButton.setDisable(true);
        userDAO.setConnection(userConnection.getConnection());
    }

    /**
     * Cria um DAO que verifica no banco de dados se as credenciais 
     * estão corretas, se sim transita para a proxima tela.
     * @throws IOException
     */
    public void authUsuario() throws IOException{
        usuario = new User(email.getText(),senha.getText());
        if(userDAO.validar(usuario)){
            App.setUsuario(usuario);
            App.setRoot("view/home");
        }
        else{
            mensagemErro.setAlertType(AlertType.ERROR);
            mensagemErro.setContentText("Usuário ou senha inválidos");
            mensagemErro.show();
        }
    }

    /**
     * Propriedade que mantém o botão desativado enquanto o email e a senha
     * não são inseridos
     */
    public void keyReleasedProperty(){
        String inputEmail = email.getText();
        String inputSenha = senha.getText();
        boolean isDisable = inputEmail.isEmpty() || inputSenha.isEmpty();
        loginButton.setDisable(isDisable);
    }
}
