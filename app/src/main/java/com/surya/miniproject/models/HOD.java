package com.surya.miniproject.models;

public class HOD{
    // attributes of a HOD
    private String name;
    private String department;

    // Constructor
    public HOD(){

    }

    public HOD(String name, String department) {
        this.name = name;
        this.department = department;
    }

    // getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
