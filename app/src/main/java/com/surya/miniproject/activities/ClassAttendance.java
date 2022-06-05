package com.surya.miniproject.activities;

import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.constants.Strings.APP_DEFAULTS;
import static com.surya.miniproject.constants.Strings.ATTENDANCE;
import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.CLASS_ADVISOR;
import static com.surya.miniproject.constants.Strings.CLASS_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_PUSH_ID;
import static com.surya.miniproject.constants.Strings.FACULTY_IS_AN_HOD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surya.miniproject.R;
import com.surya.miniproject.activities.hod.StaffDetailsActiity;
import com.surya.miniproject.adapters.AttendanceAdapter;
import com.surya.miniproject.details.Data;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.Student;
import com.surya.miniproject.utility.Functions;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;

public class ClassAttendance extends AppCompatActivity {

    // UI Elements
    private RecyclerView recyclerView;
    private TextView name;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Attendance> result = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    public static Attendance todayAttendance;

    private String classPushId;
    private String className;

    // adapter
    private AttendanceAdapter attendanceAdapter;

    public static ArrayList<Student> students = new ArrayList<Student>();
    private boolean attendanceEditedForToday = false;

    // Back Button Functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // inflating the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getBoolean(FACULTY_IS_AN_HOD, false)){
            // inflating the menu, if the faculty is the HOD
            getMenuInflater().inflate(R.menu.class_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    // menu items clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // get staff details menu
        if(item.getItemId() == R.id.menu_staff_details){
            // passing the user to the staff details of a class activity
            // passing the classPushId as the intent
            Intent intent = new Intent(this, StaffDetailsActiity.class);
            intent.putExtra(CLASS_PUSH_ID, classPushId);
            intent.putExtra(CLASS_NAME, className);
            startActivity(intent);
        }
        else if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_attendance);

        // getting the data from the intent
        className = getIntent().getStringExtra(CLASS_NAME);
        classPushId = getIntent().getStringExtra(CLASS_PUSH_ID);
        String classAdvisor = getIntent().getStringExtra(CLASS_ADVISOR);
        String department = getIntent().getStringExtra(CLASS_DEPARTMENT);

        // method to initialise the UI Elements
        initialiseUIElements(classAdvisor);

        // setting the name
        name.setText(className);

        // setting the title of the Action Bar
        getSupportActionBar().setTitle(className);

        // enabling the home button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();
        NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();

        // getting the students list from the database
        String todayDate = new Functions().date();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference()
                .child(CLASSES)
                .child(classPushId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // clearing the students array list
                            students.clear();

                            Class classx = snapshot.getValue(Class.class);
                            students = classx.getStudents();
                        }

                        firebaseDatabase.getReference()
                                .child(ATTENDANCE)
                                .child(className)
                                .child(LocalDateTime.now().getMonth()+"-"+LocalDateTime.now().getYear())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            // clearing the hash table
                                            result.clear();
                                            dates.clear();
                                            int index = 0;

                                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                                Attendance a = snapshot1.getValue(Attendance.class);
                                                dates.add(snapshot1.getKey());

                                                if(snapshot1.getKey().equals(todayDate)){
                                                    // attendance has been updated for today, already by this staff
                                                    attendanceEditedForToday = true;

                                                    // adding today's attendance
                                                    todayAttendance = a;

                                                    floatingActionButton.setImageResource(R.drawable.ic_edit);
                                                }

                                                // adding the hash table to the array list
                                                result.add(index, a);

                                                index++;
                                            }
                                        }

                                        if(! attendanceEditedForToday){
                                            floatingActionButton.setImageResource(R.drawable.ic_add);
                                        }

                                        // setting up the recycler view
                                        attendanceAdapter = new AttendanceAdapter(ClassAttendance.this, className, result, dates);
                                        AttendanceAdapter.students = students;
                                        recyclerView.setAdapter(attendanceAdapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(ClassAttendance.this));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // on click listener for the floating action button
        // button visible only to the class advisor
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(attendanceEditedForToday){
                    // attendance for today has been already been updated
                    // so, passing the faculty to the AttendanceEditing activity
                    Intent intent = new Intent(ClassAttendance.this, AttendanceEditing.class);
                    intent.putExtra(CLASS_NAME, className);
                    intent.putExtra(CLASS_DEPARTMENT, department);
                    startActivity(intent);
                }
                else{
                    // attendance for today, has not yet been uploaded
                    // passing the user to the AttendanceMarking activity
                    Intent intent = new Intent(ClassAttendance.this, AttendanceMarking.class);
                    intent.putExtra(CLASS_NAME, className);
                    intent.putExtra(CLASS_DEPARTMENT, department);
                    startActivity(intent);
                }
            }
        });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(String classAdvisor) {
        // recycler view
        recyclerView = findViewById(R.id.class_attendance_rec_view);

        // text view
        name = findViewById(R.id.class_attebdance_name);

        // floating action button
        floatingActionButton = findViewById(R.id.add_attendance_btn);

        // hiding the floating action button for all the faculty members but for the class advisor
        if(classAdvisor.equals(facultyName)){
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        else{
            floatingActionButton.setVisibility(View.GONE);
        }
    }
}