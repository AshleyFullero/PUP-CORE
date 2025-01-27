package org.htech.universityproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Professor;
import org.htech.universityproject.modal.Student;
import org.htech.universityproject.modal.User;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.htech.universityproject.utilities.SessionManager.getCurrentUserId;

public class StudentDashboardController extends Navigation implements Initializable {

    @FXML
    private ListView<VBox> announcementView;

    @FXML
    private Button eBtn;

    @FXML
    private Button cBtn;

    @FXML
    private Button eventBtn,addEventBtn;

    @FXML
    private Label greetingLbl;

    @FXML
    private Button oBtn;

    @FXML
    private ImageView profileImageIcon,logoutIcon;

    @FXML
    private Button rBtn;

    @FXML
    private Label usernameLbl;

    private String username = SessionManager.getCurrentUserName();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        greetingLbl.setText(getGreeting());
        usernameLbl.setText(username);
        profileImageIcon.setFitWidth(60);
        profileImageIcon.setFitHeight(60);
        profileImageIcon.setSmooth(true);
        Circle clip = new Circle(30);
        clip.setCenterX(30);
        clip.setCenterY(30);
        profileImageIcon.setClip(clip);
        displayProfilePicture(profileImageIcon);
        SessionManager.setCurrentUserImage(profileImageIcon.getImage());
        loadAnnouncements();
        announcementView.setOnMouseClicked(event -> UtilityMethods.switchToScene(announcementView,"announcements"));
        cBtn.setOnAction(event -> UtilityMethods.switchToScene(announcementView,"announcements"));
        oBtn.setOnAction(event -> UtilityMethods.switchToScene(announcementView,"event_hub"));
        rBtn.setOnAction(event -> UtilityMethods.switchToScene(announcementView,"resources"));
        eBtn.setOnAction(event -> UtilityMethods.switchToScene(announcementView,"posts"));
        eventBtn.setOnAction(event -> UtilityMethods.switchToScene(announcementView,"event_hub"));
        addEventBtn.setOnAction(event -> UtilityMethods.switchToScene(addEventBtn,"add_event"));
        profileImageIcon.setOnMouseClicked(event -> {
            User user = SessionManager.getCurrentUser();
            if(user instanceof Student student){
                student.viewProfile(event);
            }
            else if(user instanceof Professor professor){
                professor.viewProfile(event);
            }
        });
    }

    public String getGreeting() {
        LocalTime currentTime = LocalTime.now();
        String greeting;

        if (currentTime.isBefore(LocalTime.of(6, 0))) {
            greeting = "Good Night";
        } else if (currentTime.isBefore(LocalTime.NOON)) {
            greeting = "Good Morning";
        } else if (currentTime.isBefore(LocalTime.of(18, 0))) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }
        return greeting;
    }

    public void displayProfilePicture(ImageView imageView) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT profile_picture FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, getCurrentUserId());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                byte[] imageData = resultSet.getBytes("profile_picture");
                if (imageData != null && imageData.length > 0) {
                    Image image = new Image(new ByteArrayInputStream(imageData));
                    imageView.setImage(image);
                } else {
                    loadDefaultProfilePicture(imageView);
                }
            } else {
                loadDefaultProfilePicture(imageView);
            }
        } catch (Exception e) {
            loadDefaultProfilePicture(imageView);
        }
    }


    private void loadDefaultProfilePicture(ImageView imageView) {
        try {
            Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/default-profile.png")));
            imageView.setImage(defaultImage);
        } catch (Exception e) {
            System.err.println("Error loading default profile picture: " + e.getMessage());
        }
    }


    private void loadAnnouncements() {
        List<VBox> announcementItems = new ArrayList<>();

        String sql = "SELECT DISTINCT a.announcement_id, a.title, a.content, a.timestamp, " +
                "u.username AS sender_username, u.profile_picture " +
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
                String username = resultSet.getString("sender_username");
                byte[] profilePicBytes = resultSet.getBytes("profile_picture");

                VBox announcementVBox = new VBox(10);
                announcementVBox.getStyleClass().add("announcement-vbox");

                HBox topRow = new HBox(10);
                topRow.getStyleClass().add("announcement-top-row");

                ImageView profileImageView = new ImageView();
                profileImageView.setFitHeight(40);
                profileImageView.setFitWidth(40);
                if (profilePicBytes != null) {
                    profileImageView.setImage(new Image(new ByteArrayInputStream(profilePicBytes)));
                } else {
                    profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/default-profile.png"))));
                }

                Label usernameLabel = new Label(username);
                usernameLabel.getStyleClass().add("announcement-username");

                Label titleLabel = new Label(title);
                titleLabel.getStyleClass().add("announcement-title");

                topRow.getChildren().addAll(profileImageView, usernameLabel, titleLabel);

                HBox bottomRow = new HBox(10);
                bottomRow.getStyleClass().add("announcement-bottom-row");

                Text contentText = new Text(content);
                contentText.getStyleClass().add("announcement-content");

                Label timestampLabel = new Label(timestamp.toString());
                timestampLabel.getStyleClass().add("announcement-timestamp");

                bottomRow.getChildren().addAll(contentText, timestampLabel);

                announcementVBox.getChildren().addAll(topRow, bottomRow);
                announcementItems.add(announcementVBox);
            }

            announcementView.getItems().clear();
            announcementView.getItems().addAll(announcementItems);

        } catch (SQLException e) {
            e.printStackTrace();
            UtilityMethods.showPopupWarning("Failed to load announcements!");
        }
    }


}
