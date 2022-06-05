package com.surya.miniproject.export;

import static com.surya.miniproject.constants.Strings.CSE_DEPARTMENT;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;

import com.surya.miniproject.models.Student;

import java.util.ArrayList;

public class Export {
    // class to hold the methods for the monthly export of the PDFs
    private String className;
    private String classAdvisor;
    private String facultyName;
    private String department;
    private String month;
    private int year;
    private ArrayList<Student> studentArrayList;
    private int numberOfStudents;

    private final int pageWidth = 595;
    private final int pageHeight = 842;

    // Constructor
    public Export(String className, String classAdvisor, String department, ArrayList<Student> students, String facultyName, String month, int year, int numberOfStudents) {
        this.className = className;
        this.classAdvisor = classAdvisor;
        this.department = department;
        this.studentArrayList = students;
        this.facultyName = facultyName;
        this.month = month;
        this.year = year;
        this.numberOfStudents = numberOfStudents;
    }

    // method to return the department for the pdf
    // say, CSE DEPARTMENT input na, output - DEPARTMENT OF COMPUTER SCIENCE AND ENGINEERING
    public String PDFDepartment(String department){
        if(department.contains(CSE_DEPARTMENT)){
            return "DEPARTMENT OF COMPUTER SCIENCE AND ENGINEERING";
        }

        return null;
    }

    // getter and setter methods
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<Student> getStudentArrayList() {
        return studentArrayList;
    }

    public void setStudentArrayList(ArrayList<Student> studentArrayList) {
        this.studentArrayList = studentArrayList;
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public String getClassAdvisor() {
        return classAdvisor;
    }

    public void setClassAdvisor(String classAdvisor) {
        this.classAdvisor = classAdvisor;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }
}
