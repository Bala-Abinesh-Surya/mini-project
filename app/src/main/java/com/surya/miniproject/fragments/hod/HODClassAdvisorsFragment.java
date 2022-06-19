package com.surya.miniproject.fragments.hod;

import static com.surya.miniproject.activities.DashBoard.facultyDepartment;
import static com.surya.miniproject.constants.Strings.CLASSES;
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
import com.surya.miniproject.adapters.hod.ClassAdvisorsAdapter;
import com.surya.miniproject.models.Class;

import java.util.ArrayList;

public class HODClassAdvisorsFragment extends Fragment {

    // UI Elements
    private RecyclerView recyclerView;

    private ArrayList<Class> classArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_h_o_d_class_advisors, container, false);

        // method to initialise the UI Elements
        initialiseUIElements(view);

        // fetching the classes list only from the hod's department
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference()
                        .child(CLASSES)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    // clearing the classes array list
                                    classArrayList.clear();

                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        Class classx = dataSnapshot.getValue(Class.class);

                                        if(classx.getClassDepartment().equals(facultyDepartment)){
                                            classArrayList.add(classx);
                                        }
                                    }

                                    // setting up the adapter for the recycler view
                                    ClassAdvisorsAdapter adapter = new ClassAdvisorsAdapter(container.getContext(), classArrayList);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
                                }
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
        recyclerView = view.findViewById(R.id.hod_class_Advisor_rec_view);
    }
}