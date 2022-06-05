package com.surya.miniproject.activities.hod;

import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_PUSH_ID;
import static com.surya.miniproject.constants.Strings.FACULTIES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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

    private ArrayList<String> faculties = new ArrayList<>();

    // Back Button Functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // back button in the action bar
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_details_actiity);

        // method to initialise the UI Elements
        initialiseUIElements();

        // getting the classPusId as the intent
        String classPushId = getIntent().getStringExtra(CLASS_PUSH_ID);
        String className = getIntent().getStringExtra(CLASS_NAME);

        // setting the title of the action bar
        getSupportActionBar().setTitle("Faculty Members of " + className);

        // setting up the back button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        StaffsDetailsAdapter staffsDetailsAdapter = new StaffsDetailsAdapter(StaffDetailsActiity.this, faculties, firebaseDatabase);
                        staffsDetailsAdapter.setPurpose(1);
                        recyclerView.setAdapter(staffsDetailsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(StaffDetailsActiity.this));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(){
        // recycler view
        recyclerView = findViewById(R.id.staff_details_rec_view);

        // floating action button
        floatingActionButton = findViewById(R.id.staff_details_add_btn);
    }
}