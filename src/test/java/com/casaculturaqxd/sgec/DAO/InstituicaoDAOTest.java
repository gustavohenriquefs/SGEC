package com.casaculturaqxd.sgec.DAO;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Instituicao;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class InstituicaoDAOTest {
    private static DatabasePostgres db;
    private static int idValidInstituicao = 1, idUpdatableInstituicao = 2,
            idInvalidInstituicao = -1, idValidEvento = 1, idInvalidEvento = -1;

    public InstituicaoDAOTest() {
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
    }

    @Test
    public void testConnection() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        assertEquals(db.getConnection(), dao.getConn());
    }

    @Test
    public void testGetValidInstituicaoById() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        Instituicao result = dao.getInstituicao(instituicao).get();
        assertAll(() -> assertNotEquals(Optional.empty(), result),
                () -> assertEquals(idValidInstituicao, result.getIdInstituicao()),
                () -> assertEquals("instituicao_teste", result.getNome()));
    }

    @Test
    public void testGetValidInstituicaoByNome() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao result = dao.getInstituicao("instituicao_teste").get();
        assertAll(() -> assertNotEquals(Optional.empty(), result),
                () -> assertEquals(idValidInstituicao, result.getIdInstituicao()),
                () -> assertEquals("instituicao_teste", result.getNome()));
    }

    @Test
    public void testGetInvalidInstituicaoById() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idInvalidInstituicao);
        assertEquals(Optional.empty(), dao.getInstituicao(instituicao));
    }

    @Test
    public void testGetInvalidInstituicaoByNome() {
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
    public void testInserirInstituicaoSemNome() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(null);
        assertFalse(dao.inserirInstituicao(instituicao));
    }

    /**
     * utilizando valor aleatorio para garantir que o valor atualizado seja diferente do valor
     * antigo
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
    public void testVincularValidOrganizadorValidEvento() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao();
        instituicao.setIdInstituicao(idValidInstituicao);
        instituicao = dao.getInstituicao(instituicao).get();
        instituicao.setDescricaoContribuicao("descricao_teste");
        instituicao.setValorContribuicao("valor_teste");

        assertTrue(dao.vincularOrganizador(instituicao, idValidEvento));
    }

    /**
     * Valor da contribuicao e opcional, por isso vinculo deve ser sucedido
     */
    @Test
    public void testVincularOrganizadorSemValorContribuicao() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao =
                new Instituicao("nova_instituicao", "nova_contribuicao", null, null);
        dao.inserirInstituicao(instituicao);

        assertTrue(dao.vincularOrganizador(instituicao, idValidEvento));
    }

    @Test
    public void testVincularOrganizadorSemDescricaoContribuicao() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao("nova_instituicao", null, "novo_valor", null);
        instituicao.setIdInstituicao(idValidInstituicao);

        assertFalse(dao.vincularOrganizador(instituicao, idValidEvento));
    }

    @Test
    public void testVincularValidOrganizadorInvalidEvento() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        assertFalse(dao.vincularOrganizador(instituicao, idInvalidEvento));
    }

    @Test
    public void testVincularInvalidOrganizadorValidEvento() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idInvalidInstituicao);
        assertFalse(dao.vincularOrganizador(instituicao, idValidEvento));
    }

    @Test
    public void testVincularInvalidOrganizadorInvalidEvento() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idInvalidInstituicao);
        assertFalse(dao.vincularOrganizador(instituicao, idInvalidEvento));
    }

    @Test
    public void testDesvincularValidOrganizadorValidEvento() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao("nova descricao");
        instituicao.setValorContribuicao("novo valor");

        dao.vincularOrganizador(instituicao, idValidEvento);

        assertTrue(dao.desvincularOrganizador(instituicao.getIdInstituicao(), idValidEvento));
    }

    @Test
    public void testVincularValidColaboradorValidEvento() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao =
                new Instituicao("nova instituicao", "nova descricao", "novo valor", null);
        dao.inserirInstituicao(instituicao);

        assertTrue(dao.vincularColaborador(instituicao, idValidEvento));
    }

    @Test
    public void testVincularColaboradorSemValorContribuicao() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao("nova instituicao", "nova descricao", null, null);
        dao.inserirInstituicao(instituicao);

        assertTrue(dao.vincularColaborador(instituicao, idValidEvento));
    }

    @Test
    public void testeVincularColaboradorSemDescricaoContribuicao() throws SQLException {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao("nova instituicao", null, "novo valor", null);
        dao.inserirInstituicao(instituicao);
        assertFalse(dao.vincularColaborador(instituicao, idValidEvento));
    }

    @Test
    public void testDesvincularValidColaboradorValidEvento() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao("nova descricao");
        instituicao.setValorContribuicao("novo valor");
        dao.vincularColaborador(instituicao, idValidEvento);

        assertTrue(dao.desvincularColaborador(instituicao.getIdInstituicao(), idValidEvento));
    }

    @Test
    public void testAtualizarValidOrganizadorValidEvento() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        // criar vinculo para o teste
        String previousDescricao = "nova descricao", previousValor = null;
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao(previousDescricao);
        instituicao.setValorContribuicao(previousValor);
        dao.vincularOrganizador(instituicao, idValidEvento);

        // atualizar o model
        instituicao.setDescricaoContribuicao("updated descricao");
        instituicao.setValorContribuicao("update valor");
        boolean sucess = dao.atualizarOrganizador(instituicao, idValidEvento);
        assertAll(() -> assertTrue(sucess),
                () -> assertNotEquals(previousDescricao,
                        dao.getInstituicao(instituicao).get().getDescricaoContribuicao()),
                () -> assertNotEquals(previousValor,
                        dao.getInstituicao(instituicao).get().getValorContribuicao()));
    }

    @Test
    public void testAtualizarValidColaboradorValidEvento() {
        InstituicaoDAO dao = new InstituicaoDAO(db.getConnection());
        // criar vinculo para o teste
        String previousDescricao = "nova descricao", previousValor = null;
        Instituicao instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao(previousDescricao);
        instituicao.setValorContribuicao(previousValor);
        dao.vincularColaborador(instituicao, idValidEvento);

        // atualizar o model
        instituicao.setDescricaoContribuicao("updated descricao");
        instituicao.setValorContribuicao("update valor");

        boolean sucess = dao.atualizarColaborador(instituicao, idValidEvento);
        assertAll(() -> assertTrue(sucess),
                () -> assertNotEquals(previousDescricao,
                        dao.getInstituicao(instituicao).get().getDescricaoContribuicao()),
                () -> assertNotEquals(previousValor,
                        dao.getInstituicao(instituicao).get().getValorContribuicao()));
    }
}
