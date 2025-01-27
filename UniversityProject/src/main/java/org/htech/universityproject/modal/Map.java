package org.htech.universityproject.modal;

public abstract class Map {

    private String start;
    private String end;
    private String mode;

    /*
    Students see classrooms, libraries, and food stalls (with menu descriptions).
    Professors focus on faculty lounges, offices, and meeting rooms
     */

    public abstract void showLocationDetails();

    public void findPath(String start, String end){

    }

    public void findPath(String start, String end, String mode){

    }
}
