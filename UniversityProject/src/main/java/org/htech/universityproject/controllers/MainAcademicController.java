package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.UtilityMethods;

import java.net.URL;
import java.util.ResourceBundle;

public class MainAcademicController  extends Navigation implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private Button closeBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeBtn.setOnAction(event -> UtilityMethods.switchToScene(closeBtn,"resources"));
        imageView.setFitWidth(440);
        imageView.setFitHeight(584);
        imageView.setSmooth(true);
    }


    @FXML
    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "resources");
    }


    public void setImageView(String name){
        String imagePath = UtilityMethods.class.getResource("/frames/" + name + ".png").toExternalForm();
        Image image = new Image(imagePath);
        imageView.setFitWidth(440);
        imageView.setFitHeight(550);
        imageView.setSmooth(true);
        imageView.setImage(image);

    }

    public void setImageView(Image image){
        imageView.setFitWidth(440);
        imageView.setFitHeight(550);
        imageView.setSmooth(true);
        imageView.setImage(image);

    }

}
