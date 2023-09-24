package com.casaculturaqxd.sgec.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class ConnectionFactory {
    private Connection connection;
    private String urlDataBase;
    private String nomeUsuario;
    private String senha;

    public ConnectionFactory(String urlKey, String userNameKey, String passwordKey) {
        Dotenv dotenv = Dotenv.load();
        
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
}
