package org.htech.universityproject.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Schedule;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;

public class EventHubController extends Navigation {

    @FXML
    private Button addTaskBtn;

    @FXML
    private Label dateLabel;

    @FXML
    private ListView<HBox> scheduleListView;

    @FXML
    private ListView<HBox> eventHubListView;

    @FXML
    private Label taskCountLabel,usernameLbl;

    Queue<Schedule> schedules ;

    @FXML
    public void initialize() {
        usernameLbl.setText("Hello " + SessionManager.getCurrentUserName());
        YearMonth currentYearMonth = YearMonth.now();
        dateLabel.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());
        addTaskBtn.setOnAction(event -> UtilityMethods.switchToScene(addTaskBtn,"schedule_planner"));
        loadSchedule();
        loadEvents();
    }

    private void loadSchedule() {
        schedules = loadSchedules();
        HBox container;
        for (Schedule schedule : schedules) {
            container = createScheduleView(schedule);
            scheduleListView.getItems().add(container);
        }

    }

    private Queue<Schedule> loadSchedules(){

        Queue<Schedule> schedules = new LinkedList<>();
        String query = "SELECT schedule_id , event_title, start_time, end_time, event_type , is_completed " +
                "FROM schedule WHERE start_time BETWEEN ? AND ? and user_id = ? and is_completed = 0";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            LocalDate currentDate = LocalDate.now();
            preparedStatement.setString(1, currentDate + " 00:00:00");
            preparedStatement.setString(2, currentDate + " 23:59:59");
            preparedStatement.setInt(3, SessionManager.getCurrentUserId());
//            preparedStatement.setInt(4, 0);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                Label emptyLabel = new Label("No schedules for today!");
                emptyLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-padding: 10px;");
                scheduleListView.getItems().add(new HBox(emptyLabel));
                return schedules;
            }
            int taskCount = 0;
            while (resultSet.next()){
                taskCount++;
                int id = resultSet.getInt("schedule_id");
                String title = resultSet.getString("event_title");
                LocalDateTime startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = resultSet.getTimestamp("end_time").toLocalDateTime();
                String type = resultSet.getString("event_type");
                boolean isCompleted = resultSet.getInt("is_completed")==1;

                schedules.add(new Schedule(id,title,startTime,endTime,type,isCompleted));
            }
            taskCountLabel.setText("You have " + taskCount + " tasks scheduled today.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }


    private HBox createScheduleView(Schedule schedule){

        HBox scheduleItem = new HBox(10);
        scheduleItem.setUserData(schedule);
        scheduleItem.setStyle("-fx-alignment: center-left; -fx-padding: 10px; -fx-background-color: #2a2a2a; -fx-border-color: #3a3a3a; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        JFXCheckBox taskCheckBox = new JFXCheckBox();
        Timestamp startTime = Timestamp.valueOf(schedule.getStart());
        Timestamp endTime = Timestamp.valueOf(schedule.getEnd());

        taskCheckBox.setText(schedule.getTitle() + " (" +
                formatTime(startTime.toLocalDateTime().toLocalTime()) + " - " +
                formatTime(endTime.toLocalDateTime().toLocalTime()) + ")");
        taskCheckBox.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        taskCheckBox.setTooltip(new Tooltip("Event: " + schedule.getTitle() + "\nStart: " + startTime + "\nEnd: " + endTime));

        if (schedule.isCompleted()) {
            taskCheckBox.setSelected(true);
        }

        taskCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateTaskCompletion(schedule.getScheduleId(), newValue);
        });

        ProgressBar progressBar = new ProgressBar();
        progressBar.setMaxWidth(30);
        progressBar.setProgress(1.0);
        progressBar.setStyle(getProgressBarStyle(schedule.getType()));
        progressBar.setPrefWidth(150);

        Label statusLabel = new Label("[" + schedule.getType().toUpperCase() + "]");
        statusLabel.setStyle("-fx-text-fill: lightblue; -fx-font-size: 12px; -fx-padding: 5px;");

        JFXButton edit = new JFXButton();
        FontAwesomeIcon icon = new FontAwesomeIcon();
        icon.setIcon(FontAwesomeIcons.EDIT);
        edit.setGraphic(icon);
        icon.setFill(Color.GRAY);
        edit.setOnAction(event -> navigateToAddSchedule(schedule));
        scheduleItem.getChildren().addAll(taskCheckBox, progressBar, statusLabel,edit);

        return scheduleItem;
    }

    private void updateTaskCompletion(int scheduleId, boolean isCompleted) {
        String updateQuery = "UPDATE schedule SET is_completed = ? WHERE schedule_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setInt(1, isCompleted ? 1 : 0);
            preparedStatement.setInt(2, scheduleId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                UtilityMethods.showPopupCenter("Task updated successfully!");

                if (isCompleted) {
                    scheduleListView.getItems().removeIf(hbox -> {

                        Schedule schedule = (Schedule) hbox.getUserData();
                        return schedule != null && schedule.getScheduleId() == scheduleId;
                    });
                    taskCountLabel.setText("You have " + scheduleListView.getItems().size() + " tasks scheduled today.");
                }
            } else {
                UtilityMethods.showPopupWarning("Failed to update task");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void navigateToAddSchedule(Schedule schedule) {
        try {
            FXMLLoader loader = new FXMLLoader(UtilityMethods.class.getResource("/views/add_schedule.fxml"));
            Parent root = loader.load();

            AddScheduleController controller = loader.getController();
            controller.populateFields(schedule);
            Stage oldStage = (Stage) addTaskBtn.getScene().getWindow();
            oldStage.close();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEvents() {
        String query = "SELECT e.title, e.description, e.event_date, e.event_type, e.event_image, u.username AS organizer_name " +
                "FROM events e " +
                "JOIN users u ON e.organizer_id = u.user_id " +
                "WHERE e.event_date >= ? ORDER BY e.event_date";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            LocalDate currentDate = LocalDate.now();
            preparedStatement.setString(1, currentDate.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            eventHubListView.getItems().clear();

            if (!resultSet.isBeforeFirst()) {
                Label emptyLabel = new Label("No events found!");
                emptyLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-padding: 10px;");
                eventHubListView.getItems().add(new HBox(emptyLabel));
                return;
            }

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                Date eventDate = resultSet.getDate("event_date");
                String eventType = resultSet.getString("event_type");
                byte[] imageData = resultSet.getBytes("event_image");
                String organizerName = resultSet.getString("organizer_name"); // Fetch the organizer's name

                HBox eventItem = new HBox(10);
                eventItem.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10; -fx-border-color: #800000; " +
                        "-fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
                eventItem.setSpacing(10);

                if (imageData != null) {
                    Image eventImage = new Image(new ByteArrayInputStream(imageData));
                    ImageView imageView = new ImageView(eventImage);
                    imageView.setFitWidth(140);
                    imageView.setFitHeight(140);
                    imageView.setPreserveRatio(true);

                    eventItem.getChildren().add(imageView);
                }

                VBox eventBox = new VBox(5);
                eventBox.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10; -fx-border-color: #800000; " +
                        "-fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");

                Label titleLabel = new Label(title);
                titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #800000; -fx-font-size: 14px;");

                Label dateLabel = new Label("Date: " + eventDate.toString());
                dateLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 12px;");

                Label typeLabel = new Label("Type: " + eventType);
                typeLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

                eventBox.getChildren().addAll(titleLabel, dateLabel, typeLabel);

                if (description != null && !description.isEmpty()) {
                    Label descriptionLabel = new Label(description);
                    descriptionLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 12px;");
                    eventBox.getChildren().add(descriptionLabel);
                }

                HBox.setHgrow(eventBox, Priority.ALWAYS);
                eventItem.getChildren().add(eventBox);

                Tooltip organizerTooltip = new Tooltip("Organized by: " + organizerName);
                Tooltip.install(eventItem, organizerTooltip);

                eventHubListView.getItems().add(eventItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    private double getProgress(String status) {
//        return switch (status.toLowerCase()) {
//            case "deadline", "facultymeeting" -> 1.0;
//            case "extracurricular", "consultation" ->0.66;
//            case "classschedule", "lecture" -> 0.33;
//            default -> 0.0;
//        };
//    }

    private String getProgressBarStyle(String status) {
        switch (status.toLowerCase()) {
            case "deadline", "facultymeeting":
                return "-fx-accent: red;";
            case "extracurricular", "consultation":
                return "-fx-accent: green;";
            case "classschedule", "lecture":
                return "-fx-accent: orange;";
            default:
                return "-fx-accent: gray;";
        }
    }

    private String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }
}
