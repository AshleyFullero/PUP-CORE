package org.htech.universityproject.dao;

import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Message;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageDao {

    public boolean sendMessage(Message message){

        String query = "INSERT INTO messages (sender_id, receiver_id, content, timestamp) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, SessionManager.getCurrentUserId());
            statement.setInt(2, message.getReceiverId());
            statement.setString(3, message.getContent());
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                UtilityMethods.showPopup("Message sent successfully!");
                return true;
            } else {
                UtilityMethods.showPopupWarning("Failed to send message.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            UtilityMethods.showPopupWarning("Error occurred while sending the message.");
        }
        return false;
    }

    public List<String> getMessages(int receiverId) {
        List<String> messages = new ArrayList<>();
        String query = "SELECT m.sender_id, m.content, m.timestamp, u.username " +
                "FROM messages m " +
                "JOIN users u ON m.sender_id = u.user_id " +
                "WHERE (m.sender_id = ? AND m.receiver_id = ?) OR (m.sender_id = ? AND m.receiver_id = ?) " +
                "ORDER BY m.timestamp ASC";

        int userId = SessionManager.getCurrentUserId();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, receiverId);
            statement.setInt(3, receiverId);
            statement.setInt(4, userId);

            ResultSet resultSet = statement.executeQuery();


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yy");

            while (resultSet.next()) {
                int senderId = resultSet.getInt("sender_id");
                String content = resultSet.getString("content");
                Timestamp timestamp = resultSet.getTimestamp("timestamp");
                String senderUsername = resultSet.getString("username");

                String formattedDate = dateFormat.format(timestamp);

                String message = String.format("[%s] %s: %s",
                        formattedDate,
                        (senderId == userId ? "You" : senderUsername),
                        content);

                messages.add(message);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return messages;
    }

    public Queue<Message> getMessages(){
        Queue<Message> messages = new LinkedList<>();
        String sql = "SELECT m.message_id, m.sender_id , m.content, m.timestamp, u.username, u.profile_picture " +
                "FROM messages m " +
                "JOIN users u ON m.sender_id = u.user_id " +
                "WHERE m.receiver_id = ? " +
                "ORDER BY m.timestamp DESC LIMIT 5";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, SessionManager.getCurrentUserId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int receiverId = resultSet.getInt("sender_id");
                String content = resultSet.getString("content");
                Timestamp timestamp = resultSet.getTimestamp("timestamp");
                String username = resultSet.getString("username");
                byte[] profilePicBytes = resultSet.getBytes("profile_picture");
                Message message = new Message(receiverId,content,timestamp,username,profilePicBytes);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            UtilityMethods.showPopupWarning("Failed to load messages!");
        }
        return messages;
    }
}
