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
    
    opens com.casaculturaqxd.sgec to javafx.fxml;

    exports com.casaculturaqxd.sgec;
    requires io.github.cdimascio.dotenv.java;

}
