package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Professor;
import org.htech.universityproject.modal.Schedule;
import org.htech.universityproject.modal.Student;
import org.htech.universityproject.modal.User;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddScheduleController extends Navigation implements Initializable {

    @FXML
    private ComboBox<String> eventLocationComboBox;

    @FXML
    private TextField eventTitleField;

    @FXML
    private ComboBox<String> eventTypeComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField endTimeField;

    @FXML
    public void BackAction(){
        UtilityMethods.switchToScene(saveButton,"schedule_planner");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (SessionManager.isCurrentUserStudent()) {
            eventTypeComboBox.getItems().addAll("ClassSchedule", "Deadline", "Extracurricular");
        } else {
            eventTypeComboBox.getItems().addAll("Lecture", "Consultation", "FacultyMeeting");
        }
        eventLocationComboBox.getItems().addAll("Main-Building", "Office", "Ground");
    }

    @FXML
    void handleSaveEvent(ActionEvent event) {
        String eventTitle = eventTitleField.getText();
        LocalDateTime startTime = getDateTime(startDatePicker, startTimeField, "09:00");
        LocalDateTime endTime = getDateTime(endDatePicker, endTimeField, "17:00");
        String type = eventTypeComboBox.getValue();
        String location = eventLocationComboBox.getValue();
        String date = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : null;

        if (eventTitle.isEmpty() || startTime == null || endTime == null || type == null) {
            UtilityMethods.showPopupWarning("Please fill all the fields");
            return;
        }

        if (endTime.isBefore(startTime)) {
            UtilityMethods.showPopupWarning("End time cannot be before start time!");
            return;
        }
        User user = SessionManager.getCurrentUser();
        if(user instanceof Student student) {
            student.addSchedule(eventTitle,startTime,endTime, type, date, location);
            clearFields();
        } else if(user instanceof Professor professor) {
            professor.addSchedule(eventTitle,startTime,endTime, type, date, location);
            clearFields();
        }

        UtilityMethods.switchToScene(saveButton,"schedule_planner");
    }

    public void populateFields(Schedule schedule) {
        eventTitleField.setText(schedule.getTitle());
        startDatePicker.setValue(schedule.getStart().toLocalDate());
        startTimeField.setText(schedule.getStart().toLocalTime().toString());
        endDatePicker.setValue(schedule.getEnd().toLocalDate());
        endTimeField.setText(schedule.getEnd().toLocalTime().toString());
        eventTypeComboBox.getSelectionModel().select(schedule.getType());
        eventLocationComboBox.getSelectionModel().select(schedule.getLocation());
        saveButton.setOnAction(event -> handleUpdateEvent(schedule.getScheduleId()));
    }

    private void handleUpdateEvent(int scheduleId) {
        String eventTitle = eventTitleField.getText();
        LocalDateTime startTime = getDateTime(startDatePicker, startTimeField, "09:00");
        LocalDateTime endTime = getDateTime(endDatePicker, endTimeField, "17:00");
        String type = eventTypeComboBox.getValue();
        String location = eventLocationComboBox.getValue();

        if (eventTitle.isEmpty() || startTime == null || endTime == null || type == null) {
            UtilityMethods.showPopupWarning("Please fill all the fields");
            return;
        }

        if (endTime.isBefore(startTime)) {
            UtilityMethods.showPopupWarning("End time cannot be before start time!");
            return;
        }

        String query = "UPDATE schedule SET event_title = ?, start_time = ?, end_time = ?, event_type = ?, location = ? WHERE schedule_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, eventTitle);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(endTime));
            preparedStatement.setString(4, type);
            preparedStatement.setString(5, location);
            preparedStatement.setInt(6, scheduleId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        UtilityMethods.switchToScene(saveButton, "schedule_planner");
    }


    private void clearFields() {
        eventTitleField.setText("");
        eventTypeComboBox.getSelectionModel().clearSelection();
        startTimeField.setText("");
        endTimeField.setText("");
        eventLocationComboBox.getSelectionModel().clearSelection();
    }

    private LocalDateTime getDateTime(DatePicker datePicker, TextField timeField, String defaultTime) {
        try {
            String date = datePicker.getValue().toString();
            String time = timeField.getText().isEmpty() ? defaultTime : timeField.getText();
            return LocalDateTime.parse(date + "T" + time);
        } catch (Exception e) {
            return null;
        }
    }

}
