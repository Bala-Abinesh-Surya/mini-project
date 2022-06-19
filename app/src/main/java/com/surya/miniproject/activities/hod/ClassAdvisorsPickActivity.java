package com.surya.miniproject.activities.hod;

import static com.surya.miniproject.constants.Strings.CLASS_ADVISOR;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_PUSH_ID;
import static com.surya.miniproject.constants.Strings.FACULTIES;
import static com.surya.miniproject.constants.Strings.FACULTY_USER_NAME;
import static com.surya.miniproject.pool.ThreadPool.executorService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.hod.StaffsDetailsAdapter;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;

public class ClassAdvisorsPickActivity extends AppCompatActivity {

    // UI Elements
    private RecyclerView recyclerView;
    private ConstraintLayout parent;

    public static boolean classAdvisorPicked = false;

    private ArrayList<String> faculties = new ArrayList<>();

    // back button functionality
    @Override
    public void onBackPressed() {
        if(classAdvisorPicked){
            super.onBackPressed();
            finish();
            classAdvisorPicked = false;
        }
        else{
            // showing a snack bar to the hod
            Snackbar snackbar = Snackbar.make(parent,
                    "No changes made! Sure to go back?", Snackbar.LENGTH_LONG)
                    .setAction("GO BACK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            classAdvisorPicked = true;
                            onBackPressed();
                        }
                    });
            snackbar.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_advisors_pick);

        // getting the current class advisor's name as the intent
        String currentClassAdvisor = getIntent().getStringExtra(CLASS_ADVISOR);
        String className = getIntent().getStringExtra(CLASS_NAME);
        String classPushId = getIntent().getStringExtra(CLASS_PUSH_ID);

        // setting the title of the action bar
        getSupportActionBar().setTitle("Picking Class Advisor for " + className);

        // enabling the back button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // method to initialise the UI Elements
        initialiseUIElements();

        // fetching the faculty members name from firebase
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference()
                        .child(FACULTIES)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    // clearing the faculty array list
                                    faculties.clear();

                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        Faculty faculty = dataSnapshot.getValue(Faculty.class);

                                        if(! (faculty.getFacultyName().equals("DUMMY"))){
                                            // for the safety
                                            if(! (faculty.getFacultyName().equals(currentClassAdvisor))){
                                                faculties.add(faculty.getFacultyName());
                                            }
                                        }
                                    }

                                    // setting up the adapter for the recycler view
                                    StaffsDetailsAdapter adapter = new StaffsDetailsAdapter(parent,ClassAdvisorsPickActivity.this,
                                            faculties,
                                            className,
                                            classPushId,
                                            firebaseDatabase);
                                    adapter.setPurpose(4);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(ClassAdvisorsPickActivity.this));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(){
        // recycler view
        recyclerView = findViewById(R.id.class_Advisors_pick_rec_view);

        // constraint layout
        parent = findViewById(R.id.class_Advisors_pick_layout);
    }
}