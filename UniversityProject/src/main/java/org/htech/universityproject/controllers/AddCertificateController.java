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

public class AddCertificateController  extends Navigation {

    @FXML
    private TextField certificateNameField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button saveButton;

    @FXML
    private ImageView iconImageView;

    @FXML
    public void initialize() {
        saveButton.setOnAction(this::saveCertificate);
        iconImageView.setImage(new Image(String.valueOf(getClass().getResource("/icons/certificate_solid.png"))));
    }

    @FXML
    private void saveCertificate(ActionEvent event) {
        String certificateName = certificateNameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (certificateName.isEmpty() || description.isEmpty()) {
            UtilityMethods.showPopupWarning("Award name and description cannot be empty.");
            return;
        }

        User user = SessionManager.getCurrentUser();
        user.addAchievement(certificateName, description, AchievementType.Certificate);

        certificateNameField.clear();
        descriptionArea.clear();

        handleBackAction(event);
    }

    @FXML
    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "awards_certificates");
    }
}
