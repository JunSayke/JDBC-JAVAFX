package com.example.jdbcjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {
    @FXML
    public AnchorPane pnHome, pnLogin, pnRegister;

    @FXML
    protected void onToRegisterButtonClick() throws IOException {
        AnchorPane p = (AnchorPane) pnLogin.getParent();
        Parent scene = FXMLLoader.load(getClass().getResource("register.fxml"));
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

    @FXML
    protected void onToLoginButtonClick() throws IOException {
        AnchorPane p = (AnchorPane) pnRegister.getParent();
        Parent scene = FXMLLoader.load(getClass().getResource("login.fxml"));
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

    @FXML
    protected void onLogoutButtonClick() throws IOException {
        AnchorPane p = (AnchorPane) pnHome.getParent();
        Parent scene = FXMLLoader.load(getClass().getResource("login.fxml"));
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

    @FXML
    protected void onToHomepageButtonClick() throws IOException {
        AnchorPane p = (AnchorPane) pnLogin.getParent();
        Parent scene = FXMLLoader.load(getClass().getResource("home.fxml"));
        p.getChildren().clear();
        p.getChildren().add(scene);
    }
}