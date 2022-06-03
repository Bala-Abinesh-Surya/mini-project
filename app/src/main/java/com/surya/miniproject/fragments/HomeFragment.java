package com.surya.miniproject.fragments;

import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.constants.Strings.CLASSES;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.activities.DashBoard;
import com.surya.miniproject.adapters.ClassesListAdapter;
import com.surya.miniproject.models.Class;

import java.util.ArrayList;
import java.util.Hashtable;

public class HomeFragment extends Fragment {

    // UI Elements
    private RecyclerView recyclerView;
    private ArrayList<Class> classes = new ArrayList<Class>();

    private Hashtable<String, String> classAdvisorMap = new Hashtable<>();

    // adapter
    private ClassesListAdapter classesListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // method to initialise the UI Elements
        initialiseUIElements(view);

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

                                            // adding the class advisor of this class to the classAdvisor Map
                                            classAdvisorMap.put(classx.getClassName(), classx.getClassAdvisor());
                                        }
                                    }
                                }
                            }

                            // initialising the Recycler view
                            classesListAdapter = new ClassesListAdapter(classes, getContext(), classAdvisorMap);
                            recyclerView.setAdapter(classesListAdapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(View view) {
        // recycler view
        recyclerView = view.findViewById(R.id.classes_list_rec_view);
    }
}