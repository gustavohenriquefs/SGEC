module com.casaculturaqxd.sgec {
    exports com.casaculturaqxd.sgec.models;
    exports com.casaculturaqxd.sgec;
    exports com.casaculturaqxd.sgec.jdbc;

    requires java.logging;
    requires java.sql;
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    
    opens com.casaculturaqxd.sgec.controller to javafx.fxml;
    opens com.casaculturaqxd.sgec to javafx.fxml;

    requires io.github.cdimascio.dotenv.java;

}
