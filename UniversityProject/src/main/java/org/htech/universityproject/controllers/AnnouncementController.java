package org.htech.universityproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import org.htech.universityproject.dao.MessageDao;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Message;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class AnnouncementController extends Navigation implements Initializable {
    @FXML
    private Button addAnnouncementBtn;
    @FXML
    private ImageView homeBtn, profileIcon;

    @FXML
    private ImageView profileBtn,searchUsersIcon;

    @FXML
    private Label announcementLbl,messageLbl;

    @FXML
    private ListView<HBox> announcementsListView, messageListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeBtn.setOnMouseClicked(event -> UtilityMethods.switchToScene(homeBtn, "student_dashboard"));
        profileIcon.setOnMouseClicked(event -> UtilityMethods.switchToScene(homeBtn, "profile"));
        profileBtn.setOnMouseClicked(event -> UtilityMethods.switchToScene(profileBtn, "profile"));
        announcementLbl.setOnMouseClicked(event -> UtilityMethods.switchToScene(announcementLbl, "view_announcements"));
        addAnnouncementBtn.setOnAction(event ->{
            if(SessionManager.isCurrentUserStudent()){
                UtilityMethods.showPopupWarning("Only professors can add announcement");
                return;
            }
            UtilityMethods.switchToScene(addAnnouncementBtn, "add_announcement");
        });
        messageLbl.setOnMouseClicked(event -> UtilityMethods.switchToScene(messageLbl, "conversation"));
        searchUsersIcon.setOnMouseClicked(event -> UtilityMethods.switchToScene(searchUsersIcon,"search_user"));
        loadAnnouncements();
        loadTopMessages();
    }

    private void loadAnnouncements() {
        List<HBox> announcementItems = new ArrayList<>();

        String sql = "SELECT DISTINCT a.announcement_id, a.title, a.content, a.timestamp, " +
                "u.username AS sender_username, u.profile_picture " +
                "FROM announcements a " +
                "JOIN users u ON a.sender_id = u.user_id " +
                "LEFT JOIN announcement_user au ON a.announcement_id = au.announcement_id " +
                "WHERE (a.sender_id = ? " +
                "OR (a.target_type = 'user' AND au.user_id = ?)) " +
                "OR (a.target_type != 'user' AND au.user_id = ?)";

        int currentUserId = SessionManager.getCurrentUserId();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currentUserId);
            statement.setInt(2, currentUserId);
            statement.setInt(3, currentUserId);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No announcements found for this user.");
            }

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                Timestamp timestamp = resultSet.getTimestamp("timestamp");
                String username = resultSet.getString("sender_username");
                byte[] profilePicBytes = resultSet.getBytes("profile_picture");

                HBox announcementHBox = new HBox(10);

                ImageView profileImageView = new ImageView();
                profileImageView.setFitHeight(40);
                profileImageView.setFitWidth(40);
                if (profilePicBytes != null) {
                    profileImageView.setImage(new Image(new ByteArrayInputStream(profilePicBytes)));
                } else {
                    profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/default-profile.png"))));
                }

                VBox infoVBox = new VBox(5);
                Label titleLabel = new Label(title);
                titleLabel.setWrapText(true);
                titleLabel.getStyleClass().add("announcement-title");

                Text contentText = new Text(content);
                contentText.getStyleClass().add("announcement-content");

                Label timestampLabel = new Label(timestamp.toString());
                timestampLabel.getStyleClass().add("announcement-timestamp");

                infoVBox.getChildren().addAll(titleLabel, contentText, timestampLabel);

                announcementHBox.getChildren().addAll(profileImageView, infoVBox);
                announcementItems.add(announcementHBox);
            }

            announcementsListView.getItems().clear();
            announcementsListView.getItems().addAll(announcementItems);

        } catch (SQLException e) {
            e.printStackTrace();
            UtilityMethods.showPopupWarning("Failed to load announcements!");
        }
    }


    private void loadTopMessages() {
        MessageDao dao = new MessageDao();
        Queue<Message> messages = dao.getMessages();
        HBox postContainer;
        for (Message message : messages) {
            postContainer = createMessageCard(message);
            postContainer.setMaxWidth(440);
            postContainer.setUserData(message);
            messageListView.getItems().add(postContainer);
        }

        messageListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                HBox box = messageListView.getSelectionModel().getSelectedItem();
                if (box != null) {
                    Message message = (Message) box.getUserData();
                    openUserChat(message.getReceiverId());
                }
            }
        });
    }

    private HBox createMessageCard(Message message) {

        HBox messageHBox = new HBox(10);
        messageHBox.getStyleClass().add("message-hbox");

        ImageView profileImageView = new ImageView();
        profileImageView.setFitHeight(40);
        profileImageView.setFitWidth(40);
        if (message.getProfilePicBytes() != null) {
            profileImageView.setImage(new Image(new ByteArrayInputStream(message.getProfilePicBytes())));
        } else {
            profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/default-profile.png"))));
        }
        profileImageView.setStyle("-fx-background-radius: 20px;");

        VBox infoVBox = new VBox(5);
        Label usernameLabel = new Label(message.getSenderUsername());
        usernameLabel.getStyleClass().add("message-username");

        Text messageText = new Text(message.getContent());
        messageText.getStyleClass().add("message-content");

        Label timestampLabel = new Label(message.getTimestamp().toString());
        timestampLabel.getStyleClass().add("message-timestamp");

        infoVBox.getChildren().addAll(usernameLabel, messageText, timestampLabel);

        messageHBox.getChildren().addAll(profileImageView, infoVBox);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem openChatItem = new MenuItem("Open Chat");
        openChatItem.setOnAction(event -> openUserChat(message.getReceiverId()));
        contextMenu.getItems().add(openChatItem);

        messageHBox.setOnContextMenuRequested(event -> contextMenu.show(messageHBox, event.getScreenX(), event.getScreenY()));

        return messageHBox;
    }

    private void openUserChat(int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/chat_message.fxml"));
            UtilityMethods.setupStage(homeBtn, userId, loader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
