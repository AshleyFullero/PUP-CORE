package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddAnnouncementController extends Navigation {

    @FXML
    private TextField titleField;

    @FXML
    private TextField searchField;

    @FXML
    private Button addMemberBtn;

    @FXML
    private HBox memberHBox;

    @FXML
    private Button sendBtn;

    @FXML
    private TextArea announcementTextArea;

    @FXML
    private Button chooseImageBtn;

    private final List<Integer> selectedUserIds = new ArrayList<>();

    private File selectedImage;

    @FXML
    void handleSearch(ActionEvent event) {
        String query = searchField.getText();
        if (!query.isEmpty()) {
            searchUsers(query);
        }
    }

    @FXML
    void handleChooseImage() {
        selectedImage = UtilityMethods.handleChooseImage(chooseImageBtn);
    }

    @FXML
    public void backAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "announcements");
    }

    private void searchUsers(String query) {
        String sql = "SELECT user_id, username, profile_picture FROM users WHERE username LIKE ? and username!= ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + query + "%");
            statement.setString(2, SessionManager.getCurrentUserName());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                byte[] profilePicture = resultSet.getBytes("profile_picture");

                if (!selectedUserIds.contains(userId)) {
                    VBox userCard = createUserCard(userId, username, profilePicture);
                    memberHBox.getChildren().add(userCard);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createUserCard(int userId, String username, byte[] profilePictureBytes) {
        VBox userCard = new VBox();
        userCard.setSpacing(5);
        userCard.setPrefWidth(80);
        userCard.setPrefHeight(80);
        userCard.setStyle("-fx-alignment: center; -fx-background-color: #f8f8f8; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10;");

        ImageView profilePicture = new ImageView();
        profilePicture.setFitWidth(40);
        profilePicture.setFitHeight(40);
        profilePicture.setSmooth(true);
        Circle clip = new Circle(20);
        clip.setCenterX(20);
        clip.setCenterY(20);
        profilePicture.setClip(clip);
        if (profilePictureBytes != null) {
            profilePicture.setImage(new Image(new ByteArrayInputStream(profilePictureBytes)));
        } else {
            profilePicture.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/default-profile.png"))));
        }

        Label nameLabel = new Label(username);
        nameLabel.setStyle("-fx-font-size: 8px;");

        userCard.setOnContextMenuRequested(event -> handleRemoveUser(event, userId, userCard));
        userCard.getChildren().addAll(profilePicture, nameLabel);
        selectedUserIds.add(userId);

        return userCard;
    }

    private void handleRemoveUser(ContextMenuEvent event, int userId, VBox userCard) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem("Remove");
        removeItem.setOnAction(e -> {
            selectedUserIds.remove((Integer) userId);
            memberHBox.getChildren().remove(userCard);
        });
        contextMenu.getItems().add(removeItem);
        contextMenu.show(userCard, event.getScreenX(), event.getScreenY());
    }

    @FXML
    void handleSendAnnouncement(ActionEvent event) {
        if (SessionManager.isCurrentUserStudent()) {
            UtilityMethods.showPopupWarning("Only professors can send announcements!");
            return;
        }

        String title = titleField.getText();
        if (title.isEmpty() || selectedUserIds.isEmpty()) {
            UtilityMethods.showPopupWarning("Please provide a title and select at least one user.");
            return;
        }

        int senderId = SessionManager.getCurrentUserId();
        String content = announcementTextArea.getText();
        String targetType = "user";

        // Insert the announcement into the announcements table
        int announcementId = insertAnnouncement(senderId, title, content, targetType, selectedImage);

        // Insert each user into the announcement_user table
        for (int userId : selectedUserIds) {
            insertAnnouncementUser(announcementId, userId);
        }

        clearFields();
        UtilityMethods.switchToScene(sendBtn, "announcements");
    }

    private void clearFields() {
        titleField.setText("");
        searchField.setText("");
        announcementTextArea.setText("");
        memberHBox.getChildren().clear();
        selectedUserIds.clear();
        selectedImage = null;
    }

    private int insertAnnouncement(int senderId, String title, String content, String targetType, File selectedImage) {
        String sql = "INSERT INTO announcements (sender_id, target_type, title, content, image) " +
                "VALUES (?, ?, ?, ?, ?)";
        int announcementId = -1;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, senderId);
            statement.setString(2, targetType);
            statement.setString(3, title);
            statement.setString(4, content);
            if (selectedImage != null) {
                FileInputStream fis = new FileInputStream(selectedImage);
                statement.setBinaryStream(5, fis, (int) selectedImage.length());
            } else {
                statement.setNull(5, java.sql.Types.BLOB);
            }

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                announcementId = generatedKeys.getInt(1);
            }

        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return announcementId;
    }

    private void insertAnnouncementUser(int announcementId, int userId) {
        String sql = "INSERT INTO announcement_user (announcement_id, user_id) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, announcementId);
            statement.setInt(2, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
