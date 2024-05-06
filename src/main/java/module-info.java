module com.example.coursework {
    requires javafx.controls;
    requires javafx.fxml;
        requires lombok;
        requires mysql.connector.j;
        requires java.sql;
        requires org.hibernate.orm.core;
        requires jakarta.persistence;
        requires java.naming;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.coursework to javafx.fxml;
        exports com.example.coursework;
        opens com.example.coursework.fxControllers to javafx.fxml;
        exports com.example.coursework.fxControllers to javafx.fxml;
        opens com.example.coursework.fxControllers.tableParameters to javafx.fxml;
        exports com.example.coursework.fxControllers.tableParameters to javafx.fxml;
        opens com.example.coursework.model to javafx.fxml, org.hibernate.orm.core, jakarta.persistence, java.base;
        exports com.example.coursework.model to javafx.fxml, org.hibernate.orm.core, jakarta.persistence, java.base;
        }