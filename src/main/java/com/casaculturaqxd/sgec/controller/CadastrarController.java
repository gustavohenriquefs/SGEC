package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import com.casaculturaqxd.sgec.App;
import javafx.fxml.FXML;

public class CadastrarController {

    public void initialize() {

    }

    @FXML
    public void back() {
        try {
            App.setRoot("view/login");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
