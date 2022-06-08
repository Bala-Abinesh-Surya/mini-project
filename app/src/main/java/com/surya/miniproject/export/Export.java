package com.surya.miniproject.export;

import static com.surya.miniproject.constants.Strings.ACADEMIC_YEAR;
import static com.surya.miniproject.constants.Strings.ATTENDANCE;
import static com.surya.miniproject.constants.Strings.CLASS_ADVISOR_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.COLLEGE_NAME;
import static com.surya.miniproject.constants.Strings.COLON;
import static com.surya.miniproject.constants.Strings.CSE_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.GENERATED_FOR;
import static com.surya.miniproject.constants.Strings.GENERATED_ON;
import static com.surya.miniproject.constants.Strings.NUMBER_OF_STUDENTS;
import static com.surya.miniproject.constants.Strings.PLACE_NAME;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Student;
import com.surya.miniproject.utility.Functions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;

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

    private final int centreX;
    private final int centreY;

    // hash table attendance data
    private Hashtable<Integer, Hashtable<String, String>> data = new Hashtable<Integer, Hashtable<String, String>>();

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

        centreX = pageWidth/2;
        centreY = pageHeight/2;
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

    // method to create the main page of the PDF
    // monthly attendance - horizontal
    public void prepareMainPageForMonthlyAttendance(PdfDocument pdfDocument, PdfDocument.Page page, Canvas canvas, Paint paint, ArrayList<Attendance> attendances){
        /**************************************************************************************************************************************/
        // method to initialise the HashTable data using the ArrayList of attendances
        initialiseHashTableDate(attendances, studentArrayList);

        /*
         *
         *
         * PAINTING STARTS
         *
         *
         *
         * */
        // margin
        drawMargin(canvas, paint);

        // COLLEGE NAME
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(10f);
        paint.setTextAlign(Paint.Align.CENTER);

        drawText(canvas, paint, 50, centreY, COLLEGE_NAME);

        // PLACE NAME
        drawText(canvas, paint, 65, centreY, PLACE_NAME);

        // DEPARTMENT TEXT
        drawText(canvas, paint, 85, centreY, PDFDepartment(getDepartment()));

        // MONTH TEXT
        drawText(canvas, paint, 100, centreY, "Attendance of " + getMonth() + "-" + getYear());

        // class name, class advisor, generated for section
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(8f);

        // class name
        drawText(canvas, paint, 125, 630, CLASS_NAME);
        // class advisor
        drawText(canvas, paint, 140, 630, CLASS_ADVISOR_NAME);
        // generated for
        drawText(canvas, paint, 155, 630, GENERATED_FOR);

        // colons
        drawText(canvas, paint, 125, 530, COLON);
        drawText(canvas, paint, 140, 530, COLON);
        drawText(canvas, paint, 155, 530, COLON);

        // class advisor name
        drawText(canvas, paint, 125, 523, getClassName());
        drawText(canvas, paint, 140, 523, getClassAdvisor());
        drawText(canvas, paint, 155, 523, getFacultyName());

        // Academic year, number of students, generated on section
        // academic year
        drawText(canvas, paint, 125, 350, ACADEMIC_YEAR);
        // number of students
        drawText(canvas, paint, 140, 350, NUMBER_OF_STUDENTS);
        // generated on
        drawText(canvas, paint, 155, 350, GENERATED_ON);

        // colons
        drawText(canvas, paint, 125, 270, COLON);
        drawText(canvas, paint, 140, 270, COLON);
        drawText(canvas, paint, 155, 270, COLON);

        // year
        drawText(canvas, paint, 125, 263, getYear()+"");
        // number of students value
        drawText(canvas, paint, 140, 263, getNumberOfStudents()+"");
        // generated date
        drawText(canvas, paint, 155, 263, new Functions().date());

        // ATTENDANCE TEXT
        paint.setTextSize(10f);
        paint.setStrokeWidth(2f);
        paint.setTextAlign(Paint.Align.CENTER);
        drawText(canvas, paint, 175, centreY, ATTENDANCE.toUpperCase());

        /*
         *
         *
         * MAIN AREA
         *
         *
         * */

        // displaying names
        paint.setTextSize(9f);
        paint.setStrokeWidth(1f);
        paint.setTextAlign(Paint.Align.LEFT);

        int startX = 200;
        int y = 0;
        int counter = 0;
        int xy = 0;

        for(int i = 0; i <= getStudentArrayList().size(); i++){
            if(i == 0){
                // no
                paint.setTextAlign(Paint.Align.CENTER);
                drawText(canvas, paint, startX, 800, "No.");

                // Name
                paint.setTextAlign(Paint.Align.LEFT);
                drawText(canvas, paint, startX, 780, "Name");

                // dates
                xy = 608/(attendances.size()+3);
                for(int j = 0; j < attendances.size(); j++){
                    y = 640 - (608/(attendances.size()+3)*(j+1));

                    paint.setTextAlign(Paint.Align.CENTER);
                    drawText(canvas, paint, startX, y+(xy/2), (j+1)+"");

                    canvas.drawLine(185, y, 545, y, paint);
                }
            }
            else{
                if(startX >= 560){
                    // drawing the divider lines
                    canvas.drawLine(185, 810, 185, 640, paint);
                    canvas.drawLine(185, 810, startX-15, 810, paint);
                    canvas.drawLine(185, 790, startX-15, 790, paint);
                    canvas.drawLine(185, 640, startX-15, 640, paint);

                    break;
                }
                else{
                    // No.
                    paint.setColor(Color.BLACK);
                    paint.setTextAlign(Paint.Align.CENTER);
                    drawText(canvas, paint, startX, 800, i+"");

                    // name
                    paint.setTextAlign(Paint.Align.LEFT);
                    drawText(canvas, paint, startX, 780, getStudentArrayList().get(i-1).getStudentName());

                    // attendance data
                    for(int j = 0; j < attendances.size(); j++){
                        counter++;

                        y = 640 - (608/(attendances.size()+3)*(j+1));

                        // marking the actual attendance of the student
                        String d = data.get(j).get(studentArrayList.get(i+1).getStudentName());

                        if(d.equals("A")){
                            paint.setColor(Color.RED);
                            Log.d("vikram", studentArrayList.get(i+1).getStudentName());
                        }
                        else{
                            paint.setColor(Color.BLACK);
                        }

                        paint.setTextAlign(Paint.Align.CENTER);
                        drawText(canvas, paint, startX, y+(xy/2), d);
                    }
                }
            }

            paint.setColor(Color.BLACK);

            canvas.drawLine(startX+5, 810, startX+5, y, paint);

            // canvas.drawLine(startX, 800, 0, 0, paint);
            // canvas.drawLine(startX-9, 800, 0, 0, paint);

            startX = startX + 20;
        }

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(185, 32, startX-15, 640, paint);

        pdfDocument.finishPage(page);
    }

    // method to draw the margin
    public void drawMargin(Canvas canvas, Paint paint){
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(25, 25, 570, 817, paint);
    }

    // method to restore and save the canvas
    public void restoreAndSaveCanvas(Canvas canvas){
        canvas.restore();
        canvas.save();
    }

    // method to draw the text in -90
    public void drawText(Canvas canvas, Paint paint, int x, int y, String text){
        // rotating the canvas
        canvas.rotate(-90f, x, y);

        // drawing the text
        canvas.drawText(text, x, y, paint);

        // bringing back the canvas to original position and saving the changes
        restoreAndSaveCanvas(canvas);
    }

    // method to initialise the HashTable data using the ArrayList of attendances
    private void initialiseHashTableDate(ArrayList<Attendance> attendances, ArrayList<Student> students){
        data.clear();

        // going through the attendance array list
        for(int i = 0; i < attendances.size(); i++){
            Attendance attendance =attendances.get(i);
            String date = attendance.getDate();

            // getting the json attendance string from the attendance object
            String json = attendance.getJson();

            // hash table for the student's attendance data
            Hashtable<String, String> temp = new Hashtable<>();

            // converting the json to ArrayList<String>
            Type type = new TypeToken<Hashtable<String, String>>(){}.getType();
            temp = new Gson().fromJson(json, type);

            // entering the data in the main hash table
            // date - HashTable<String, String>
            // 3-5-2022 - {"BALA ABINESH SURYA B" : "P"}
            //data.put(date, temp);
            data.put(i, temp);
        }
    }
}
