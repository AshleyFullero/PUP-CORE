package org.htech.universityproject.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.htech.universityproject.utilities.UtilityMethods;

public abstract class Navigation {

    @FXML
    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "student_dashboard");
    }

    @FXML
    public void handleNavigateToCore(ActionEvent event) {
        UtilityMethods.switchToScene(event, "announcements");
    }

    @FXML
    public void handleNavigateToExtras(ActionEvent event) {
      UtilityMethods.switchToScene(event, "posts");
    }

    @FXML
    public void handleNavigateToOther(ActionEvent event) {
        UtilityMethods.switchToScene(event, "event_hub");
    }

    @FXML
    public void handleNavigateToResources(ActionEvent event) {
        UtilityMethods.switchToScene(event, "resources");
    }

}
