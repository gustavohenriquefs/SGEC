package com.casaculturaqxd.sgec.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestDatabasePostgres {
    DatabasePostgres database;
    String INVALID_ENV = "";

    public TestDatabasePostgres() {
        database = DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
        INVALID_ENV = "";
    }

    @BeforeEach
    public void setUp() throws IncorrectEnvironmentVariableException {
        database = DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
    }

    @AfterAll
    public void tearDownClass() throws SQLException {
        database.getConnection().commit();
        database.desconectar(database.getConnection());
    }

    @Test
    public void testUniqueInstance() {
        Connection firstConnection = database.getConnection();
        Connection secondConnection = database.getConnection();
        assertEquals(firstConnection, secondConnection);
    }

    @Test
    public void testDesconectarOnValidConnection() {
        database.getConnection();
        assertDoesNotThrow(() -> {
            database.desconectar(database.getConnection());
        });
    }

    @Test
    public void testSetInvalidUrlDataBase() throws IncorrectEnvironmentVariableException {
        assertThrows(IncorrectEnvironmentVariableException.class, () -> {
            database.setUrlDataBase(INVALID_ENV);
        });
    }

    @Test
    public void testSetInvalidNomeUsuario() {
        assertThrows(IncorrectEnvironmentVariableException.class, () -> {
            database.setNomeUsuario(INVALID_ENV);
        });
    }

    @Test
    public void testSetInvalidSenha() {
        assertThrows(IncorrectEnvironmentVariableException.class, () -> {
            database.setSenha(INVALID_ENV);
        });
    }

}
