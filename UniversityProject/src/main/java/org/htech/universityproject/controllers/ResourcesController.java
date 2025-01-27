package org.htech.universityproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.UtilityMethods;

import java.net.URL;
import java.util.ResourceBundle;

import static org.htech.universityproject.utilities.UtilityMethods.setupMovement;

public class ResourcesController extends Navigation implements Initializable {

    @FXML
    private Button eightBtn;

    @FXML
    private Button eighteenBtn;

    @FXML
    private Button elevenBtn;

    @FXML
    private Button facilitiesBtn;

    @FXML
    private Button fifteenBtn;

    @FXML
    private Button fiftyBtn;

    @FXML
    private Button fiveBtn;

    @FXML
    private Button fourBtn;

    @FXML
    private Button fourteenBtn;

    @FXML
    private Button fourtyBtn;

    @FXML
    private Button fourtyeightBtn;

    @FXML
    private Button fourtyfiveBtn;

    @FXML
    private Button fourtyfourBtn;

    @FXML
    private Button fourtynineBtn;

    @FXML
    private Button fourtyoneBtn;

    @FXML
    private Button fourtysevenBtn;

    @FXML
    private Button fourtysixBtn;

    @FXML
    private Button fourtythreeBtn;

    @FXML
    private Button fourtytwoBtn;

    @FXML
    private ImageView homeBtn;

    @FXML
    private Button nineBtn;

    @FXML
    private Button oneBtn;

    @FXML
    private Button sevenBtn;

    @FXML
    private Button seventeenBtn;

    @FXML
    private Button sixBtn;

    @FXML
    private Button sixteenBtn;

    @FXML
    private Button stallsBtn;

    @FXML
    private Button tenBtn;

    @FXML
    private Button thirteenBtn;

    @FXML
    private Button thirtythreeBtn;

    @FXML
    private Button thirtyBtn;

    @FXML
    private Button thirtySevenBtn;

    @FXML
    private Button thirtyeightBtn;

    @FXML
    private Button thirtyfiveBtn;

    @FXML
    private Button thirtyfourBtn;

    @FXML
    private Button thirtynineBtn;

    @FXML
    private Button thirtyoneBtn;

    @FXML
    private Button thirtysixBtn;

    @FXML
    private Button threeBtn;

    @FXML
    private Button twelveBtn;

    @FXML
    private Button twentyBtn;

    @FXML
    private Button nineteenBtn;

    @FXML
    private Button twentyeightBtn;

    @FXML
    private Button twentyfiveBtn;

    @FXML
    private Button twentyfourBtn;

    @FXML
    private Button twentynineBtn;

    @FXML
    private Button twentysevenBtn;

    @FXML
    private Button twentysixBtn;

    @FXML
    private Button twentythreeBtn;

    @FXML
    private Button twentytwoBtn;

    @FXML
    private Button thirtytwoBtn;;

    @FXML
    private Button twoBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeBtn.setOnMouseClicked(event -> UtilityMethods.switchToScene(homeBtn,"student_dashboard"));
        thirteenBtn.setOnAction(event -> openMapImageView("Frame 7",false));
        fourteenBtn.setOnAction(event -> openMapImageView("Frame 9",false));
        thirtySevenBtn.setOnAction(event -> openMapImageView("Frame 8",false));
        facilitiesBtn.setOnAction(event -> UtilityMethods.switchToScene(homeBtn,"facilities_offices"));
        stallsBtn.setOnAction(event -> UtilityMethods.switchToScene(homeBtn,"stalls"));

        oneBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/1_The Pylon.jpg",true));
        twoBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/2_The Mural.jpg",true));
        sevenBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/7_Souvenir Shop.jpg",true));
        eightBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/8.HEIC.png",true));
        nineBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/9.HEIC.png",true));
        tenBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/10_Obelisk.jpg",true));
        twentyBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/20.HEIC.png",true));
        twentytwoBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/22.HEIC.png",true));
        twentyfourBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/24_Charlie Del Rosario Building.jpg",true));
        twentyfiveBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/25_Linear Park.jpg",true));
        twentysevenBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/27.HEIC.png",true));
        twentyeightBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/28.HEIC.png",true));
        twentynineBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/29.HEIC.png",true));
        thirtyoneBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/31.HEIC.png",true));
        thirtythreeBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/33.HEIC.png",true));
        thirtyfourBtn.setOnAction(event -> openMapImageView("/UNIVERSITY FACILITIES/34.HEIC.png",true));
        elevenBtn.setOnAction(event -> openMapImageView("/UNDER CONSTRUCTION/11.HEIC.png",true));
        twelveBtn.setOnAction(event -> openMapImageView("/UNDER CONSTRUCTION/12.HEIC.png",true));
        twentythreeBtn.setOnAction(event -> openMapImageView("/UNDER CONSTRUCTION/23.HEIC.png",true));
        fifteenBtn.setOnAction(event -> openMapImageView("/STUDENT FACILITIES/15.HEIC.png",true));
        twentysixBtn.setOnAction(event -> openMapImageView("/STUDENT FACILITIES/26.HEIC.png",true));
        thirtyBtn.setOnAction(event -> openMapImageView("/STUDENT FACILITIES/30_Ninoy Aquino Learning Resource Center.jpg",true));
        thirtytwoBtn.setOnAction(event -> openMapImageView("/STUDENT FACILITIES/32_Ampitheather.jpg",true));
        thirtyfiveBtn.setOnAction(event -> openMapImageView("/STUDENT FACILITIES/35.HEIC.png",true));
        thirtysixBtn.setOnAction(event -> openMapImageView("/STUDENT FACILITIES/36_Tahanan ng Alumni.jpg",true));
        thirtynineBtn.setOnAction(event -> openMapImageView("/STUDENT FACILITIES/39.HEIC.png",true));
        threeBtn.setOnAction(event -> openMapImageView("/SPORTS FACILITIES/3.HEIC.png",true));
        fourBtn.setOnAction(event -> openMapImageView("/SPORTS FACILITIES/4.HEIC.png",true));
        fiveBtn.setOnAction(event -> openMapImageView("/SPORTS FACILITIES/5_Lawn Tennis Court.jpg",true));
        sixteenBtn.setOnAction(event -> openMapImageView("/SPORTS FACILITIES/16.HEIC.png",true));
        seventeenBtn.setOnAction(event -> openMapImageView("/SPORTS FACILITIES/17_Oval.jpg",true));
        eighteenBtn.setOnAction(event -> openMapImageView("/SPORTS FACILITIES/18.HEIC.png",true));
        thirtySevenBtn.setOnAction(event -> openMapImageView("/SPORTS FACILITIES/37_Swimming Pool.jpg",true));
        thirtyeightBtn.setOnAction(event -> openMapImageView("/SPORTS FACILITIES/38_Multi-purpose Building.jpg",true));

    }

    private void openMapImageView(String frameNumber,boolean path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_academic.fxml"));
            Parent root = loader.load();

            MainAcademicController controller = loader.getController();
            if(path){
                String imagePath = UtilityMethods.class.getResource(frameNumber).toExternalForm();
                Image image = new Image(imagePath);
                controller.setImageView(image);
            }
            else{
            controller.setImageView(frameNumber);
            }
            Stage oldStage = (Stage) homeBtn.getScene().getWindow();
            oldStage.close();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

            setupMovement(stage, root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
