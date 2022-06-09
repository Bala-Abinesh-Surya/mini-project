package com.surya.miniproject.activities.hod;

import static com.surya.miniproject.constants.Strings.APP_DEFAULTS;
import static com.surya.miniproject.constants.Strings.DO_NOT_ASK_PIN_FOR_HOD_PANEL;
import static com.surya.miniproject.constants.Strings.PIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.surya.miniproject.R;

public class HODPanelEntering extends AppCompatActivity {

    // UI Elements
    private EditText editText;
    private Button button;
    private CheckBox checkBox;

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

        // listener for the check box
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(isChecked){
                    // hod checked the check box
                    // so this panel, will never have to ask the PIN again on this device again
                    editor.putBoolean(DO_NOT_ASK_PIN_FOR_HOD_PANEL, true);
                }
                else{
                    // box is not checked
                    // so this panel will have to ask for the PIN
                    editor.putBoolean(DO_NOT_ASK_PIN_FOR_HOD_PANEL, false);
                }
                editor.apply();
            }
        });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(){
        // edit text
        editText = findViewById(R.id.hod_text);

        // button
        button = findViewById(R.id.hod_btn);

        // check box
        checkBox = findViewById(R.id.hod_panel_check_box);
    }
}