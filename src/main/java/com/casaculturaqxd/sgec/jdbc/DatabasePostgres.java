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
public class DatabasePostgres implements Database {
    private static DatabasePostgres instance;
    private Connection connection;
    private String urlDataBase;
    private String nomeUsuario;
    private String senha;
    private Dotenv dotenv = Dotenv.load();

    public static DatabasePostgres getInstance(String urlKey, String userNameKey, String passwordKey) {
        if (instance == null) {
            instance = new DatabasePostgres(urlKey, userNameKey, passwordKey);
        }
        return instance;
    }

    private DatabasePostgres(String urlKey, String userNameKey, String passwordKey) {
        this.urlDataBase = dotenv.get(urlKey);
        this.nomeUsuario = dotenv.get(userNameKey);
        this.senha = dotenv.get(passwordKey);

        try {
            this.connection = DriverManager.getConnection(urlDataBase, nomeUsuario, senha);
        } catch (SQLException erro) {
            throw new RuntimeException(erro);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void desconectar(Connection connection) {
        try {
            connection.close();
        } catch (SQLException erro) {
            throw new RuntimeException(erro);
        }
    }

    public void setUrlDataBase(String urlKey) throws IncorrectEnvironmentVariableException{
        if (variavelEnvExists(urlKey)) {
            System.out.println("Sucesso");
            desconectar(connection);
            this.urlDataBase = dotenv.get(urlKey);
        } else {
            throw new IncorrectEnvironmentVariableException();
        }
    }

    public void setNomeUsuario(String userNameKey) throws IncorrectEnvironmentVariableException {
        if (variavelEnvExists(userNameKey)) {
            desconectar(connection);
            this.nomeUsuario = dotenv.get(userNameKey);

        } else {
            throw new IncorrectEnvironmentVariableException();
        }
    }

    public void setSenha(String passwordKey) throws IncorrectEnvironmentVariableException {
        if (variavelEnvExists(passwordKey)) {
            desconectar(connection);
            this.senha = dotenv.get(passwordKey);
        } else {
            throw new IncorrectEnvironmentVariableException();
        }
    }

    private boolean variavelEnvExists(String envKey) {
        return dotenv.get(envKey) != null;
    }

}
