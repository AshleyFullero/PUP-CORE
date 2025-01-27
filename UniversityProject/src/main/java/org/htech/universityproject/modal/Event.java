package org.htech.universityproject.modal;

import org.htech.universityproject.dao.EventsDao;

import java.io.File;

public class Event {

    private String title;
    private String location;
    private String date;
    private String description;
    private String type;
    private File image;


    public Event(String title, String description, String date, String type, String location) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
        this.location = location;
    }

    public Event(String title, String description, String date, String type, String location, File image) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
        this.location = location;
        this.image = image;
    }

    public void addEvent(Event event){
        EventsDao eventsDao = new EventsDao();
        eventsDao.addEvent(event);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
