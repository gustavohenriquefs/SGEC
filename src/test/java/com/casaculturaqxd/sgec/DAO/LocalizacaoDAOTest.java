package com.casaculturaqxd.sgec.DAO;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Localizacao;

public class LocalizacaoDAOTest {
    private static DatabasePostgres db;
    private static int validIdLocal = 1, updatableIdLocal = 2, validIdEvento = 1,
            invalidIdLocal = -1, invalidIdEvento = -1;

    public LocalizacaoDAOTest() {
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
    public static void tearDownClass() {
        db.desconectar(db.getConnection());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        db.getConnection().rollback();
        db.getConnection().commit();
    }

    @Test
    public void testGetConnection() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        assertNotNull(localizacaoDAO.getConnection());
    }

    @Test
    public void testSetConnection() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        assertEquals(db.getConnection(), localizacaoDAO.getConnection());
    }

    @Test
    public void testInserirValidLocalizacao() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("local_teste", "new_rua_teste", "new_cidade_teste",
                "new_estado_teste", "new_pais_teste");

        assertAll(() -> assertTrue(localizacaoDAO.inserirLocalizacao(local)),
                () -> assertNotEquals(0, local.getIdLocalizacao()));
    }

    @Test
    public void testInserirLocalizacaoSemNome() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(null, "rua_teste", "new_cidade_teste", "new_estado_teste",
                "new_pais_teste");
        assertAll(() -> assertFalse(localizacaoDAO.inserirLocalizacao(local)),
                () -> assertEquals(0, local.getIdLocalizacao()));
    }

    @Test
    public void testInserirLocalizacaoSemRua() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("local_teste", null, "new_cidade_teste", "new_estado_teste",
                "new_pais_teste");
        assertAll(() -> assertFalse(localizacaoDAO.inserirLocalizacao(local)),
                () -> assertEquals(0, local.getIdLocalizacao()));
    }

    @Test
    public void testInserirLocalizacaoSemCidade() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("local_teste", "new_rua_teste", null, "new_estado_teste",
                "new_pais_teste");
        assertAll(() -> assertFalse(localizacaoDAO.inserirLocalizacao(local)),
                () -> assertEquals(0, local.getIdLocalizacao()));
    }

    @Test
    public void testInserirLocalizacaoSemEstado() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("local_teste", "new_rua_teste", "new_cidade_teste", null,
                "new_pais_teste");

        assertAll(() -> assertFalse(localizacaoDAO.inserirLocalizacao(local)),
                () -> assertEquals(0, local.getIdLocalizacao()));
    }

    @Test
    public void testInserirLocalizacaoSemPais() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("local_teste", "new_rua_teste", "new_cidade_este",
                "new_estado_teste", null);

        assertAll(() -> assertFalse(localizacaoDAO.inserirLocalizacao(local)),
                () -> assertEquals(0, local.getIdLocalizacao()));
    }

    @Test
    public void testGetValidLocalizacao() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        // id de local conhecido do banco
        local.setIdLocalizacao(validIdLocal);

        Localizacao result = localizacaoDAO.getLocalizacao(local);
        assertAll(() -> assertEquals(1, result.getIdLocalizacao()),
                () -> assertEquals("local_teste", result.getNome()),
                () -> assertEquals("rua_teste", result.getRua()),
                () -> assertEquals(0, result.getNumeroRua()), () -> assertNull(result.getBairro()),
                () -> assertEquals("cidade_teste", result.getCidade()),
                () -> assertNull(result.getCep()),
                () -> assertEquals("pais_teste", result.getPais()));
    }

    @Test
    public void testGetInvalidLocalizacao() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        // utilizando
        local.setIdLocalizacao(invalidIdLocal);

        Localizacao result = localizacaoDAO.getLocalizacao(local);
        assertNull(result);
    }

    @Test
    public void testVincularValidLocalValidEvento() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        local.setIdLocalizacao(validIdLocal);

        assertTrue(localizacaoDAO.vincularEvento(local.getIdLocalizacao(), validIdEvento));
    }

    @Test
    public void testVincularValidLocalInvalidEvento() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        local.setIdLocalizacao(validIdLocal);

        assertFalse(localizacaoDAO.vincularEvento(local.getIdLocalizacao(), invalidIdEvento));
    }

    @Test
    public void testVincularInvalidLocalValidEvento() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        local.setIdLocalizacao(invalidIdLocal);

        assertFalse(localizacaoDAO.vincularEvento(local.getIdLocalizacao(), validIdEvento));
    }

    @Test
    public void testVincularInvalidLocalInvalidEvento() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        local.setIdLocalizacao(invalidIdLocal);

        assertFalse(localizacaoDAO.vincularEvento(local.getIdLocalizacao(), invalidIdEvento));
    }

    @Test
    public void testDesvincularValidLocalizacaoValidEvento() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        local.setIdLocalizacao(validIdLocal);

        localizacaoDAO.vincularEvento(local.getIdLocalizacao(), validIdEvento);

        assertTrue(localizacaoDAO.desvincularEvento(local.getIdLocalizacao(), validIdEvento));
    }

    @Test
    public void testDesvincularValidLocalizacaoInvalidEvento() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        local.setIdLocalizacao(validIdLocal);

        localizacaoDAO.vincularEvento(local.getIdLocalizacao(), invalidIdEvento);

        assertFalse(localizacaoDAO.desvincularEvento(local.getIdLocalizacao(), invalidIdEvento));
    }

    @Test
    public void testDesvincularInvalidLocalizacaoValidEvento() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        local.setIdLocalizacao(invalidIdLocal);

        localizacaoDAO.vincularEvento(local.getIdLocalizacao(), validIdEvento);

        assertFalse(localizacaoDAO.desvincularEvento(local.getIdLocalizacao(), validIdEvento));
    }

    @Test
    public void testDesvincularInvalidLocalizacaoInvalidEvento() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        local.setIdLocalizacao(invalidIdLocal);

        localizacaoDAO.vincularEvento(local.getIdLocalizacao(), invalidIdEvento);

        assertFalse(localizacaoDAO.desvincularEvento(local.getIdLocalizacao(), invalidIdEvento));
    }

    @Test
    public void testUpdateValidLocalizacao() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        local.setIdLocalizacao(updatableIdLocal);
        String updateString = "update_value", updateCep = "00000-000";
        local.setNome(updateString);
        local.setRua(updateString);
        local.setNumeroRua(updatableIdLocal);
        local.setBairro(updateString);
        local.setCidade(updateString);
        local.setCep(updateCep);
        local.setEstado(updateString);
        local.setPais(updateString);

        assertTrue(localizacaoDAO.updateLocalizacao(local));

        Localizacao result = localizacaoDAO.getLocalizacao(local);
        assertAll(() -> assertEquals(updateString, result.getNome()),
                () -> assertEquals(updateString, result.getRua()),
                () -> assertEquals(updatableIdLocal, result.getNumeroRua()),
                () -> assertEquals(updateString, result.getBairro()),
                () -> assertEquals(updateString, result.getCidade()),
                () -> assertEquals(updateCep, result.getCep()),
                () -> assertEquals(updateString, result.getEstado()),
                () -> assertEquals(updateString, result.getPais()));
    }

    @Test
    public void testUpdateInvalidLocalizacao() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        local.setIdLocalizacao(invalidIdLocal);

        assertFalse(localizacaoDAO.updateLocalizacao(local));
    }

    @Test
    public void testDeletarValidLocalizacao() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("remotion_teste_local", "remotion_teste_rua",
                "remotion_teste_cidade", "remotion_teste_estado", "remotion_teste_pais");
        localizacaoDAO.inserirLocalizacao(local);

        assertTrue(localizacaoDAO.deletarLocalizacao(local));
    }

    @Test
    public void testDeletarInvalidLocalizacao() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao();
        local.setIdLocalizacao(invalidIdLocal);

        assertFalse(localizacaoDAO.deletarLocalizacao(local));
    }
}
