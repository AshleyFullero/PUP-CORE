package org.htech.universityproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AwardsCertificatesController extends Navigation implements Initializable {

    @FXML
    private Button addMoreAwardBtn;

    @FXML
    private Button addMoreCertificationsBtn;

    @FXML
    private Button addMoreClubBtn;
    @FXML
    private ListView<HBox> awardsListView;

    @FXML
    private ListView<HBox> certificatesListView;

    @FXML
    private ListView<HBox> clubRecognitionsListView;

    private final ObservableList<HBox> awards = FXCollections.observableArrayList();
    private final ObservableList<HBox> certificates = FXCollections.observableArrayList();
    private final ObservableList<HBox> clubRecognitions = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAwardsFromDB();
        loadCertificatesFromDB();
        loadClubRecognitionsFromDB();
        addMoreAwardBtn.setOnAction(event -> UtilityMethods.switchToScene(addMoreAwardBtn, "add_award"));
        addMoreCertificationsBtn.setOnAction(event -> UtilityMethods.switchToScene(addMoreAwardBtn, "add_certificate"));
        addMoreClubBtn.setOnAction(event -> UtilityMethods.switchToScene(addMoreAwardBtn, "add_club_recognition"));
    }

    private void addAwardToList(String iconName , String awardName, String description , ObservableList<HBox> list, ListView<HBox> listView) {
        Label nameLabel = new Label(awardName);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #800100; -fx-font-size: 14px");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-text-fill: #800100; -fx-font-size: 12px");
        descriptionLabel.setWrapText(true);

        ImageView awardIcon = new ImageView(new Image(String.valueOf(getClass().getResource("/icons/"+iconName+".png"))));
        awardIcon.setFitWidth(50);
        awardIcon.setFitHeight(50);
        awardIcon.setPreserveRatio(true);

        HBox awardItem = new HBox(10, awardIcon, new VBox(nameLabel, descriptionLabel));
        list.add(awardItem);

        listView.setItems(list);
    }

    private void loadAwardsFromDB() {
        try (Connection connection = DBConnection.getConnection()) {
            String selectQuery = "SELECT title, description FROM achievements WHERE user_id = ? and achievement_type_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

            int userId = SessionManager.getCurrentUserId();
            statement.setInt(1, userId);
            statement.setInt(2, 1);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                addAwardToList("award_solid",title, description,awards,awardsListView);
            }
        } catch (SQLException e) {
            UtilityMethods.showPopupWarning("Error loading awards: " + e.getMessage());
        }
    }

    private void loadCertificatesFromDB() {
        try (Connection connection = DBConnection.getConnection()) {
            String selectQuery = "SELECT title, description FROM achievements WHERE user_id = ? and achievement_type_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

            int userId = SessionManager.getCurrentUserId();
            statement.setInt(1, userId);
            statement.setInt(2, 3);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                addAwardToList("certificate_solid",title, description,certificates,certificatesListView);
            }
        } catch (SQLException e) {
            UtilityMethods.showPopupWarning("Error loading awards: " + e.getMessage());
        }
    }

    private void loadClubRecognitionsFromDB() {
        try (Connection connection = DBConnection.getConnection()) {
            String selectQuery = "SELECT title, description FROM achievements WHERE user_id = ? and achievement_type_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

            int userId = SessionManager.getCurrentUserId();
            statement.setInt(1, userId);
            statement.setInt(2, 2);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                addAwardToList("Vector",title, description,clubRecognitions,clubRecognitionsListView);
            }
        } catch (SQLException e) {
            UtilityMethods.showPopupWarning("Error loading awards: " + e.getMessage());
        }
    }

    @FXML
    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "profile");
    }

}
