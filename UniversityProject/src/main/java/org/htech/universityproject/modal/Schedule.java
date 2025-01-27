package org.htech.universityproject.modal;

import org.htech.universityproject.dao.ScheduleDao;

import java.time.LocalDateTime;
import java.util.List;

public class Schedule {

    private int scheduleId;
    private int userId;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String type;
    private String date;
    private String location;

    private String status;
    private boolean isCompleted;

    public Schedule(int scheduleId, String title, LocalDateTime start, LocalDateTime end, String type,boolean isCompleted) {
       this.scheduleId=scheduleId;
        this.title = title;
        this.start = start;
        this.end = end;
        this.type = type;
        this.isCompleted=isCompleted;
    }

    public Schedule(String title, LocalDateTime start, LocalDateTime end, String type,  String date, String location) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.type = type;
        this.date = date;
        this.location = location;
    }

    public Schedule(String title, String type, LocalDateTime start, LocalDateTime end, String status) {
        this.title = title;
        this.type = type;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public List<Schedule> getEvents(int userId){
        ScheduleDao scheduleDao = new ScheduleDao();
       return scheduleDao.fetchEventsForUser(userId);

    }

    public  void addTask (Schedule schedule){
        ScheduleDao scheduleDao = new ScheduleDao();
        scheduleDao.addSchedule(schedule);
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String status) {
        this.type = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
