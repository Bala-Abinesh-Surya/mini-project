package com.surya.miniproject.models;

public class Student {
    // attributes of a Student
    private String studentName;
    private String studentRegNo;
    private String studentClass;

    // Constructor
    public Student() {

    }

    public Student(String studentName, String studentRegNo, String studentClass) {
        this.studentName = studentName;
        this.studentRegNo = studentRegNo;
        this.studentClass = studentClass;
    }

    // getter and setter methods
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRegNo() {
        return studentRegNo;
    }

    public void setStudentRegNo(String studentRegNo) {
        this.studentRegNo = studentRegNo;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }
}
