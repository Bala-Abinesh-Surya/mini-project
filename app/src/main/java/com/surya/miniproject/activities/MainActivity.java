package com.surya.miniproject.activities;

import static com.surya.miniproject.constants.Strings.APP_DEFAULTS;
import static com.surya.miniproject.constants.Strings.FACULTIES;
import static com.surya.miniproject.constants.Strings.FACULTY_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.FACULTY_GENDER;
import static com.surya.miniproject.constants.Strings.FACULTY_IS_AN_HOD;
import static com.surya.miniproject.constants.Strings.FACULTY_NAME;
import static com.surya.miniproject.constants.Strings.FACULTY_PUSH_ID;
import static com.surya.miniproject.constants.Strings.FACULTY_SIGNED_IN;
import static com.surya.miniproject.constants.Strings.FACULTY_USER_NAME;
import static com.surya.miniproject.constants.Strings.HOD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Half;
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
import com.surya.miniproject.details.Data;
import com.surya.miniproject.models.Faculty;
import com.surya.miniproject.models.HOD;
import com.surya.miniproject.setup.Init;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    private TextInputLayout userName, password;
    private Button login;

    private SharedPreferences sharedPreferences;
    private boolean signedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // checking if the user has already signed in
        // if so, passing the suer to the DashBoard
        sharedPreferences = getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE);

        if(sharedPreferences.getBoolean(FACULTY_SIGNED_IN, false)){
            // getting the signed in facultyName, pushId
            DashBoard.facultyName = sharedPreferences.getString(FACULTY_NAME, null);
            DashBoard.facultyPushId = sharedPreferences.getString(FACULTY_PUSH_ID, null);
            DashBoard.facultyUserName = sharedPreferences.getString(FACULTY_USER_NAME, null);
            DashBoard.facultyGender = sharedPreferences.getString(FACULTY_GENDER, null);
            DashBoard.facultyDepartment = sharedPreferences.getString(FACULTY_DEPARTMENT, null);

            Intent intent = new Intent(MainActivity.this, DashBoard.class);
            startActivity(intent);
            finish();
        }

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
                                                            // now checking if the faculty is an HOD
                                                            firebaseDatabase.getReference()
                                                                    .child(HOD)
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            if(snapshot.exists()){
                                                                                for(DataSnapshot snapshot2 : snapshot.getChildren()){
                                                                                    com.surya.miniproject.models.HOD hod = snapshot2.getValue(HOD.class);

                                                                                    if(hod.getDepartment().equals(faculty.getFacultyDepartment())){
                                                                                        if(hod.getName().equals(faculty.getFacultyName())){
                                                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                                            editor.putBoolean(FACULTY_SIGNED_IN, true);
                                                                                            editor.putBoolean(FACULTY_IS_AN_HOD, true);
                                                                                            editor.apply();
                                                                                        }
                                                                                        else{
                                                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                                            editor.putBoolean(FACULTY_SIGNED_IN, true);
                                                                                            editor.putBoolean(FACULTY_IS_AN_HOD, false);
                                                                                            editor.apply();
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });

                                                            // passing the user to dashboard activity
                                                            Intent intent = new Intent(MainActivity.this, DashBoard.class);

                                                            DashBoard.facultyName = faculty.getFacultyName();
                                                            DashBoard.facultyPushId = faculty.getFacultyPushId();
                                                            DashBoard.facultyUserName = faculty.getFacultyUserName();
                                                            DashBoard.facultyGender = faculty.getFacultyGender();
                                                            DashBoard.facultyDepartment = faculty.getFacultyDepartment();

                                                            // updating the shared preferences
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putBoolean(FACULTY_SIGNED_IN, true);
                                                            editor.putString(FACULTY_NAME, faculty.getFacultyName());
                                                            editor.putString(FACULTY_PUSH_ID, faculty.getFacultyPushId());
                                                            editor.putString(FACULTY_GENDER, faculty.getFacultyGender());
                                                            editor.putString(FACULTY_USER_NAME, faculty.getFacultyUserName());
                                                            editor.putString(FACULTY_DEPARTMENT, faculty.getFacultyDepartment());
                                                            editor.apply();

                                                            signedIn = true;

                                                            startActivity(intent);
                                                            finishAffinity();
                                                        }
                                                        else{
                                                            // password is wrong
                                                            password.setError("Password is incorrect");
                                                            password.getEditText().setText("");
                                                            return;
                                                        }
                                                    }
                                                }

                                                if( ! signedIn){
                                                    // means the user entered invalid username as password
                                                    Toast.makeText(MainActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                                                    userName.getEditText().setText("");
                                                    password.getEditText().setText("");
                                                }
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