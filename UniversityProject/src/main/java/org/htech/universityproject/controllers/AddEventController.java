package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.htech.universityproject.modal.Professor;
import org.htech.universityproject.modal.Student;
import org.htech.universityproject.modal.User;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.File;
import java.time.LocalDate;

public class AddEventController extends Navigation {

    @FXML
    private Button chooseImageBtn;
    @FXML
    private TextArea eventDescriptionField;

    @FXML
    private TextField eventTitleField;

    @FXML
    private Button saveButton;

    @FXML
    private DatePicker eventDatePicker;

    @FXML
    private ComboBox<String> eventLocationComboBox;

    @FXML
    private ComboBox<String> eventTypeComboBox;

    private File selectedImage;

    @FXML
    private void initialize() {
        eventTypeComboBox.getItems().addAll("Academic", "Cultural", "Sports");
        eventLocationComboBox.getItems().addAll("Main-Building","Ground");
        chooseImageBtn.setOnAction(event -> handleChooseImage());
    }

    @FXML
    void handleChooseImage() {
        selectedImage = UtilityMethods.handleChooseImage(chooseImageBtn);
    }

    @FXML
    private void handleSaveEvent(ActionEvent event) {
        String eventTitle = eventTitleField.getText();
        String eventDescription = eventDescriptionField.getText();
        String location = eventLocationComboBox.getValue();
        LocalDate eventDate = eventDatePicker.getValue();
        String eventType = eventTypeComboBox.getValue();

        if(location==null){
            location= "";
        }
        if (eventTitle.isEmpty() || eventDescription.isEmpty() || eventType.isEmpty()) {
            UtilityMethods.showPopupWarning("Please fill all the fields");
            return;
        }

        if (eventDate.isBefore(LocalDate.now())) {
            UtilityMethods.showPopupWarning("Wrong Date!");
            return;
        }

        User user = SessionManager.getCurrentUser();
        if(user instanceof Student student){
            student.addEvent( eventTitle,  eventDescription, String.valueOf(eventDate),  eventType,  location , selectedImage);
            clearFields();
        }
        else if(user instanceof Professor professor){
            professor.addEvent( eventTitle,  eventDescription, String.valueOf(eventDate),  eventType,  location , selectedImage);
            clearFields();
        }

        UtilityMethods.switchToScene(saveButton,"event_hub");
    }

    private void clearFields(){
        eventLocationComboBox.getSelectionModel().clearSelection();
        eventTitleField.setText("");
        eventDescriptionField.setText("");
        eventTypeComboBox.getSelectionModel().clearSelection();
    }


}
