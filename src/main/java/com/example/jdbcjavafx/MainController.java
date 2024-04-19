package com.example.jdbcjavafx;

import com.example.jdbcjavafx.datas.UserData;
import com.example.jdbcjavafx.mysqlconnection.MySqlConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class MainController {
    @FXML
    public AnchorPane pnHome, pnLogin, pnRegister;
    public PasswordField tfPassword;
    public TextField tfUsername;
    public Button btnRegister;
    public Button btnShowPassword;
    public Label btnToLogin;
    public Button btnLogin;
    public Label lblUsername;
    public Label btnToRegister;
    public Button btnLogout;
    public Button btnDeleteAccount;
    public Button btnUpdateProfile;
    public Label lblFeedback;

    private static UserData userData;

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
        MainController.userData = null;
        AnchorPane p = (AnchorPane) pnHome.getParent();
        Parent scene = FXMLLoader.load(getClass().getResource("login.fxml"));
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

    @FXML
    protected void onLoginButtonClick() throws IOException {
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        UserData userData = MySqlConnection.getInstance().loginUser(username, password);
        if (userData != null) {
            MainController.userData = userData;
            AnchorPane p = (AnchorPane) pnLogin.getParent();
            Parent scene = FXMLLoader.load(getClass().getResource("home.fxml"));
            ((Label) scene.lookup("#lblUsername")).setText(userData.getUsername());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } else {
            lblFeedback.setTextFill(Color.RED);
            lblFeedback.setText("Incorrect Credentials!");
        }
    }

    @FXML
    protected void onRegisterButtonClick() {
        boolean success = MySqlConnection.getInstance().registerUser(tfUsername.getText(), tfPassword.getText());
        if (success) {
            lblFeedback.setTextFill(Color.GREEN);
            lblFeedback.setText("Registered Successfully!");
        } else {
            lblFeedback.setTextFill(Color.RED);
            lblFeedback.setText("Registration Failed!");
        }
    }

    @FXML
    protected void onDeleteAccountButtonClick() throws IOException {
        boolean success = MySqlConnection.getInstance().deleteAccount(lblUsername.getText());
        if (success) {
            onLogoutButtonClick();
        } else {
            lblFeedback.setTextFill(Color.RED);
            lblFeedback.setText("Deletion of Account Failed!");
        }
    }

    @FXML
    protected void onUpdateAccountButtonClick() {
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        UserData userData = MySqlConnection.getInstance().updateAccount(MainController.userData.getId(),
                username.isEmpty() ? null : username,
                password.isEmpty() ? null : password);
        if (userData != null) {
            MainController.userData = userData;
            lblUsername.setText(userData.getUsername());
            lblFeedback.setTextFill(Color.GREEN);
            lblFeedback.setText("Successfully updated user profile!");
        } else {
            lblFeedback.setTextFill(Color.RED);
            lblFeedback.setText("Failed to update user profile!");
        }
    }
}