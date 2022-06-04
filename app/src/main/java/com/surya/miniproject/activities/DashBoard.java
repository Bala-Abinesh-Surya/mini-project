package com.surya.miniproject.activities;

import static com.surya.miniproject.constants.Strings.APP_DEFAULTS;
import static com.surya.miniproject.constants.Strings.FACULTY_SIGNED_IN;
import static com.surya.miniproject.constants.Strings.MALE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.surya.miniproject.R;

public class DashBoard extends AppCompatActivity {

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

    // Back Button Functionality
    // Like MX PLayer
    @Override
    public void onBackPressed() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        // hiding the ActionBar
        getSupportActionBar().hide();

        // method to initialise the UI Elements
        initialiseUIElements();

        // setting up the texts and image in navigation header
        String mUserName = "@"+facultyUserName;
        headerFacultyName.setText(facultyName);
        headerUserName.setText(mUserName);
        headerPushId.setText(facultyPushId);

        if(facultyGender.equals(MALE)){
            headerImage.setImageResource(R.drawable.male);
        }
        else{
            // gender may be female, or Rather Not Say
            headerImage.setImageResource(R.drawable.female);
        }

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

                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Logout");
                alertDialog.show();

                return false;
            }
        });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements() {
        // drawer layout
        drawerLayout = findViewById(R.id.drawerLayout);

        // NavigationView
        navigationView = findViewById(R.id.navigationView);

        // alert dialog builder
        builder = new AlertDialog.Builder(this);

        // method to setup the Alert Dialog Builder
        setupAlertDialogBuilder();

        // text view
        headerText = findViewById(R.id.headerText);

        // Elements in the navigation view header
        // text view
        headerFacultyName = navigationView.getHeaderView(0).findViewById(R.id.header_name);
        headerUserName = navigationView.getHeaderView(0).findViewById(R.id.header_user_id);
        headerPushId = navigationView.getHeaderView(0).findViewById(R.id.header_push_id);

        // image view
        headerImage = navigationView.getHeaderView(0).findViewById(R.id.header_image);
    }

    // method to setup the Alert Dialog Builder
    private void setupAlertDialogBuilder(){
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
                        editor.apply();

                        // passing the faculty back to the MainActivity
                        Intent intent = new Intent(DashBoard.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}