package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.htech.universityproject.modal.AchievementType;
import org.htech.universityproject.modal.User;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;


public class AddAwardController  extends Navigation {

    @FXML
    private TextField awardNameField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button saveButton;

    @FXML
    private ImageView iconImageView;

    @FXML
    public void initialize() {
        iconImageView.setImage(new Image(String.valueOf(getClass().getResource("/icons/award_solid.png"))));
        saveButton.setOnAction(this::saveAward);
    }

    @FXML
    private void saveAward(ActionEvent event) {
        String awardName = awardNameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (awardName.isEmpty() || description.isEmpty()) {
            UtilityMethods.showPopupWarning("Award name and description cannot be empty.");
            return;
        }

        User user = SessionManager.getCurrentUser();
        user.addAchievement(awardName, description, AchievementType.Award);

        awardNameField.clear();
        descriptionArea.clear();

        handleBackAction(event);
    }

    @FXML
    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "awards_certificates");
    }

}
