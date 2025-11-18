package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.UserDAO;
import com.casaculturaqxd.sgec.jdbc.Database;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CadastrarController {

    @FXML
    private TextField nomeUsuario;
    @FXML
    private TextField email;
    @FXML
    private PasswordField senha;
    @FXML
    private PasswordField confirmarSenha;
    @FXML
    private Button signupButton;

    private Alert alerta;
    private User usuario;
    private UserDAO userDAO;

    private final Database userConnection = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");

    /**
     * Carrega a página com o botão de cadastro desabilitado
     */
    public void initialize() {
        userDAO = new UserDAO(userConnection.getConnection());
        alerta = new Alert(AlertType.NONE);
        signupButton.setDisable(true);
    }

    /**
     * Cadastra um novo usuário no banco de dados após validar os dados
     * 
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void cadastrarUsuario() throws IOException, SQLException {
        String inputNome = nomeUsuario.getText();
        String inputEmail = email.getText();
        String inputSenha = senha.getText();
        String inputConfirmarSenha = confirmarSenha.getText();

        // Validação: verifica se a senha e a confirmação correspondem
        if (!inputSenha.equals(inputConfirmarSenha)) {
            alerta.setAlertType(AlertType.ERROR);
            alerta.setContentText("As senhas não correspondem");
            alerta.show();
            return;
        }

        try {
            // Verifica se o usuário já existe
            if (userDAO.usuarioExists(inputEmail)) {
                alerta.setAlertType(AlertType.ERROR);
                alerta.setContentText("Já existe um usuário cadastrado com este e-mail");
                alerta.show();
            } else {
                // Cria novo usuário (editor = false por padrão)
                usuario = new User(inputNome, inputEmail, inputSenha, false);
                
                // Insere no banco de dados
                boolean sucesso = userDAO.inserir(usuario);
                
                if (sucesso) {
                    alerta.setAlertType(AlertType.INFORMATION);
                    alerta.setContentText("Usuário cadastrado com sucesso!");
                    alerta.show();
                    
                    // Redireciona para a tela de login
                    App.setRoot("view/login");
                } else {
                    alerta.setAlertType(AlertType.ERROR);
                    alerta.setContentText("Falha ao cadastrar usuário");
                    alerta.show();
                }
            }
        } catch (SQLException e) {
            alerta.setAlertType(AlertType.ERROR);
            alerta.setContentText("Falha realizando cadastro de usuário");
            alerta.show();
        }
    }

    /**
     * Propriedade que mantém o botão desativado enquanto todos os campos não são
     * preenchidos
     */
    @FXML
    public void keyReleasedProperty() {
        String inputNome = nomeUsuario.getText();
        String inputEmail = email.getText();
        String inputSenha = senha.getText();
        String inputConfirmarSenha = confirmarSenha.getText();
        
        boolean isDisable = inputNome.isEmpty() || inputEmail.isEmpty() || 
                           inputSenha.isEmpty() || inputConfirmarSenha.isEmpty();
        signupButton.setDisable(isDisable);
    }

    @FXML
    public void back() {
        try {
            App.setRoot("view/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
