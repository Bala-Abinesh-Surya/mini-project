package com.surya.miniproject.activities.hod;

import static com.surya.miniproject.constants.Strings.PIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.surya.miniproject.R;

public class HODPanelEntering extends AppCompatActivity {

    // UI Elements
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hodpanel_entering);

        // hiding the Action Bar
        getSupportActionBar().hide();

        // method to initialise the UI Elements
        initialiseUIElements();

        // on click listener for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting the password
                String password = editText.getText().toString();

                if(password.equals(PIN)){
                    Intent intent = new Intent(HODPanelEntering.this, HODPanel.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(HODPanelEntering.this, "Sorry! Mr. Vishwanath Shenoi!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(){
        // edit text
        editText = findViewById(R.id.hod_text);

        // button
        button = findViewById(R.id.hod_btn);
    }
}