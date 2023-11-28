package com.casaculturaqxd.sgec.DAO;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Localizacao;

public class LocalizacaoDAOTest {
    private static DatabasePostgres db;
    private static int idValidLocal = 1, idUpdatableLocal = 2, idValidEvento = 1, idInvalidLocal = -1,
            idInvalidEvento = -1;

    public LocalizacaoDAOTest() throws SQLException {
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
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        assertEquals(db.getConnection(), localizacaoDAO.getConnection());
    }

    @Test
    public void testInserirValidLocalizacao() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("local_teste", "new_rua_teste", "new_cidade_teste", "new_estado_teste",
                "new_pais_teste");

        assertAll(() -> assertTrue(localizacaoDAO.inserirLocalizacao(local)),
                () -> assertNotEquals(0, local.getIdLocalizacao()));
    }

    @Test
    public void testInserirLocalizacaoSemNome() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(null, "rua_teste", "new_cidade_teste", "new_estado_teste",
                "new_pais_teste");
        assertThrows(SQLException.class, () -> localizacaoDAO.inserirLocalizacao(local));
    }

    @Test
    public void testInserirLocalizacaoSemRua() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("local_teste", null, "new_cidade_teste", "new_estado_teste",
                "new_pais_teste");
        assertThrows(SQLException.class, () -> localizacaoDAO.inserirLocalizacao(local));
    }

    @Test
    public void testInserirLocalizacaoSemCidade() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("local_teste", "new_rua_teste", null, "new_estado_teste", "new_pais_teste");
        assertThrows(SQLException.class, () -> localizacaoDAO.inserirLocalizacao(local));
    }

    @Test
    public void testInserirLocalizacaoSemEstado() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("local_teste", "new_rua_teste", "new_cidade_teste", null, "new_pais_teste");

        assertThrows(SQLException.class, () -> localizacaoDAO.inserirLocalizacao(local));
    }

    @Test
    public void testInserirLocalizacaoSemPais() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("local_teste", "new_rua_teste", "new_cidade_este", "new_estado_teste",
                null);

        assertThrows(SQLException.class, () -> localizacaoDAO.inserirLocalizacao(local));
    }

    @Test
    public void testGetValidLocalizacao() throws SQLException {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(idValidLocal);

        Localizacao result = localizacaoDAO.getLocalizacao(local).get();
        assertAll(() -> assertEquals(1, result.getIdLocalizacao()), () -> assertEquals("local_teste", result.getNome()),
                () -> assertEquals("rua_teste", result.getRua()), () -> assertEquals(0, result.getNumeroRua()),
                () -> assertNull(result.getBairro()), () -> assertEquals("cidade_teste", result.getCidade()),
                () -> assertNull(result.getCep()), () -> assertEquals("pais_teste", result.getPais()));
    }

    @Test
    public void testGetInvalidLocalizacao() throws SQLException {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(idInvalidLocal);

        Optional<Localizacao> result = localizacaoDAO.getLocalizacao(local);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testVincularValidLocalValidEvento() throws SQLException {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(idValidLocal);

        assertTrue(localizacaoDAO.vincularEvento(local.getIdLocalizacao(), idValidEvento));
    }

    @Test
    public void testVincularValidLocalInvalidEvento() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(idValidLocal);

        assertThrows(SQLException.class,
                () -> localizacaoDAO.vincularEvento(local.getIdLocalizacao(), idInvalidEvento));
    }

    @Test
    public void testVincularInvalidLocalValidEvento() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(idInvalidLocal);

        assertThrows(SQLException.class, () -> localizacaoDAO.vincularEvento(local.getIdLocalizacao(), idValidEvento));
    }

    @Test
    public void testVincularInvalidLocalInvalidEvento() {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(idInvalidLocal);

        assertThrows(SQLException.class,
                () -> localizacaoDAO.vincularEvento(local.getIdLocalizacao(), idInvalidEvento));
    }

    @Test
    public void testDesvincularValidLocalizacaoValidEvento() throws SQLException {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(idValidLocal);

        localizacaoDAO.vincularEvento(local.getIdLocalizacao(), idValidEvento);

        assertTrue(localizacaoDAO.desvincularEvento(local.getIdLocalizacao(), idValidEvento));
    }

    @Test
    public void testDesvincularUnexistantLocalizacaoEvento() throws SQLException {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(idValidLocal);

        assertFalse(localizacaoDAO.desvincularEvento(local.getIdLocalizacao(), idValidEvento));
    }

    @Test
    public void testUpdateValidLocalizacao() throws SQLException {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(idUpdatableLocal);
        String updateString = "update_value", updateCep = "00000-000";
        local.setNome(updateString);
        local.setRua(updateString);
        local.setNumeroRua(idUpdatableLocal);
        local.setBairro(updateString);
        local.setCidade(updateString);
        local.setCep(updateCep);
        local.setEstado(updateString);
        local.setPais(updateString);

        assertTrue(localizacaoDAO.updateLocalizacao(local));

        Localizacao result = localizacaoDAO.getLocalizacao(local).get();
        assertAll(() -> assertEquals(updateString, result.getNome()), () -> assertEquals(updateString, result.getRua()),
                () -> assertEquals(idUpdatableLocal, result.getNumeroRua()),
                () -> assertEquals(updateString, result.getBairro()),
                () -> assertEquals(updateString, result.getCidade()), () -> assertEquals(updateCep, result.getCep()),
                () -> assertEquals(updateString, result.getEstado()),
                () -> assertEquals(updateString, result.getPais()));
    }

    @Test
    public void testUpdateInvalidLocalizacao() throws SQLException {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(idInvalidLocal);

        assertFalse(localizacaoDAO.updateLocalizacao(local));
    }

    @Test
    public void testDeletarValidLocalizacao() throws SQLException {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao("remotion_teste_local", "remotion_teste_rua", "remotion_teste_cidade",
                "remotion_teste_estado", "remotion_teste_pais");
        localizacaoDAO.inserirLocalizacao(local);

        assertTrue(localizacaoDAO.deletarLocalizacao(local));
    }

    @Test
    public void testDeletarInvalidLocalizacao() throws SQLException {
        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(db.getConnection());
        Localizacao local = new Localizacao(idInvalidLocal);

        assertFalse(localizacaoDAO.deletarLocalizacao(local));
    }
}
