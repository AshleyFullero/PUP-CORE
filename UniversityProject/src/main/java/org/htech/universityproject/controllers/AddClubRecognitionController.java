package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.htech.universityproject.modal.AchievementType;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.modal.User;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;


public class AddClubRecognitionController extends Navigation {

    @FXML
    private TextField clubRecognitionNameField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button saveButton;

    @FXML
    private ImageView iconImageView;

    @FXML
    public void initialize() {
        saveButton.setOnAction(this::saveAward);
        iconImageView.setImage(new Image(String.valueOf(getClass().getResource("/icons/Vector.png"))));

    }

    @FXML
    private void saveAward(ActionEvent event) {
        String RecognitionName = clubRecognitionNameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (RecognitionName.isEmpty() || description.isEmpty()) {
            UtilityMethods.showPopupWarning("Name and description cannot be empty.");
            return;
        }

        User user = SessionManager.getCurrentUser();
        user.addAchievement(RecognitionName, description, AchievementType.ClubRecognition);

        clubRecognitionNameField.clear();
        descriptionArea.clear();

        handleBackAction(event);
    }

    @FXML
    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "awards_certificates");
    }
}