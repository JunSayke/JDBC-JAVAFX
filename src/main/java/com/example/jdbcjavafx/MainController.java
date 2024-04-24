package com.example.jdbcjavafx;

import com.example.jdbcjavafx.datas.UserData;
import com.example.jdbcjavafx.sqlquery.UserTable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class MainController {
    @FXML
    public AnchorPane pnLogin, pnRegister;
    @FXML
    public TextField tfMaskPassword;
    @FXML
    public TextField tfUnmaskPassword;
    @FXML
    public TextField tfUsername;
    @FXML
    public Button btnShowPassword;
    @FXML
    public Label btnToLogin;
    @FXML
    public Button btnLogin;
    @FXML
    public Label btnToRegister;

    @FXML
    protected void onMaskPasswordInputListener() {
        tfUnmaskPassword.setText(tfMaskPassword.getText());
    }

    @FXML
    protected void onUnmaskPasswordInputListener() {
        tfMaskPassword.setText(tfUnmaskPassword.getText());
    }

    @FXML
    protected void onShowPasswordPressed() {
        tfMaskPassword.setVisible(false);
        tfUnmaskPassword.setVisible(true);
    }

    @FXML
    protected void onShowPasswordReleased() {
        tfMaskPassword.setVisible(true);
        tfUnmaskPassword.setVisible(false);
    }

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
    protected void onLoginButtonClick() throws IOException {
        String username = tfUsername.getText();
        String password = tfMaskPassword.getText();

        UserData userData = UserTable.getInstance().loginUser(username, password);
        if (userData != null) {
            Stage stage = (Stage) pnLogin.getScene().getWindow();
            stage.setTitle("Home - Flashcard App");
            feedbackMsgBox(Alert.AlertType.INFORMATION, "Login successfully!");
            SessionManager.getInstance().setUserData(userData);
            pnLogin.getScene().setRoot(FXMLLoader.load(getClass().getResource("home.fxml")));
        } else {
            feedbackMsgBox(Alert.AlertType.ERROR, "Login failed!");
        }
    }

    @FXML
    protected void initialize() {
        Platform.runLater(() -> {
            if (pnLogin != null) {
                Stage stage = (Stage) pnLogin.getScene().getWindow();
                if (!stage.getTitle().equals("Login - Flashcard App"))
                    stage.setTitle("Login - Flashcard App");
            } else if (pnRegister != null) {
                Stage stage = (Stage) pnRegister.getScene().getWindow();
                if (!stage.getTitle().equals("Register - Flashcard App"))
                    stage.setTitle("Register - Flashcard App");
            }
        });
    }

    @FXML
    protected void onRegisterButtonClick() {
        boolean success = UserTable.getInstance().registerUser(tfUsername.getText(), tfMaskPassword.getText());

        if (success)
            feedbackMsgBox(Alert.AlertType.INFORMATION, "Registration success!");
        else
            feedbackMsgBox(Alert.AlertType.ERROR, "Registration failed!");
    }

    private void feedbackMsgBox(Alert.AlertType alertType, String message) {
        TextFlow alertContainer = new TextFlow();
        Text text = new Text(message);
        alertContainer.getChildren().add(text);

        if (alertType != Alert.AlertType.INFORMATION)
            alertContainer.getStyleClass().addAll("alert", "alert-danger");
        else
            alertContainer.getStyleClass().addAll("alert", "alert-success");

        GridPane root = new GridPane();
        root.addRow(0, alertContainer);
        GridPane.setHgrow(alertContainer, Priority.ALWAYS);

        Alert alert = new Alert(alertType);

        Stage owner = (Stage) (pnLogin != null ? pnLogin.getScene().getWindow() : pnRegister.getScene().getWindow());

        alert.initOwner(owner);
        alert.initStyle(StageStyle.UNDECORATED);;
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(root);
        alert.getDialogPane().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        alert.showAndWait();
    }
}