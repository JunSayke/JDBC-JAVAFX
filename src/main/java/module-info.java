module com.example.jdbcjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.java;

    opens com.example.jdbcjavafx to javafx.fxml;
    exports com.example.jdbcjavafx;
}