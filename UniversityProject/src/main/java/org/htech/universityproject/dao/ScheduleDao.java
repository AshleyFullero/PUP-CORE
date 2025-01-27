package org.htech.universityproject.dao;

import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Schedule;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDao {

    public void addSchedule(Schedule schedule) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO schedule (user_id, event_title, start_time, end_time, event_type, location, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, SessionManager.getCurrentUserId());
            statement.setString(2, schedule.getTitle());
            statement.setObject(3, schedule.getStart());
            statement.setObject(4, schedule.getEnd());
            statement.setObject(5, schedule.getType());
            statement.setString(6, schedule.getLocation());
            statement.setString(7, schedule.getDate());

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

    public List<Schedule> fetchEventsForUser(int userId) {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT event_title, event_type, start_time, end_time, event_status FROM schedule WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("event_title");
                String type = resultSet.getString("event_type");
                LocalDate startDate = resultSet.getTimestamp("start_time").toLocalDateTime().toLocalDate();
                LocalDate endDate = resultSet.getTimestamp("end_time").toLocalDateTime().toLocalDate();
                String status = resultSet.getString("event_status");

                schedules.add(new Schedule(title, type, startDate.atStartOfDay(), endDate.atStartOfDay(), status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedules;
    }
}
