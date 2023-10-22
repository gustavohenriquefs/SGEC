package com.casaculturaqxd.sgec.DAO;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Localizacao;

public class LocalizacaoDAOTest {
    private static DatabasePostgres db = DatabasePostgres.getInstance("URL_TEST","USER_NAME_TEST","PASSWORD_TEST");
    private static LocalizacaoDAO localizacaoDAO;
    private static Localizacao local;
    private static int validIdLocal = 1,validIdEvento = 1, invalidIdLocal= -1,invalidIdEvento = -1;

    @BeforeAll
    public static void setUpClass(){
        localizacaoDAO = new LocalizacaoDAO();
        localizacaoDAO.setConnection(db.getConnection());
    }

    @AfterAll
    public static void tearDownClass(){
        db.desconectar(db.getConnection());
    }

    @AfterEach
    public void tearDown(){
        //remover do banco o objeto usado no teste
        //mantendo o registro conhecido
        if(local != null ){
            //desvinculando o local do evento utilizado
            localizacaoDAO.desvincularEvento(local.getIdLocalizacao(), validIdEvento);

            if(local.getIdLocalizacao() != validIdLocal){
            localizacaoDAO.deletarLocalizacao(local);
            }
        }
    }
    
    @Test
    public void testGetConnection() {
        assertNotNull(localizacaoDAO.getConnection());
    }

    @Test
    public void testSetConnection() {
        assertEquals(db.getConnection(),localizacaoDAO.getConnection());
    }


    @Test
    public void testInserirValidLocalizacao() {
        local = new Localizacao("local_teste", "new_rua_teste","new_cidade_teste","new_estado_teste","new_pais_teste");

        assertAll(
            ()-> assertTrue(localizacaoDAO.inserirLocalizacao(local)),
            ()-> assertNotEquals(0,local.getIdLocalizacao())
        );
    }
    @Test
    public void testInserirLocalizacaoSemNome() {
        local = new Localizacao(null, "rua_teste","new_cidade_teste", "new_estado_teste","new_pais_teste");
        assertAll(
            ()-> assertFalse(localizacaoDAO.inserirLocalizacao(local)),
            ()-> assertEquals(0,local.getIdLocalizacao())
        );
    }

    @Test
    public void testInserirLocalizacaoSemRua() {
        local = new Localizacao("local_teste", null,"new_cidade_teste", "new_estado_teste","new_pais_teste");
        assertAll(
            ()-> assertFalse(localizacaoDAO.inserirLocalizacao(local)),
            ()-> assertEquals(0,local.getIdLocalizacao())
        );
    }

    @Test
    public void testInserirLocalizacaoSemCidade() {
        local = new Localizacao("local_teste","new_rua_teste",null, "new_estado_teste","new_pais_teste");
        assertAll(
            ()-> assertFalse(localizacaoDAO.inserirLocalizacao(local)),
            ()-> assertEquals(0,local.getIdLocalizacao())
        );
    }

    @Test
    public void testInserirLocalizacaoSemEstado() {
        local = new Localizacao("local_teste", "new_rua_teste","new_cidade_teste", null,"new_pais_teste");

        assertAll(
            ()-> assertFalse(localizacaoDAO.inserirLocalizacao(local)),
            ()-> assertEquals(0,local.getIdLocalizacao())
        );
    }

    @Test
    public void testInserirLocalizacaoSemPais() {
        local = new Localizacao("local_teste","new_rua_teste","new_cidade_este", "new_estado_teste",null);
        
        assertAll(
            ()-> assertFalse(localizacaoDAO.inserirLocalizacao(local)),
            ()-> assertEquals(0,local.getIdLocalizacao())
        );
    }

    @Test
    public void testGetValidLocalizacao() {
        local = new Localizacao();
        //id de local conhecido do banco
        local.setIdLocalizacao(validIdLocal);

        Localizacao result = localizacaoDAO.getLocalizacao(local);
        assertAll(
            ()-> assertEquals(1,result.getIdLocalizacao()),
            ()-> assertEquals("local_teste",result.getNome()),
            ()-> assertEquals("rua_teste",result.getRua()),
            ()-> assertEquals(0,result.getNumeroRua()),
            ()-> assertNull(result.getBairro()),
            ()-> assertEquals("cidade_teste",result.getCidade()),
            ()-> assertNull(result.getCep()),
            ()-> assertEquals("pais_teste",result.getPais())
        );
    }

    @Test
    public void testGetInvalidLocalizacao() {
        local = new Localizacao();
        //utilizando 
        local.setIdLocalizacao(invalidIdLocal);

        Localizacao result = localizacaoDAO.getLocalizacao(local);
        assertNull(result);
    }
    
    @Test
    public void testVincularValidLocalValidEvento() {
        local = new Localizacao();
        local.setIdLocalizacao(validIdLocal);

        assertTrue(localizacaoDAO.vincularEvento(local.getIdLocalizacao(), validIdEvento));
    }

    @Test 
    public void testVincularValidLocalInvalidEvento() {
        local = new Localizacao();
        local.setIdLocalizacao(validIdLocal);

        assertFalse(localizacaoDAO.vincularEvento(local.getIdLocalizacao(), invalidIdEvento));
    }

    @Test
    public void testVincularInvalidLocalValidEvento() {
        local = new Localizacao();
        local.setIdLocalizacao(invalidIdLocal);

        assertFalse(localizacaoDAO.vincularEvento(local.getIdLocalizacao(), validIdEvento));
    }

    @Test
    public void testVincularInvalidLocalInvalidEvento() {
        local = new Localizacao();
        local.setIdLocalizacao(invalidIdLocal);

        assertFalse(localizacaoDAO.vincularEvento(local.getIdLocalizacao(), invalidIdEvento));
    }

    @Test
    public void testDesvincularValidLocalizacaoValidEvento() {
        local = new Localizacao();
        local.setIdLocalizacao(validIdLocal);

        localizacaoDAO.vincularEvento(local.getIdLocalizacao(), validIdEvento);

        assertTrue(localizacaoDAO.desvincularEvento(local.getIdLocalizacao(), validIdEvento));
    }

    @Test
    public void testDesvincularValidLocalizacaoInvalidEvento() {
        local = new Localizacao();
        local.setIdLocalizacao(validIdLocal);

        localizacaoDAO.vincularEvento(local.getIdLocalizacao(), invalidIdEvento);

        assertFalse(localizacaoDAO.desvincularEvento(local.getIdLocalizacao(), invalidIdEvento));
    }

    @Test
    public void testDesvincularInvalidLocalizacaoValidEvento() {
        local = new Localizacao();
        local.setIdLocalizacao(invalidIdLocal);

        localizacaoDAO.vincularEvento(local.getIdLocalizacao(), validIdEvento);

        assertFalse(localizacaoDAO.desvincularEvento(local.getIdLocalizacao(), validIdEvento));
    }
    
    @Test
    public void testDesvincularInvalidLocalizacaoInvalidEvento(){
        local = new Localizacao();
        local.setIdLocalizacao(invalidIdLocal);

        localizacaoDAO.vincularEvento(local.getIdLocalizacao(), invalidIdEvento);

        assertFalse(localizacaoDAO.desvincularEvento(local.getIdLocalizacao(), invalidIdEvento));
    }

    @Test
    public void testUpdateValidLocalizacao() {

    }
    
    @Test
    public void testUpdateInvalidLocalizacao() {
        
    }

    @Test
    public void testDeletarValidLocalizacao(){

    }

    @Test
    public void testDeletarInvalidLocalizacao() {

    }
}
