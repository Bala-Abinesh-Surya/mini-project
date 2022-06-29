package com.surya.miniproject.activities;

import static com.surya.miniproject.activities.ClassAttendance.students;
import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.adapters.ClassesListAdapter.studentsArrayList;
import static com.surya.miniproject.constants.Strings.APP_DEFAULTS;
import static com.surya.miniproject.constants.Strings.ATTENDANCE;
import static com.surya.miniproject.constants.Strings.CLASS_ADVISOR;
import static com.surya.miniproject.constants.Strings.CLASS_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_PUSH_ID;
import static com.surya.miniproject.constants.Strings.DEPARTMENT;
import static com.surya.miniproject.constants.Strings.FACULTY_IS_AN_HOD;
import static com.surya.miniproject.constants.Strings.TODAY_IS_A_LEAVE;
import static com.surya.miniproject.pool.ThreadPool.executorService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surya.miniproject.R;
import com.surya.miniproject.activities.hod.StaffDetailsActiity;
import com.surya.miniproject.adapters.AbsenteesListAdapter;
import com.surya.miniproject.adapters.ClassesListAdapter;
import com.surya.miniproject.adapters.TodayAttendanceAdapter;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.CurrentClass;
import com.surya.miniproject.models.Student;
import com.surya.miniproject.utility.Functions;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

public class TodayAttendance extends AppCompatActivity {

    // UI Elements
    private RecyclerView status, absentees;
    private TextView classNameText, dateText, presentText, absenteesText, editedByText, editedBy, updateText, noAbsenteesText;
    private Button update;

    // data variables
    private String className;
    private String classPushId;
    private String classAdvisor;
    private String department;
    private boolean facultyIsTheClassAdvisor = false;

    // inflating the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.class_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Back Button Functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            // back button in the Action Bar
            onBackPressed();
        }
        else if(item.getItemId() == R.id.menu_staff_details){
            // staff details menu
            // checking if the faculty is an hod or the class advisor
            if(facultyIsTheClassAdvisor || getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getBoolean(FACULTY_IS_AN_HOD, false)){
                // faculty is either the class advisor or the Hod
                // passing the faculty to the StaffDetailsActivity
                Intent intent = new Intent(this, StaffDetailsActiity.class);
                intent.putExtra(CLASS_PUSH_ID, classPushId);
                intent.putExtra(CLASS_NAME, className);
                startActivity(intent);
            }
            else{
                // faculty is not a class advisor and also not an hod
                // so faculty has no permission to edit the faculty members for this class
                Toast.makeText(this, "Sorry! You got no permission to view", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            // get monthly attendance menu
            // a faculty taking any subject can view the monthly attendance
            Intent intent = new Intent(this, ClassAttendance.class);
            intent.putExtra(CLASS_NAME, className);
            intent.putExtra(CLASS_PUSH_ID, classPushId);
            intent.putExtra(CLASS_DEPARTMENT, department);
            intent.putExtra(CLASS_ADVISOR, classAdvisor);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_attendance);

        // getting the data through the intent
        className = getIntent().getStringExtra(CLASS_NAME);
        classPushId = getIntent().getStringExtra(CLASS_PUSH_ID);
        classAdvisor = getIntent().getStringExtra(CLASS_ADVISOR);
        department = getIntent().getStringExtra(CLASS_DEPARTMENT);

        // method to initialise the UI Elements
        initialiseUIElements();

        // setting the classNameText and dateText
        classNameText.setText(className);
        dateText.setText("Attendance of "+new Functions().date());

        // setting the title in the Action Bar
        getSupportActionBar().setTitle(className);

        // enabling the back button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // submitting the attendance fetching job to the executor service
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // fetching the attendance for this class, for today
                // checking first, if today is a holiday
                if(getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getBoolean(TODAY_IS_A_LEAVE, false)){
                    // today is leave
                    // so making all the unnecessary views invisible
                    // present/absent details, absentees, status and absentees recycler view, edited by and edited by text
                    // also the update button
                    presentText.setVisibility(View.GONE);
                    absenteesText.setVisibility(View.GONE);
                    status.setVisibility(View.GONE);
                    absentees.setVisibility(View.GONE);
                    editedBy.setVisibility(View.GONE);
                    editedByText.setVisibility(View.GONE);
                    update.setVisibility(View.GONE);
                    noAbsenteesText.setVisibility(View.GONE);

                    // setting the updateText
                    updateText.setVisibility(View.VISIBLE);
                    updateText.setText("Today is a holiday");
                }
                else{
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                    firebaseDatabase.getReference()
                            .child(ATTENDANCE)
                            .child(className)
                            .child(LocalDateTime.now().getMonth() + "-" + LocalDateTime.now().getYear()) // JUNE-2022
                            .child(new Functions().date()) // today's date, 17-6-2022
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        // if the snapshot exists, then the attendance has been updated for today
                                        // making the update text invisible
                                        updateText.setVisibility(View.GONE);

                                        // making the items visible
                                        // present/absent details, absentees, status and absentees recycler view, edited by and edited by text
                                        presentText.setVisibility(View.VISIBLE);
                                        absenteesText.setVisibility(View.VISIBLE);
                                        status.setVisibility(View.VISIBLE);
                                        absentees.setVisibility(View.VISIBLE);
                                        editedBy.setVisibility(View.VISIBLE);
                                        editedByText.setVisibility(View.VISIBLE);
                                        noAbsenteesText.setVisibility(View.GONE);

                                        // also the update button
                                        // visible only to the class advisor
                                        if(facultyIsTheClassAdvisor){
                                            update.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            update.setVisibility(View.GONE);
                                        }

                                        Attendance attendance = snapshot.getValue(Attendance.class);
                                        attendance.setLocalTable();

                                        // setting the edited by text
                                        editedBy.setText(attendance.getEditedBy());

                                        // setting up the adapter for the status recycler view
                                        int purpose = facultyIsTheClassAdvisor ? 1 : 0;
                                        TodayAttendanceAdapter adapter = new TodayAttendanceAdapter(TodayAttendance.this, studentsArrayList, attendance.getTable(), purpose, update, absentees, noAbsenteesText);
                                        status.setAdapter(adapter);
                                        status.setLayoutManager(new GridLayoutManager(TodayAttendance.this, 5));

                                        // setting up the adapter for the absentees recycler view
                                        class AbsenteesList{
                                            public AbsenteesList(){
                                                // preparing the absentees list, from the day's attendance
                                                // going through the students list
                                                ArrayList<Student> absent = new ArrayList<>();

                                                for(Student student : studentsArrayList){
                                                    if(attendance.getTable().get(student.getStudentName()).equals("A")){
                                                        absent.add(student);
                                                    }
                                                }

                                                if(absent.size() == 0){
                                                    // means no absentees today
                                                    // making the absentees recycler view invisible
                                                    noAbsenteesText.setVisibility(View.VISIBLE);
                                                }
                                                else{
                                                    // absentees are there
                                                    // absentees.setVisibility(View.VISIBLE);
                                                    noAbsenteesText.setVisibility(View.GONE);

                                                    // adapter for the absentees recycler view
                                                    AbsenteesListAdapter absenteesListAdapter = new AbsenteesListAdapter(TodayAttendance.this, absent);
                                                    absentees.setAdapter(absenteesListAdapter);
                                                    absentees.setLayoutManager(new LinearLayoutManager(TodayAttendance.this));
                                                }
                                            }
                                        }

                                        new AbsenteesList();
                                    }
                                    else{
                                        // attendance for today has not been updated yet
                                        // so making all the unnecessary views invisible
                                        // present/absent details, absentees, status and absentees recycler view, edited by and edited by text, noAbsenteesText
                                        // also the update button
                                        presentText.setVisibility(View.GONE);
                                        absenteesText.setVisibility(View.GONE);
                                        status.setVisibility(View.GONE);
                                        absentees.setVisibility(View.GONE);
                                        editedBy.setVisibility(View.GONE);
                                        editedByText.setVisibility(View.GONE);
                                        update.setVisibility(View.GONE);
                                        noAbsenteesText.setVisibility(View.GONE);

                                        // making the update text visible
                                        updateText.setVisibility(View.VISIBLE);

                                        // text of the update text
                                        // if the faculty is the class advisor
                                        if(facultyIsTheClassAdvisor){
                                            updateText.setText("Attendance for today has not been updated, please update the same");
                                        }
                                        else{
                                            // normal subject handling faculty
                                            updateText.setText("Attendance for today has not been updated yet, please wait a while");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }
        });

        // on click listener for the updateText
        /*
        *
        * Enabled only with two conditions
        *
        *   1. Should not be a holiday today
        *   2. works only for the class advisor, that too if the today's attendance has not been updated
        *
        * */
        updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! (getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getBoolean(TODAY_IS_A_LEAVE, false))){
                    if(facultyIsTheClassAdvisor){
                        // directing the class advisor to the AttendanceMarking activity
                        Intent intent = new Intent(TodayAttendance.this, AttendanceMarking.class);
                        intent.putExtra(CLASS_NAME, className);
                        intent.putExtra(CLASS_DEPARTMENT, department);
                        startActivity(intent);
                    }
                }
            }
        });

        // on click listener for the update button
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        // updating the existing attendance object
                        Attendance attendance = new Attendance(className, new Functions().date());
                        attendance.setJson(new Gson().toJson(TodayAttendanceAdapter.attendance));
                        attendance.setEditedBy(facultyName);

                        // now updating this updated attendance object to the firebase
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        firebaseDatabase.getReference()
                                .child(ATTENDANCE)
                                .child(className)
                                .child(LocalDateTime.now().getMonth() + "-" + LocalDateTime.now().getYear()) // JUNE-2022
                                .child(attendance.getDate())
                                .setValue(attendance)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // clearing all the unwanted things now
                                        TodayAttendanceAdapter.forHereToEditTheAttendance = false;
                                        TodayAttendanceAdapter.attendance.clear();

                                        if(! (task.isSuccessful())){
                                            Toast.makeText(TodayAttendance.this, "Attendance not updated! Try Again", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(TodayAttendance.this, "Attendance updated!", Toast.LENGTH_SHORT).show();
                                        }
                                       // onBackPressed();
                                    }
                                });
                    }
                });
            }
        });
    }

    // initialising the UI Elements
    private void initialiseUIElements(){
        // recycler view
        status = findViewById(R.id.today_attendance_status_rec_view);
        absentees = findViewById(R.id.today_attendance_absentees_rec_view);

        // text view
        classNameText = findViewById(R.id.today_attendance_class_name);
        dateText = findViewById(R.id.today_attendance_date_text);
        presentText = findViewById(R.id.today_attendance_present_text);
        absenteesText = findViewById(R.id.today_attendance_absentees_text);
        editedByText = findViewById(R.id.today_attendance_edited_by_text);
        editedBy = findViewById(R.id.today_attendance_edited_by);
        updateText = findViewById(R.id.today_attendance_update_text);
        noAbsenteesText = findViewById(R.id.today_attendance_no_absentees_text);

        // button
        update = findViewById(R.id.today_attendance_update);

        // checking if the faculty signed in, is the class advisor for this class
        if(facultyName.equals(classAdvisor)){
            facultyIsTheClassAdvisor = true;
        }

        // initialising CurrentClass
        new CurrentClass(className, department, classPushId, classAdvisor);
    }
}