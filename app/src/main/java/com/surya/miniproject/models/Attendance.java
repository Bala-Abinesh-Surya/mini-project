package com.surya.miniproject.models;

import java.util.ArrayList;
import java.util.Date;

public class Attendance {
    // attributes of an Attendance
    private Date date;
    private String className;
    private String pushId;
    private ArrayList<String> attendance = new ArrayList<>();
    private String editedBy;

    // Constructor
    public Attendance() {

    }

    public Attendance(Date date, String className, ArrayList<String> attendance) {
        this.date = date;
        this.className = className;
        this.attendance = attendance;

        // pushId is same as the className
        pushId = className;
    }

    // getter and setter methods
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public ArrayList<String> getAttendance() {
        return attendance;
    }

    public void setAttendance(ArrayList<String> attendance) {
        this.attendance = attendance;
    }

    public String getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }
}
