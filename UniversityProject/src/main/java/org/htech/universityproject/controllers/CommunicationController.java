package org.htech.universityproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import org.htech.universityproject.dao.MessageDao;

import org.htech.universityproject.modal.Professor;
import org.htech.universityproject.modal.Student;
import org.htech.universityproject.modal.User;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.net.URL;
import java.util.ResourceBundle;

public class CommunicationController extends Navigation implements Initializable {

    @FXML
    public TextField messageTextField;
    @FXML
    private Button backBtn;

    @FXML
    private ListView<String> messageListView;

    @FXML
    private ImageView profileBtn;

    @FXML
    private ImageView userProfileIcon;

    @FXML
    private Label usernameLbl;

    @FXML
    private Button sendBtn;

    private int userId = SessionManager.getCurrentUserId();
    private String username = SessionManager.getCurrentUserName();
    private int receiverPersonId;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameLbl.setText(username);
        userProfileIcon.setFitWidth(40);
        userProfileIcon.setFitHeight(40);
        userProfileIcon.setSmooth(true);
        Circle clip = new Circle(20);
        clip.setCenterX(20);
        clip.setCenterY(20);
        userProfileIcon.setClip(clip);
        userProfileIcon.setImage(SessionManager.getCurrentUserImage());
        profileBtn.setOnMouseClicked(e -> UtilityMethods.switchToScene(usernameLbl, "profile"));
        messageListView.getStylesheets().add(getClass().getResource("/styles/chat.css").toExternalForm());
        messageListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox container = new HBox();
                    Label messageLabel = new Label(item);
                    if (item.startsWith("You: ")) {
                        container.setStyle("-fx-alignment: center-right;");
                        messageLabel.setStyle(
                                "-fx-background-color: #CDE4FF; " +
                                        "-fx-padding: 10; " +
                                        "-fx-border-radius: 10; " +
                                        "-fx-background-radius: 10; " +
                                        "-fx-font-size: 14px;"
                        );
                        messageLabel.getStyleClass().add("sender-bubble");
                        messageLabel.setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        container.setStyle("-fx-alignment: center-left;");
                        messageLabel.setStyle(
                                "-fx-background-color: #E4FFC9; " +
                                        "-fx-padding: 10; " +
                                        "-fx-border-radius: 10; " +
                                        "-fx-background-radius: 10; " +
                                        "-fx-font-size: 14px;"
                        );
                        messageLabel.getStyleClass().add("receiver-bubble");
                        messageLabel.setAlignment(Pos.CENTER_LEFT);
                    }
                    container.getChildren().add(messageLabel);
                    setGraphic(container);
                }
            }
        });

    }

    public void loadMessages(int receiverId) {
        receiverPersonId = receiverId;
        messageListView.getItems().clear();
        MessageDao messageDao = new MessageDao();
        messageDao.getMessages(receiverId).forEach(message -> messageListView.getItems().add(message));
    }

    @FXML
    private void handleSendMessage() {
        String content = messageTextField.getText().trim();

        if (content.isEmpty()) {
            UtilityMethods.showPopupWarning("Message cannot be empty.");
            return;
        }
        User user = SessionManager.getCurrentUser();
        if(user instanceof Student student){
            if(student.sendMessage(receiverPersonId,content)){
                messageTextField.clear();
                loadMessages(receiverPersonId);
            }
        } else if (user instanceof Professor professor) {
            if(professor.sendMessage(receiverPersonId, content)){
                messageTextField.clear();
                loadMessages(receiverPersonId);
            }
        }

    }

}
