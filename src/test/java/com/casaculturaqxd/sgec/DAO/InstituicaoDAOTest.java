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
    private static InstituicaoDAO instituicaoDAO;
    private static Instituicao instituicao;
    private static int idValidInstituicao = 1, idUpdatableInstituicao = 2,
            idInvalidInstituicao = -1, idValidEvento = 1, idInvalidEvento = -1;

    public InstituicaoDAOTest() {
        setUpClass();
    }

    @BeforeAll
    public static void setUpClass() {
        db = DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
        instituicaoDAO = new InstituicaoDAO();
        instituicaoDAO.setConnection(db.getConnection());
    }

    @AfterAll
    public static void tearDownClass() {
        db.desconectar(db.getConnection());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (instituicao != null && instituicao.getIdInstituicao() != null) {
            instituicaoDAO.desvincularColaborador(instituicao.getIdInstituicao(), idValidEvento);
            instituicaoDAO.desvincularOrganizador(instituicao.getIdInstituicao(), idValidEvento);

            if (idValidInstituicao != instituicao.getIdInstituicao()
                    && idUpdatableInstituicao != instituicao.getIdInstituicao()) {
                instituicaoDAO.removerInstituicao(instituicao);
            }
        }
    }

    @Test
    public void testSetConnection() {
        assertEquals(db.getConnection(), instituicaoDAO.getConn());
    }

    @Test
    public void testGetValidInstituicaoById() {
        instituicao = new Instituicao(idValidInstituicao);
        Instituicao result = instituicaoDAO.getInstituicao(instituicao).get();
        assertAll(() -> assertNotEquals(Optional.empty(), result),
                () -> assertEquals(idValidInstituicao, result.getIdInstituicao()),
                () -> assertEquals("instituicao_teste", result.getNome()));
    }

    @Test
    public void testGetValidInstituicaoByNome() {
        Instituicao result = instituicaoDAO.getInstituicao("instituicao_teste").get();
        assertAll(() -> assertNotEquals(Optional.empty(), result),
                () -> assertEquals(idValidInstituicao, result.getIdInstituicao()),
                () -> assertEquals("instituicao_teste", result.getNome()));
    }

    @Test
    public void testGetInvalidInstituicaoById() {
        instituicao = new Instituicao(idInvalidInstituicao);
        assertEquals(Optional.empty(), instituicaoDAO.getInstituicao(instituicao));
    }

    @Test
    public void testGetInvalidInstituicaoByNome() {
        assertEquals(Optional.empty(), instituicaoDAO.getInstituicao("invalid_instituicao"));
    }

    @Test
    public void testInserirValidInstituicao() throws Exception {
        instituicao = new Instituicao("new name");

        assertAll(() -> assertTrue(instituicaoDAO.inserirInstituicao(instituicao)),
                () -> assertNotNull(instituicao.getIdInstituicao()));
    }

    @Test
    public void testInserirInstituicaoSemNome() throws SQLException {
        instituicao = new Instituicao(null);
        assertFalse(instituicaoDAO.inserirInstituicao(instituicao));
    }

    /**
     * utilizando hashcode para garantir que o valor atualizado seja diferente do valor antigo
     */
    @Test
    public void testAtualizarValidInstituicao() throws SQLException {
        instituicao = new Instituicao(idUpdatableInstituicao);

        Random rand = new Random(instituicao.hashCode());
        String updateValue = "new nome" + rand.nextInt();
        instituicao.setNome(updateValue);
        assertAll(() -> assertTrue(instituicaoDAO.atualizarInstituicao(instituicao)),
                () -> assertEquals(updateValue,
                        instituicaoDAO.getInstituicao(instituicao).get().getNome()));
    }

    @Test
    public void testAtualizarInvalidInstituicao() throws SQLException {
        instituicao = new Instituicao();
        instituicao.setIdInstituicao(idInvalidInstituicao);

        assertFalse(instituicaoDAO.atualizarInstituicao(instituicao));
    }

    @Test
    public void testRemoverValidInstituicao() throws Exception {
        instituicao = new Instituicao("new_instituicao");
        instituicaoDAO.inserirInstituicao(instituicao);

        assertTrue(instituicaoDAO.removerInstituicao(instituicao));
    }

    @Test
    public void testRemoverInvalidInstituicao() throws SQLException {
        instituicao = new Instituicao("new_instituicao");
        instituicao.setIdInstituicao(idInvalidInstituicao);

        assertFalse(instituicaoDAO.removerInstituicao(instituicao));
    }

    @Test
    public void testVincularValidOrganizadorValidEvento() {
        instituicao = new Instituicao();
        instituicao.setIdInstituicao(idValidInstituicao);
        instituicao = instituicaoDAO.getInstituicao(instituicao).get();
        instituicao.setDescricaoContribuicao("descricao_teste");
        instituicao.setValorContribuicao("valor_teste");

        assertTrue(instituicaoDAO.vincularOrganizador(instituicao, idValidEvento));
    }

    /**
     * Valor da contribuicao e opcional, por isso vinculo deve ser sucedido
     */
    @Test
    public void testVincularOrganizadorSemValorContribuicao() throws SQLException {
        instituicao = new Instituicao("nova_instituicao", "nova_contribuicao", null, null);
        instituicaoDAO.inserirInstituicao(instituicao);

        assertTrue(instituicaoDAO.vincularOrganizador(instituicao, idValidEvento));
    }

    @Test
    public void testVincularOrganizadorSemDescricaoContribuicao() {
        instituicao = new Instituicao("nova_instituicao", null, "novo_valor", null);
        instituicao.setIdInstituicao(idValidInstituicao);

        assertFalse(instituicaoDAO.vincularOrganizador(instituicao, idValidEvento));
    }

    @Test
    public void testVincularValidOrganizadorInvalidEvento() {
        instituicao = new Instituicao(idValidInstituicao);
        assertFalse(instituicaoDAO.vincularOrganizador(instituicao, idInvalidEvento));
    }

    @Test
    public void testVincularInvalidOrganizadorValidEvento() {
        instituicao = new Instituicao(idInvalidInstituicao);
        assertFalse(instituicaoDAO.vincularOrganizador(instituicao, idValidEvento));
    }

    @Test
    public void testVincularInvalidOrganizadorInvalidEvento() {
        instituicao = new Instituicao(idInvalidInstituicao);
        assertFalse(instituicaoDAO.vincularOrganizador(instituicao, idInvalidEvento));
    }

    @Test
    public void testDesvincularValidOrganizadorValidEvento() {
        instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao("nova descricao");
        instituicao.setValorContribuicao("novo valor");

        instituicaoDAO.vincularOrganizador(instituicao, idValidEvento);

        assertTrue(instituicaoDAO.desvincularOrganizador(instituicao.getIdInstituicao(),
                idValidEvento));
    }

    @Test
    public void testVincularValidColaboradorValidEvento() throws SQLException {
        instituicao = new Instituicao("nova instituicao", "nova descricao", "novo valor", null);
        instituicaoDAO.inserirInstituicao(instituicao);

        assertTrue(instituicaoDAO.vincularColaborador(instituicao, idValidEvento));
    }

    @Test
    public void testVincularColaboradorSemValorContribuicao() throws SQLException {
        instituicao = new Instituicao("nova instituicao", "nova descricao", null, null);
        instituicaoDAO.inserirInstituicao(instituicao);

        assertTrue(instituicaoDAO.vincularColaborador(instituicao, idValidEvento));
    }

    @Test
    public void testeVincularColaboradorSemDescricaoContribuicao() throws SQLException {
        instituicao = new Instituicao("nova instituicao", null, "novo valor", null);
        instituicaoDAO.inserirInstituicao(instituicao);
        assertFalse(instituicaoDAO.vincularColaborador(instituicao, idValidEvento));
    }

    @Test
    public void testDesvincularValidColaboradorValidEvento() {
        instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao("nova descricao");
        instituicao.setValorContribuicao("novo valor");
        instituicaoDAO.vincularColaborador(instituicao, idValidEvento);

        assertTrue(instituicaoDAO.desvincularColaborador(instituicao.getIdInstituicao(),
                idValidEvento));
    }

    @Test
    public void testAtualizarValidOrganizadorValidEvento() {
        // criar vinculo para o teste
        String previousDescricao = "nova descricao", previousValor = null;
        instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao(previousDescricao);
        instituicao.setValorContribuicao(previousValor);
        instituicaoDAO.vincularOrganizador(instituicao, idValidEvento);

        // atualizar o model
        instituicao.setDescricaoContribuicao("updated descricao");
        instituicao.setValorContribuicao("update valor");
        boolean sucess = instituicaoDAO.atualizarOrganizador(instituicao, idValidEvento);
        assertAll(() -> assertTrue(sucess),
                () -> assertNotEquals(previousDescricao,
                        instituicaoDAO.getInstituicao(instituicao).get()
                                .getDescricaoContribuicao()),
                () -> assertNotEquals(previousValor,
                        instituicaoDAO.getInstituicao(instituicao).get().getValorContribuicao()));
    }

    @Test
    public void testAtualizarValidColaboradorValidEvento() {
        // criar vinculo para o teste
        String previousDescricao = "nova descricao", previousValor = null;
        instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao(previousDescricao);
        instituicao.setValorContribuicao(previousValor);
        instituicaoDAO.vincularColaborador(instituicao, idValidEvento);

        // atualizar o model
        instituicao.setDescricaoContribuicao("updated descricao");
        instituicao.setValorContribuicao("update valor");

        boolean sucess = instituicaoDAO.atualizarColaborador(instituicao, idValidEvento);
        assertAll(() -> assertTrue(sucess),
                () -> assertNotEquals(previousDescricao,
                        instituicaoDAO.getInstituicao(instituicao).get()
                                .getDescricaoContribuicao()),
                () -> assertNotEquals(previousValor,
                        instituicaoDAO.getInstituicao(instituicao).get().getValorContribuicao()));
    }

    @Test
    public void testAtualizarValidColaboradorValidEventoFalhandoDebug() {
        // criar vinculo para o teste
        String previousDescricao = "nova descricao", previousValor = null;
        instituicao = new Instituicao(idValidInstituicao);
        instituicao.setDescricaoContribuicao(previousDescricao);
        instituicao.setValorContribuicao(previousValor);
        instituicaoDAO.vincularColaborador(instituicao, idValidEvento);

        // atualizar o model
        instituicao.setDescricaoContribuicao("updated descricao");
        instituicao.setValorContribuicao("update valor");

        boolean sucess = instituicaoDAO.atualizarColaborador(instituicao, idValidEvento);
        assertAll(() -> assertFalse(sucess),
                () -> assertNotEquals(previousDescricao,
                        instituicaoDAO.getInstituicao(instituicao).get()
                                .getDescricaoContribuicao()),
                () -> assertNotEquals(previousValor,
                        instituicaoDAO.getInstituicao(instituicao).get().getValorContribuicao()));
    }
}
