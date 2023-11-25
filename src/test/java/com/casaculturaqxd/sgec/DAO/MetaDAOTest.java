package com.casaculturaqxd.sgec.DAO;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.casaculturaqxd.sgec.jdbc.Database;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Meta;

public class MetaDAOTest {
    private static Database database;
    private static String nameValidMeta = "Fruição";
    private static int idValidMeta = 1, idInvalidMeta = -1, idValidEvento = 1, idInvalidEvento = -1,
            idValidGrupoEventos = 1, idInvalidGrupoEventos = -1;

    @BeforeAll
    public static void setUpClass() throws SQLException {
        database = DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
        database.getConnection().setAutoCommit(false);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        database.getConnection().rollback();
    }

    @Test
    void testGetValidMetaById() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);

        Optional<Meta> result = metaDAO.getMeta(meta);
        assertAll(() -> assertTrue(result.isPresent()), () -> assertNotNull(result.get().getIdMeta()),
                () -> assertFalse(result.get().getNomeMeta().isEmpty()));
    }

    @Test
    void testGetValidMetaByIdDoesNotThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);

        assertDoesNotThrow(() -> metaDAO.getMeta(meta));
    }

    @Test
    void testGetInvalidMetaById() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idInvalidMeta);

        Optional<Meta> result = metaDAO.getMeta(meta);
        assertAll(() -> assertFalse(result.isPresent()));
    }

    @Test
    void testGetMetaWithoutIdThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(nameValidMeta);

        assertThrows(SQLException.class, () -> metaDAO.getMeta(meta));
    }

    @Test
    void testGetValidMetaByName() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());

        Optional<Meta> result = metaDAO.getMeta(nameValidMeta);

        assertAll(() -> assertTrue(result.isPresent()), () -> assertNotNull(result.get().getIdMeta()),
                () -> assertFalse(result.get().getNomeMeta().isEmpty()));
    }

    @Test
    void testGetValidMetaByNameDoesNotThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());

        assertDoesNotThrow(() -> metaDAO.getMeta(nameValidMeta));
    }

    @Test
    void testInserirNewMeta() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta("new_name");

        assertTrue(metaDAO.inserirMeta(meta));
    }

    @Test
    void testInserirNewMetaDoesNotThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta("new_name");

        assertDoesNotThrow(() -> metaDAO.inserirMeta(meta));
    }

    @Test
    void testInserirMetaWithExistingNameThrows() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(nameValidMeta);

        assertThrows(SQLException.class, () -> metaDAO.inserirMeta(meta));
    }

    @Test
    void testInserirMetaWithoutNameThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);

        assertThrows(SQLException.class, () -> metaDAO.inserirMeta(meta));
    }

    @Test
    void testUpdateValidMeta() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        meta.setNomeMeta("new_nome");

        assertAll(() -> assertTrue(metaDAO.updateMeta(meta)),
                () -> assertEquals(meta.getNomeMeta(), metaDAO.getMeta(meta).get().getNomeMeta()));
    }

    @Test
    void testUpdateValidMetaDoesNotThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        meta.setNomeMeta("new_nome");

        assertDoesNotThrow(() -> metaDAO.updateMeta(meta));
    }

    @Test
    void testUpdateInvalidMeta() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idInvalidMeta);
        meta.setNomeMeta("new_nome");

        assertFalse(metaDAO.updateMeta(meta));
    }

    @Test
    void testUpdateMetaWithoutIdThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta("new_nome");

        assertThrows(SQLException.class, () -> metaDAO.updateMeta(meta));
    }

    @Test
    void testDeleteValidMeta() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);

        boolean result = metaDAO.deleteMeta(meta);
        assertAll(() -> assertTrue(result), () -> assertEquals(Optional.empty(), metaDAO.getMeta(meta)));
    }

    @Test
    void testDeleteValidMetaDoesNotThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);

        assertDoesNotThrow(() -> metaDAO.deleteMeta(meta));
    }

    @Test
    void testDeleteInvalidMeta() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idInvalidMeta);

        boolean result = metaDAO.deleteMeta(meta);
        assertFalse(result);
    }

    @Test
    void testDeleteMetaWithoutIdThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(nameValidMeta);

        assertThrows(SQLException.class, () -> metaDAO.deleteMeta(meta));
    }

    @Test
    void testVincularValidMetaValidEvento() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        Evento evento = new Evento();
        evento.setIdEvento(idValidEvento);

        assertTrue(metaDAO.vincularEvento(meta.getIdMeta(), evento.getIdEvento()));
    }

    @Test
    void testInvalidMetaValidEventoThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idInvalidMeta);
        Evento evento = new Evento();
        evento.setIdEvento(idValidEvento);

        assertThrows(SQLException.class, () -> metaDAO.vincularEvento(meta.getIdMeta(), evento.getIdEvento()));
    }

    @Test
    void testValidMetaInvalidEventoThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        Evento evento = new Evento();
        evento.setIdEvento(idInvalidEvento);

        assertThrows(SQLException.class, () -> metaDAO.vincularEvento(meta.getIdMeta(), evento.getIdEvento()));
    }

    @Test
    void testInvalidMetaInvalidEventoThrows() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idInvalidMeta);
        Evento evento = new Evento();
        evento.setIdEvento(idInvalidEvento);

        assertThrows(SQLException.class, () -> metaDAO.vincularEvento(meta.getIdMeta(), evento.getIdEvento()));
    }

    @Test
    void testVincularValidMetaValidGrupoEventos() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);

        assertTrue(metaDAO.vincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos()));
    }

    @Test
    void testVincularInvalidMetaValidGrupoEventos() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idInvalidMeta);
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);

        assertThrows(SQLException.class,
                () -> metaDAO.vincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos()));
    }

    @Test
    void testVincularValidMetaInvalidGrupoEventos() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        GrupoEventos grupoEventos = new GrupoEventos(idInvalidGrupoEventos);

        assertThrows(SQLException.class,
                () -> metaDAO.vincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos()));
    }

    @Test
    void testVincularInvalidMetaInvalidGrupoEventos() {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idInvalidMeta);
        GrupoEventos grupoEventos = new GrupoEventos(idInvalidGrupoEventos);

        assertThrows(SQLException.class,
                () -> metaDAO.vincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos()));
    }

    @Test
    void testDesvincularEvento() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        Evento evento = new Evento();
        evento.setIdEvento(idValidEvento);

        metaDAO.vincularEvento(meta.getIdMeta(), evento.getIdEvento());
        assertTrue(metaDAO.desvincularEvento(meta.getIdMeta(), evento.getIdEvento()));
    }

    @Test
    void testDesvincularEventoDoesNotThrows() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        Evento evento = new Evento();
        evento.setIdEvento(idValidEvento);

        metaDAO.vincularEvento(meta.getIdMeta(), evento.getIdEvento());
        assertDoesNotThrow(() -> metaDAO.desvincularEvento(meta.getIdMeta(), evento.getIdEvento()));

    }

    @Test
    void testDesvincularGrupoEventos() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);

        metaDAO.vincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos());
        assertTrue(metaDAO.desvincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos()));
    }

    @Test
    void testDesvincularGrupoEventosDoesNotThrows() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);

        metaDAO.vincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos());
        assertDoesNotThrow(() -> metaDAO.desvincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos()));
    }

    @Test
    void testListarMetas() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());

        List<Meta> result = metaDAO.listarMetas();
        assertFalse(result.isEmpty());
    }

    @Test
    void testListarMetasEvento() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        Evento evento = new Evento();
        evento.setIdEvento(idValidEvento);

        metaDAO.vincularEvento(meta.getIdMeta(), evento.getIdEvento());
        List<Meta> result = metaDAO.listarMetasEvento(evento.getIdEvento());
        assertAll(() -> assertFalse(result.isEmpty()), () -> assertTrue(result.contains(metaDAO.getMeta(meta).get())));
    }

    @Test
    void testListarMetasGrupoEventos() throws SQLException {
        MetaDAO metaDAO = new MetaDAO(database.getConnection());
        Meta meta = new Meta(idValidMeta);
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);

        metaDAO.vincularGrupoEventos(meta.getIdMeta(), grupoEventos.getIdGrupoEventos());
        List<Meta> result = metaDAO.listarMetasGrupoEventos(grupoEventos.getIdGrupoEventos());
        assertAll(() -> assertFalse(result.isEmpty()), () -> assertTrue(result.contains(metaDAO.getMeta(meta).get())));
    }

}
