package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import static org.htech.universityproject.utilities.SessionManager.getCurrentUserId;

public class EditAboutMeController extends Navigation implements Initializable {

    @FXML
    private TextArea aboutMeTextArea;

    @FXML
    private Button saveBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aboutMeTextArea.setText(fetchAboutMe());
        saveBtn.setOnAction(event -> handleSaveAboutMe());
    }

    @FXML
    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "profile");
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
            throw new RuntimeException(e);
        }
        return aboutMe;
    }

    public void handleSaveAboutMe() {
        String updatedAboutMe = aboutMeTextArea.getText();
        if (updatedAboutMe != null && !updatedAboutMe.trim().isEmpty()) {
            saveAboutMe(updatedAboutMe);
        } else {
            UtilityMethods.showPopupWarning("About-Me is empty!");
        }
    }

    private void saveAboutMe(String aboutMeText) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "UPDATE users SET about_me = ? WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, aboutMeText);
            preparedStatement.setInt(2, getCurrentUserId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                UtilityMethods.showPopupCenter("About-Me Saved Successfully!");
                aboutMeTextArea.setEditable(false);

                UtilityMethods.switchToScene(saveBtn,"profile");
            } else {
                UtilityMethods.showPopupWarning("Error while updating about me.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
