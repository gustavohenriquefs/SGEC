/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.casaculturaqxd.sgec;

import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import java.util.Stack;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 *
 * @author gusta
 */
public class AppIT {
    
    public AppIT() {
    }

    /**
     * Test of start method, of class App.
     */
    @Test
    public void testStart() throws Exception {
        DatabasePostgres db;
        db = DatabasePostgres.getInstance("URL_TESTE", "USER_NAME_TEST", "PASSWORD_TEST");
    }
    
}
