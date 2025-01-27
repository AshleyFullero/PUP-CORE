package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.UtilityMethods;

public class FacilitiesController extends Navigation {

    @FXML
    private Button backBtn;

    @FXML
    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "resources");
    }

}
