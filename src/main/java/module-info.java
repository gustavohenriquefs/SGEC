module com.casaculturaqxd.sgec {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.casaculturaqxd.sgec.controller to javafx.fxml;
    exports com.casaculturaqxd.sgec;
    exports com.casaculturaqxd.sgec.models;
    requires io.github.cdimascio.dotenv.java;
}
