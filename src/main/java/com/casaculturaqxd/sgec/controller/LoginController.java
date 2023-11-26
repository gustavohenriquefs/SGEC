package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.UserDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField fieldEmail;
    @FXML
    private PasswordField fieldSenha;
    @FXML
    private Button loginButton;

    private Alert alerta;

    private User usuario;
    private UserDAO userDAO;
    private final DatabasePostgres userConnection = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");

    /**
     * Carrega a página com o botão de login desabilitado
     */
    public void initialize() {
        userDAO = new UserDAO(userConnection.getConnection());
        alerta = new Alert(AlertType.NONE);
        loginButton.setDisable(true);
    }

    /**
     * verifica no banco de dados se as credenciais estão corretas,
     * se sim transita para a proxima tela.
     * 
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void authUsuario() throws IOException {
        usuario = new User(fieldEmail.getText(), fieldSenha.getText());
        try {
            if (userDAO.validar(usuario)) {
                App.setUsuario(usuario);
                App.setRoot("view/home");
            } else {
                alerta.setAlertType(AlertType.ERROR);
                alerta.setContentText("Usuário ou senha inválidos");
                alerta.show();
            }
        } catch (SQLException e) {
            alerta.setAlertType(AlertType.ERROR);
            alerta.setContentText("Falha realizando validação de usuário");
            alerta.show();
        }
    }

    public void goToCadastro() {
        try {
            App.setRoot("view/cadastro");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Propriedade que mantém o botão desativado enquanto o email e a senha não são
     * inseridos
     */
    public void keyReleasedProperty() {
        String inputEmail = fieldEmail.getText();
        String inputSenha = fieldSenha.getText();
        boolean isDisable = inputEmail.isEmpty() || inputSenha.isEmpty();
        loginButton.setDisable(isDisable);
    }
}
