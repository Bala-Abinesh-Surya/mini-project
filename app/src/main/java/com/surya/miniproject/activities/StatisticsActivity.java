package com.surya.miniproject.activities;

import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.pool.ThreadPool.executorService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.admin.AllClassesAdapter;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.CurrentClass;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    // UI Elements
    private RecyclerView recyclerView;
    private View bottomSheetView;

    private ArrayList<Class> classArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_export);

        // initialising the UI Elements
        initialiseUIElements();

        // fetching the faculty subject handling classes from the firebase
        // with the help from, CurrentClass.currentFacultyHandlingClasses
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
                                    /// clearing the classes array list
                                    classArrayList.clear();

                                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                        Class classx = snapshot1.getValue(Class.class);

                                        // adding the class, only if the faculty is handling the subject to that class
                                        if(CurrentClass.currentFacultyHandlingClasses.contains(classx.getClassName())){
                                            classArrayList.add(classx);
                                        }
                                    }

                                    // setting up the adapter for the recycler view
                                    AllClassesAdapter adapter = new AllClassesAdapter(StatisticsActivity.this, classArrayList, firebaseDatabase, 1);
                                    adapter.setBottomSheetView(bottomSheetView);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(StatisticsActivity.this));
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
        recyclerView = findViewById(R.id.stat_classes_rec_view);

        // bottom sheet view
        bottomSheetView = LayoutInflater.from(this).inflate(R.layout.stat_class_bottom_sheet, (ConstraintLayout) findViewById(R.id.stat_parent_layout), false);
    }
}