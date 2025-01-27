package org.htech.universityproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.htech.universityproject.dao.MessageDao;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Message;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConversationController extends Navigation implements Initializable {

    @FXML
    private Button backBtn;

    @FXML
    private Button sendBtn;

    @FXML
    private TextField fromField;

    @FXML
    private TextArea mailText;

    @FXML
    private TextField toField;

    @FXML
    private ListView<String> suggestionListView;

    private ObservableList<String> suggestions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backBtn.setOnAction(event -> UtilityMethods.switchToScene(backBtn, "student_dashboard"));
        fromField.setText(SessionManager.getCurrentUserName());
        fromField.setDisable(true);
        suggestions = FXCollections.observableArrayList();
        suggestionListView.setItems(suggestions);

        suggestionListView.setVisible(false);

        toField.setOnKeyReleased(this::handleToFieldInput);


        suggestionListView.setOnMouseClicked(event -> {
            String selectedUser = suggestionListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                toField.setText(selectedUser);
                suggestionListView.setVisible(false);
            }
        });

        sendBtn.setOnAction(event -> sendMessage());
    }

    private void handleToFieldInput(KeyEvent event) {
        String query = toField.getText().trim();
        if (!query.isEmpty()) {
            fetchUsersFromDatabase(query);
            suggestionListView.setVisible(true);
        } else {
            suggestionListView.setVisible(false);
        }
    }

    private void fetchUsersFromDatabase(String query) {
        String sql = "SELECT username FROM users WHERE username LIKE ? and username!= ?";

        suggestions.clear();

        try (Connection connection = DBConnection.getConnection())
        {PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, "%" + query + "%");
        statement.setString(2,SessionManager.getCurrentUserName());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    suggestions.add(username);
                }
            }

            if (suggestions.isEmpty()) {
                suggestions.add("No matching users found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            UtilityMethods.showPopup("An error occurred while fetching user data.");
        }
    }

    private void sendMessage() {
        String receiver = toField.getText().trim();
        String content = mailText.getText().trim();

        if (receiver.isEmpty() || content.isEmpty()) {
            UtilityMethods.showPopupWarning("Please fill in all fields before sending.");
            return;
        }

        int senderId = SessionManager.getCurrentUserId();
        int receiverId = getUserIdByUsername(receiver);

        if (receiverId == -1) {
            UtilityMethods.showPopupWarning("Receiver not found. Please check the username.");
            return;
        }

        Message message = new Message(senderId, receiverId, content);
        MessageDao dao = new MessageDao();

        if (dao.sendMessage(message)) {
            mailText.clear();
            toField.clear();
        } else {
            UtilityMethods.showPopup("Failed to send the message.");
        }

        UtilityMethods.switchToScene(backBtn,"announcements");
    }

    private int getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("user_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @FXML
    public void backAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "announcements");
    }
}
