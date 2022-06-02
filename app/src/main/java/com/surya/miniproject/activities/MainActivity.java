package com.surya.miniproject.activities;

import static com.surya.miniproject.constants.Strings.FACULTIES;
import static com.surya.miniproject.constants.Strings.FACULTY_NAME;
import static com.surya.miniproject.constants.Strings.FACULTY_PUSH_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.models.Faculty;
import com.surya.miniproject.setup.Init;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    private TextInputLayout userName, password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hiding the Action bar
        getSupportActionBar().hide();

        // method to initialise the UI Elements
        init();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // on click listener for the login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameText = userName.getEditText().getText().toString();
                String passwordText = password.getEditText().getText().toString();

                class Validate{
                    // method to validate the inputs
                    private void validateTheInputs(String userNameText, String passwordText){
                        if(userNameText.length() <= 0){
                            userName.setError("UserName cannot be empty");
                            return;
                        }
                        if(passwordText.length() <= 0){
                            password.setError("Password cannot be empty");
                            return;
                        }
                        if(userNameText.length() >= 16){
                            userName.setError("UserName cannot be more than 15 characters long");
                            return;
                        }
                        if(passwordText.length() >= 16){
                            password.setError("Password cannot be more than 15 characters long");
                            return;
                        }

                        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();
                        NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();

                        if (mobile == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTED) {
                            // either mobile data or the Wifi is turned on
                            // verifying if the username the user entered exists in the database

                            firebaseDatabase.getReference()
                                    .child(FACULTIES)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                                    Faculty faculty = snapshot1.getValue(Faculty.class);

                                                    if(faculty.getFacultyUserName().equals(userNameText)){
                                                        if(faculty.getFacultyPassword().equals(passwordText)){
                                                            // user's credentials exist
                                                            // passing the user to dashboard activity
                                                            Intent intent = new Intent(MainActivity.this, DashBoard.class);
                                                            // passing the staffName, pushId as the intent
                                                            intent.putExtra(FACULTY_NAME, faculty.getFacultyName());
                                                            intent.putExtra(FACULTY_PUSH_ID, faculty.getFacultyPushId());
                                                            startActivity(intent);
                                                            finishAffinity();
                                                            return;
                                                        }
                                                        else{
                                                            // password is wrong
                                                            password.setError("Password is incorrect");
                                                            password.getEditText().setText("");
                                                            return;
                                                        }
                                                    }
                                                }

                                                // means the user entered invalid username as password
                                                Toast.makeText(MainActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                                                userName.getEditText().setText("");
                                                password.getEditText().setText("");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                        else{
                            // Both wifi and mobile data are turned off
                            // telling the user to turn on any one of them

                            Toast.makeText(MainActivity.this, "Turn on the Mobile Data/Wifi to Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                Validate v = new Validate();
                v.validateTheInputs(userNameText, passwordText);
            }
        });
    }

    // method to initialise the UI Elements
    private void init(){
        // text input layout
        userName = findViewById(R.id.main_username);
        password = findViewById(R.id.main_password);

        // button
        login = findViewById(R.id.main_login);
    }
}