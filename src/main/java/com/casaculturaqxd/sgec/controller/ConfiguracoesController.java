package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.UserDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ConfiguracoesController {
    @FXML
    private Pane root;

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
    private final DatabasePostgres userConnection = DatabasePostgres.getInstance("URL", "USER_NAME",
            "PASSWORD");
    private UserDAO userDAO = new UserDAO(userConnection.getConnection());

    private Alert alert;

    public void initialize() {
        try {
            loadMenu();
            usuario = App.getUsuario();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMenu() throws IOException {
        FXMLLoader carregarMenu =
                new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }

    //Permite alterar email e senha do usuário logado
    public void alterarEmailESenha() throws SQLException{
        userConnection.getConnection().setAutoCommit(false);
        try {
            if (!(nome.getText().isEmpty()) || !(email.getText().isEmpty())) {
                // Atualizar apenas os campos fornecidos, mantendo a senha original
                if (!nome.getText().isEmpty()) {
                    usuario.setNomeUsuario(nome.getText());
                }
                if (!email.getText().isEmpty()) {
                    usuario.setEmail(email.getText());
                }

                // Realizar a atualização no banco de dados
                User novoUsuario = new User(usuario.getIdUsuario(),usuario.getNomeUsuario(), usuario.getEmail(), usuario.getSenha(), usuario.isEditor());
                userDAO.update(novoUsuario);
        
                // Exibir a mensagem de confirmação
                alert = new Alert(AlertType.CONFIRMATION, "Alterações salvas com sucesso");
                alert.showAndWait();
            } else {
                // Caso nenhum campo seja fornecido, exibir uma mensagem adequada
                alert = new Alert(AlertType.WARNING, "Nenhum dado fornecido para atualização");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Em caso de exceção, realizar rollback
            userConnection.getConnection().rollback();
            e.printStackTrace();  // Você pode querer tratar essa exceção de uma maneira mais apropriada
        } finally {
            // Certificar-se de redefinir o autocommit
            userConnection.getConnection().setAutoCommit(true);
        }        
    }


    //Altera a senha checando se a nova senha é válida e a antiga é igual a senha do usuário
    public void alterarSenha() throws SQLException, NoSuchAlgorithmException{
        userConnection.getConnection().setAutoCommit(false);
        try {
            if (senhaAntiga.getText().equals(usuario.getSenha())) {
                if(novaSenha.getText().equals(confirmarSenha.getText())){
                    usuario.setSenha(novaSenha.getText());
                    // Realizar a atualização no banco de dados
                    User novoUsuario = new User(usuario.getIdUsuario(),usuario.getNomeUsuario(), usuario.getEmail(), novaSenha.getText(), usuario.isEditor());
                    userDAO.update(novoUsuario);
                    alert = new Alert(AlertType.CONFIRMATION, "Alterações salvas com sucesso");
                    alert.showAndWait();
                }else{
                    alert = new Alert(AlertType.ERROR, "Senhas do campo de nova senha e de confirmação diferentes");
                    alert.showAndWait();
                }
            } else {
                alert = new Alert(AlertType.WARNING, "Senha antiga incorreta");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Em caso de exceção, realizar rollback
            userConnection.getConnection().rollback();
            e.printStackTrace();
        } finally {
            // Certificar-se de redefinir o autocommit
            userConnection.getConnection().setAutoCommit(true);
        }        
    }
}
