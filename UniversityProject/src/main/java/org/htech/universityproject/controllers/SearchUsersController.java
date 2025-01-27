package org.htech.universityproject.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.UtilityMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchUsersController extends Navigation {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<User> resultsTable;

    @FXML
    private TableColumn<User, Integer> userIdColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, Void> viewProfileColumn;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        searchField.setOnKeyReleased(this::handleSearch);

        addViewProfileButton();
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            resultsTable.setItems(FXCollections.observableArrayList());
            return;
        }

        fetchUsersFromDatabase(query);
    }

    private void fetchUsersFromDatabase(String query) {
        String sql = "SELECT user_id, username FROM users WHERE username LIKE ?";
        ObservableList<User> filteredUsers = FXCollections.observableArrayList();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + query + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    String username = resultSet.getString("username");
                    filteredUsers.add(new User(userId, username));
                }
            }

            resultsTable.setItems(filteredUsers);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addViewProfileButton() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = param -> new TableCell<>() {
            private final Button viewButton = new Button("Profile");
            private final FontAwesomeIcon profileIcon = new FontAwesomeIcon();

            {
                profileIcon.setStyle("-fx-fill: white; -fx-font-size: 14px;");
                profileIcon.setIcon(FontAwesomeIcons.USER);
                viewButton.setGraphic(profileIcon);
                viewButton.setStyle(
                        "-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 12px; -fx-border-radius: 5px; -fx-background-radius: 5px;"
                );
                viewButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    UtilityMethods.openUserProfile(resultsTable, user.getUserId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        };

        viewProfileColumn.setCellFactory(cellFactory);
    }

    public static class User {
        private final int userId;
        private final String username;

        public User(int userId, String username) {
            this.userId = userId;
            this.username = username;
        }

        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }
    }
}
