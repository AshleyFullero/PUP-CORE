package org.htech.universityproject.modal;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.File;
import java.time.LocalDateTime;

public class Professor extends User {

    private String department;
    private String office_hours;
    private String subjects_taught;

    public Professor(String department, String office_hours, String subjects_taught) {
        this.department = department;
        this.office_hours = office_hours;
        this.subjects_taught = subjects_taught;
    }

    public Professor(int userId, String username, String password, String email) {
        super(userId, username, password, email, "professor");
    }

    public Professor(int userId, String username, String password, String email, String role, String department, String office_hours, String subjects_taught) {
        super(userId, username, password, email, role);
        this.department = department;
        this.office_hours = office_hours;
        this.subjects_taught = subjects_taught;
    }

    /**
     * Professors access their department,
     * office hours,
     * and subjects they teach
     */
    @Override
    public void viewProfile(ActionEvent event){
        UtilityMethods.switchToScene(event,"profile");
    }

    /**
     * Professor can update email address and department
     * @param contactInfo email address
     */
    @Override
    public void updateProfile(ActionEvent event,String contactInfo, String department) {
        UtilityMethods.switchToScene(event,"add_professor_details");
    }

    /**
     * Professor can update email address
     * @param contactInfo email address
     */
    @Override
    public void updateProfile(ActionEvent event,String contactInfo) {
        UtilityMethods.switchToScene(event,"add_professor_details");
    }

    @Override
    public boolean sendMessage(int recipientId, String content) {
        Message message = new Message(recipientId,content);
        return message.sendMessage(message);
    }

    @Override
    public void addSchedule(String title, String description) {

    }

    @Override
    public void addSchedule(String title, LocalDateTime start, LocalDateTime end, String type, String date, String location) {
        Schedule schedule = new Schedule(title,start,end,type,date,location);
        schedule.addTask(schedule);
    }

    @Override
    public void addEvent(String title, String date) {

    }

    public void addEvent(String title, String description, String date, String type, String location){
        Event event = new Event(title,description,date,type,location);
        event.addEvent(event);
    }

    public void addEvent(String title, String description, String date, String type, String location , File image){
        Event event = new Event(title,description,date,type,location,image);
        event.addEvent(event);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOffice_hours() {
        return office_hours;
    }

    public void setOffice_hours(String office_hours) {
        this.office_hours = office_hours;
    }

    public String getSubjects_taught() {
        return subjects_taught;
    }

    public void setSubjects_taught(String subjects_taught) {
        this.subjects_taught = subjects_taught;
    }


    // overloading:
    public void addAchievement(String title){
        Achievement achievement = new Achievement(title,"",AchievementType.KeyNote);
        achievement.addAchievement(achievement);
    }
    // overloading:
    public void addAchievement(String title,String description , AchievementType type){
        Achievement achievement = new Achievement(title,description,type);
        achievement.addAchievement(achievement);
    }

    @Override
    public void addAchievement(String title, String description, String date, AchievementType type) {
        Achievement achievement = new Achievement(title,description,date,type);
        achievement.addAchievement(achievement);

    }


    public void viewProfile(MouseEvent event) {
        UtilityMethods.switchToScene(event,"profile");
    }
}
