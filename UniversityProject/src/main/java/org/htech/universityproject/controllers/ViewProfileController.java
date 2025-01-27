package org.htech.universityproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class ViewProfileController extends Navigation {

    @FXML
    private Button awardsBtn;
    @FXML
    private TextArea aboutMeTextArea;

    @FXML
    private Label statusLbl,statusLbl2;

    @FXML
    private ImageView userProfileIcon;

    @FXML
    private Label userYear;

    @FXML
    private Label username,contactInfoLbl;

    @FXML
    private Button chatBtn;

    private int profileUserId;

    private String role;

    private String profileUserEmail;

    @FXML
    public void initialize(){
        contactInfoLbl.setOnMouseClicked(e -> UtilityMethods.showPopup("Contact me at : "+profileUserEmail));
        chatBtn.setOnAction(event -> openUserChat(profileUserId));
        userProfileIcon.setFitWidth(60);
        userProfileIcon.setFitHeight(60);
        userProfileIcon.setSmooth(true);
        Circle clip = new Circle(30);
        clip.setCenterX(30);
        clip.setCenterY(30);
        userProfileIcon.setClip(clip);
    }

    private void fetchUserData(int profileUserId) {
        if(role.equalsIgnoreCase("student")){
            fetchStudentData(profileUserId);
        }else{
            fetchProfessorData(profileUserId);
        }

        if(role.equalsIgnoreCase("Student")){
            awardsBtn.setText("View - Awards, Club Recognitions, Certifications");
        }else{
            awardsBtn.setText("View - Research, Key Notes, Professional Accolades");
        }
    }

    private void fetchStudentData(int profileUserId){
        String query = """
                SELECT c.course_name, s.year, s.level, s.active_subjects
                FROM students s
                JOIN courses c ON s.course_id = c.course_id
                WHERE s.user_id = ?
                """;
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, profileUserId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String course  = rs.getString("course_name");
                String year  = rs.getString("year");
                String lvl  = rs.getString("level");
                if(lvl.equals("Undergraduate")){
                    lvl = "BS";
                }
                else{
                    lvl ="MS";
                }
                userYear.setText(year+"-Year");
//                statusLbl.setText("Student-" + lvl);
//                statusLbl.setStyle("-fx-text-fill: #801000");
//                statusLbl2.setText(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchProfessorData(int profileUserId){
        String query = """
        SELECT d.department_name, p.office_hours, p.subjects_taught AS subjects
        FROM professors p
        JOIN departments d ON p.department_id = d.department_id
        WHERE p.user_id = ?
    """;
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, profileUserId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String departments  = rs.getString("department_name");
                String office_hours  = rs.getString("office_hours");
                String subjects_taught  = rs.getString("subjects");
                userYear.setText(departments);
//                statusLbl.setText("Professor-" +office_hours);
//                statusLbl.setStyle("-fx-text-fill: #801000");
//                statusLbl2.setText(subjects_taught);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openUserAchievements(int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/view_awards_certificates.fxml"));
            Parent root = loader.load();

            ViewAwardsCertificatesController controller = loader.getController();
            controller.loadUserAchievements(userId);
            Stage oldStage = (Stage) username.getScene().getWindow();
            oldStage.close();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openProfessorAchievements(int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/view_research_keynotes.fxml"));
            Parent root = loader.load();

            ViewResearchAccoladesController controller = loader.getController();
            controller.loadUserAchievements(userId);
            Stage oldStage = (Stage) username.getScene().getWindow();
            oldStage.close();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackAction() {
        UtilityMethods.switchToScene(aboutMeTextArea, "posts");
    }

    public void handleNavigateToAwards() {
        if(role.equalsIgnoreCase("Student")){
        openUserAchievements(profileUserId);
        }else{
            openProfessorAchievements(profileUserId);
        }
    }


    public void loadProfileDetails(int userId) {
        String query = "select  username ,email, profile_picture , about_me , role from users where user_id =?";
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                profileUserId =userId;
                profileUserEmail=rs.getString("email");
                role = rs.getString("role");
                String name = rs.getString("username");
                username.setText(name);
                aboutMeTextArea.setText(rs.getString("about_me"));
                byte[] imageData = rs.getBytes("profile_picture");
                if (imageData != null && imageData.length > 0){
                    Image image = new Image(new ByteArrayInputStream(imageData));
                    userProfileIcon.setImage(image);
                }
                else{
                    loadDefaultProfilePicture(userProfileIcon);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Tooltip tooltip = new Tooltip(profileUserEmail);
        contactInfoLbl.setTooltip(tooltip);
        fetchUserData(userId);
    }

    private void loadDefaultProfilePicture(ImageView imageView) {
        try {
            Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/default-profile.png")));
            imageView.setImage(defaultImage);
        } catch (Exception e) {
            System.err.println("Error loading default profile picture: " + e.getMessage());
        }
    }

    private void openUserChat(int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/chat_message.fxml"));
            UtilityMethods.setupStage(contactInfoLbl, userId, loader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
