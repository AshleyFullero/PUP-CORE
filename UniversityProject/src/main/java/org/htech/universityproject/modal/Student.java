package org.htech.universityproject.modal;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class Student extends User  {

    private String level;
    private String course;
    private String year;
    private String activeSubjects;

    public Student(int userId, String username, String password, String email) {
        super(userId, username, password, email, "student");
    }

    public Student(int userId, String username, String password, String email, String role, String level, String course, String year, String activeSubjects) {
        super(userId, username, password, email, role);
        this.level = level;
        this.course = course;
        this.year = year;
        this.activeSubjects = activeSubjects;
    }

    /**
     * Students see academic details
     * like their course, year level,
     * and active subjects.
     */
    @Override
    public void viewProfile(ActionEvent event){
        UtilityMethods.switchToScene(event,"profile");
    }

    /**
     * Student can update email address and course
     * @param contactInfo email address
     * @param event window event
     */
    public void updateProfile(ActionEvent event,String contactInfo, String departmentOrCourse) {
        UtilityMethods.switchToScene(event,"add_student_details");
    }

    /**
     * Student can update email address
     * @param contactInfo email address
     */
    public void updateProfile(ActionEvent event,String contactInfo) {
        UtilityMethods.switchToScene(event,"add_student_details");
    }

    @Override
    public boolean sendMessage(int receiverId, String content) {
        Message message = new Message(receiverId,content);
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

    public void sendMessage(List<String> recipients){

    }

    //Overloading:
    public void addEvent(String title, String time){

    }
    //Overloading:

    public void addEvent(String title, String description, String date, String type, String location){
        Event event = new Event(title,description,date,type,location);
        event.addEvent(event);
    }

    public void addEvent(String title, String description, String date, String type, String location , File image){
        Event event = new Event(title,description,date,type,location,image);
        event.addEvent(event);
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getActiveSubjects() {
        return activeSubjects;
    }

    public void setActiveSubjects(String activeSubjects) {
        this.activeSubjects = activeSubjects;
    }


    // overloading:
    public void addAchievement(String title){
        Achievement achievement = new Achievement(title,"",AchievementType.Award);
        achievement.addAchievement(achievement);
    }

    // overloading:
    public void addAchievement(String title,String description , AchievementType type){
        Achievement achievement = new Achievement(title,description,type);
        achievement.addAchievement(achievement);
    }

    @Override
    public void addAchievement(String title, String description, String date , AchievementType type) {
        Achievement achievement = new Achievement(title,description,date,type);
        achievement.addAchievement(achievement);
    }

    public void viewProfile(MouseEvent event) {
        UtilityMethods.switchToScene(event,"profile");
    }
}
