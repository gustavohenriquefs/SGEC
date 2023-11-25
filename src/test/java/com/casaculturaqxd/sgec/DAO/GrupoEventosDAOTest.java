package com.casaculturaqxd.sgec.DAO;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Instituicao;
import com.casaculturaqxd.sgec.models.Meta;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class GrupoEventosDAOTest {
    private static DatabasePostgres db = DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
    private static int idValidGrupoEventos = 1, idValidEvento = 1, idValidInstituicao = 1, idValidMeta = 1,
            idInvalidMeta = -1, idValidServiceFile = 11;

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
    void testDeleteGrupoEventos() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        assertTrue(grupoEventosDAO.deleteGrupoEventos(grupoEventos));
    }

    @Test
    void testVincularEvento() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Evento evento = new Evento();
        evento.setIdEvento(idValidEvento);

        assertTrue(grupoEventosDAO.vincularEvento(grupoEventos, evento));
    }

    @Test
    void testListEventos() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Evento evento = new Evento();
        evento.setIdEvento(idValidEvento);

        grupoEventosDAO.vincularEvento(grupoEventos, evento);

        List<Evento> result = grupoEventosDAO.listEventos(grupoEventos);
        assertAll(() -> assertFalse(result.isEmpty()),
                () -> assertTrue(result.stream().anyMatch(element -> element.getIdEvento() == evento.getIdEvento())));
    }

    @Test
    void testListMetas() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Meta meta = new Meta(idValidMeta);

        grupoEventosDAO.vincularMeta(meta, grupoEventos);

        ArrayList<Meta> result = grupoEventosDAO.listMetas(grupoEventos);
        assertAll(() -> assertFalse(result.isEmpty()),
                () -> assertTrue(result.stream().anyMatch(element -> element.getIdMeta() == meta.getIdMeta())));
    }

    @Test
    void testPesquisaPreviewGrupoEventos() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        ArrayList<GrupoEventos> result = grupoEventosDAO.listUltimosGrupoEventos();

        assertAll(() -> assertFalse(result.isEmpty()), () -> assertTrue(result.size() <= 5));
    }

    @Test
    void testUpdateGrupoEventos() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        ServiceFileDAO serviceFileDAO = new ServiceFileDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidEvento);
        String updateStringValues = "updated";
        String updateClassificaoEtaria = "10 anos";
        int updateInt = 1;
        Date updateDate = new Date(System.currentTimeMillis());
        ServiceFile updateServiceFile = new ServiceFile(idValidServiceFile);
        grupoEventos.setNome(updateStringValues);
        grupoEventos.setDescricao("updated string");
        grupoEventos.setClassificacaoEtaria(updateClassificaoEtaria);
        grupoEventos.setPublicoEsperado(updateInt);
        grupoEventos.setPublicoAlcancado(updateInt);
        grupoEventos.setNumAcoesEsperado(updateInt);
        grupoEventos.setNumMunicipiosEsperado(updateInt);
        grupoEventos.setNumParticipantesEsperado(updateInt);
        grupoEventos.setDataInicial(updateDate);
        grupoEventos.setDataFinal(updateDate);
        grupoEventos.setImagemCapa(serviceFileDAO.getArquivo(updateServiceFile));

        boolean result = grupoEventosDAO.updateGrupoEventos(grupoEventos);
        GrupoEventos updatedGrupoEventos = grupoEventosDAO.getGrupoEventos(grupoEventos).get();
        assertAll(() -> assertTrue(result),
                () -> assertTrue(grupoEventos.getNome().equals(updatedGrupoEventos.getNome())),
                () -> assertTrue(grupoEventos.getDescricao().equals(updatedGrupoEventos.getDescricao())),
                () -> assertTrue(
                        grupoEventos.getClassificacaoEtaria().equals(updatedGrupoEventos.getClassificacaoEtaria())),
                () -> assertTrue(grupoEventos.getPublicoEsperado() == updatedGrupoEventos.getPublicoEsperado()),
                () -> assertTrue(grupoEventos.getPublicoAlcancado() == updatedGrupoEventos.getPublicoAlcancado()),
                () -> assertTrue(grupoEventos.getNumAcoesEsperado() == updatedGrupoEventos.getNumAcoesEsperado()),
                () -> assertTrue(
                        grupoEventos.getNumMunicipiosEsperado() == updatedGrupoEventos.getNumMunicipiosEsperado()),
                () -> assertTrue(grupoEventos.getNumParticipantesEsperado() == updatedGrupoEventos
                        .getNumParticipantesEsperado()),
                () -> assertTrue(grupoEventos.getDataInicial().toLocalDate()
                        .equals(updatedGrupoEventos.getDataInicial().toLocalDate())),
                () -> assertTrue(grupoEventos.getDataFinal().toLocalDate()
                        .equals(updatedGrupoEventos.getDataFinal().toLocalDate())),
                () -> assertTrue(grupoEventos.getImagemCapa().getServiceFileId()
                        .equals(updatedGrupoEventos.getImagemCapa().getServiceFileId())));
    }

    @Test
    void testVincularValidGrupoEventosValidColaborador() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Instituicao colaborador = new Instituicao(idValidInstituicao);

        assertTrue(grupoEventosDAO.vincularColaborador(grupoEventos, colaborador));
    }

    @Test
    void testDesvincularValidGrupoEventosValidColaborador() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Instituicao organizador = new Instituicao(idValidInstituicao);
        grupoEventosDAO.vincularColaborador(grupoEventos, organizador);

        assertTrue(grupoEventosDAO.desvincularColaborador(grupoEventos, organizador));
    }

    @Test
    void testVincularValidGrupoEventosValidOrganizador() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Instituicao organizador = new Instituicao(idValidInstituicao);

        assertTrue(grupoEventosDAO.vincularOrganizador(grupoEventos, organizador));
    }

    @Test
    void testDesvincularValidGrupoEventosValidOrganizador() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Instituicao organizador = new Instituicao(idValidInstituicao);
        grupoEventosDAO.vincularOrganizador(grupoEventos, organizador);

        assertTrue(grupoEventosDAO.desvincularOrganizador(grupoEventos, organizador));
    }

    @Test
    void testVincularValidMeta() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        boolean result = grupoEventosDAO.vincularMeta(new Meta(idValidMeta), grupoEventos);
        assertAll(() -> assertTrue(result), () -> assertFalse(grupoEventosDAO.listMetas(grupoEventos).isEmpty()));
    }

    @Test
    void testVincularInvalidMetaThrows() {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Meta meta = new Meta(idInvalidMeta);
        assertThrows(SQLException.class, () -> grupoEventosDAO.vincularMeta(meta, grupoEventos));
    }

    @Test
    void testDesvincularValidMeta() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Meta meta = new Meta(idValidMeta);
        grupoEventosDAO.vincularMeta(meta, grupoEventos);

        boolean result = grupoEventosDAO.desvincularMeta(meta, grupoEventos);

        assertAll(() -> assertTrue(result), () -> assertTrue(grupoEventosDAO.listMetas(grupoEventos).isEmpty()));
    }

    @Test
    void testDesvincularInvalidMeta() throws SQLException {
        GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());
        GrupoEventos grupoEventos = new GrupoEventos(idValidGrupoEventos);
        Meta meta = new Meta(idInvalidMeta);

        assertFalse(grupoEventosDAO.desvincularMeta(meta, grupoEventos));
    }

}
