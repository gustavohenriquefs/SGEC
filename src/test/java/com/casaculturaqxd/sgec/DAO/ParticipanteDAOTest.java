package com.casaculturaqxd.sgec.DAO;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Participante;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParticipanteDAOTest {
    private static DatabasePostgres db;
    private static int idValidParticipante = 1, idUpdatableParticipante = 2, idInvalidParticipante = -1,
            idValidEvento = 1, idInvalidEvento = -1, idValidServiceFile = 11;

    public ParticipanteDAOTest() throws SQLException {
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
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        assertEquals(db.getConnection(), participanteDAO.getConn());
    }

    @Test
    public void testGetValidParticipante() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idValidParticipante);
        Participante result = participanteDAO.getParticipante(participante).get();
        assertAll(() -> assertEquals(idValidParticipante, result.getIdParticipante()),
                () -> assertEquals("participante teste", result.getNome()),
                () -> assertEquals("bio teste", result.getBio()),
                () -> assertEquals("artesanato, pintura, cinema", result.getAreaDeAtuacao()),
                () -> assertEquals("link teste", result.getLinkMapaDaCultura()),
                () -> assertEquals(idValidServiceFile, result.getImagemCapa().getServiceFileId()));
    }

    @Test
    public void testGetValidParticipanteDoesNotThrows() {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idValidParticipante);
        assertDoesNotThrow(() -> participanteDAO.getParticipante(participante));
    }

    @Test
    public void testGetInvalidParticipante() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);
        participante = new Participante(idInvalidParticipante, null, null, null, null, null);
        assertEquals(Optional.empty(), participanteDAO.getParticipante(participante));
    }

    @Test
    public void testGetInvalidParticipanteThrows() {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);

        assertThrows(NoSuchElementException.class, () -> participanteDAO.getParticipante(participante).get());
    }

    @Test
    public void testInserirValidParticipante() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante("novo_participante", "nova_area_atuacao", "nova bio", "link novo");
        assertAll(() -> assertTrue(participanteDAO.inserirParticipante(participante)),
                () -> assertNotEquals(0, participante.getIdParticipante()));
    }

    @Test
    public void testInserirValidParticipanteDoesNotThrows() {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante("novo_participante", "nova_area_atuacao", "nova bio", "link novo");
        assertDoesNotThrow(() -> participanteDAO.inserirParticipante(participante));
    }

    @Test
    public void testInserirParticipanteWithoutNameThrows() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(0, null, "area_atuacao", "bio", "link");

        assertThrows(SQLException.class, () -> participanteDAO.inserirParticipante(participante));
    }

    @Test
    public void testUpdateValidParticipante() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);
        participante = new Participante(idUpdatableParticipante, "updated_nome", "updated area atuacao", "updated bio",
                "updated link", null);
        assertTrue(participanteDAO.updateParticipante(participante));
    }

    @Test
    public void testUpdateValidParticipanteDoesNotThrows() {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idUpdatableParticipante, "updated_nome", "updated_area",
                "updated_bio", "updated_link", new ServiceFile(idValidServiceFile));
        assertDoesNotThrow(() -> participanteDAO.updateParticipante(participante));
    }

    @Test
    public void testUpdateInvalidParticipante() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);
        participante = new Participante(idInvalidParticipante, null, null, null, null, null);
        assertFalse(participanteDAO.updateParticipante(participante));
    }

    @Test
    public void testDeletarValidParticipante() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);
        participante = new Participante("novo_teste", null, null, null);
        participanteDAO.inserirParticipante(participante);
        assertTrue(participanteDAO.deletarParticipante(participante));
    }

    @Test
    public void testDeletarValidParticipanteDoesNotThrows() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idValidParticipante);
        assertDoesNotThrow(() -> participanteDAO.deletarParticipante(participante));
    }

    @Test
    public void testDeletarInvalidParticipante() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);
        participante = new Participante(idInvalidParticipante, null, null, null, null, null);
        assertFalse(participanteDAO.deletarParticipante(participante));
    }

    @Test
    public void testVincularValidParticipanteValidEvento() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idValidParticipante);
        assertTrue(participanteDAO.vincularEvento(participante.getIdParticipante(), idValidEvento));
    }

    @Test
    public void testVincularValidParticipanteInvalidEvento() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idValidParticipante);
        assertThrows(SQLException.class,
                () -> participanteDAO.vincularEvento(participante.getIdParticipante(), idInvalidEvento));
    }

    @Test
    public void testVincularInvalidParticipanteValidEvento() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);
        assertThrows(SQLException.class,
                () -> participanteDAO.vincularEvento(participante.getIdParticipante(), idValidEvento));
    }

    @Test
    public void testVincularInvalidParticipanteInvalidEvento() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);
        assertThrows(SQLException.class,
                () -> participanteDAO.vincularEvento(participante.getIdParticipante(), idInvalidEvento));
    }

    @Test
    public void testDesvincularValidParticipanteValidEvento() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);
        participante = new Participante(idValidParticipante, null, null, null, null);
        participanteDAO.vincularEvento(participante.getIdParticipante(), idValidEvento);
        assertTrue(participanteDAO.desvincularEvento(participante.getIdParticipante(), idValidEvento));
    }

    @Test
    public void testDesvincularValidParticipanteInvalidEvento() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);
        participante = new Participante(idValidParticipante, null, null, null, null);
        assertFalse(participanteDAO.desvincularEvento(participante.getIdParticipante(), idInvalidEvento));
    }

    @Test
    public void testDesvincularInvalidParticipanteValidEvento() throws SQLException {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);
        participante = new Participante(idInvalidParticipante, null, null, null, null);
        assertFalse(participanteDAO.desvincularEvento(participante.getIdParticipante(), idValidEvento));
    }

    @Test
    public void testDesvincularInvalidParticipanteInvalidEvento() {
        ParticipanteDAO participanteDAO = new ParticipanteDAO(db.getConnection());
        Participante participante = new Participante(idInvalidParticipante);
        assertThrows(SQLException.class,
                () -> participanteDAO.vincularEvento(participante.getIdParticipante(), idInvalidEvento));
    }
}
