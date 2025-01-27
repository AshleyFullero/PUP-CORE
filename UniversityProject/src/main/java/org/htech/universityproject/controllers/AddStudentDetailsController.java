package org.htech.universityproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddStudentDetailsController extends Navigation {

    @FXML
    private ComboBox<String> courseComboBox, yearComboBox, levelComboBox;

    @FXML
    private TextField activeSubjectsField;

    @FXML
    private ListView<String> subjectsListView;

    private ObservableList<String> initialSubjects = FXCollections.observableArrayList();

    @FXML
    private Label usernameLbl;

    @FXML
    public void initialize() {
        loadCourses();
        usernameLbl.setText("Welcome : " + SessionManager.getCurrentUserName());
        loadInitialData();
    }

    private void loadCourses() {
        String query = "SELECT course_id, course_name FROM courses";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            ObservableList<String> courses = FXCollections.observableArrayList();
            while (resultSet.next()) {
                courses.add(resultSet.getString("course_name"));
            }
            courseComboBox.setItems(courses);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleAddDetails() {
        String course = courseComboBox.getValue();
        String year = yearComboBox.getValue();
        String level = levelComboBox.getValue();
        String activeSubjects = activeSubjectsField.getText();

        if (course == null || year == null || level == null || activeSubjects.isEmpty()) {
            UtilityMethods.showPopupWarning("Please fill in all fields");
            return;
        }

        String selectDepartmentIdQuery = "SELECT course_id FROM courses WHERE course_name = ?";
        String updateQuery = "UPDATE students SET course_id = ?, year = ?, level = ? , active_subjects = ?  WHERE user_id = ?";
        String insertQuery = "INSERT INTO students (user_id, course_id, level, year , active_subjects) VALUES (?, ?, ?, ?,?)";

        try (Connection connection = DBConnection.getConnection()) {
            int courseId;
            try (PreparedStatement selectDeptStmt = connection.prepareStatement(selectDepartmentIdQuery)) {
                selectDeptStmt.setString(1, course);
                ResultSet resultSet = selectDeptStmt.executeQuery();
                if (resultSet.next()) {
                    courseId = resultSet.getInt("course_id");
                } else {
                    UtilityMethods.showPopupWarning("Selected course is invalid.");
                    return;
                }
            }

            int userId = SessionManager.getCurrentUserId();

            try (PreparedStatement checkStatement = connection.prepareStatement(
                    "SELECT COUNT(*) AS count FROM students WHERE user_id = ?"
            )) {
                checkStatement.setInt(1, userId);
                ResultSet checkResult = checkStatement.executeQuery();
                checkResult.next();
                int count = checkResult.getInt("count");

                if (count > 0) {
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setInt(1, courseId);
                        updateStatement.setString(2, year);
                        updateStatement.setString(3, level);
                        updateStatement.setString(4, activeSubjects);
                        updateStatement.setInt(5, userId);

                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            UtilityMethods.showPopupCenter("Student details updated successfully!");
                        } else {
                            UtilityMethods.showPopupWarning("Failed to update student details.");
                        }
                    }
                } else {
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setInt(1, userId);
                        insertStatement.setInt(2, courseId);
                        insertStatement.setString(3, level);
                        insertStatement.setString(4, year);
                        insertStatement.setString(5, activeSubjects);

                        int rowsInserted = insertStatement.executeUpdate();
                        if (rowsInserted > 0) {
                            UtilityMethods.showPopup("Student details added successfully!");
                            clearFields();
                        } else {
                            UtilityMethods.showPopupWarning("Failed to add student details.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        UtilityMethods.switchToScene(usernameLbl, "profile");
    }


    private void clearFields() {
        courseComboBox.getSelectionModel().clearSelection();
        yearComboBox.getSelectionModel().clearSelection();
        levelComboBox.getSelectionModel().clearSelection();
        activeSubjectsField.setText("");
    }

    private void loadInitialData() {
        int userId = SessionManager.getCurrentUserId();
        String query = """
                SELECT c.course_name, s.year, s.level, s.active_subjects
                FROM students s
                JOIN courses c ON s.course_id = c.course_id
                WHERE s.user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String activeSubjects = resultSet.getString("active_subjects");
                String course = resultSet.getString("course_name");
                String year = resultSet.getString("year");
                String level = resultSet.getString("level");

                if (activeSubjects != null) {
                    String[] subjectsArray = activeSubjects.split(",");
                    initialSubjects.addAll(subjectsArray);
                    courseComboBox.getSelectionModel().select(course);
                    yearComboBox.getSelectionModel().select(year);
                    levelComboBox.getSelectionModel().select(level);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        subjectsListView.setItems(initialSubjects);
    }

    public void backAction() {
        UtilityMethods.switchToScene(usernameLbl, "profile");
    }
}
