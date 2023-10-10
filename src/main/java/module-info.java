module com.casaculturaqxd.sgec {
    exports com.casaculturaqxd.sgec.models;
    exports com.casaculturaqxd.sgec;
    exports com.casaculturaqxd.sgec.jdbc;
    exports com.casaculturaqxd.sgec.controller to javafx.fxml;
    exports com.casaculturaqxd.sgec.controller.componentes to javafx.fxml;
    exports com.casaculturaqxd.sgec.controller.preview to javafx.fxml;

    requires java.logging;
    requires java.sql;
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    
    opens com.casaculturaqxd.sgec to javafx.fxml;
    opens com.casaculturaqxd.sgec.controller to javafx.fxml;
    opens com.casaculturaqxd.sgec.controller.componentes to javafx.fxml;
    opens com.casaculturaqxd.sgec.controller.preview to javafx.fxml;
    requires io.github.cdimascio.dotenv.java;

}
