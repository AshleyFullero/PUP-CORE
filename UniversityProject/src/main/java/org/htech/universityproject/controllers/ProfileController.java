package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Professor;
import org.htech.universityproject.modal.Student;
import org.htech.universityproject.modal.User;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.htech.universityproject.utilities.SessionManager.getCurrentUserId;

public class ProfileController extends Navigation implements Initializable {

    @FXML
    private ListView<HBox> postListView;
    @FXML
    private TextArea aboutMeTextArea;

    @FXML
    private VBox activityBox;

    @FXML
    private ImageView userProfileIcon;

    @FXML
    private Label userYear;

    @FXML
    private Label username, statusLbl, statusLbl2;

    @FXML
    private Label activityDateLbl;

    @FXML
    private Label activityUsernameLbl;

    @FXML
    private Button awardsBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aboutMeTextArea.setText(fetchAboutMe());
        username.setText(SessionManager.getCurrentUserName());
        fetchUserData();
        userProfileIcon.setFitWidth(60);
        userProfileIcon.setFitHeight(60);
        userProfileIcon.setSmooth(true);
        Circle clip = new Circle(30);
        clip.setCenterX(30);
        clip.setCenterY(30);
        userProfileIcon.setClip(clip);
        userProfileIcon.setImage(SessionManager.getCurrentUserImage());
        activityUsernameLbl.setText(SessionManager.getCurrentUserName());
        LocalDate today = LocalDate.now();
        String formattedToday = today.getMonth() + ", " + today.getDayOfMonth();
        activityDateLbl.setText(formattedToday);
        fetchAndDisplayPosts();
    }

    private void fetchUserData() {
        if (SessionManager.isCurrentUserStudent()) {
            fetchStudentData();
        } else {
            fetchProfessorData();
        }

        if (SessionManager.isCurrentUserStudent()) {
            awardsBtn.setText("Awards, Club Recognitions, Certifications");
        } else {
            awardsBtn.setText("Research, Key Notes, Professional Accolades");
        }
    }

    private String fetchAboutMe() {
        String aboutMe = "";
        String query = "select about_me from users where user_id =?";
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, SessionManager.getCurrentUserId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                aboutMe = rs.getString("about_me");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aboutMe;
    }

    private void fetchStudentData() {
        String query = """
                SELECT c.course_name, s.year, s.level, s.active_subjects
                FROM students s
                JOIN courses c ON s.course_id = c.course_id
                WHERE s.user_id = ?
                """;
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, SessionManager.getCurrentUserId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String course = rs.getString("course_name");
                String year = rs.getString("year");
                String lvl = rs.getString("level");
                String activeSubjects = rs.getString("active_subjects");
                userYear.setText(year + "-Year");
//                statusLbl.setText("Student-" + lvl);
//                statusLbl.setStyle("-fx-text-fill: #801000");
//                statusLbl2.setText(course+"-"+activeSubjects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchProfessorData() {
        String query = """
        SELECT d.department_name, p.office_hours, p.subjects_taught AS subjects
        FROM professors p
        JOIN departments d ON p.department_id = d.department_id
        WHERE p.user_id = ?
    """;
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, SessionManager.getCurrentUserId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String departments = rs.getString("department_name");
                String office_hours = rs.getString("office_hours");
                String subjects_taught = rs.getString("subjects");
                userYear.setText(departments);
//                statusLbl.setText("Professor-" + office_hours);
//                statusLbl.setStyle("-fx-text-fill: #801000");
//                statusLbl2.setText(subjects_taught);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleEditPhoto() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            if (selectedFile.length() > 2 * 1024 * 1024) {
                System.out.println("File size exceeds the 2MB limit.");
                return;
            }
            saveProfilePicture(selectedFile);
        }
    }

    private void saveProfilePicture(File imageFile) throws Exception {
        try (Connection connection = DBConnection.getConnection();
             FileInputStream fis = new FileInputStream(imageFile)) {
            String sql = "UPDATE users SET profile_picture = ? WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBinaryStream(1, fis, (int) imageFile.length());
            preparedStatement.setInt(2, getCurrentUserId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                UtilityMethods.showPopupCenter("Profile picture updated successfully.");
                displayProfilePicture(userProfileIcon);
                SessionManager.setCurrentUserImage(userProfileIcon.getImage());
            } else {
                System.out.println("Failed to update profile picture.");
            }
        }
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
                    userProfileIcon.setFitWidth(50);
                    userProfileIcon.setFitHeight(50);
                    userProfileIcon.setSmooth(true);
                    Circle clip = new Circle(25);
                    clip.setCenterX(25);
                    clip.setCenterY(25);
                    userProfileIcon.setClip(clip);
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
            imageView.setFitWidth(60);
            imageView.setFitHeight(60);
            imageView.setSmooth(true);
            Circle clip = new Circle(30);
            clip.setCenterX(30);
            clip.setCenterY(30);
            imageView.setClip(clip);
        } catch (Exception e) {
            System.err.println("Error loading default profile picture: " + e.getMessage());
        }
    }

    private void fetchAndDisplayPosts() {
        String query = "SELECT p.content, p.created_at, u.username FROM posts p JOIN users u ON p.user_id = u.user_id WHERE p.user_id = ? ORDER BY p.created_at DESC";
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, SessionManager.getCurrentUserId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String content = rs.getString("content");
                String createdAt = rs.getString("created_at");
                String username = rs.getString("username");

                HBox postBox = new HBox();
                postBox.setSpacing(10);

                Label postUsername = new Label(username);
                Label postContent = new Label(content);
                Label postDate = new Label(createdAt);

                postBox.getChildren().addAll(postUsername, postContent, postDate);
                postListView.getItems().add(postBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleEditAboutMe() {
        UtilityMethods.switchToScene(aboutMeTextArea, "edit_aboutme");
    }

    public void handleEditProfile(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user instanceof Student student) {
            student.updateProfile(event, "");
        } else if (user instanceof Professor professor) {
            professor.updateProfile(event, "", "");
        }
    }

    public void handleNavigateToAwards() {
        if (SessionManager.isCurrentUserStudent()) {
            UtilityMethods.switchToScene(username, "awards_certificates");
        } else {
            UtilityMethods.switchToScene(username, "research_keynotes");
        }
    }

    @FXML
    void handleLogout(){
        UtilityMethods.switchToScene(username, "login");
    }

}
