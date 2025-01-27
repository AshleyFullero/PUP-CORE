package org.htech.universityproject.modal;

import java.sql.Timestamp;

public class Post {

    private String content;
    private String username;
    private int userId;
    private int postId;
    private Timestamp createdAt;
    private  byte[] media;
    private  byte[] profilePicture;

    private String role;
    private String course;
    private String activeSubjects;
    private String subjectsTaught;

    public Post(int userId, int postId,String username,byte[] profilePicture ,String content, Timestamp createdAt, byte[] media) {
        this.userId = userId;
        this.postId = postId;
        this.username = username;
        this.profilePicture = profilePicture;
        this.content=content;
        this.createdAt = createdAt;
        this.media = media;
    }

    public Post(int userId, int postId, String username, byte[] profilePicture, String content, Timestamp createdAt) {
        this.userId = userId;
        this.postId = postId;
        this.username = username;
        this.profilePicture = profilePicture;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public byte[] getMedia() {
        return media;
    }

    public void setMedia(byte[] media) {
        this.media = media;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getActiveSubjects() {
        return activeSubjects;
    }

    public void setActiveSubjects(String activeSubjects) {
        this.activeSubjects = activeSubjects;
    }

    public String getSubjectsTaught() {
        return subjectsTaught;
    }

    public void setSubjectsTaught(String subjectsTaught) {
        this.subjectsTaught = subjectsTaught;
    }
}
