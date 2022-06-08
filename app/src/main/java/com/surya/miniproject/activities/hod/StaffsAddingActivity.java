package com.surya.miniproject.activities.hod;

import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.FACULTIES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.hod.StaffsDetailsAdapter;
import com.surya.miniproject.details.Data;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.CurrentClass;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;

public class StaffsAddingActivity extends AppCompatActivity {

    // UI Elements
    private RecyclerView recyclerView;
    private ImageView back;
    private TextView title;

    // adapter
    private StaffsDetailsAdapter adapter;

    private ArrayList<String> facultyMembers = new ArrayList<>();

    // Back Button Functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffs_adding);

        // hiding the action br
        getSupportActionBar().hide();

        // method to initialise the UI Elements
        initialiseUIElements();

        // setting the title
        title.setText("Adding Faculty Members for " + CurrentClass.className);

        // on click listener for the back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // getting the staffs list for the class from Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference()
                .child(FACULTIES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // clearing the faculty Members array list
                            facultyMembers.clear();

                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                Faculty faculty = snapshot1.getValue(Faculty.class);

                                if(! (faculty.getFacultyName().equals("DUMMY"))){
                                    // if the faculty is not already in the list of faculties of this class, adding this faculty name to the array list
                                    if(! (CurrentClass.currentClassFacultyMember.contains(faculty.getFacultyName()))){
                                        facultyMembers.add(faculty.getFacultyName());
                                    }
                                }
                            }

                            // setting up the adapter fpr the recycler view
                            adapter = new StaffsDetailsAdapter(
                                    StaffsAddingActivity.this,
                                    facultyMembers,
                                    firebaseDatabase,
                                    CurrentClass.className,
                                    CurrentClass.classPushId
                            );
                            adapter.setPurpose(2); // to indicate that the purpose is to add the staffs

                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(StaffsAddingActivity.this));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // method tp initialise the UI Elements
    private void initialiseUIElements(){
        // recycler view
        recyclerView = findViewById(R.id.staffs_Adding_rec_view);

        // back button - image view
        back = findViewById(R.id.back_image);

        // text view
        title = findViewById(R.id.back_title);
    }
}