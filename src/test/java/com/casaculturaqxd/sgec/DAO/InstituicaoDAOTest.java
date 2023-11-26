package com.casaculturaqxd.sgec.DAO;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Instituicao;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InstituicaoDAOTest {
    private static DatabasePostgres db;
    private static int idValidInstituicao = 1, idUpdatableInstituicao = 2, idInvalidInstituicao = -1, idValidEvento = 1,
            idInvalidEvento = -1;

    public InstituicaoDAOTest() throws SQLException {
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
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        assertEquals(db.getConnection(), dao.getConn());
    }

    @Test
    public void testGetValidInstituicaoById() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        Instituicao result = dao.getInstituicao(instituicao).get();
        assertAll(() -> assertNotEquals(Optional.empty(), result),
                () -> assertEquals(idValidInstituicao, result.getIdInstituicao()),
                () -> assertEquals("instituicao_teste", result.getNome()));
    }

    @Test
    public void testGetValidInstituicaoByNome() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao result = dao.getInstituicao("instituicao_teste").get();
        assertAll(() -> assertNotEquals(Optional.empty(), result),
                () -> assertEquals(idValidInstituicao, result.getIdInstituicao()),
                () -> assertEquals("instituicao_teste", result.getNome()));
    }

    @Test
    public void testGetInvalidInstituicaoById() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idInvalidInstituicao);
        assertEquals(Optional.empty(), dao.getInstituicao(instituicao));
    }

    @Test
    public void testGetInvalidInstituicaoByNome() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        assertEquals(Optional.empty(), dao.getInstituicao("invalid_instituicao"));
    }

    @Test
    public void testInserirValidInstituicao() throws Exception {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao("new name");

        assertAll(() -> assertTrue(dao.inserirInstituicao(instituicao)),
                () -> assertNotNull(instituicao.getIdInstituicao()));
    }

    @Test
    public void testInserirInstituicaoSemNome() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(null);
        assertThrows(SQLException.class, () -> dao.inserirInstituicao(instituicao));
    }

    /**
     * utilizando valor aleatorio para garantir que o valor atualizado seja
     * diferente do valor antigo
     */
    @Test
    public void testAtualizarValidInstituicao() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idUpdatableInstituicao);

        Random rand = new Random(instituicao.hashCode());
        String updateValue = "new nome" + rand.nextInt();
        instituicao.setNome(updateValue);
        assertAll(() -> assertTrue(dao.atualizarInstituicao(instituicao)),
                () -> assertEquals(updateValue, dao.getInstituicao(instituicao).get().getNome()));
    }

    @Test
    public void testAtualizarInvalidInstituicao() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao();
        instituicao.setIdInstituicao(idInvalidInstituicao);

        assertFalse(dao.atualizarInstituicao(instituicao));
    }

    @Test
    public void testRemoverValidInstituicao() throws Exception {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao("new_instituicao");
        dao.inserirInstituicao(instituicao);

        assertTrue(dao.removerInstituicao(instituicao));
    }

    @Test
    public void testRemoverInvalidInstituicao() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao("new_instituicao");
        instituicao.setIdInstituicao(idInvalidInstituicao);

        assertFalse(dao.removerInstituicao(instituicao));
    }

    @Test
    public void testVincularValidOrganizadorValidEvento() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao();
        instituicao.setIdInstituicao(idValidInstituicao);
        instituicao = dao.getInstituicao(instituicao).get();
        instituicao.setDescricaoContribuicao("descricao_teste");
        instituicao.setValorContribuicao("valor_teste");

        assertTrue(dao.vincularOrganizadorEvento(instituicao, idValidEvento));
    }

    /**
     * Valor da contribuicao e opcional, por isso vinculo deve ser sucedido
     */
    @Test
    public void testVincularOrganizadorSemValorContribuicao() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao("nova_instituicao", "nova_contribuicao", null, null);
        dao.inserirInstituicao(instituicao);

        assertTrue(dao.vincularOrganizadorEvento(instituicao, idValidEvento));
    }

    @Test
    public void testVincularOrganizadorSemDescricaoContribuicao() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao("nova_instituicao", null, "novo_valor", null);
        instituicao.setIdInstituicao(idValidInstituicao);

        assertThrows(SQLException.class, () -> dao.vincularOrganizadorEvento(instituicao, idValidEvento));
    }

    @Test
    public void testVincularValidOrganizadorInvalidEvento() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        assertThrows(SQLException.class, () -> dao.vincularOrganizadorEvento(instituicao, idInvalidEvento));
    }

    @Test
    public void testVincularInvalidOrganizadorValidEvento() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idInvalidInstituicao);
        assertThrows(SQLException.class, () -> dao.vincularOrganizadorEvento(instituicao, idValidEvento));
    }

    @Test
    public void testVincularInvalidOrganizadorInvalidEvento() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idInvalidInstituicao);
        assertThrows(SQLException.class, () -> dao.vincularOrganizadorEvento(instituicao, idInvalidEvento));
    }

    @Test
    public void testDesvincularValidOrganizadorValidEvento() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao("nova descricao");
        instituicao.setValorContribuicao("novo valor");

        dao.vincularOrganizadorEvento(instituicao, idValidEvento);

        assertTrue(dao.desvincularOrganizadorEvento(instituicao.getIdInstituicao(), idValidEvento));
    }

    @Test
    public void testVincularValidColaboradorValidEvento() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao("nova instituicao", "nova descricao", "novo valor", null);
        dao.inserirInstituicao(instituicao);

        assertTrue(dao.vincularColaboradorEvento(instituicao, idValidEvento));
    }

    @Test
    public void testVincularColaboradorSemValorContribuicao() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao("nova instituicao", "nova descricao", null, null);
        dao.inserirInstituicao(instituicao);

        assertTrue(dao.vincularColaboradorEvento(instituicao, idValidEvento));
    }

    @Test
    public void testeVincularColaboradorSemDescricaoContribuicao() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao("nova instituicao", null, "novo valor", null);
        dao.inserirInstituicao(instituicao);
        assertThrows(SQLException.class, () -> dao.vincularColaboradorEvento(instituicao, idValidEvento));
    }

    @Test
    public void testDesvincularValidColaboradorValidEvento() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao("nova descricao");
        instituicao.setValorContribuicao("novo valor");
        dao.vincularColaboradorEvento(instituicao, idValidEvento);

        assertTrue(dao.desvincularColaboradorEvento(instituicao.getIdInstituicao(), idValidEvento));
    }

    @Test
    public void testListarOrganizadoresValidEvento() throws SQLException {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = instituicaoDAO.getInstituicao(new Instituicao(idValidInstituicao)).get();
        instituicaoDAO.vincularOrganizadorEvento(instituicao.getIdInstituicao(), idValidEvento, "contribuicao_teste",
                null);

        List<Instituicao> result = instituicaoDAO.listarOrganizadoresEvento(idValidEvento);
        Instituicao inserted = result.get(0);
        assertAll(() -> assertFalse(result.isEmpty()),
                () -> assertEquals(instituicao.getIdInstituicao(), inserted.getIdInstituicao()),
                () -> assertEquals(instituicao.getNome(), inserted.getNome()),
                () -> assertEquals("contribuicao_teste", inserted.getDescricaoContribuicao()),
                () -> assertNull(inserted.getValorContribuicao()));
    }

    @Test
    public void testListarOrganizadoresEmptyEvento() throws SQLException {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(db.getConnection());
        List<Instituicao> result = instituicaoDAO.listarOrganizadoresEvento(idValidEvento);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testListarColaboradoresValidEvento() throws SQLException {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = instituicaoDAO.getInstituicao(new Instituicao(idValidInstituicao)).get();
        instituicaoDAO.vincularColaboradorEvento(instituicao.getIdInstituicao(), idValidEvento, "contribuicao_teste",
                null);

        List<Instituicao> result = instituicaoDAO.listarColaboradoresEvento(idValidEvento);
        Instituicao inserted = result.get(0);
        assertAll(() -> assertFalse(result.isEmpty()),
                () -> assertEquals(instituicao.getIdInstituicao(), inserted.getIdInstituicao()),
                () -> assertEquals(instituicao.getNome(), inserted.getNome()),
                () -> assertEquals("contribuicao_teste", inserted.getDescricaoContribuicao()),
                () -> assertNull(inserted.getValorContribuicao()));
    }

    @Test
    public void testListarColaboradoresEmptyEvento() throws SQLException {
        InstituicaoDAO instituicaoDAO = new InstituicaoDAO(db.getConnection());
        List<Instituicao> result = instituicaoDAO.listarColaboradoresEvento(idValidEvento);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAtualizarValidOrganizadorValidEvento() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        // criar vinculo para o teste
        String previousDescricao = "nova descricao", previousValor = null;
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao(previousDescricao);
        instituicao.setValorContribuicao(previousValor);
        dao.vincularOrganizadorEvento(instituicao, idValidEvento);

        // atualizar o model
        instituicao.setDescricaoContribuicao("updated descricao");
        instituicao.setValorContribuicao("update valor");
        boolean sucess = dao.atualizarOrganizadorEvento(instituicao, idValidEvento);
        assertAll(() -> assertTrue(sucess),
                () -> assertNotEquals(previousDescricao,
                        dao.getInstituicao(instituicao).get().getDescricaoContribuicao()),
                () -> assertNotEquals(previousValor, dao.getInstituicao(instituicao).get().getValorContribuicao()));
    }

    @Test
    public void testAtualizarValidColaboradorValidEvento() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        // criar vinculo para o teste
        String previousDescricao = "nova descricao", previousValor = null;
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao(previousDescricao);
        instituicao.setValorContribuicao(previousValor);
        dao.vincularColaboradorEvento(instituicao, idValidEvento);

        // atualizar o model
        instituicao.setDescricaoContribuicao("updated descricao");
        instituicao.setValorContribuicao("update valor");

        boolean sucess = dao.atualizarColaboradorEvento(instituicao, idValidEvento);
        assertAll(() -> assertTrue(sucess),
                () -> assertNotEquals(previousDescricao,
                        dao.getInstituicao(instituicao).get().getDescricaoContribuicao()),
                () -> assertNotEquals(previousValor, dao.getInstituicao(instituicao).get().getValorContribuicao()));
    }
}
