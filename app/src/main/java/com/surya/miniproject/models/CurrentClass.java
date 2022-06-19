package com.surya.miniproject.models;

import java.util.ArrayList;

public class CurrentClass {
    // attributes of a currently opened class
    public static String className;
    public static String department;
    public static String classPushId;
    public static String classAdvisor;
    public static ArrayList<String> currentClassFacultyMember = new ArrayList<>();
    public static ArrayList<String> currentFacultyHandlingClasses = new ArrayList<>();

    // Constructor
    public CurrentClass(String className, String department, String classPushId, String classAdvisor) {
        CurrentClass.className = className;
        CurrentClass.department = department;
        CurrentClass.classPushId = classPushId;
        CurrentClass.classAdvisor = classAdvisor;
    }
}
