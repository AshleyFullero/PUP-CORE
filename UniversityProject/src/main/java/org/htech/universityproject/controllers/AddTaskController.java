package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddTaskController extends Navigation implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button addMemberBtn;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField endTimeField;

    @FXML
    private RadioButton groupBtn1;

    @FXML
    private RadioButton groupBtn2;

    @FXML
    private RadioButton groupBtn3;

    @FXML
    private HBox memberHBox;

    @FXML
    private Button saveBtn;

    @FXML
    private ComboBox<String> startTimeComboBox,endTimeComboBox;

    private final List<Integer> selectedUserIds = new ArrayList<>();

    @FXML
    void handleSearch(ActionEvent event) {
        String query = searchField.getText();
        if (!query.isEmpty()) {
            searchUsers(query);
        }
    }

    private void searchUsers(String query) {
        String sql = "SELECT user_id, username, profile_picture FROM users WHERE username LIKE ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + query + "%");
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
