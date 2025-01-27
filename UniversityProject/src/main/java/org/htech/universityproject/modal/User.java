package org.htech.universityproject.modal;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.time.LocalDateTime;

public abstract class User {

    private int userId;
    private String username;
    private String password;
    private String email;
    private String role;

    public User() {
    }

    public User(int userId, String username, String password, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Overriding:
    public void viewProfile(ActionEvent event){

    }
    // Overloading:
    public abstract void updateProfile(ActionEvent event,String contactInfo, String departmentOrCourse);
    public abstract void updateProfile(ActionEvent event,String contactInfo);
    public abstract boolean sendMessage(int receiverId, String message);
    public abstract void addSchedule(String title, String description);
    public abstract void addSchedule(String title, LocalDateTime start, LocalDateTime end, String type, String date, String location);
    public abstract void addEvent(String title, String date);
    public abstract void addEvent(String title, String description, String date, String type, String location);
    public abstract void addEvent(String title, String description, String date, String type, String location , File image);
    public abstract void addAchievement(String title);
    public abstract void addAchievement(String title, String description,AchievementType type);
    public abstract void addAchievement(String title, String description, String date ,AchievementType type);
}
