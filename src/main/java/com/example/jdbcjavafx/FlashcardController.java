package com.example.jdbcjavafx;

import com.example.jdbcjavafx.datas.FlashcardData;
import com.example.jdbcjavafx.sqlquery.FlashcardTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.List;

public class FlashcardController {
    @FXML
    public AnchorPane pnCreateFlashcard;
    @FXML
    public TextArea taFront;
    @FXML
    public Button btnCreateFlashcard;
    @FXML
    public TextArea taBack;
    @FXML
    public TableView<FlashcardData> tblFlashcards;
    @FXML
    public TableColumn<FlashcardData, String> tblFlashcard;
    @FXML
    public TableColumn<FlashcardData, Void> tblAction;
    @FXML
    public AnchorPane pnViewFlashcard;
    @FXML
    public AnchorPane pnFlashcardList;
    @FXML
    public Button btnUpdateFlashcard;
    @FXML
    public Label lblFlashcardId;
    @FXML
    public Button btnReturn;
    @FXML
    public AnchorPane pnFlashcardReviewer;
    @FXML
    public Label lblFront;
    @FXML
    public Label lblBack;
    private static List<FlashcardData> flashcardDataList;
    private static int flashcardDataListIndex = -1;
    @FXML
    protected void initialize() {
        if (pnFlashcardReviewer != null) {
            flashcardDataList = FlashcardTable.getInstance().getUserFlashcards(
                    SessionManager.getInstance().getUserData().getId()
            );
        }

        if (tblFlashcards != null) {
            tblFlashcard.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFront()));

            tblAction.setCellFactory(param -> new TableCell<>() {
                private final Button viewButton = new Button("View");
                private final Button deleteButton = new Button("Delete");
                {
                    viewButton.getStyleClass().add("btn-primary");
                    viewButton.setOnAction(event -> {
                        FlashcardData flashcard = getTableView().getItems().get(getIndex());
                        handleViewAction(flashcard);
                    });

                    deleteButton.getStyleClass().add("btn-danger");
                    deleteButton.setOnAction(event -> {
                        FlashcardData flashcard = getTableView().getItems().get(getIndex());
                        handleDeleteAction(flashcard);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox buttons = new HBox(10);
                        buttons.setAlignment(Pos.CENTER);
                        buttons.getChildren().addAll(viewButton, deleteButton);
                        setGraphic(buttons);
                    }
                }

                private void handleViewAction(FlashcardData flashcard) {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("view_flashcard.fxml"));
                        Label flashcardId = (Label) root.lookup("#lblFlashcardId");
                        TextArea front = (TextArea) root.lookup("#taFront");
                        TextArea back = (TextArea) root.lookup("#taBack");
                        flashcardId.setText(String.valueOf(flashcard.getId()));
                        front.setText(flashcard.getFront());
                        back.setText(flashcard.getBack());
                        pnFlashcardList.getScene().setRoot(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                private void handleDeleteAction(FlashcardData flashcard) {
                    boolean success = FlashcardTable.getInstance().deleteFlashcard(flashcard.getId());
                    if (success) {
                        feedbackMsgBox(Alert.AlertType.INFORMATION, "Flashcard deleted!");
                        tblFlashcards.getItems().remove(flashcard);
                    } else {
                        feedbackMsgBox(Alert.AlertType.ERROR, "Failed to delete flashcard!");
                    }
                }
            });

            List<FlashcardData> flashcardDataList = FlashcardTable.getInstance().getUserFlashcards(SessionManager.getInstance().getUserData().getId());
            tblFlashcards.getItems().addAll(flashcardDataList);
        }
    }

    @FXML
    protected void onNextButtonClick() {
        if (++flashcardDataListIndex >= flashcardDataList.size()) {
            feedbackMsgBox(Alert.AlertType.ERROR, "No more flashcards!");
            flashcardDataListIndex = flashcardDataList.size() - 1;
        } else {
            FlashcardData flashcardData = flashcardDataList.get(flashcardDataListIndex);
            lblFront.setText(flashcardData.getFront());
            lblBack.setText(flashcardData.getBack());
        }
    }

    @FXML
    protected void onPreviousButtonClick() {
        if (--flashcardDataListIndex < 0) {
            feedbackMsgBox(Alert.AlertType.ERROR, "No more flashcards!");
            flashcardDataListIndex = -1;
        } else {
            FlashcardData flashcardData = flashcardDataList.get(flashcardDataListIndex);
            lblFront.setText(flashcardData.getFront());
            lblBack.setText(flashcardData.getBack());
        }
    }

    @FXML
    protected void toggleFlashcardFrontBack() {
        if (lblFront.isVisible()) {
            lblFront.setVisible(false);
            lblBack.setVisible(true);
        } else {
            lblFront.setVisible(true);
            lblBack.setVisible(false);
        }
    }


    @FXML
    protected void onUpdateFlashcardButtonClick() {
        int flashcardId = Integer.parseInt(lblFlashcardId.getText());
        String front = taFront.getText();
        String back = taBack.getText();

        boolean success = FlashcardTable.getInstance().updateFlashcard(
                flashcardId,
                front,
                back
        );

        if (success) {
            feedbackMsgBox(Alert.AlertType.INFORMATION, "Flashcard updated!");
        } else {
            feedbackMsgBox(Alert.AlertType.ERROR, "Failed to update flashcard!");
        }
    }

    @FXML
    protected void onReturnButtonClick() throws IOException {
        Stage stage = (Stage) btnReturn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("flashcard_list.fxml"));
        stage.getScene().setRoot(root);
    }

    @FXML
    protected void onCreateFlashcardButtonClick() {
        String front = taFront.getText();
        String back = taBack.getText();

        int flashcardId = FlashcardTable.getInstance().insertUserFlashcard(
                SessionManager.getInstance().getUserData().getId(),
                front,
                back
        );

        if (flashcardId != -1) {
            feedbackMsgBox(Alert.AlertType.INFORMATION, "Flashcard created!");
        } else {
            feedbackMsgBox(Alert.AlertType.ERROR, "Creating flashcard failed!");
        }
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

        Stage owner = null;
        if (pnCreateFlashcard != null)
            owner = (Stage) (pnCreateFlashcard.getScene().getWindow());
        else if (pnViewFlashcard != null)
            owner = (Stage) (pnViewFlashcard.getScene().getWindow());

        alert.initOwner(owner);
        alert.initStyle(StageStyle.UNDECORATED);;
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(root);
        alert.getDialogPane().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        alert.showAndWait();
    }
}
