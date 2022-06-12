package com.surya.miniproject.activities.admin;

import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.activities.DashBoard.facultyPushId;
import static com.surya.miniproject.activities.DashBoard.facultyUserName;
import static com.surya.miniproject.constants.Strings.APP_DEFAULTS;
import static com.surya.miniproject.constants.Strings.FACULTY_GENDER;
import static com.surya.miniproject.constants.Strings.MALE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.surya.miniproject.R;
import com.surya.miniproject.activities.MainActivity;
import com.surya.miniproject.fragments.admin.AdminNotificationsFragment;

public class AdminPanel extends AppCompatActivity{

    // UI Elements
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView headerText, headerNameText, headerUserName, headerPushId;
    private ImageView imageView;

    private int backButtonPressed = 0;

    // Back Button Functionality
    // Like MX PLayer
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
        setContentView(R.layout.activity_admin_panel);

        // hiding the action bar
        getSupportActionBar().hide();

        // method to initialise the UI Elements
        initialiseUIElements();

        // opening/closing the drawer on click the menu image
        findViewById(R.id.admin_menu)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawerLayout.open();
                    }
                });

        // Navigation view essentials
        navigationView.setItemIconTintList(null);

//        // setting up the texts and image in navigation header
//        String mUserName = "@"+facultyUserName;
//        headerNameText.setText(facultyName);
//        headerUserName.setText(mUserName);
//        headerPushId.setText(facultyPushId);
//
//        if(getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getString(FACULTY_GENDER, null).equals(MALE)){
//            imageView.setImageResource(R.drawable.male);
//        }
//        else{
//            // gender may be female, or Rather Not Say
//            imageView.setImageResource(R.drawable.female);
//        }

        NavController navController = Navigation.findNavController(this, R.id.admin_dash_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        // changing the header text, when the user goes into different fragments
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                headerText.setText(navDestination.getLabel());
            }
        });

        // logout option in the navigation view
        navigationView.getMenu().findItem(R.id.admin_menu_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // closing the drawer
                drawerLayout.close();

                // showing an alert dialog to the admin
                class Alert{
                    private final AlertDialog.Builder builder;

                    // Constructor
                    public Alert() {
                        // initialising the builder
                        builder = new AlertDialog.Builder(AdminPanel.this);

                        // setting up the builder
                        builder.setMessage("Do you want to exit the Admin Panel ?")
                                .setCancelable(true)
                                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // just dismissing the dialog
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // passing the user back to the MainActivity
                                        Intent intent = new Intent(AdminPanel.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        finishAffinity();
                                    }
                                });
                    }

                    public void create(){
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Exit");
                        alertDialog.show();
                    }
                }

                new Alert().create();

                return false;
            }
        });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(){
        // drawer layout
        drawerLayout = findViewById(R.id.admin_panel_drawer);

        // navigation view
        navigationView = findViewById(R.id.adminNavigationView);

        // text view
        headerText = findViewById(R.id.admin_text);

        // Elements in the navigation view header
        View view = navigationView.getHeaderView(0);

        // text view
        headerNameText = view.findViewById(R.id.header_name);
        headerUserName = view.findViewById(R.id.header_user_id);
        headerPushId = view.findViewById(R.id.header_push_id);

        // image view
        imageView = view.findViewById(R.id.header_image);
    }
}