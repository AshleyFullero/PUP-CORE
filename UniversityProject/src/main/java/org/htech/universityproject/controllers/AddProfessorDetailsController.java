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

public class AddProfessorDetailsController extends Navigation {

    @FXML
    private ComboBox<String> departmentComboBox;

    @FXML
    private TextField officeHoursField, subjectsTaughtField;

    @FXML
    private ListView<String> subjectsListView;

    private ObservableList<String> initialSubjects = FXCollections.observableArrayList();

    @FXML
    private Label usernameLbl;


    @FXML
    public void initialize() {
        usernameLbl.setText("Welcome : "+SessionManager.getCurrentUserName());
        loadInitialData();
        loadDepartments();
    }

    private void loadDepartments(){
        String query = "SELECT department_name FROM departments";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            ObservableList<String> departments = FXCollections.observableArrayList();
            while (resultSet.next()) {
                departments.add(resultSet.getString("department_name"));
            }
            departmentComboBox.setItems(departments);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleAddDetails() {
        String department = departmentComboBox.getValue();
        String officeHours = officeHoursField.getText();
        String subjectsTaught = subjectsTaughtField.getText();

        if (department == null || officeHours.isEmpty() || subjectsTaught.isEmpty()) {
            UtilityMethods.showPopupWarning("Please fill in all fields");
            return;
        }

        String selectDepartmentIdQuery = "SELECT department_id FROM departments WHERE department_name = ?";
        String updateQuery = "UPDATE professors SET department_id = ?, office_hours = ?, subjects_taught = ? WHERE user_id = ?";
        String insertQuery = "INSERT INTO professors (user_id, department_id, office_hours, subjects_taught) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {
            int departmentId;
            try (PreparedStatement selectDeptStmt = connection.prepareStatement(selectDepartmentIdQuery)) {
                selectDeptStmt.setString(1, department);
                ResultSet resultSet = selectDeptStmt.executeQuery();
                if (resultSet.next()) {
                    departmentId = resultSet.getInt("department_id");
                } else {
                    UtilityMethods.showPopupWarning("Selected department is invalid.");
                    return;
                }
            }

            int userId = SessionManager.getCurrentUserId();

            try (PreparedStatement checkStatement = connection.prepareStatement(
                    "SELECT COUNT(*) AS count FROM professors WHERE user_id = ?"
            )) {
                checkStatement.setInt(1, userId);
                ResultSet checkResult = checkStatement.executeQuery();
                checkResult.next();
                int count = checkResult.getInt("count");

                if (count > 0) {
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setInt(1, departmentId);
                        updateStatement.setString(2, officeHours);
                        updateStatement.setString(3, subjectsTaught);
                        updateStatement.setInt(4, userId);

                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            UtilityMethods.showPopupCenter("Professor details updated successfully!");
                        } else {
                            UtilityMethods.showPopupWarning("Failed to update professor details.");
                        }
                    }
                } else {
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setInt(1, userId);
                        insertStatement.setInt(2, departmentId);
                        insertStatement.setString(3, officeHours);
                        insertStatement.setString(4, subjectsTaught);

                        int rowsInserted = insertStatement.executeUpdate();
                        if (rowsInserted > 0) {
                            UtilityMethods.showPopup("Professor details added successfully!");
                            clearFields();
                        } else {
                            UtilityMethods.showPopupWarning("Failed to add professor details.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        UtilityMethods.switchToScene(usernameLbl, "profile");
    }


    private void loadInitialData() {
        int userId = SessionManager.getCurrentUserId();
        String query = """
        SELECT d.department_name, p.office_hours, p.subjects_taught AS subjects
        FROM professors p
        JOIN departments d ON p.department_id = d.department_id
        WHERE p.user_id = ?
    """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String subjects = resultSet.getString("subjects");
                String departmentName = resultSet.getString("department_name");
                String officeHours = resultSet.getString("office_hours");

                if (subjects != null) {
                    String[] subjectsArray = subjects.split(",");
                    initialSubjects.addAll(subjectsArray);
                }
                departmentComboBox.getSelectionModel().select(departmentName);
                officeHoursField.setText(officeHours);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        subjectsListView.setItems(initialSubjects);
        clearFields();

    }


    private void clearFields() {
        departmentComboBox.getSelectionModel().clearSelection();
        subjectsTaughtField.setText("");
        officeHoursField.setText("");
    }

    public void backAction() {
        UtilityMethods.switchToScene(usernameLbl, "profile");
    }

}
