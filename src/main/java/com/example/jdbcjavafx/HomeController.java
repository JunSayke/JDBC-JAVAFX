package com.example.jdbcjavafx;

import com.example.jdbcjavafx.datas.UserData;
import com.example.jdbcjavafx.sqlquery.UserTable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class HomeController {
    @FXML
    public Button btnLogout;
    @FXML
    public Button btnDeleteAccount;
    @FXML
    public Button btnUpdateProfile;
    @FXML
    public AnchorPane pnHome;
    @FXML
    public AnchorPane pnProfile;
    @FXML
    public TextField tfUsername;
    @FXML
    public TextField tfUnmaskPassword;
    @FXML
    public PasswordField tfMaskPassword;
    @FXML
    public Button btnShowPassword;
    @FXML
    public Label lblFeedback;
    @FXML
    public Label lblUsername;
    @FXML
    public Button btnShowEditProfile;
    @FXML
    public Button btnNewFlashcard;
    @FXML
    public Button btnReviewFlashcard;
    @FXML
    public Button btnShowFlashcards;

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
    protected void onLogoutButtonClick() throws IOException {
        SessionManager.getInstance().setUserData(null);
        pnHome.getScene().setRoot(FXMLLoader.load(getClass().getResource("login.fxml")));
    }

    @FXML
    protected void onDeleteAccountButtonClick() throws IOException {
        boolean success = UserTable.getInstance().deleteAccount(SessionManager.getInstance().getUserData().getId());
        if (success) {
            feedbackMsgBox(Alert.AlertType.INFORMATION, "Account deleted!");
            onLogoutButtonClick();
        } else {
            feedbackMsgBox(Alert.AlertType.ERROR, "Failed to delete account!");
        }
    }

    @FXML
    protected void onUpdateProfileButtonClick() {
        String username = tfUsername.getText();
        String password = tfMaskPassword.getText();
        UserData userData = UserTable.getInstance().updateAccount(SessionManager.getInstance().getUserData().getId(),
                username.isEmpty() ? null : username,
                password.isEmpty() ? null : password);
        if (userData != null) {
            feedbackMsgBox(Alert.AlertType.INFORMATION, "Account updated!");
            SessionManager.getInstance().setUserData(userData);
        } else {
            feedbackMsgBox(Alert.AlertType.ERROR, "Failed to update account!");
        }
    }

    @FXML
    protected void initialize() {
        if (lblUsername != null)
            lblUsername.setText(SessionManager.getInstance().getUserData().getUsername());

        Platform.runLater(() -> {
            if (pnHome != null) {
                Stage stage = (Stage) pnHome.getScene().getWindow();
                if (!stage.getTitle().equals("Home - Flashcard App"))
                    stage.setTitle("Home - Flashcard App");
            }
        });
    }

    @FXML
    protected void onShowEditProfileButtonClick() throws Exception {
        Stage popupStage = new Stage();
        popupStage.setResizable(false);

        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Edit Profile - Flashcard App");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_profile.fxml"));
        Parent root = loader.load();

        Scene popupScene = new Scene(root);
        popupScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        popupStage.setScene(popupScene);
        popupStage.sizeToScene();

        popupStage.setOnCloseRequest(event -> {
            initialize();
        });

        popupStage.show();
    }

    @FXML
    protected void onShowFlashcardListButtonClick() throws Exception {
        Stage popupStage = new Stage();
        popupStage.setResizable(false);

        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Your Flashcards - Flashcard App");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("flashcard_list.fxml"));
        Parent root = loader.load();

        Scene popupScene = new Scene(root);
        popupScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        popupStage.setScene(popupScene);
        popupStage.sizeToScene();

        popupStage.setOnCloseRequest(event -> {
            initialize();
        });

        popupStage.show();
    }

    @FXML
    protected void onShowNewFlashcardButtonClick() throws Exception {
        Stage popupStage = new Stage();
        popupStage.setResizable(false);

        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("New Flashcard - Flashcard App");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("create_flashcard.fxml"));
        Parent root = loader.load();

        Scene popupScene = new Scene(root);
        popupScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        popupStage.setScene(popupScene);
        popupStage.sizeToScene();

        popupStage.setOnCloseRequest(event -> {
            initialize();
        });

        popupStage.show();
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

        Stage owner = (Stage) (pnHome != null ? pnHome.getScene().getWindow() : pnProfile.getScene().getWindow());

        alert.initOwner(owner);
        alert.initStyle(StageStyle.UNDECORATED);;
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(root);
        alert.getDialogPane().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        alert.showAndWait();
    }
}
