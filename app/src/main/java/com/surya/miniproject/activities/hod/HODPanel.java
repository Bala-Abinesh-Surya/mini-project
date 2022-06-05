package com.surya.miniproject.activities.hod;

import static com.surya.miniproject.constants.Strings.CSE_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.FACULTIES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.hod.PanelStaffsListAdapter;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;

public class HODPanel extends AppCompatActivity {

    /// UI Elements
    private RecyclerView recyclerView;

    private PanelStaffsListAdapter adapter;
    private ArrayList<Faculty> faculties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hodpanel);

        // setting the title of Action Bar
        getSupportActionBar().setTitle("Faculties of CSE");

        // method to initialise the UI Elements
        initialiseUIElements();

        // fetching the staffs list from firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference()
                .child(FACULTIES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // clearing the faculties array list
                            faculties.clear();

                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                Faculty faculty = snapshot1.getValue(Faculty.class);

                                if(faculty.getFacultyDepartment().equals(CSE_DEPARTMENT)){
                                    faculties.add(faculty);
                                }
                            }

                            // setting up the adapter for the recycler view
                            adapter = new PanelStaffsListAdapter(HODPanel.this, faculties);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(HODPanel.this, 2));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(){
        // recycler view
        recyclerView = findViewById(R.id.hod_panel_rec_view);
    }
}