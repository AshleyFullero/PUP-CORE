package org.htech.universityproject.dao;

import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Event;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EventsDao {

    public void addEvent(Event event) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO events (title, description, event_date, event_type, location , organizer_id , event_image) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, event.getTitle());
            statement.setString(2, event.getDescription());
            statement.setObject(3, event.getDate());
            statement.setObject(4, event.getType());
            statement.setString(5, event.getLocation());
            statement.setInt(6, SessionManager.getCurrentUserId());
            if (event.getImage() != null) {
                File selectedImage = event.getImage();
                FileInputStream fis = new FileInputStream(selectedImage);
                statement.setBinaryStream(7, fis, (int) selectedImage.length());
            } else {
                statement.setNull(7, java.sql.Types.BLOB);
            }

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                UtilityMethods.showPopup("Event added successfully!");

            } else {
                UtilityMethods.showPopupWarning("Failed to add the event");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
