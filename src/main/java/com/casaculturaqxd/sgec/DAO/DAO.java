package com.casaculturaqxd.sgec.DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.casaculturaqxd.sgec.service.ServiceOperationException;

public abstract class DAO {
    private Connection connection;

    public DAO() {

    }

    public DAO(Connection connection) {
        this.connection = connection;
    }

    protected void logException(Exception exception) {
        if (exception instanceof SQLException) {
            Logger erro = Logger.getLogger("erroSQL");
            erro.log(Level.SEVERE, "excecao levantada:", exception);
        } else if (exception instanceof ServiceOperationException) {
            Logger erro = Logger.getLogger("erro service");
            erro.log(Level.SEVERE, "excecao levantada:", exception);
        } else if (exception instanceof IOException) {
            Logger erro = Logger.getLogger("erro arquivo");
            erro.log(Level.SEVERE, "excecao levantada:", exception);
        }
    }
}
