package com.casaculturaqxd.sgec;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class LoginController {
    @FXML
    private TextField email;
    @FXML
    private PasswordField senha;
    @FXML 
    private Button loginButton;

    //private final ConnectionFactory userConnection = new ConnectionFactory();


    /**
     * Carrega a página com o botão de login desabilitado
     */
    public void initialize(){
        loginButton.setDisable(true);
    }

    /**
     * Cria um DAO que verifica no banco de dados se as credenciais 
     * estão corretas, depois envia o nome de usuario e senha para a proxima
     * página
     * @throws IOException
     */
    public void authUsuario() throws IOException{
        //UserDAO dao = new UserDAO();
        //try{
            //dao.verificarUsuario()
            //App.setRoot("view/home");
        //catch (usuarioIncorreto){
            
        //}
        //catch (senhaiIncorreta){
            
        //}

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
