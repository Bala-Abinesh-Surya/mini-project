package com.surya.miniproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.surya.miniproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hiding the Action bar
        getSupportActionBar().hide();
    }
}