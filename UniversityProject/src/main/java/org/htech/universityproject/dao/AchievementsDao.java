package org.htech.universityproject.dao;

import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Achievement;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AchievementsDao {

    public void addAchievementToDB(Achievement achievement) {
        try (Connection connection = DBConnection.getConnection()) {
            String insertQuery = "INSERT INTO achievements (user_id, title, description, achievement_date, achievement_type_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);

            int userId = SessionManager.getCurrentUserId();


            statement.setInt(1, userId);
            statement.setString(2, achievement.getTitle());
            statement.setString(3, achievement.getDescription());
            if (achievement.getDate() != null) {
                statement.setDate(4, java.sql.Date.valueOf(achievement.getDate()));
            } else {
                statement.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            }
            statement.setInt(5, achievement.getType().ordinal() + 1);

            statement.executeUpdate();
            UtilityMethods.showPopup("Achievement saved successfully!");


        } catch (SQLException e) {
            UtilityMethods.showPopupWarning("Database error: " + e.getMessage());
        }

    }
}
