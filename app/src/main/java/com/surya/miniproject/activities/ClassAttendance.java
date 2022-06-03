package com.surya.miniproject.activities;

import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.constants.Strings.ATTENDANCE;
import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.CLASS_ADVISOR;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_PUSH_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.surya.miniproject.adapters.AttendanceAdapter;
import com.surya.miniproject.details.Data;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.Student;

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

    // adapter
    private AttendanceAdapter attendanceAdapter;

    public static ArrayList<Student> students = new ArrayList<Student>();
    private ArrayList<Attendance> attendance = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_attendance);

        // getting the data from the intent
        String className = getIntent().getStringExtra(CLASS_NAME);
        String classPushId = getIntent().getStringExtra(CLASS_PUSH_ID);
        String classAdvisor = getIntent().getStringExtra(CLASS_ADVISOR);

        // method to initialise the UI Elements
        initialiseUIElements(classAdvisor);

        // setting the name
        name.setText(className);

        // setting the title of the Action Bar
        getSupportActionBar().setTitle(className);

        // getting the students list from the database
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
                                .child("JUNE")
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
                                                dates.add(snapshot1.getKey().toString());

                                                // adding the hash table to the array list
                                                result.add(index, a);

                                                index++;
                                            }
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
                // passing the user to the AttendanceMarking activity
                Intent intent = new Intent(ClassAttendance.this, AttendanceMarking.class);
                intent.putExtra(CLASS_NAME, className);
                startActivity(intent);
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