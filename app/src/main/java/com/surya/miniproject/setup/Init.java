package com.surya.miniproject.setup;

import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.FACULTIES;
import static com.surya.miniproject.constants.Strings.FEMALE;
import static com.surya.miniproject.constants.Strings.MALE;

import com.google.firebase.database.FirebaseDatabase;
import com.surya.miniproject.details.Data;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.Faculty;
import com.surya.miniproject.utility.Functions;

import java.util.function.Function;

public class Init {
    // used to setup the data initially for the project
    // must be a singleton class
    private static Init init;
    private static Data data;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public static Init getInitInstance(){
        if(init == null){
            data = new Data();
            init = new Init();
        }

        return init;
    }

    // Constructor
    public Init() {

    }

    // method to initialise all the Faculties
    // method called, only one time that too at the beginning setup of the entire application
    private void setupFacultyMembers(){
        // going through the String[]
        for(String detail : data.getSTAFF_DETAILS()){
            // Mr. Vishwanath Shenoi@@@M@@@CSE
            String[] temp = detail.split("@@@");

            String facultyName = temp[0]; // Mr. Vishwanath Shenoi
            String department = new Functions().department(temp[2]);
            String gender = temp[1].equals("M") ? MALE : FEMALE;

            // creating a Faculty object
            Faculty faculty = new Faculty(facultyName, gender, department);

            // uploading the Faculty in the database
            // done only when the project is setup for the very first time
            String key = firebaseDatabase.getReference().push().getKey();

            faculty.setFacultyPushId(key);

            firebaseDatabase.getReference()
                    .child(FACULTIES)
                    .child(key)
                    .setValue(faculty);
        }
    }

    // method to initialise all the Classes
    // method called, only one time that too at the beginning setup of the entire application
    private void setupAllClasses(){
        // going through the String[]
        for(String detail : data.getCLASS_DETAILS()){
            // CSE-III-A@@@CSE@@@3@@@Mrs. Vidhya
            String[] temp = detail.split("@@@");

            String className = temp[0];
            String department = new Functions().department(temp[1]);
            int year = Integer.parseInt(temp[2]);
            String classAdvisor = temp[3];

            // creating a Class Object
            Class Class = new Class(className, department, year, classAdvisor);

            // adding the students for the Class
            Class.setStudents(data.getCLASS_STUDENTS_LIST().get(className));

            // adding the Faculty members for the Class
            Class.setFacultyMembers(data.getCLASS_STAFFS_LIST().get(className));

            // uploading the Class in the database
            String key = firebaseDatabase.getReference().push().getKey();

            Class.setClassPushId(key);

            firebaseDatabase.getReference()
                    .child(CLASSES)
                    .child(key)
                    .setValue(Class);
        }
    }
}
