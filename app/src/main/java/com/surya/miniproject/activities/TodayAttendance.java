package com.surya.miniproject.activities;

import static com.surya.miniproject.activities.ClassAttendance.students;
import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.adapters.ClassesListAdapter.studentsArrayList;
import static com.surya.miniproject.constants.Strings.ATTENDANCE;
import static com.surya.miniproject.constants.Strings.CLASS_ADVISOR;
import static com.surya.miniproject.constants.Strings.CLASS_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_PUSH_ID;
import static com.surya.miniproject.constants.Strings.DEPARTMENT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.reflect.TypeToken;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.ClassesListAdapter;
import com.surya.miniproject.adapters.TodayAttendanceAdapter;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Student;
import com.surya.miniproject.utility.Functions;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;

public class TodayAttendance extends AppCompatActivity {

    // UI Elements
    private RecyclerView status, absentees;
    private TextView classNameText, dateText, presentText, absenteesText, editedByText, editedBy, updateText;
    private Button update;

    // data variables
    private String className;
    private String classPushId;
    private String classAdvisor;
    private String department;
    private boolean facultyIsTheClassAdvisor = false;

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
        dateText.setText(new Functions().date());

        // setting the title in the Action Bar
        getSupportActionBar().setTitle(className);

        // enabling the back button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // thread to fetch the today's attendance
        new Thread(new Runnable() {
            @Override
            public void run() {
                // fetching the attendance for this class, for today
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
                                    // also the update button
                                    presentText.setVisibility(View.VISIBLE);
                                    absenteesText.setVisibility(View.VISIBLE);
                                    status.setVisibility(View.VISIBLE);
                                    absentees.setVisibility(View.VISIBLE);
                                    editedBy.setVisibility(View.VISIBLE);
                                    editedByText.setVisibility(View.VISIBLE);
                                    update.setVisibility(View.VISIBLE);

                                    Attendance attendance = snapshot.getValue(Attendance.class);
                                    attendance.setTable();

                                    Toast.makeText(TodayAttendance.this, studentsArrayList.size()+"", Toast.LENGTH_SHORT).show();

                                    // setting up the adapter for the status recycler view
                                    TodayAttendanceAdapter adapter = new TodayAttendanceAdapter(TodayAttendance.this, studentsArrayList, attendance.getTable());
                                    status.setAdapter(adapter);
                                    status.setLayoutManager(new GridLayoutManager(TodayAttendance.this, 5));
                                }
                                else{
                                    // attendance for today has not been updated yet
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
        }).start();

        // on click listener for the updateText
        // enabled, only for the class advisor, that too when the attendance for today has not been updated yet
        updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // directing the class advisor to the AttendanceMarking activity
                Intent intent = new Intent(TodayAttendance.this, AttendanceMarking.class);
                intent.putExtra(CLASS_NAME, className);
                intent.putExtra(CLASS_DEPARTMENT, department);
                startActivity(intent);
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

        // button
        update = findViewById(R.id.today_attendance_update);

        // checking if the faculty signed in, is the class advisor for this class
        if(facultyName.equals(classAdvisor)){
            facultyIsTheClassAdvisor = true;
        }
    }
}