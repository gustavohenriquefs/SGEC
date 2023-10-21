package com.casaculturaqxd.sgec.DAO;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        if(local != null && local.getIdLocalizacao()!=1 ){
            localizacaoDAO.deletarLocalizacao(local);
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
        local = new Localizacao("rua_teste","cidade_teste","estado_teste","pais_teste");
        
        assertAll(
            ()-> assertTrue(localizacaoDAO.inserirLocalizacao(local)),
            ()-> assertNotEquals(0,local.getIdLocalizacao())
        );
    }

    @Test
    public void testInserirLocalizacaoSemRua() {

        assertAll(
            ()-> assertTrue(localizacaoDAO.inserirLocalizacao(local)),
            ()-> assertNotEquals(0,local.getIdLocalizacao())
        );
    }

    @Test
    public void testInserirLocalizacaoSemCidade() {

        assertAll(
            ()-> assertTrue(localizacaoDAO.inserirLocalizacao(local)),
            ()-> assertNotEquals(0,local.getIdLocalizacao())
        );
    }

    @Test
    public void testInserirLocalizacaoSemEstado() {

        assertAll(
            ()-> assertTrue(localizacaoDAO.inserirLocalizacao(local)),
            ()-> assertNotEquals(0,local.getIdLocalizacao())
        );
    }

    @Test
    public void testInserirLocalizacaoSemPais() {

        assertAll(
            ()-> assertTrue(localizacaoDAO.inserirLocalizacao(local)),
            ()-> assertNotEquals(0,local.getIdLocalizacao())
        );
    }

    @Test
    public void testGetValidLocalizacao() {

    }

    @Test
    public void testGetInvalidLocalizacao() {

    }
    
    @Test
    public void testVincularValidLocalValidEvento() {

    }

    @Test 
    public void testVincularValidLocalInvalidEvento() {

    }

    @Test
    public void testVincularInvalidLocalValidEvento() {

    }

    @Test
    public void testVincularInvalidLocalInvalidEvento() {

    }

    @Test
    public void testDesvincularValidLocalizacaoValidEvento() {

    }

    @Test
    public void testDesvincularValidLocalizacaoInvalidEvento() {

    }

    @Test
    public void testDesvincularInvalidLocalizacaoValidEvento() {

    }
    
    @Test
    public void testDesvincularInvalidLocalizacaoInvalidEvento(){

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
