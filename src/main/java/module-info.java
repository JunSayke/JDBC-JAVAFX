module com.example.jdbcjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.jdbcjavafx to javafx.fxml;
    exports com.example.jdbcjavafx;
}