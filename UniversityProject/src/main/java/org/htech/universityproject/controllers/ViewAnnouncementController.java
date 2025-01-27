package org.htech.universityproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewAnnouncementController extends Navigation implements Initializable {

    @FXML
    private ListView<HBox> announcementsListView;

    @FXML
    private Button backBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAnnouncements();
        backBtn.setOnAction(event -> backAction());
    }

    private void loadAnnouncements() {
        List<HBox> announcementItems = new ArrayList<>();

        String sql = "SELECT DISTINCT a.announcement_id, a.title, a.content, a.timestamp, " +
                "u.username, u.profile_picture, a.image " +
                "FROM announcements a " +
                "JOIN users u ON a.sender_id = u.user_id " +
                "LEFT JOIN announcement_user au ON a.announcement_id = au.announcement_id ";

        boolean currentUserRole = SessionManager.isCurrentUserStudent();
        int currentUserId = SessionManager.getCurrentUserId();

        if (currentUserRole) {
            sql += "WHERE a.target_type = 'user' AND au.user_id = ?";
        } else {
            sql += "WHERE (a.sender_id = ? OR (a.target_type != 'user' AND au.user_id = ?))";
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currentUserId);

            if (!currentUserRole) {
                statement.setInt(2, currentUserId);
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                Timestamp timestamp = resultSet.getTimestamp("timestamp");
                String username = resultSet.getString("username");
                byte[] profilePicBytes = resultSet.getBytes("profile_picture");
                byte[] announcementImageBytes = resultSet.getBytes("image");

                HBox announcementHBox = new HBox(10);
//            announcementHBox.getStyleClass().add("announcement-hbox");

                ImageView profileImageView = new ImageView();
                profileImageView.setFitHeight(40);
                profileImageView.setFitWidth(40);
                if (profilePicBytes != null) {
                    profileImageView.setImage(new Image(new ByteArrayInputStream(profilePicBytes)));
                } else {
                    profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/default-profile.png"))));
                }

                ImageView announcementImageView = new ImageView();
                if (announcementImageBytes != null) {
                    Image announcementImage = new Image(new ByteArrayInputStream(announcementImageBytes));
                    announcementImageView.setImage(announcementImage);
                    announcementImageView.setFitHeight(120);
                    announcementImageView.setFitWidth(120);
                    announcementImageView.setPreserveRatio(true);
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

                announcementHBox.getChildren().addAll(profileImageView, announcementImageView, infoVBox);
                announcementItems.add(announcementHBox);
            }

            announcementsListView.getItems().addAll(announcementItems);

        } catch (SQLException e) {
            e.printStackTrace();
            UtilityMethods.showPopupWarning("Failed to load announcements!");
        }
    }


    @FXML
   public void backAction(){
        UtilityMethods.switchToScene(backBtn,"announcements");
    }


}
