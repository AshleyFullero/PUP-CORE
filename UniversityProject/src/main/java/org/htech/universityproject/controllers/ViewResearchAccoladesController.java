package org.htech.universityproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ViewResearchAccoladesController extends Navigation {

    @FXML
    private ListView<HBox> accoladeListView;

    @FXML
    private ListView<HBox> keynotesListView;

    @FXML
    private ListView<HBox> researchListView;

    @FXML
    private Label usernameLbl;


    private final ObservableList<HBox> research = FXCollections.observableArrayList();
    private final ObservableList<HBox> keynotes = FXCollections.observableArrayList();
    private final ObservableList<HBox> accolades = FXCollections.observableArrayList();


    public void loadUserAchievements(int userId){
        loadResearchFromDB(userId);
        loadKeyNotesFromDB(userId);
        loadProfessionalAccoladesFromDB(userId);
        if(researchListView.getItems().isEmpty()){
            researchListView.getItems().add(new HBox(new Label("No Awards Available")));
        }
        if(keynotesListView.getItems().isEmpty()){
            keynotesListView.getItems().add(new HBox(new Label("No Certificates Available")));
        }
        if(accoladeListView.getItems().isEmpty()){
            accoladeListView.getItems().add(new HBox(new Label("No Club Recognitions Available")));
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

    private void loadResearchFromDB(int userId) {
        try (Connection connection = DBConnection.getConnection()) {
            String selectQuery = "SELECT title, description FROM achievements WHERE user_id = ? and achievement_type_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

            statement.setInt(1, userId);
            statement.setInt(2, 4);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                addAwardToList("research",title, description,research,researchListView);
            }
        } catch (SQLException e) {
            UtilityMethods.showPopupWarning("Error loading awards: " + e.getMessage());
        }
    }

    private void loadKeyNotesFromDB(int userId) {
        try (Connection connection = DBConnection.getConnection()) {
            String selectQuery = "SELECT title, description FROM achievements WHERE user_id = ? and achievement_type_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

            statement.setInt(1, userId);
            statement.setInt(2, 5);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                addAwardToList("keynote_icon",title, description,keynotes,keynotesListView);
            }
        } catch (SQLException e) {
            UtilityMethods.showPopupWarning("Error loading awards: " + e.getMessage());
        }
    }

    private void loadProfessionalAccoladesFromDB(int userId) {
        try (Connection connection = DBConnection.getConnection()) {
            String selectQuery = "SELECT title, description FROM achievements WHERE user_id = ? and achievement_type_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

            statement.setInt(1, userId);
            statement.setInt(2, 6);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                addAwardToList("professional_accolades",title, description,accolades,accoladeListView);
            }
        } catch (SQLException e) {
            UtilityMethods.showPopupWarning("Error loading awards: " + e.getMessage());
        }
    }

}
