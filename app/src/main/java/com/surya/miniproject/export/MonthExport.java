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

    // Constructor
    public MonthExport(String className, String classAdvisor, String department, ArrayList<Student> students, String facultyName, String month, int year, int number, ArrayList<Attendance> attendances) {
        super(className, classAdvisor, department, students, facultyName, month, year, number);
        this.attendances = attendances;
    }

    // PDF Document variables
    private PdfDocument pdfDocument;
    private PdfDocument.PageInfo pageInfo;
    private PdfDocument.Page page;

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

        // preparing the main page
        prepareMainPageForMonthlyAttendance(
                pdfDocument, page, canvas, paint, attendances);

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
