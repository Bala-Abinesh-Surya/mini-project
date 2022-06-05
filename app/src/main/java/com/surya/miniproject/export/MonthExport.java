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

import android.graphics.Canvas;
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
    private int centre;

    // Constructor
    public MonthExport(String className, String classAdvisor, String department, ArrayList<Student> students, String facultyName, String month, int year, int number, ArrayList<Attendance> attendances) {
        super(className, classAdvisor, department, students, facultyName, month, year, number);
        this.attendances = attendances;

        this.width = getPageWidth();
        this.height = getPageHeight();
        centre = width/2;
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

        /**************************************************************************************************************************************/
        /*
         *
         *
         * PAINTING STARTS
         *
         *
         *
         * */

        // creating a border line
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(25, 25, 570, 817, paint);

        // College Name
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12f);
        canvas.drawText(COLLEGE_NAME, centre, 45, paint);

        // ERACHAKULAM TEXT
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(10f);
        canvas.drawText("Erachakulam, Nagercoil", centre, 60, paint);

        // Department text
        canvas.drawText(PDFDepartment(getDepartment()), centre, 80, paint);

        // Attendance pf Month-year text
        // say, Attendance pf August-2022 text
        String result = "Attendance of " + getMonth() + " - " + getYear();
        canvas.drawText(result, centre, 95, paint);

        // Class Name, Class Advisor Text
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(9f);
        canvas.drawText(CLASS_NAME_NAME, 75, 125, paint);
        canvas.drawText(CLASS_ADVISOR_NAME, 75, 140, paint);
        canvas.drawText(GENERATED_FOR, 75, 155, paint);

        // colons
        canvas.drawText(COLON, 185, 125, paint);
        canvas.drawText(COLON, 185, 140, paint);
        canvas.drawText(COLON, 185, 155, paint);

        // class name, class advisor
        canvas.drawText(getClassName(), 195, 125, paint);
        canvas.drawText(getClassAdvisor(), 195, 140, paint);
        canvas.drawText(getFacultyName(), 195, 155, paint);

        // Academic Year, Number of Students, Generated On
        canvas.drawText(ACADEMIC_YEAR, 310, 125, paint);
        canvas.drawText(NUMBER_OF_STUDENTS, 310, 140, paint);
        canvas.drawText(GENERATED_ON, 310, 155, paint);

        // colons
        canvas.drawText(COLON, 395, 125, paint);
        canvas.drawText(COLON, 395, 140, paint);
        canvas.drawText(COLON, 395, 155, paint);

        // year, number of students, Time
        canvas.drawText(getYear()+"", 405, 125, paint);
        canvas.drawText(getNumberOfStudents()+"", 405, 140, paint);
        canvas.drawText(LocalDateTime.now().toString(), 405, 155, paint);

        // ATTENDANCE text
        paint.setTextSize(10f);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(ATTENDANCE, centre, 185, paint);

        // drawing the table
        int startY = 200;
        int lastY = 0;
        canvas.drawLine(50, 200, 545, 200, paint);
        for(int i = 0; i < getStudentArrayList().size(); i++){
            int y = startY + (i+1)*25;

            if(y >= 800){





                // finishing the current page
//
//
//                pageInfo = new PdfDocument.PageInfo.Builder(getPageWidth(), getPageHeight(), 1).create();
//                page = pdfDocument.startPage(pageInfo);
//
//                Paint paint1 = new Paint();
//                Canvas canvas1 = page.getCanvas();
//
//                // drawing the border line for the second page
//                paint1.setStyle(Paint.Style.STROKE);
//                paint1.setStrokeWidth(1);
//                canvas1.drawRect(25, 25, 570, 817, paint1);
//
//                // continuing the work in the second page
//                y = 50;
//                for(int j = i; j < getStudentArrayList().size(); j++){
//                    y = (j-1)*25;
//                    canvas.drawLine(50, y, 500, y, paint);
//                    canvas.drawText(getStudentArrayList().get(i).getStudentName(), 70, y-10, paint);
//                }
//
//                pdfDocument.finishPage(page);
                // setting up new page
                break;

            }
            else{
                canvas.drawLine(50, y, 545, y, paint);

                if(i == 0){
                    paint.setTextSize(8f);
                    paint.setTextAlign(Paint.Align.LEFT);

                    canvas.drawText("No.", 60, y-10, paint);
                    canvas.drawText("Name", 90, y-10, paint);
                }
                else{
                    // no.
                    canvas.drawText((i)+"", 60, y-10, paint);

                    // name
                    canvas.drawText(getStudentArrayList().get(i-1).getStudentName(), 90, y-10, paint);
                }


                lastY = y;
            }

        }

        // divider lines
        canvas.drawLine(50, 200, 50, lastY, paint);
        canvas.drawLine(80, 200, 80, lastY, paint);
        canvas.drawLine(230, 200, 230, lastY, paint);
        canvas.drawLine(545, 200, 545, lastY, paint);

        // attendance
        // diving the space into 31 columns
        for(int i = 1; i <= 15; i++){
            int x = 230 + (315/15)*i;

            canvas.drawLine(x, 200, x, lastY, paint);
        }



        // finishing the 1st page
        pdfDocument.finishPage(page);

        /*
        *
        *
        * MAIN AREA
        *
        *
        * */



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

    // method to check
    private int check(int y) {
        timesOpened++;

        if (y >= nominalY) {
            // finishing the current page
            pdfDocument.finishPage(page);

            // specifying the height, width and number of pages of the pdf
            pageInfo = new PdfDocument.PageInfo.Builder(getPageWidth(), getPageHeight(), timesOpened + 1).create();
            page = pdfDocument.startPage(pageInfo);

            // creating the paint object
            Paint paint = new Paint();

            // getting the surface to paint
            Canvas canvas = page.getCanvas();
        }

        return nominalY - y;
    }
}
