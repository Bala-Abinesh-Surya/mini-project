package com.surya.miniproject.fragments;

import static com.surya.miniproject.activities.DashBoard.facultyDepartment;
import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.constants.Strings.APP_DEFAULTS;
import static com.surya.miniproject.constants.Strings.ATTENDANCE;
import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.FACULTY_IS_AN_HOD;

import android.content.Context;
import android.icu.util.Currency;
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
import com.surya.miniproject.export.MonthExport;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.CurrentClass;

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
                            CurrentClass.currentFacultyHandlingClasses.clear();

                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                if(snapshot1.exists()){
                                    Class classx = snapshot1.getValue(Class.class);

                                    if(getContext().getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getBoolean(FACULTY_IS_AN_HOD, false)){
                                        // faculty is an HOD
                                        // so getting all the classes, in his/her department though that class is not handled by faculty
                                        if(classx.getClassDepartment().equals(facultyDepartment)){
                                            // faculty is handling some subject for this class
                                            classes.add(classx);

                                            CurrentClass.currentFacultyHandlingClasses.add(classx.getClassName());
                                            Log.d("vikram", classx.getClassName());

                                            // adding the class advisor of this class to the classAdvisor Map
                                            classAdvisorMap.put(classx.getClassName(), classx.getClassAdvisor());
                                        }
                                    }
                                    else{
                                        // faculty is not an HOD
                                        // so, getting only the classes that the faculty is handling
                                        ArrayList<String> facultyMembers = classx.getFacultyMembers();
                                        for(String faculty : facultyMembers){
                                            if(faculty.equals(facultyName)){
                                                // faculty is handling some subject for this class
                                                classes.add(classx);

                                                CurrentClass.currentFacultyHandlingClasses.add(classx.getClassName());

                                                // adding the class advisor of this class to the classAdvisor Map
                                                classAdvisorMap.put(classx.getClassName(), classx.getClassAdvisor());
                                            }
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