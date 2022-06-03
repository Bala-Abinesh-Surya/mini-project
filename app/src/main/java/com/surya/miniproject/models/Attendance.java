package com.surya.miniproject.models;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Hashtable;

public class Attendance {
    // attribute of an Attendance
    private String className;
    private String editedBy;
    private Hashtable<String, String> table;
    private String json;
    private String date;
    private Month month;
    private int day;

    // Constructor
    public Attendance() {

    }

    public Attendance(String className, String date) {
        this.className = className;
        this.date = date;

        month = LocalDateTime.now().getMonth();
        day = LocalDateTime.now().getDayOfMonth();
    }

    // getter and setter methods
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }

    public Hashtable<String, String> getTable() {
        return table;
    }

    public void setTable(Hashtable<String, String> table) {
        this.table = table;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
