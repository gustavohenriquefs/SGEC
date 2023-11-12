package com.casaculturaqxd.sgec.DAO;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.User;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private static DatabasePostgres db;
    private static int idValidUsuario = 1, idInvalidUsuario = -1;

    public UserDAOTest() {
        setUpClass();
    }

    @BeforeAll
    public static void setUpClass() {
        db = DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
        try {
            db.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDownClass() throws SQLException {
        db.desconectar(db.getConnection());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        db.getConnection().rollback();
        db.getConnection().commit();
    }

    @Test
    public void testGetConnection() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        Connection connection = userDAO.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testSetConnection() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        assertEquals(db.getConnection(), userDAO.getConnection());
    }

    @Test
    public void testInserir() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User("new_test_user", "newtest@mail", "newtestpassword", false);

        assertAll(() -> assertTrue(userDAO.inserir(user)),
                () -> assertNotEquals(0, user.getIdUsuario()));
    }

    @Test
    public void testGetValidUsuario() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User validUser = new User(idValidUsuario);

        User result = userDAO.getUsuario(validUser);
        assertAll(() -> assertEquals(idValidUsuario, result.getIdUsuario()),
                () -> assertEquals("test user", result.getNomeUsuario()),
                () -> assertEquals("test@mail", result.getEmail()),
                () -> assertEquals("testpassword", result.getSenha()),
                () -> assertEquals(true, result.isEditor()));
    }

    @Test
    public void testGetInvalidUsuario() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User(idInvalidUsuario);
        User result = userDAO.getUsuario(user);
        assertNull(result);
    }

    @Test
    public void testValidarValidUsuario() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User("test@mail", "testpassword");

        assertAll(() -> assertTrue(userDAO.validar(user)),
                () -> assertEquals(1, user.getIdUsuario()),
                () -> assertEquals("test user", user.getNomeUsuario()),
                () -> assertEquals("test@mail", user.getEmail()),
                () -> assertEquals("testpassword", user.getSenha()),
                () -> assertTrue(user.isEditor()));
    }

    @Test
    public void testValidarInvalidUsuario() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User("invalidEmail", "invalidPassword");

        // verifica se a validacao falhou e se os outros atributos nao foram setados
        assertAll(() -> assertFalse(userDAO.validar(user)),
                () -> assertEquals(0, user.getIdUsuario()),
                () -> assertNull(user.getNomeUsuario()),
                () -> assertFalse(user.isEditor()));
    }

    @Test
    public void testUpdateValidUsuario() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User(idValidUsuario);
        user = userDAO.getUsuario(user);

        assertTrue(userDAO.update(user));
    }

    @Test
    public void testUpdateInvalidUsuario() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User previous = new User(idInvalidUsuario);

        assertFalse(userDAO.update(previous));
    }

    @Test
    public void testDeletarValidUsuario() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User("new_test_user", "newtest@mail", "newtestpassword", false);
        userDAO.inserir(user);
        assertTrue(userDAO.deletar(user));
    }

    @Test
    public void testDeletarInvalidUsuario() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User(idInvalidUsuario);

        assertFalse(userDAO.deletar(user));
    }
}
