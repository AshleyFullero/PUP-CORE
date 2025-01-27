package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class AddPostController extends Navigation implements Initializable {

    @FXML
    private Button chooseImageBtn;

    @FXML
    private Button postBtn;

    @FXML
    private TextArea postDescription;

    private File selectedImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        postBtn.setOnAction(event -> handlePost());
        chooseImageBtn.setOnAction(event -> handleChooseImage());
    }

    @FXML
    void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg")
        );
        Stage stage = (Stage) chooseImageBtn.getScene().getWindow();
        selectedImage = fileChooser.showOpenDialog(stage);

        if (selectedImage != null && selectedImage.length() <= 2 * 1024 * 1024) {
            UtilityMethods.showPopup("Image selected successfully!");
        } else {
            selectedImage = null;
            UtilityMethods.showPopupWarning("Invalid image!");
        }
    }

    @FXML
    void handlePost() {
        String content = postDescription.getText().trim();
        if (content.isEmpty()) {
            System.out.println("Content cannot be empty!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO posts (user_id, content, media) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, SessionManager.getCurrentUserId());
            stmt.setString(2, content);

            if (selectedImage != null) {
                FileInputStream fis = new FileInputStream(selectedImage);
                stmt.setBinaryStream(3, fis, (int) selectedImage.length());
            } else {
                stmt.setNull(3, java.sql.Types.BLOB);
            }

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                UtilityMethods.showPopupCenter("Post added successfully!");
            }

            UtilityMethods.switchToScene(postBtn,"posts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
   public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(postBtn,"posts");
    }

}
