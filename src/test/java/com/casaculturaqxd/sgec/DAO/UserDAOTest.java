package com.casaculturaqxd.sgec.DAO;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.User;
import java.sql.Connection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class UserDAOTest {
    private static DatabasePostgres db =
            DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
    private static UserDAO userDAO;
    private static User obj;

    public UserDAOTest() {
        setUpClass();
    }

    @BeforeAll
    public static void setUpClass() {
        userDAO = new UserDAO();
        userDAO.setConnection(db.getConnection());
    }

    @AfterAll
    public static void tearDownClass() {
        db.desconectar(db.getConnection());
    }

    @AfterEach
    public void tearDown() {
        // remover do banco o obj usado no teste
        // mantendo o registro conhecido
        if (obj.getIdUsuario() != 1) {
            userDAO.deletar(obj);
        }
    }

    @Test
    public void testGetConnection() {
        Connection connection = userDAO.getConnection();
        assertNotNull(connection);
    }


    @Test
    public void testSetConnection() {
        assertEquals(db.getConnection(), userDAO.getConnection());
    }

    @Test
    public void testInserir() {
        obj = new User("new_test_user", "newtest@mail", "newtestpassword", false);

        assertAll(() -> assertTrue(userDAO.inserir(obj)),
                () -> assertNotEquals(0, obj.getIdUsuario()));
    }

    @Test
    public void testGetValidUsuario() {
        // registro conhecido
        User validUser = new User();
        validUser.setIdUsuario(1);

        User result = userDAO.getUsuario(validUser);
        assertAll(() -> assertEquals(1, result.getIdUsuario()),
                () -> assertEquals("test user", result.getNomeUsuario()),
                () -> assertEquals("test@mail", result.getEmail()),
                () -> assertEquals("testpassword", result.getSenha()),
                () -> assertEquals(true, result.isEditor()));
    }

    @Test
    public void testGetInvalidUsuario() {
        // usando id invalido
        obj = new User();
        obj.setIdUsuario(Integer.MIN_VALUE);

        User result = userDAO.getUsuario(obj);
        assertNull(result);
    }

    @Test
    public void testValidarValidUsuario() {
        obj = new User("test@mail", "testpassword");

        assertAll(() -> assertTrue(userDAO.validar(obj)), () -> assertEquals(1, obj.getIdUsuario()),
                () -> assertEquals("test user", obj.getNomeUsuario()),
                () -> assertEquals("test@mail", obj.getEmail()),
                () -> assertEquals("testpassword", obj.getSenha()),
                () -> assertTrue(obj.isEditor()));
    }

    @Test
    public void testValidarInvalidUsuario() {
        obj = new User("invalidEmail", "invalidPassword");

        // verifica se a validacao falhou e se os outros atributos nao foram setados
        assertAll(() -> assertFalse(userDAO.validar(obj)),
                () -> assertEquals(0, obj.getIdUsuario()), () -> assertNull(obj.getNomeUsuario()),
                () -> assertFalse(obj.isEditor()));
    }

    @Test
    public void testUpdateValidUsuario() {
        obj = new User();
        obj.setIdUsuario(1);
        obj = userDAO.getUsuario(obj);

        assertTrue(userDAO.update(obj));
    }

    @Test
    public void testUpdateInvalidUsuario() {
        obj = new User();
        obj.setIdUsuario(Integer.MIN_VALUE);

        assertFalse(userDAO.update(obj));
    }

    @Test
    public void testDeletarValidUsuario() {
        obj = new User("new_test_user", "newtest@mail", "newtestpassword", false);
        userDAO.inserir(obj);
        assertTrue(userDAO.deletar(obj));
    }

    @Test
    public void testDeletarInvalidUsuario() {
        obj = new User();
        obj.setIdUsuario(Integer.MIN_VALUE);
        assertFalse(userDAO.deletar(obj));
    }
}
