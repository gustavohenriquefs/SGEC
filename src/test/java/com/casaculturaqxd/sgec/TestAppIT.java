package com.casaculturaqxd.sgec;

import org.junit.jupiter.api.Test;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;

/**
 *
 * @author gusta
 */
public class TestAppIT {
    
    public TestAppIT() {
    }

    /**
     * Test of start method, of class App.
     */
    @Test
    public void testStart() throws Exception {
        DatabasePostgres db;
        db = DatabasePostgres.getInstance("URL_TEST", "USER_NAME_TEST", "PASSWORD_TEST");
    }
    
}
