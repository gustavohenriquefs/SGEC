module com.casaculturaqxd.sgec {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.casaculturaqxd.sgec to javafx.fxml;
    opens com.casaculturaqxd.sgec.controller to javafx.fxml;
    exports com.casaculturaqxd.sgec.controller to javafx.fxml;
    exports com.casaculturaqxd.sgec;
    requires io.github.cdimascio.dotenv.java;
}
