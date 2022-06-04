package com.surya.miniproject.models;

import java.util.ArrayList;

public class Faculty {
    // attributes of a Faculty
    private String facultyName;
    private String facultyGender;
    private String facultyDepartment;
    private String facultyPushId;
    private String facultyUserName;
    private String facultyPassword;
    private boolean isTheStaffAHod;

    // list of all the Faculties
    public static ArrayList<Faculty> allFacultiesList = new ArrayList<>();

    // Constructor
    public Faculty() {

    }

    public Faculty(String facultyName, String facultyGender, String facultyDepartment) {
        this.facultyName = facultyName;
        this.facultyGender = facultyGender;
        this.facultyDepartment = facultyDepartment;

        // setting the initial username and password
        // staffName is in the format, "Mr. Viswanath Shenoi"
        facultyUserName = facultyName.split(" ")[1].toLowerCase(); // viswanath
        facultyPassword = "123456";
    }

    // getter and setter methods
    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getFacultyGender() {
        return facultyGender;
    }

    public void setFacultyGender(String facultyGender) {
        this.facultyGender = facultyGender;
    }

    public String getFacultyPushId() {
        return facultyPushId;
    }

    public void setFacultyPushId(String facultyPushId) {
        this.facultyPushId = facultyPushId;
    }

    public String getFacultyUserName() {
        return facultyUserName;
    }

    public void setFacultyUserName(String facultyUserName) {
        this.facultyUserName = facultyUserName;
    }

    public String getFacultyPassword() {
        return facultyPassword;
    }

    public void setFacultyPassword(String facultyPassword) {
        this.facultyPassword = facultyPassword;
    }

    public String getFacultyDepartment() {
        return facultyDepartment;
    }

    public void setFacultyDepartment(String facultyDepartment) {
        this.facultyDepartment = facultyDepartment;
    }

    public boolean isTheStaffAHod() {
        return isTheStaffAHod;
    }

    public void setTheStaffAHod(boolean theStaffAHod) {
        isTheStaffAHod = theStaffAHod;
    }
}
