package org.htech.universityproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.utilities.UtilityMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateProfessorAccountController {

    @FXML
    private Button backBtn;

    @FXML
    private Button createBtn;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private PasswordField passwordField,confirmPasswordField;


    @FXML
    public void initialize() {
        createBtn.setOnAction(event -> createProfessorAccount());
        backBtn.setOnAction(event -> UtilityMethods.switchToScene(backBtn,"register"));

    }

    private void createProfessorAccount() {
        String firstName = firstNameField.getText();
        String middleName = middleNameField.getText().trim();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            UtilityMethods.showPopupWarning("All fields are required!");
            return;
        }

        if (password.length() < 4) {
            UtilityMethods.showPopupWarning("Password must be at least 4 characters long!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            UtilityMethods.showPopupWarning("Passwords do not match!");
            return;
        }

        if (middleName.equalsIgnoreCase("None")) {
            middleName = "";
        }

        String fullName = firstName + (middleName.isEmpty() ? "" : " " + middleName) + " " + lastName;

        String checkUsernameQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
        String checkEmailQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
        String insertQuery = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, 'professor')";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkUsernameStatement = connection.prepareStatement(checkUsernameQuery);
             PreparedStatement checkEmailStatement = connection.prepareStatement(checkEmailQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            checkUsernameStatement.setString(1, fullName);
            ResultSet usernameResult = checkUsernameStatement.executeQuery();
            if (usernameResult.next() && usernameResult.getInt(1) > 0) {
                UtilityMethods.showPopupWarning("Username is already in use!");
                return;
            }

            checkEmailStatement.setString(1, email);
            ResultSet emailResult = checkEmailStatement.executeQuery();
            if (emailResult.next() && emailResult.getInt(1) > 0) {
                UtilityMethods.showPopupWarning("Email is already in use!");
                return;
            }

            insertStatement.setString(1, fullName);
            insertStatement.setString(2, password);
            insertStatement.setString(3, email);

            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted > 0) {
                UtilityMethods.showPopup("Professor account created successfully!");
                clearFields();
                UtilityMethods.switchToScene(createBtn, "login");
            } else {
                showAlert("Error", "Failed to create the professor account.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        firstNameField.clear();
        middleNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
