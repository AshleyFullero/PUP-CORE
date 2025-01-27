package org.htech.universityproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.UtilityMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ViewAwardsCertificatesController extends Navigation {

    @FXML
    private ListView<HBox> awardsListView;

    @FXML
    private ListView<HBox> certificatesListView;

    @FXML
    private ListView<HBox> clubRecognitionsListView;

    @FXML
    private Label usernameLbl;


    private final ObservableList<HBox> awards = FXCollections.observableArrayList();
    private final ObservableList<HBox> certificates = FXCollections.observableArrayList();
    private final ObservableList<HBox> clubRecognitions = FXCollections.observableArrayList();


    public void loadUserAchievements(int userId){
        loadAwardsFromDB(userId);
        loadCertificatesFromDB(userId);
        loadClubRecognitionsFromDB(userId);
        if(awardsListView.getItems().isEmpty()){
            awardsListView.getItems().add(new HBox(new Label("No Awards Available")));
        }
        if(certificatesListView.getItems().isEmpty()){
            awardsListView.getItems().add(new HBox(new Label("No Certificates Available")));
        }
        if(clubRecognitionsListView.getItems().isEmpty()){
            awardsListView.getItems().add(new HBox(new Label("No Club Recognitions Available")));
        }
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

    private void loadAwardsFromDB(int userId) {
        try (Connection connection = DBConnection.getConnection()) {
            String selectQuery = "SELECT title, description FROM achievements WHERE user_id = ? and achievement_type_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

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

    private void loadCertificatesFromDB(int userId) {
        try (Connection connection = DBConnection.getConnection()) {
            String selectQuery = "SELECT title, description FROM achievements WHERE user_id = ? and achievement_type_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

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

    private void loadClubRecognitionsFromDB(int userId) {
        try (Connection connection = DBConnection.getConnection()) {
            String selectQuery = "SELECT title, description FROM achievements WHERE user_id = ? and achievement_type_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

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

}
