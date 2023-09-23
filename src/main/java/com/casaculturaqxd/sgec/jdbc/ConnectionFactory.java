package com.casaculturaqxd.sgec.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {
    private Connection connection;
    private String urlDataBase;
    private String nomeUsuario;
    private String senha;

    public ConnectionFactory(String urlDataBase, String nomeUsuario, String senha){
        this.urlDataBase = urlDataBase;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
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
