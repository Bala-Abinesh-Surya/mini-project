package com.surya.miniproject.models;

import java.util.ArrayList;

public class Faculty {
    // attributes of a Faculty
    private String facultyName;
    private String facultyGender;
    private boolean isTheFacultyAClassAdvisor;
    private String userName;
    private String password;

    // list of all faculties
    public static final ArrayList<Faculty> allFacultyList = new ArrayList<>();

    // Constructor
    public Faculty(String facultyName, String facultyGender, boolean isTheFacultyAClassAdvisor) {
        this.facultyName = facultyName;
        this.facultyGender = facultyGender;
        this.isTheFacultyAClassAdvisor = isTheFacultyAClassAdvisor;

        // setting the initial username and password
        userName = facultyName.split(" ")[0].trim().toLowerCase();
        password = "123456";
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

    public boolean isTheFacultyAClassAdvisor() {
        return isTheFacultyAClassAdvisor;
    }

    public void setTheFacultyAClassAdvisor(boolean theFacultyAClassAdvisor) {
        isTheFacultyAClassAdvisor = theFacultyAClassAdvisor;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
