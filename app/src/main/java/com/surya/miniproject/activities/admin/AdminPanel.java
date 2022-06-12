package com.surya.miniproject.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.surya.miniproject.R;
import com.surya.miniproject.fragments.admin.AdminNotificationsFragment;

public class AdminPanel extends AppCompatActivity{

    // UI Elements
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView headerText;

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

        NavController navController = Navigation.findNavController(this, R.id.admin_dash_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        // changing the header text, when the user goes into different fragments
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                headerText.setText(navDestination.getLabel());
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
    }
}