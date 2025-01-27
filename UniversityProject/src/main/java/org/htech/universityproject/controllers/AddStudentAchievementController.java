package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.htech.universityproject.utilities.UtilityMethods;

public class AddStudentAchievementController {

    @FXML
    private Button awardsBtn;

    @FXML
    private Button certificatesBtn;

    @FXML
    private Button clubRecognitionBtn;

    @FXML
    private ImageView logo;

    @FXML
    void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(awardsBtn,"profile");
    }

}
