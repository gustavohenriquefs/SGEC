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
    private static ParticipanteDAO participanteDAO;
    private static Participante participante;
    private static int idValidParticipante = 1, idUpdatableParticipante = 2,
            idInvalidParticipante = -1, idValidEvento = 1, idInvalidEvento = -1,
            idValidServiceFile = 11;

    public ParticipanteDAOTest() {
        setUpClass();
    }

    @BeforeAll
    public static void setUpClass() {
        db = DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
        participanteDAO = new ParticipanteDAO();
        participanteDAO.setConnection(db.getConnection());
        try {
            participanteDAO.getConn().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDownClass() {
        db.desconectar(db.getConnection());
    }

    @AfterEach
    public void tearDown() {
        if (participante != null) {
            if (participante.getIdParticipante() != 1 && participante.getIdParticipante() != 2) {
                try {
                    participanteDAO.deletarParticipante(participante);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                }
            }
            participanteDAO.desvincularEvento(participante.getIdParticipante(), idValidEvento);
        }
    }

    @Test
    public void testSetConnection() {
        assertEquals(db.getConnection(), participanteDAO.getConn());
    }

    @Test
    public void testGetValidParticipante() throws SQLException {
        participante = new Participante(idValidParticipante, null, null, null, null, null);
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
        participante = new Participante(idValidParticipante, null, null, null, null, null);
        assertDoesNotThrow(() -> participanteDAO.getParticipante(participante));
    }

    @Test
    public void testGetInvalidParticipante() throws SQLException {
        participante = new Participante(idInvalidParticipante, null, null, null, null, null);
        assertEquals(Optional.empty(), participanteDAO.getParticipante(participante));
    }

    @Test
    public void testGetInvalidParticipanteThrows() {
        participante = new Participante(idInvalidParticipante, null, null, null, null, null);
        assertThrows(NoSuchElementException.class,
                () -> participanteDAO.getParticipante(participante).get());
    }

    @Test
    public void testInserirValidParticipante() throws SQLException {
        participante = new Participante("novo_participante", "nova_area_atuacao", "nova bio", "link novo");
        assertAll(() -> assertTrue(participanteDAO.inserirParticipante(participante)),
                () -> assertNotEquals(0, participante.getIdParticipante()));
    }

    @Test
    public void testInserirValidParticipanteDoesNotThrows() {
        participante = new Participante(idValidParticipante, null, null, null, null);
        assertDoesNotThrow(() -> participanteDAO.inserirParticipante(participante));
    }

    @Test
    public void testInserirInvalidParticipante() throws SQLException {
        participante = new Participante(idInvalidParticipante, null, null, null, null);
        assertFalse(participanteDAO.inserirParticipante(participante));
    }

    @Test
    public void testUpdateValidParticipante() throws SQLException {
        participante = new Participante(idUpdatableParticipante, "updated_nome",
                "updated area atuacao", "updated bio", "updated link", null);
        assertTrue(participanteDAO.updateParticipante(participante));
    }

    @Test
    public void testUpdateValidParticipanteDoesNotThrows() {
        participante = new Participante(idUpdatableParticipante, "updated_nome", "updated_area",
                "updated_bio", "updated_link", new ServiceFile(idValidServiceFile));
        assertDoesNotThrow(() -> participanteDAO.updateParticipante(participante));
    }

    @Test
    public void testUpdateInvalidParticipante() throws SQLException {
        participante = new Participante(idInvalidParticipante, null, null, null, null, null);
        assertFalse(participanteDAO.updateParticipante(participante));
    }

    @Test
    public void testDeletarValidParticipante() throws SQLException {
        participante = new Participante("novo_teste", null, null, null);
        participanteDAO.inserirParticipante(participante);
        assertTrue(participanteDAO.deletarParticipante(participante));
    }

    @Test
    public void testDeletarValidParticipanteDoesNotThrows() throws SQLException {
        participante = new Participante("novo_teste", null, null, null);
        participanteDAO.inserirParticipante(participante);
        assertDoesNotThrow(() -> participanteDAO.deletarParticipante(participante));
    }

    @Test
    public void testDeletarInvalidParticipante() throws SQLException {
        participante = new Participante(idInvalidParticipante, null, null, null, null, null);
        assertFalse(participanteDAO.deletarParticipante(participante));
    }

    @Test
    public void testVincularValidParticipanteValidEvento() {
        participante = new Participante(idValidParticipante, null, null, null, null);
        assertTrue(participanteDAO.vincularEvento(participante.getIdParticipante(), idValidEvento));
    }

    @Test
    public void testVincularValidParticipanteInvalidEvento() {
        participante = new Participante(idValidParticipante, null, null, null, null);
        assertFalse(
                participanteDAO.vincularEvento(participante.getIdParticipante(), idInvalidEvento));
    }

    @Test
    public void testVincularInvalidParticipanteValidEvento() {
        participante = new Participante(idInvalidParticipante, null, null, null, null);
        assertFalse(
                participanteDAO.vincularEvento(participante.getIdParticipante(), idValidEvento));
    }

    @Test
    public void testVincularInvalidParticipanteInvalidEvento() {
        participante = new Participante(idInvalidParticipante, null, null, null, null);
        assertFalse(
                participanteDAO.vincularEvento(participante.getIdParticipante(), idInvalidEvento));
    }

    @Test
    public void testDesvincularValidParticipanteValidEvento() {
        participante = new Participante(idValidParticipante, null, null, null, null);
        participanteDAO.vincularEvento(participante.getIdParticipante(), idValidEvento);
        assertTrue(
                participanteDAO.desvincularEvento(participante.getIdParticipante(), idValidEvento));
    }

    @Test
    public void testDesvincularValidParticipanteInvalidEvento() {
        participante = new Participante(idValidParticipante, null, null, null, null);
        assertFalse(participanteDAO.desvincularEvento(participante.getIdParticipante(),
                idInvalidEvento));
    }

    @Test
    public void testDesvincularInvalidParticipanteValidEvento() {
        participante = new Participante(idInvalidParticipante, null, null, null, null);
        assertFalse(
                participanteDAO.desvincularEvento(participante.getIdParticipante(), idValidEvento));
    }

    @Test
    public void testDesvincularInvalidParticipanteInvalidEvento() {
        participante = new Participante(idInvalidParticipante, null, null, null, null);
        assertFalse(
                participanteDAO.vincularEvento(participante.getIdParticipante(), idInvalidEvento));
    }
}
