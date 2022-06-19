package com.surya.miniproject.fragments.hod;

import static com.surya.miniproject.constants.Strings.FACULTIES;
import static com.surya.miniproject.pool.ThreadPool.executorService;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.hod.StaffsDetailsAdapter;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;

public class HODRemoveAFaculty extends Fragment {
    // UI Elements
    private RecyclerView recyclerView;

    private ArrayList<String> facultyArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_h_o_d_remove_a_faculty, container, false);

        // method to initialise the UI Elements
        initialiseUIElements(view);

        // fetching the faculty members list from the database
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
                                    // clearing the faculty members array list
                                    facultyArrayList.clear();

                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        Faculty faculty = dataSnapshot.getValue(Faculty.class);

                                        if(! (faculty.getFacultyName().equals("DUMMY"))){
                                            // for safety purposes
                                            facultyArrayList.add(faculty.getFacultyName());
                                        }
                                    }
                                }

                                // setting up the adapter for the recycler view
                                StaffsDetailsAdapter adapter = new StaffsDetailsAdapter(getContext(), facultyArrayList, firebaseDatabase);
                                adapter.setPurpose(3);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        return view;
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(View view){
        // recycler view
        recyclerView = view.findViewById(R.id.hod_remove_faculty_rec_view);
    }
}