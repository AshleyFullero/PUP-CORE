package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.htech.universityproject.modal.AchievementType;
import org.htech.universityproject.modal.Professor;

import org.htech.universityproject.modal.User;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;


public class AddResearchController extends Navigation {

    @FXML
    private TextField publicationNameField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private ImageView iconImageView;

    @FXML
    private Button saveButton;

    @FXML
    public void initialize() {
        iconImageView.setImage(new Image(String.valueOf(getClass().getResource("/icons/research.png"))));
        saveButton.setOnAction(this::savePublication);
    }


    @FXML
    void savePublication(ActionEvent event) {
        String publicationName = publicationNameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (publicationName.isEmpty() || description.isEmpty()) {
            UtilityMethods.showPopupWarning("Publication name and description cannot be empty.");
            return;
        }

        User user =SessionManager.getCurrentUser();
        if(user instanceof Professor professor){
            professor.addAchievement(publicationName, description,AchievementType.ResearchPublication);
        }
        else{
            UtilityMethods.showPopupWarning("Only Professors can add research publications.");
            return;
        }

        publicationNameField.clear();
        descriptionArea.clear();


        handleBackAction(event);
    }

    @FXML
    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "research_keynotes");
    }

}
