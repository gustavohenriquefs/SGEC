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
    private Connection connection;
    private String urlDataBase;
    private String nomeUsuario;
    private String senha;
    private Dotenv dotenv = Dotenv.load();

    public DatabasePostgres(String urlKey, String userNameKey, String passwordKey) {
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
        if(dotenv.get(urlKey) == null){
            throw new RuntimeException("variavel de ambiente inexistente");
        }
        else{
        desconectar(connection);            
        this.urlDataBase = dotenv.get(urlKey);
        }
    }

    public void setNomeUsuario(String userNameKey) {
        if(dotenv.get(userNameKey) == null){
            throw new RuntimeException("variavel de ambiente inexistente");
        }
        else{
        desconectar(connection);            
        this.nomeUsuario = dotenv.get(userNameKey);
        }
    }

    public void setSenha(String passwordKey) {
        if(dotenv.get(passwordKey) == null){
            throw new RuntimeException("variavel de ambiente inexistente");
        }
        else{
        desconectar(connection);            
        this.senha = dotenv.get(passwordKey);
        }
    }
}
