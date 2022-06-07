package com.surya.miniproject.export;

import static com.surya.miniproject.constants.Strings.ACADEMIC_YEAR;
import static com.surya.miniproject.constants.Strings.ATTENDANCE;
import static com.surya.miniproject.constants.Strings.CLASS_ADVISOR_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_NAME_NAME;
import static com.surya.miniproject.constants.Strings.COLLEGE_NAME;
import static com.surya.miniproject.constants.Strings.COLON;
import static com.surya.miniproject.constants.Strings.GENERATED_FOR;
import static com.surya.miniproject.constants.Strings.GENERATED_ON;
import static com.surya.miniproject.constants.Strings.NUMBER_OF_STUDENTS;
import static com.surya.miniproject.constants.Strings.PLACE_NAME;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Student;
import com.surya.miniproject.utility.Functions;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MonthExport extends Export{
    // child class of Export
    private ArrayList<Attendance> attendances; // monthly attendance data

    private int width;
    private int height;
    private int centreX;
    private int centreY;

    // Constructor
    public MonthExport(String className, String classAdvisor, String department, ArrayList<Student> students, String facultyName, String month, int year, int number, ArrayList<Attendance> attendances) {
        super(className, classAdvisor, department, students, facultyName, month, year, number);
        this.attendances = attendances;

        this.width = getPageWidth();
        this.height = getPageHeight();
        centreX = width/2;
        centreY = height/2;
    }

    // PDF Document variables
    private PdfDocument pdfDocument;
    private PdfDocument.PageInfo pageInfo;
    private PdfDocument.Page page;
    private int nominalY = 800;
    private int timesOpened = 0;

    // method to create the PDF
    public void createPDF() {
        // creating an instance to PdfDocument class
        pdfDocument = new PdfDocument();

        // specifying the height, width and number of pages of the pdf
        pageInfo = new PdfDocument.PageInfo.Builder(getPageWidth(), getPageHeight(), 1).create();
        page = pdfDocument.startPage(pageInfo);

        // creating the paint object
        Paint paint = new Paint();

        // getting the surface to paint
        Canvas canvas = page.getCanvas();
        canvas.save();

        /**************************************************************************************************************************************/
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

                        if(j%6 == 0){

                        }
                        else{
                            if(counter == 13){
                                paint.setColor(Color.RED);
                                counter = 0;

                                paint.setTextAlign(Paint.Align.CENTER);
                                drawText(canvas, paint, startX, y+(xy/2), "A");
                            }
                            else{
                                paint.setColor(Color.BLACK);
                                paint.setTextAlign(Paint.Align.CENTER);
                                drawText(canvas, paint, startX, y+(xy/2), "P");
                            }
                        }

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

        // creating a File object
        File file;
        String fileName;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ACETAT");

            // ":" in date is not allowed in file names in Android 11 I guess
            // so replacing ":" with "-"
            fileName = file + "/" + "ACETAT - " + new Functions().date().replaceAll(":", "-") + ".pdf";
        }
        else{
            file = new File(Environment.getExternalStorageDirectory(), "ACETAT");
            fileName = file + "/" + "ACETAT - " + new Functions().date() + ".pdf";
        }

        // writing the pdf to the file created
        try{
            pdfDocument.writeTo(new FileOutputStream(fileName));
            //Toast.makeText(getApplicationContext(), "created", Toast.LENGTH_LONG).show();
        }
        catch (Exception exception){
            exception.printStackTrace();
            //Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
