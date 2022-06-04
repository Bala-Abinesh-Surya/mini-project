package com.surya.miniproject.models;

import static com.surya.miniproject.constants.Strings.NOTIFICATION_UPDATE;

public class Notification {
    // attributes of a Notification
    private String notificationDepartment;
    private String notifiedBy;
    private String className;
    private String date;
    private String category;

    // Constructor
    public Notification() {

    }

    public Notification(String notificationDepartment, String notifiedBy, String className, String date) {
        this.notificationDepartment = notificationDepartment;
        this.notifiedBy = notifiedBy;
        this.className = className;
        this.date = date;
    }

    // message from the notification
    public String returnNotificationMessage(){
        if(category.equals(NOTIFICATION_UPDATE)){
            return notifiedBy + " updated the " + className + "'s attendance";
        }

        // unreachable statement
        return null;
    }

    // getter and setter methods
    public String getNotificationDepartment() {
        return notificationDepartment;
    }

    public void setNotificationDepartment(String notificationDepartment) {
        this.notificationDepartment = notificationDepartment;
    }

    public String getNotifiedBy() {
        return notifiedBy;
    }

    public void setNotifiedBy(String notifiedBy) {
        this.notifiedBy = notifiedBy;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
