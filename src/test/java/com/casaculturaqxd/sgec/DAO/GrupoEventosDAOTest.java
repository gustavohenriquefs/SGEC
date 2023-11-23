package com.casaculturaqxd.sgec.DAO;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Meta;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class GrupoEventosDAOTest {
    private static DatabasePostgres db = DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
    private static int idValidGrupoEventos = 1, idInvalidGrupoEventos = -1, idValidEvento = 1, idValidInstituicao = 1,
            idInvalidInstituicao = -1, idValidMeta = 1, idInvalidMeta = -1, idValidServiceFile = 1,
            idInvalidServiceFile = -1;

    public GrupoEventosDAOTest() {
        setUpClass();
    }

    @BeforeAll
    public static void setUpClass() {
        try {
            db.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            db.getConnection().rollback();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    @Test
    void testGetValidGrupoEventos() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Optional<GrupoEventos> result = grupoEventosDAO.getGrupoEventos(grupoEventos);
        assertAll(() -> assertTrue(result.isPresent()), () -> assertNotNull(result.get().getIdGrupoEventos()),
                () -> assertNotNull(result.get().getNome()));
    }

    @Test
    void testGetValidGrupoEventosDoesNotThrows() {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        assertDoesNotThrow(() -> grupoEventosDAO.getGrupoEventos(grupoEventos));
    }

    @Test
    void testGetValidPreviewGrupoEventos() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Optional<GrupoEventos> result = grupoEventosDAO.getGrupoEventos(grupoEventos);
        assertAll(() -> assertTrue(result.isPresent()), () -> assertNotNull(result.get().getIdGrupoEventos()),
                () -> assertNotNull(result.get().getNome()));
    }

    @Test
    void testGetValidPreviewGrupoEventosDoesNotThrows() {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        assertDoesNotThrow(() -> grupoEventosDAO.getPreviewGrupoEventos(grupoEventos));
    }

    @Test
    void testInsertGrupoEventosOnlyWithName() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos();
        grupoEventos.setNome("nome_grupo_eventos_teste");

        assertTrue(grupoEventosDAO.insertGrupoEventos(grupoEventos));
    }

    @Test
    void testInsertGrupoEventosOnlyWithNameDoesNotThrows() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos();
        grupoEventos.setNome("nome_grupo_eventos_teste");

        assertDoesNotThrow(() -> grupoEventosDAO.insertGrupoEventos(grupoEventos));
    }

    @Test
    void testListUltimosGrupoEventos() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        ArrayList<GrupoEventos> result = grupoEventosDAO.listUltimosGrupoEventos();
        assertAll(() -> assertFalse(result.isEmpty()),
                () -> assertTrue(result.stream().allMatch(resultItem -> resultItem.getIdGrupoEventos() != null)));
    }

    @Test
    void testListPreviewGrupoEventosDoesNotThrows() {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        assertDoesNotThrow(() -> grupoEventosDAO.listUltimosGrupoEventos());
    }

    @Test
    void testDeleteGrupoEventos() {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        assertTrue(grupoEventosDAO.deleteGrupoEventos(grupoEventos));
    }

    @Test
    void testVincularEvento() {

    }

    @Test
    void testDesvincularColaborador() {

    }

    @Test
    void testDesvincularEvento() {

    }

    @Test
    void testDesvincularOrganizador() {

    }

    @Test
    void testListEventos() {

    }

    @Test
    void testListMetas() {

    }

    @Test
    void testPesquisaPreviewGrupoEventos() {

    }

    @Test
    void testUpdateGrupoEventos() {
        // TODO
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidEvento);
        String updateStringValues;
        int updateInt = 1;
        ServiceFile updateServiceFile = new ServiceFile(idValidServiceFile);
        grupoEventos.setDescricao("updated string");
        grupoEventos.setImagemCapa(updateServiceFile);
    }

    @Test
    void testVincularValidGrupoEventosValidColaborador() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Instituicao colaborador = new Instituicao(idValidInstituicao);

        assertTrue(grupoEventosDAO.vincularColaborador(grupoEventos, colaborador));
    }

    @Test
    void testVincularValidGrupoEventosValidOrganizador() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Instituicao organizador = new Instituicao(idValidInstituicao);

        assertTrue(grupoEventosDAO.vincularOrganizador(grupoEventos, organizador));
    }

    @Test
    void testVincularMeta() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        boolean result = grupoEventosDAO.vincularMeta(new Meta(idValidMeta), grupoEventos);
        assertAll(() -> assertTrue(result), () -> assertFalse(grupoEventosDAO.listMetas(grupoEventos).isEmpty()));
    }

    @Test
    void testDesvincularMeta() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Meta meta = new Meta(idValidMeta);
        grupoEventosDAO.vincularMeta(meta, grupoEventos);

        boolean result = grupoEventosDAO.desvincularMeta(meta, grupoEventos);

        assertAll(() -> assertTrue(result), () -> assertTrue(grupoEventosDAO.listMetas(grupoEventos).isEmpty()));
    }
}
