package com.casaculturaqxd.sgec.DAO;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.User;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private static DatabasePostgres db;
    private static int idValidUsuario = 2, idInvalidUsuario = -1;

    public UserDAOTest() throws SQLException {
        setUpClass();
    }

    @BeforeAll
    public static void setUpClass() throws SQLException {
        db = DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
        db.getConnection().setAutoCommit(false);
    }

    @AfterAll
    public static void tearDownClass() {
        db.desconectar(db.getConnection());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        db.getConnection().rollback();
    }

    @Test
    public void testConnection() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        assertEquals(db.getConnection(), userDAO.getConnection());
    }

    @Test
    public void testInserirValidUsuario() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User("new_test_user", "newtest@mail", "newtestpassword", false);

        assertAll(() -> assertTrue(userDAO.inserir(user)), () -> assertNotEquals(0, user.getIdUsuario()));
    }

    @Test
    public void testInserirUsuarioWithoutNameThrows() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User(null, "newtest@mail", "newtestpassword", false);

        assertThrows(SQLException.class, () -> userDAO.inserir(user));
    }

    @Test
    public void testInserirUsuarioWithoutEmailThrows() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User("new_test_user", null, "newtestpassword", false);

        assertThrows(SQLException.class, () -> userDAO.inserir(user));
    }

    @Test
    public void testInserirUsuarioWithoutPasswordThrows() {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User("new_test_user", "newtest@mail", null, false);

        assertThrows(SQLException.class, () -> userDAO.inserir(user));
    }

    @Test
    public void testGetValidUsuario() throws SQLException {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User(idValidUsuario);

        Optional<User> result = userDAO.getUsuario(user);
        assertAll(() -> assertTrue(result.isPresent()), () -> assertNotEquals(0, result.get().getIdUsuario()),
                () -> assertFalse(result.get().getNomeUsuario().isEmpty()),
                () -> assertFalse(result.get().getNomeUsuario().isEmpty()),
                () -> assertFalse(result.get().getSenha().isEmpty()));
    }

    @Test
    public void testGetInvalidUsuario() throws SQLException {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User(idInvalidUsuario);

        Optional<User> result = userDAO.getUsuario(user);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testValidarInvalidUsuario() throws SQLException {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User("invalid_email", "invalid_password");
        assertFalse(userDAO.validar(user));
    }

    @Test
    public void testUpdateValidUsuario() throws SQLException {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User(idValidUsuario);
        user.setNomeUsuario("updatedname");
        user.setEmail("updated@mail");
        user.setSenha("updatedpassword");
        user.setEditor(true);
        boolean result = userDAO.update(user);
        User updatedUser = userDAO.getUsuario(user).get();
        assertAll(() -> assertTrue(result), () -> assertEquals(user, updatedUser));
    }

    @Test
    public void testUpdateInvalidUsuario() throws SQLException {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User(idInvalidUsuario);
        user.setNomeUsuario("updatedname");
        user.setEmail("updated@mail");
        user.setSenha("updatedpassword");
        user.setEditor(true);
        boolean result = userDAO.update(user);
        assertFalse(result);
    }

    @Test
    public void testDeletarValidUsuario() throws SQLException {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User(idValidUsuario);

        assertTrue(userDAO.deletar(user));
    }

    @Test
    public void testDeletarInvalidUsuario() throws SQLException {
        UserDAO userDAO = new UserDAO(db.getConnection());
        User user = new User(idInvalidUsuario);
        assertFalse(userDAO.deletar(user));
    }
}
