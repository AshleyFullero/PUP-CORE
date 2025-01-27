package org.htech.universityproject.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Professor;
import org.htech.universityproject.modal.Student;
import org.htech.universityproject.modal.User;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController  {

    @FXML
    public AnchorPane stage;
    @FXML
    private Label forgotPasswordLbl;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerBtn;

    @FXML
    private TextField emailField;

    @FXML
    private ImageView logoutIcon;

    public void initialize(){
        logoutIcon.setOnMouseClicked(event -> System.exit(0));
    }

    @FXML
    private void handleLogout(){
        System.exit(0);
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            UtilityMethods.showPopupWarning("Please Fill in all Fields");
            return;
        }
        if (!UtilityMethods.isValidEmail(email)) {
            UtilityMethods.showPopupWarning("Wrong email format");
            return;
        }
        if (password.length() < 4) {
            UtilityMethods.showPopupWarning("Password minimum length does not meet");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT user_id, username, email, role FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, email);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("user_id");
                        String name = rs.getString("username");
                        String userEmail = rs.getString("email");
                        String role = rs.getString("role");

                        SessionManager.setCurrentUserId(userId);
                        SessionManager.setCurrentUserName(name);
                        SessionManager.setCurrentUserEmail(userEmail);
                        boolean isProfessor = role.equalsIgnoreCase("professor");
                        SessionManager.setCurrentUserStudent(!isProfessor);

                        if (isProfessor) {
                            User professor  = new Professor(userId,name, password, userEmail);
                            SessionManager.setCurrentUser(professor);
                            UtilityMethods.showPopup("Welcome to  Dashboard");
                            UtilityMethods.switchToScene(registerBtn, "student_dashboard");
                        } else {
                            User student  = new Student(userId,name, password, userEmail);
                            SessionManager.setCurrentUser(student);
                            UtilityMethods.showPopup("Welcome to User Dashboard");
                            UtilityMethods.switchToScene(registerBtn, "student_dashboard");
                        }
                    } else {
                        UtilityMethods.showPopupWarning("Invalid email or password.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            UtilityMethods.showPopupWarning("Error during login: " + e.getMessage());
        }
    }

    @FXML
    private void handleSignUp() {
        UtilityMethods.switchToScene(registerBtn, "register");
    }

}
