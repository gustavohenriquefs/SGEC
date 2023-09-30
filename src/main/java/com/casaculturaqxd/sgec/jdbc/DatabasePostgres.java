package com.casaculturaqxd.sgec.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Implementacao de Database utilizando o driver jdbc
 * do Postgres, todos os parâmetros são carregados a partir
 * de variáveis de ambiente definidas em um arquivo .env.
 * 
 * <p>
 * Todos os métodos set desconectam da conexão atual
 * antes de tentar atualizar uma das credenciais.
 * </p>
 */
public class DatabasePostgres implements Database{
    private static DatabasePostgres instance;
    private Connection connection;
    private String urlDataBase;
    private String nomeUsuario;
    private String senha;
    private Dotenv dotenv = Dotenv.load();

    public static DatabasePostgres getInstance(String urlKey, String userNameKey, String passwordKey){
        if(instance==null){
            instance = new DatabasePostgres(urlKey, userNameKey, passwordKey);
        }
        return instance;
    }

    private DatabasePostgres(String urlKey, String userNameKey, String passwordKey) {
        this.urlDataBase = dotenv.get(urlKey);
        this.nomeUsuario = dotenv.get(userNameKey);
        this.senha       = dotenv.get(passwordKey);
    }

    public Connection conectar() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(urlDataBase, nomeUsuario,senha);
            return this.connection;
        } catch (SQLException | ClassNotFoundException erro) {
            throw new RuntimeException(erro);
        }
    }
    
    public void desconectar(Connection connection) {
        try {
            connection.close();
        } catch (SQLException erro) {
            throw new RuntimeException(erro);
        }
    }

    public void setUrlDataBase(String urlKey) {
        if(variavelEnvExists(urlKey)){
            desconectar(connection);            
            this.urlDataBase = dotenv.get(urlKey);
        }
        else{
            throw new RuntimeException("variavel de ambiente inexistente");
        }
    }

    public void setNomeUsuario(String userNameKey) {
        if(variavelEnvExists(userNameKey)){
            desconectar(connection);            
            this.nomeUsuario = dotenv.get(userNameKey);
            
        }
        else{
            throw new RuntimeException("variavel de ambiente inexistente");
        }
    }

    public void setSenha(String passwordKey) {
        if(variavelEnvExists(passwordKey)){
            desconectar(connection);            
            this.senha = dotenv.get(passwordKey);
        }
        else{
            throw new RuntimeException("variavel de ambiente inexistente");
        }
    }

    private boolean variavelEnvExists(String envKey){
        if(dotenv.get(envKey) == null){
            return false;
        }
        else{
            return true;
        }
    }

}
