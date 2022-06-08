package com.surya.miniproject.activities.hod;

import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_PUSH_ID;
import static com.surya.miniproject.constants.Strings.FACULTIES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.hod.StaffsDetailsAdapter;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;

public class StaffDetailsActiity extends AppCompatActivity {

    // UI Elements
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ImageView back;
    private TextView title;

    private ArrayList<String> faculties = new ArrayList<>();

    // Back Button Functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_details_actiity);

        // hiding the action bar
        getSupportActionBar().hide();

        // method to initialise the UI Elements
        initialiseUIElements();

        // getting the classPusId as the intent
        String classPushId = getIntent().getStringExtra(CLASS_PUSH_ID);
        String className = getIntent().getStringExtra(CLASS_NAME);

        // setting the title
        title.setText("Faculty Members of " + className);

        // on click listener for the back image
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // getting the faculties from the database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference()
                .child(CLASSES)
                .child(classPushId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // clearing the faculties array list first
                            faculties.clear();

                            Class classx = snapshot.getValue(Class.class);

                            // adding the faculties of the classx to the faculties array list
                            faculties.addAll(classx.getFacultyMembers());
                        }

                        // setting up the adapter for the recycler view
                        StaffsDetailsAdapter staffsDetailsAdapter = new StaffsDetailsAdapter(StaffDetailsActiity.this, faculties, firebaseDatabase, className, classPushId);
                        staffsDetailsAdapter.setPurpose(1);
                        recyclerView.setAdapter(staffsDetailsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(StaffDetailsActiity.this));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // on click listener for the floatingActionButton
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passing the HOD to the StaffsAddingActivity
                Intent intent = new Intent(StaffDetailsActiity.this, StaffsAddingActivity.class);
                startActivity(intent);
            }
        });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(){
        // recycler view
        recyclerView = findViewById(R.id.staff_details_rec_view);

        // floating action button
        floatingActionButton = findViewById(R.id.staff_details_add_btn);

        // title
        title = findViewById(R.id.back_title);

        // back button - image view
        back = findViewById(R.id.back_image);
    }
}