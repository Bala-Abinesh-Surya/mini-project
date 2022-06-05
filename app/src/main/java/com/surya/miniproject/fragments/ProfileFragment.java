package com.surya.miniproject.fragments;

import static com.surya.miniproject.constants.Strings.APP_DEFAULTS;
import static com.surya.miniproject.constants.Strings.FACULTY_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.FACULTY_GENDER;
import static com.surya.miniproject.constants.Strings.FACULTY_IS_AN_HOD;
import static com.surya.miniproject.constants.Strings.FACULTY_NAME;
import static com.surya.miniproject.constants.Strings.FACULTY_PUSH_ID;
import static com.surya.miniproject.constants.Strings.FACULTY_USER_NAME;
import static com.surya.miniproject.constants.Strings.MALE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surya.miniproject.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    // UI Elements
    private CircleImageView imageView;
    private TextView staffName, department, gender, pushId, hod, userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // method to initialise the UI Elements
        initialiseUIElements(view);

        // getting the contents from the Shared Preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE);

        // setting up the texts
        staffName.setText(sharedPreferences.getString(FACULTY_NAME, null));
        pushId.setText(sharedPreferences.getString(FACULTY_PUSH_ID, null));
        gender.setText(sharedPreferences.getString(FACULTY_GENDER, null));
        department.setText(sharedPreferences.getString(FACULTY_DEPARTMENT, null));
        userName.setText("@"+sharedPreferences.getString(FACULTY_USER_NAME, null));
        if(sharedPreferences.getBoolean(FACULTY_IS_AN_HOD, false)){
            hod.setText("YES");
        }
        else{
            hod.setText("NO");
        }

        // setting up the image view
        if(sharedPreferences.getString(FACULTY_GENDER, null).equals(MALE)){
            imageView.setImageDrawable(getContext().getDrawable(R.drawable.male));
        }
        else{
            imageView.setImageDrawable(getContext().getDrawable(R.drawable.female));
        }

        return view;
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(View view){
        // image view
        imageView = view.findViewById(R.id.profile_image);

        // text view
        staffName = view.findViewById(R.id.profile_name);
        pushId = view.findViewById(R.id.profile_push);
        gender = view.findViewById(R.id.profile_gender);
        department = view.findViewById(R.id.profile_department);
        hod = view.findViewById(R.id.profile_hod);
        userName = view.findViewById(R.id.profile_user_name);
    }
}