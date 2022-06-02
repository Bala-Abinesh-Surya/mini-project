package com.surya.miniproject.activities;

import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.FACULTIES;
import static com.surya.miniproject.constants.Strings.FACULTY_NAME;
import static com.surya.miniproject.constants.Strings.FACULTY_PUSH_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.ClassesListAdapter;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.setup.Init;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {

    // UI Elements
    private RecyclerView recyclerView;
    private ArrayList<Class> classes = new ArrayList<Class>();

    // adapter
    private ClassesListAdapter classesListAdapter;

    public static String facultyName;
    public static String facultyPushId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        // method to initialise the UI Elements
        initialiseUIElements();

        // getting the data from the intent
        facultyName = getIntent().getStringExtra(FACULTY_NAME);
        facultyPushId = getIntent().getStringExtra(FACULTY_PUSH_ID);

        // getting the classes handled by this particular staff
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference()
                .child(CLASSES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            classes.clear();

                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                if(snapshot1.exists()){
                                    Class classx = snapshot1.getValue(Class.class);

                                    ArrayList<String> facultyMembers = classx.getFacultyMembers();
                                    for(String faculty : facultyMembers){
                                        if(faculty.equals(facultyName)){
                                            // faculty is handling some subject for this class
                                            classes.add(classx);
                                        }
                                    }
                                }
                            }

                            // initialising the Recycler view
                            classesListAdapter = new ClassesListAdapter(classes, DashBoard.this);
                            recyclerView.setAdapter(classesListAdapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(DashBoard.this, 2));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements() {
        // recycler view
        recyclerView = findViewById(R.id.classes_list_rec_view);
    }
}