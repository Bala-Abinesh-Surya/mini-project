package com.surya.miniproject.activities;

import static com.surya.miniproject.constants.Strings.APP_DEFAULTS;
import static com.surya.miniproject.constants.Strings.DO_NOT_ASK_PIN_FOR_HOD_PANEL;
import static com.surya.miniproject.constants.Strings.FACULTY_GENDER;
import static com.surya.miniproject.constants.Strings.FACULTY_IS_AN_HOD;
import static com.surya.miniproject.constants.Strings.FACULTY_SIGNED_IN;
import static com.surya.miniproject.constants.Strings.HOD;
import static com.surya.miniproject.constants.Strings.MALE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.background.IsTodayALeaveAsyncTask;
import com.surya.miniproject.activities.hod.HODPanel;
import com.surya.miniproject.activities.hod.HODPanelEntering;
import com.surya.miniproject.fragments.RequestsFragment;
import com.surya.miniproject.models.CurrentClass;
import com.surya.miniproject.models.HOD;

import java.util.Currency;

public class DashBoard extends AppCompatActivity implements RequestsFragment.BottomSheetViewSettingInterface {

    // UI Elements
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView headerUserName, headerPushId, headerFacultyName;
    private ImageView headerImage;
    private TextView headerText;

    // Alert Dialog for logout option
    private AlertDialog.Builder builder;

    public static String facultyName;
    public static String facultyPushId;
    public static String facultyGender;
    public static String facultyUserName;
    public static String facultyDepartment;

    private int backButtonPressed = 0;
    private int hodSecretClicked = 0;

    // Back Button Functionality
    // Like MX Player
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(navigationView)){
            // closing the drawer if it is open
            backButtonPressed = 0;
            drawerLayout.close();
        }
        else{
            backButtonPressed++;

            if(backButtonPressed < 2){
                Toast.makeText(this, "Tap again to exit app", Toast.LENGTH_SHORT).show();
            }
            else{
                // the user pressed/tapped the back button twice
                // exiting the app
                backButtonPressed = 0;
                finish();
                finishAffinity();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        // hiding the ActionBar
        getSupportActionBar().hide();

        // method to initialise the UI Elements
        initialiseUIElements();

        //Init.getInitInstance();

        // setting up the texts and image in navigation header
        String mUserName = "@"+facultyUserName;
        headerFacultyName.setText(facultyName);
        headerUserName.setText(mUserName);
        headerPushId.setText(facultyPushId);

        if(getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getString(FACULTY_GENDER, null).equals(MALE)){
            headerImage.setImageResource(R.drawable.male);
        }
        else{
            // gender may be female, or Rather Not Say
            headerImage.setImageResource(R.drawable.female);
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // checking if the faculty is an HOD
        firebaseDatabase.getReference()
                .child(HOD)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                        com.surya.miniproject.models.HOD hod = snapshot1.getValue(HOD.class);

                                        if(hod.getDepartment().equals(facultyDepartment)){
                                            if(hod.getName().equals(facultyName)){
                                                getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).edit().putBoolean(FACULTY_IS_AN_HOD, true).apply();;
                                            }
                                            else{
                                                getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).edit().putBoolean(FACULTY_IS_AN_HOD, false).apply();
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        // opening/closing the drawer on clicking the menu in the so called action bar
        findViewById(R.id.dash_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // navigation view essentials
        navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.dash_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        // changing the header text, when the user goes into different fragments
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                headerText.setText(navDestination.getLabel());
            }
        });

        // on click listener for the logout menu in the NavigationView
        navigationView.getMenu().findItem(R.id.menu_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // closing the drawer first
                drawerLayout.close();

                // method to setup the Alert Dialog Builder
                setupAlertDialogBuilder(facultyPushId);

                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Logout");
                alertDialog.show();

                return false;
            }
        });

        // on click listener for the monthly export option in the navigation view
        navigationView.getMenu().findItem(R.id.menu_month).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // closing the drawer
                drawerLayout.close();

                // showing a toast to the faculty
                Toast.makeText(DashBoard.this, "Data not sufficient to generate Monthly Report", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        // on click listener for thr weekly export option in the navigation menu
        navigationView.getMenu().findItem(R.id.menu_week).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // closing the drawer
                drawerLayout.close();

                // passing the faculty to the WeeklyExportActivity
                Intent intent = new Intent(DashBoard.this, WeeklyExportActivity.class);
                startActivity(intent);

                return false;
            }
        });

        // on click listener for the user name in the header
        headerUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getBoolean(FACULTY_IS_AN_HOD, false)){
                    hodSecretClicked++;

                    if(hodSecretClicked >= 7){
                        // resetting the counter
                        hodSecretClicked = 0;

                        // closing the drawer
                        drawerLayout.close();

                        // deciding whether to pass the hod directly to the Panel or the panel entering activity
                        SharedPreferences sharedPreferences = getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE);
                        boolean dontAsk = sharedPreferences.getBoolean(DO_NOT_ASK_PIN_FOR_HOD_PANEL, false);

                        Intent intent;
                        if(dontAsk){
                            // hod opted for the do not ask PIN option
                            // so passing the hod directly to the hod Panel
                            intent = new Intent(DashBoard.this, HODPanel.class);
                        }
                        else{
                            intent = new Intent(DashBoard.this, HODPanelEntering.class);
                        }
                        startActivity(intent);
                    }
                }
            }
        });

        // checking if today is leave
        new IsTodayALeaveAsyncTask(getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).edit()).execute(FirebaseDatabase.getInstance());
    }

    // method to initialise the UI Elements
    private void initialiseUIElements() {
        // drawer layout
        drawerLayout = findViewById(R.id.drawerLayout);

        // NavigationView
        navigationView = findViewById(R.id.navigationView);

        // alert dialog builder
        builder = new AlertDialog.Builder(this);

        // text view
        headerText = findViewById(R.id.admin_text);

        // Elements in the navigation view header
        // text view
        headerFacultyName = navigationView.getHeaderView(0).findViewById(R.id.header_name);
        headerUserName = navigationView.getHeaderView(0).findViewById(R.id.header_user_id);
        headerPushId = navigationView.getHeaderView(0).findViewById(R.id.header_push_id);

        // image view
        headerImage = navigationView.getHeaderView(0).findViewById(R.id.header_image);
    }

    // method to setup the Alert Dialog Builder
    private void setupAlertDialogBuilder(String facultyPushId){
        builder.setMessage("Do you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // faculty chose not to Logout
                        // closing the dialog
                        Toast.makeText(DashBoard.this, "Glad you chose not to go out!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // faculty chose to Logout
                        // setting the FACULTY_SIGNED_IN boolean value to false
                        // remaining everything is taken care of by the MainActivity
                        SharedPreferences sharedPreferences = getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(FACULTY_SIGNED_IN, false);
                        editor.putBoolean(FACULTY_IS_AN_HOD, false);
                        editor.apply();

                        // clearing the classes handled array list by the faculty
                        CurrentClass.currentFacultyHandlingClasses.clear();

                        // passing the faculty back to the MainActivity
                        Intent intent = new Intent(DashBoard.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        finishAffinity();
                    }
                });
    }

    @Override
    public View setRequestAcceptingView() {
        return LayoutInflater.from(this)
                .inflate(R.layout.request_access_accept_layout, (ConstraintLayout) findViewById(R.id.request_accepting_layout), false);
    }

    @Override
    public View setRequestAcceptedView() {
        return null;
    }
}