package org.htech.universityproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.htech.universityproject.utilities.UtilityMethods;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private ImageView logo;

    @FXML
    private Button professorBtn;

    @FXML
    private Button studentBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginBtn.setOnAction(event -> UtilityMethods.switchToScene(loginBtn,"login"));
        professorBtn.setOnAction(event -> UtilityMethods.switchToScene(professorBtn,"create_account_professor"));
        studentBtn.setOnAction(event -> UtilityMethods.switchToScene(studentBtn,"create_account_student"));
    }
}
