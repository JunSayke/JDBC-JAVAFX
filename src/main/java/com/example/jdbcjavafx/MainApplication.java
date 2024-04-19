package com.example.jdbcjavafx;

import com.example.jdbcjavafx.mysqlconnection.MySqlConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
//        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("JDBC - JAVAFX");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        MySqlConnection.getInstance().createdTable();
        launch();
    }
}