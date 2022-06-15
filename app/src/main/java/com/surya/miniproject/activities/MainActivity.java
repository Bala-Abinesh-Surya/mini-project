package com.surya.miniproject.activities;

import static com.surya.miniproject.constants.Strings.ADMIN_PIN;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Half;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.activities.admin.AdminPanel;
import com.surya.miniproject.details.Data;
import com.surya.miniproject.models.Faculty;
import com.surya.miniproject.models.HOD;
import com.surya.miniproject.setup.Init;

import java.io.File;
import java.time.LocalDateTime;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    private TextInputLayout userName, password;
    private Button login;
    private ImageView imageView;

    private SharedPreferences sharedPreferences;
    private boolean signedIn = false;

    private static final int PERMISSION_REQUEST_CODE = 50;
    private File file;

    private int adminImageClicked = 0;

    public static boolean folderCreated = false;

    private int backButtonPressed = 0;

    // Back Button
    // Like MX PLayer
    @Override
    public void onBackPressed() {
        backButtonPressed++;

        if(backButtonPressed < 2){
            Toast.makeText(this, "Tap again to exit app", Toast.LENGTH_SHORT).show();
        }
        else {
            // the user pressed/tapped the back button twice
            // exiting the app
            backButtonPressed = 0;
            finish();
            finishAffinity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // checking if the app has permission to write to the external storage
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            // if yes, creating a directory
            createDirectory();
        }
        else{
            // else, asking the user for the permission
            askPermission();
        }

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
                        else if(passwordText.length() <= 0){
                            password.setError("Password cannot be empty");
                            return;
                        }
                        else if(userNameText.length() >= 16){
                            userName.setError("UserName cannot be more than 15 characters long");
                            return;
                        }
                        else if(passwordText.length() >= 16){
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
                                                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                    Faculty faculty = snapshot1.getValue(Faculty.class);

                                                    if(! (faculty.getFacultyName().equals("DUMMY"))){
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

        // on click listener for the admin panel entering image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminImageClicked++;

                Intent intent = new Intent(MainActivity.this, AdminPanel.class);
                                            startActivity(intent);
                                            finish();

//                if(adminImageClicked >= 5){
//                    // resetting the count down
//                    adminImageClicked = 0;
//
//                    // inflating the admin panel bottom sheet
//                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
//                    View bottomSheetView = LayoutInflater.from(MainActivity.this)
//                            .inflate(R.layout.admin_panel_bottom_sheet, (ConstraintLayout) findViewById(R.id.admin_panel_bottom_sheet_container));
//
//                    class Admin{
//                        private final EditText editText;
//
//                        // Constructor
//                        public Admin(View view) {
//                            Button enter = view.findViewById(R.id.admin_panel_btn);
//                            editText = view.findViewById(R.id.admin_master_password);
//
//                            // on click listener for the button
//                            enter.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    String password = editText.getText().toString();
//
//                                    if(password.length() == 0){
//                                        Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else{
//                                        if(password.equals(ADMIN_PIN)){
//                                            // passing the user to the Admin Panel
//                                            Intent intent = new Intent(MainActivity.this, AdminPanel.class);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                        else{
//                                            // wrong pin
//                                            // clearing the editText
//                                            editText.setText("");
//                                            Toast.makeText(MainActivity.this, "Wrong Master Password!!!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                    bottomSheetDialog.dismiss();
//                                }
//                            });
//                        }
//                    }
//
//                    new Admin(bottomSheetView);
//
//                    bottomSheetDialog.setContentView(bottomSheetView);
//                    bottomSheetDialog.show();
//                }
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

        // admin image view
       imageView = findViewById(R.id.admin_image_img);
    }

    // method which returns the result for our askPermission() method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                createDirectory();
            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // method to ask the permissions that are needed for the application to perform
    private void askPermission(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE
        );
    }

    // method to create a separate directory for the app
    private void createDirectory() {
        // creating a directory for the ACETAT app
        // let the name be My Paintings
        folderNameDetermination();

        if(! file.exists()){
            // creating a folder if the folder does not already exist
            if(file.mkdir()){
                folderCreated = true;
            }
            else{
                folderCreated = false;
            }
        }
        else{
            folderCreated = true;
        }
    }

    // determining the folder destination for the application
    // for android 11 and above devices, creating a folder in the Documents folder
    // else, creating a folder in the outer main region
    private void folderNameDetermination(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ACETAT");
        }
        else{
            file = new File(Environment.getExternalStorageDirectory(), "ACETAT");
        }
    }
}