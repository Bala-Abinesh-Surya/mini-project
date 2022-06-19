package com.surya.miniproject.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.surya.miniproject.R;
import com.surya.miniproject.models.CurrentClass;

public class WeeklyExportActivity extends AppCompatActivity {

    // UI Elements
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_export);

        // initialising the UI Elements
        initialiseUIElements();

        // fetching the faculty subject handling classes from the firébase
        // with the help from, CurrentClass.currentFacultyHandlingClasses
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(){
        // recycler view
        recyclerView = findViewById(R.id.stat_classes_rec_view);
    }
}