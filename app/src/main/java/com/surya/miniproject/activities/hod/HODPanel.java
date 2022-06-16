package com.surya.miniproject.activities.hod;

import static com.surya.miniproject.activities.DashBoard.facultyDepartment;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.surya.miniproject.R;
import com.surya.miniproject.fragments.hod.HODStaffsListFragment;

public class HODPanel extends AppCompatActivity implements HODStaffsListFragment.FacultyDetailsBottomSheet {

    // UI Elements
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView headerUserName, headerPushId, headerFacultyName;
    private ImageView headerImage;
    private TextView headerText;

    // Back Button Functionality
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(navigationView)){
            // closing the drawer if is open
            drawerLayout.close();
        }
        else{
            new alert().show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hodpanel);

        // hiding the Action Bar
        getSupportActionBar().hide();

        // method to initialise the UI Elements
        initialiseUIElements();

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

        // opening/closing the drawer on clicking the menu in the so called action bar
        findViewById(R.id.dash_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // navigation view essentials
        navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.hod_dash_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        // changing the header text, when the user goes into different fragments
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                String header = navDestination.getLabel().toString();

                if(header.equals("All Faculty Members")){
                    header = "Faculty Members of " + facultyDepartment;
                    headerText.setText(header);
                }
                else{
                    headerText.setText(header);
                }
            }
        });

        // on click listener for the logout option the navigation view
        navigationView.getMenu().findItem(R.id.hod_exit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // closing the drawer layout
                drawerLayout.close();

                alert alert = new alert();
                alert.show();

                return false;
            }
        });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements() {
        // drawer layout
        drawerLayout = findViewById(R.id.hod_drawer_layout);

        // NavigationView
        navigationView = findViewById(R.id.hod_nav_view);

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

    class alert{
        private AlertDialog.Builder builder;

        // Constructor
        public alert() {
            // initialising the builder
            builder = new AlertDialog.Builder(HODPanel.this);

            // setting up the builder
            builder.setMessage("Do you want to exit the HOD Panel?")
                    .setCancelable(false)
                    .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // exiting the panel and passing the hod to the staff dashboard
                            finish();
                        }
                    });
        }

        private void show(){
            AlertDialog alertDialog = builder.create();
            alertDialog.setTitle("Exit");
            alertDialog.show();
        }
    }

    // returning the staff details bottom sheet
    @Override
    public View facultyDetailsBottomSheet() {
        return LayoutInflater.from(this)
                .inflate(R.layout.staff_info_bottom_sheet, (ConstraintLayout) findViewById(R.id.admin_all_faculty_bottom_sheet));
    }
}