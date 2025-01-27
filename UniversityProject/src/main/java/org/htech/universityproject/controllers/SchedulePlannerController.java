package org.htech.universityproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SchedulePlannerController extends Navigation {

    @FXML
    private Button addTaskButton;
    @FXML
    private GridPane calendarGrid;

    @FXML
    private Label monthYearLabel;

    @FXML
    private Button prevMonthButton, nextMonthButton,openSettingsButton;

    @FXML
    private Label selectedDateLabel, taskCountLabel;

    @FXML
    private Label prevDayLabel, currentDayLabel, nextDayLabel;

    private LocalDate currentDate;


    private YearMonth currentMonth;
    private Map<LocalDate, String> notesMap;
    private LocalDate selectedDate;

    private String weekdayColor = "#000000";
    private String weekendColor = "#FF0000";
    private String selectedDateColor = "#ADD8E6";
    private String dateWithNoteColor = "#FFECD1";
//    private String dateWithNoteColor = "#801000";
    private int fontSize = 20;
    private boolean isDarkMode = false;

    @FXML
    public void initialize() {
        currentMonth = YearMonth.now();
        notesMap = new HashMap<>();
        currentDate = LocalDate.now();
        updateCalendar();
        updateTodayTaskSection();
        updateDateNavigation();
        fetchEventsForUser(SessionManager.getCurrentUserId());
        addTaskButton.setOnAction(event -> UtilityMethods.switchToScene(addTaskButton,"add_schedule"));

        prevMonthButton.setOnAction(event -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
        });

        nextMonthButton.setOnAction(event -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
        });

        prevDayLabel.setOnMouseClicked(event -> {
            currentDate = currentDate.minusDays(1);
            updateTodayTaskSection();
            updateDateNavigation();
        });

        nextDayLabel.setOnMouseClicked(event -> {
            currentDate = currentDate.plusDays(1);
            updateTodayTaskSection();
            updateDateNavigation();
        });

        openSettingsButton.setOnAction(event -> openSettingsPopup());
    }


    private void updateTodayTaskSection() {
        selectedDateLabel.setText(currentDate.getMonth() + ", " + currentDate.getDayOfMonth());
    }

    private void updateDateNavigation() {
        prevDayLabel.setText(formatDateLabel(currentDate.minusDays(1)));
        currentDayLabel.setText(formatDateLabel(currentDate));
        nextDayLabel.setText(formatDateLabel(currentDate.plusDays(1)));
    }

    private String formatDateLabel(LocalDate date) {
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        return date.getDayOfMonth() + " " + dayName;
    }


    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        monthYearLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

        String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < weekdays.length; i++) {
            Label weekdayLabel = new Label(weekdays[i]);
            weekdayLabel.setStyle("-fx-font-weight: bold; -fx-font-size: " + fontSize + "px;");
            if (i == 0 || i == 6) {
                weekdayLabel.setStyle(weekdayLabel.getStyle() + "-fx-text-fill: " + weekendColor + ";");
            } else {
                weekdayLabel.setStyle(weekdayLabel.getStyle() + "-fx-text-fill: " + weekdayColor + ";");
            }
            calendarGrid.add(weekdayLabel, i, 0);
        }

        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentMonth.lengthOfMonth();

        int row = 1;
        int col = dayOfWeek;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            StackPane cell = new StackPane();
            enableDragAndDrop(cell, date);
            String defaultStyle = "-fx-border-color: #ddd; -fx-padding: 10; -fx-background-color: " +
                    (isDarkMode ? "#444444;" : "#f9f9f9;");
            cell.setStyle(defaultStyle);

            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setStyle("-fx-font-size: " + fontSize + "px; -fx-font-weight: bold;");

            if (date.getDayOfWeek().getValue() % 7 == 0 || date.getDayOfWeek().getValue() % 7 == 6) { // Weekend
                dayLabel.setStyle(dayLabel.getStyle() + "-fx-text-fill: " + weekendColor + ";");
            } else {
                dayLabel.setStyle(dayLabel.getStyle() + "-fx-text-fill: " + weekdayColor + ";");
            }

            if (notesMap.containsKey(date)) {
                cell.setStyle(cell.getStyle() + "-fx-background-color: " + dateWithNoteColor + ";");
                Tooltip tooltip = new Tooltip("Day: " + date.getDayOfWeek().name() + "\nNote: " + notesMap.get(date));
                Tooltip.install(cell, tooltip);
            }
            else{
                Tooltip tooltip = new Tooltip("Day: " + date.getDayOfWeek().name());
                Tooltip.install(cell, tooltip);
            }

            cell.setOnMouseEntered(event -> {
                String previousStyle = cell.getStyle();
                cell.setStyle(previousStyle + "-fx-background-color: #d3d3f7;");
            });

            cell.setOnMouseExited(event -> {
                String restoredStyle = defaultStyle;
                if (selectedDate != null && selectedDate.equals(date)) {
                    restoredStyle = defaultStyle + "-fx-background-color: " + selectedDateColor + ";";
                } else if (notesMap.containsKey(date)) {
                    restoredStyle = defaultStyle + "-fx-background-color: " + dateWithNoteColor + ";";
                } else {
                    restoredStyle = defaultStyle + "-fx-background-color: " +
                            (isDarkMode ? "#444444;" : "#f9f9f9;");
                }
                cell.setStyle(restoredStyle);
            });


            cell.setOnMouseClicked(event -> {
                if (selectedDate != null) {
                    updateCalendar();
                }
                selectedDate = date;
                cell.setStyle(defaultStyle + "-fx-background-color: " + selectedDateColor + ";");
            });

            cell.getChildren().add(dayLabel);
            calendarGrid.add(cell, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private void fetchEventsForUser(int userId) {
        String query = "SELECT event_title, event_type, start_time, end_time, event_status FROM schedule WHERE user_id = ? and is_completed = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2,0);
            ResultSet resultSet = statement.executeQuery();
            int taskCount=0;
            while (resultSet.next()) {
                taskCount++;
                String title = resultSet.getString("event_title");
                String type = resultSet.getString("event_type");
                LocalDate startDate = resultSet.getTimestamp("start_time").toLocalDateTime().toLocalDate();
                LocalDate endDate = resultSet.getTimestamp("end_time").toLocalDateTime().toLocalDate();
                String status = resultSet.getString("event_status");

                notesMap.put(endDate, title + " - " + type + " (" + status + ")");
            }
            updateCalendar();
            taskCountLabel.setText(taskCount + " task" + (taskCount == 1 ? "" : "s") + " pending!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }


    private void enableDragAndDrop(StackPane cell, LocalDate date) {
        cell.setOnDragDetected(event -> {
            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(date.toString());
            db.setContent(content);
            event.consume();
        });

        cell.setOnDragOver(event -> {
            if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        cell.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String droppedDate = db.getString();
                notesMap.put(date, notesMap.remove(LocalDate.parse(droppedDate)));
                updateCalendar();
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(currentDayLabel,"event_hub");
    }


    private void openSettingsPopup() {
        Stage settingsStage = new Stage();
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.initOwner(openSettingsButton.getScene().getWindow());
        settingsStage.setTitle("Settings");

        VBox settingsBox = new VBox(10);
        settingsBox.setStyle("-fx-padding: 20;");

        Label lightDarkLabel = new Label("Theme:");
        ToggleGroup themeGroup = new ToggleGroup();
        RadioButton lightModeButton = new RadioButton("Light");
        lightModeButton.setToggleGroup(themeGroup);
        lightModeButton.setSelected(!isDarkMode);
        RadioButton darkModeButton = new RadioButton("Dark");
        darkModeButton.setToggleGroup(themeGroup);
        darkModeButton.setSelected(isDarkMode);

        lightModeButton.setOnAction(event -> {
            isDarkMode = false;
            updateCalendar();
        });

        darkModeButton.setOnAction(event -> {
            isDarkMode = true;
            updateCalendar();
        });

        Label fontSizeLabel = new Label("Font Size:");
        Spinner<Integer> fontSizeSpinner = new Spinner<>(12, 36, fontSize);
        fontSizeSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            fontSize = newVal;
            updateCalendar();
        });

        Label weekdayColorLabel = new Label("Weekday Color:");
        ColorPicker weekdayColorPicker = new ColorPicker(Color.valueOf(weekdayColor));
        weekdayColorPicker.setOnAction(event -> {
            weekdayColor = toHexString(weekdayColorPicker.getValue());
            updateCalendar();
        });

        Label weekendColorLabel = new Label("Weekend Color:");
        ColorPicker weekendColorPicker = new ColorPicker(Color.valueOf(weekendColor));
        weekendColorPicker.setOnAction(event -> {
            weekendColor = toHexString(weekendColorPicker.getValue());
            updateCalendar();
        });

        Label selectedDateColorLabel = new Label("Selected Date Color:");
        ColorPicker selectedDateColorPicker = new ColorPicker(Color.valueOf(selectedDateColor));
        selectedDateColorPicker.setOnAction(event -> {
            selectedDateColor = toHexString(selectedDateColorPicker.getValue());
            updateCalendar();
        });

        Label dateWithNoteColorLabel = new Label("Date With Note Color:");
        ColorPicker dateWithNoteColorPicker = new ColorPicker(Color.valueOf(dateWithNoteColor));
        dateWithNoteColorPicker.setOnAction(event -> {
            dateWithNoteColor = toHexString(dateWithNoteColorPicker.getValue());
            updateCalendar();
        });

        settingsBox.getChildren().addAll(
                lightDarkLabel, lightModeButton, darkModeButton,
                fontSizeLabel, fontSizeSpinner,
                weekdayColorLabel, weekdayColorPicker,
                weekendColorLabel, weekendColorPicker,
                selectedDateColorLabel, selectedDateColorPicker,
                dateWithNoteColorLabel, dateWithNoteColorPicker
        );

        Scene scene = new Scene(settingsBox, 350, 450);
        settingsStage.setScene(scene);
        settingsStage.centerOnScreen();
        settingsStage.show();
    }

    public void backAction(ActionEvent event) {
        UtilityMethods.switchToScene(addTaskButton,"event_hub");
    }
}
