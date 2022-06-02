package com.surya.miniproject.models;

import java.util.ArrayList;
import java.util.List;

public class Class {
    // attributes of a Class
    private String className;
    private String classDepartment;
    private int year;
    private String classAdvisor;
    private String classPushId;
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<String> facultyMembers = new ArrayList<>();

    // list of all the Classes
    public static ArrayList<Class> allClassesList = new ArrayList<>();

    // Constructor
    public Class() {

    }

    public Class(String className, String classDepartment, int year, String classAdvisor) {
        this.className = className;
        this.classDepartment = classDepartment;
        this.year = year;
        this.classAdvisor = classAdvisor;
    }

    // getter and setter methods
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDepartment() {
        return classDepartment;
    }

    public void setClassDepartment(String classDepartment) {
        this.classDepartment = classDepartment;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getClassAdvisor() {
        return classAdvisor;
    }

    public void setClassAdvisor(String classAdvisor) {
        this.classAdvisor = classAdvisor;
    }

    public String getClassPushId() {
        return classPushId;
    }

    public void setClassPushId(String classPushId) {
        this.classPushId = classPushId;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public ArrayList<String> getFacultyMembers() {
        return facultyMembers;
    }

    public void setFacultyMembers(ArrayList<String> facultyMembers) {
        this.facultyMembers = facultyMembers;
    }
}
