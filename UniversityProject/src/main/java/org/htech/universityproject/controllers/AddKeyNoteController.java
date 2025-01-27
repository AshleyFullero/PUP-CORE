package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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


public class AddKeyNoteController extends Navigation {

    @FXML
    private TextField keynoteTitleField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private ImageView iconImageView;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button saveButton;

    @FXML
    public void initialize() {
        iconImageView.setImage(new Image(String.valueOf(getClass().getResource("/icons/keynote_icon.png"))));
        saveButton.setOnAction(this::saveKeynote);
    }

    @FXML
    void saveKeynote(ActionEvent event) {
        String title = keynoteTitleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String date = datePicker.getValue().toString();

        if (title.isEmpty() || description.isEmpty()) {
            UtilityMethods.showPopupWarning("Title and description cannot be empty.");
            return;
        }

        User user =SessionManager.getCurrentUser();
        if(user instanceof Professor professor){
            professor.addAchievement(title, description, date,AchievementType.KeyNote);
        }
        else{
            UtilityMethods.showPopupWarning("Only Professors can add Keynotes.");
            return;
        }

        keynoteTitleField.clear();
        descriptionArea.clear();

        handleBackAction(event);
    }

    @FXML
    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "research_keynotes");
    }
}
