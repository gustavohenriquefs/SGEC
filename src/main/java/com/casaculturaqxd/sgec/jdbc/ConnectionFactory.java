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

    public ConnectionFactory(){
        this.obterDadosConexao();
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
    
    public void obterDadosConexao() {
        Dotenv dotenv = Dotenv.load();
        
        this.urlDataBase = dotenv.get("URL");
        this.nomeUsuario = dotenv.get("USER_NAME");
        this.senha = dotenv.get("PASSWORD");
        
        System.out.println("AQUUUI: " + this.urlDataBase + " " + this.nomeUsuario + " " + this.senha);
    }
    
    public void desconectar(Connection connection) {
        try {
            connection.close();
        } catch (SQLException erro) {
            throw new RuntimeException(erro);
        }
    }
}
