package com.surya.miniproject.fragments.hod;

import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.CSE_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.ECE_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.FACULTIES;
import static com.surya.miniproject.constants.Strings.FACULTY_USER_NAME;
import static com.surya.miniproject.constants.Strings.FEMALE;
import static com.surya.miniproject.constants.Strings.MALE;
import static com.surya.miniproject.constants.Strings.RATHER_NOT_SAY;
import static com.surya.miniproject.constants.Strings.SH_DEPARTMENT;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.hod.AddFacultyAdapter;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HODAddStaffsFragment extends Fragment {

    // UI Elements
    private RecyclerView recyclerView;
    private Button addFaculty;
    private RadioGroup gender, designation, department;
    private EditText name;

    // data fields
    private String facultyName = null;
    private String departmentValue = null;
    private String designationValue = null;
    private String facultyGender = null;

    // adapter
    private AddFacultyAdapter adapter;

    private ArrayList<Class> allClasses = new ArrayList<>();
    public static ArrayList<Class> addedClasses = new ArrayList<>();
    public static ArrayList<String> addedClassesPushID = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_h_o_d_add_staffs, container, false);

        // method to initialise the UI Elements
        initialiseUIElements(view);

        // listener for the department group
        department.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(group.getCheckedRadioButtonId() == R.id.hod_faculty_cse_dep){
                    departmentValue = CSE_DEPARTMENT;
                }
                else if(group.getCheckedRadioButtonId() == R.id.hod_faculty_ece_dep){
                    departmentValue = ECE_DEPARTMENT;
                }
                else{
                    // as of now, First Years
                    departmentValue = SH_DEPARTMENT;
                }
            }
        });

        // listener for the gender radio group
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(group.getCheckedRadioButtonId() == R.id.hod_faculty_male){
                    facultyGender = MALE;
                }
                else if(group.getCheckedRadioButtonId() == R.id.hod_faculty_female){
                    facultyGender = FEMALE;
                }
                else{
                    // Rather not say
                    facultyGender = RATHER_NOT_SAY;
                }
            }
        });

        // listener for the designation radio group
        designation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(group.getCheckedRadioButtonId() == R.id.hod_fac_mr_des){
                    designationValue = "Mr. ";
                }
                else if(group.getCheckedRadioButtonId() == R.id.hod_fac_mrs_des){
                    designationValue = "Mrs. ";
                }
                else{
                    // Designation - Ms.
                    designationValue = "Ms. ";
                }
            }
        });

        // showing the classes list to the hod
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference()
                .child(CLASSES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // clearing the allClasses array lit
                            allClasses.clear();

                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Class classx = dataSnapshot.getValue(Class.class);

                                // adding the classx to the array list
                                allClasses.add(classx);
                            }

                            // setting up the adapter for the recycler view
                            adapter = new AddFacultyAdapter(getContext(), allClasses);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // on click listener for the button
        addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking if the hod entered all the details
                class check{
                    private void check(){
                        facultyName = name.getText().toString();

                        if(facultyName.length() == 0){
                            Toast.makeText(getContext(), "Faculty Name cannot be empty!!!", Toast.LENGTH_SHORT).show();
                        }

                        else if(facultyGender == null){
                            Toast.makeText(getContext(), "Choose a gender", Toast.LENGTH_SHORT).show();
                        }

                        else if(designationValue == null){
                            Toast.makeText(getContext(), "Choose a designation", Toast.LENGTH_SHORT).show();
                        }

                        else if(departmentValue == null){
                            Toast.makeText(getContext(), "Choose a department", Toast.LENGTH_SHORT).show();
                        }

                        // checking if any faculty members has this same name
                        facultyName = designationValue + facultyName; // Mr. + Anantraj
//                        Toast.makeText(getContext(), facultyName, Toast.LENGTH_SHORT).show();
//
//                        final boolean[] duplicate = {false};
//                        class name{
//                            private void nameCheck(){
//                                firebaseDatabase.getReference()
//                                        .child(FACULTIES)
//                                        .addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                if(snapshot.exists()){
//                                                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
//                                                        Faculty faculty = snapshot1.getValue(Faculty.class);
//
//                                                        if(! (duplicate[0])){
//                                                            if(faculty.getFacultyName().contains(facultyName)){
//                                                                duplicate[0] = true;
//                                                                Toast.makeText(getContext(), "Faculty Name already exists", Toast.LENGTH_SHORT).show();
//                                                                break;
//                                                            }
//                                                        }
//                                                    }
//
//                                                    // all conditions have been met
//                                                    if(! (duplicate[0])){
//                                                        // by here the inputs are valid
//                                                        // creating a faculty object
//                                                        Faculty faculty = new Faculty(facultyName, departmentValue, facultyGender);
//
//                                                        String key = firebaseDatabase.getReference().push().getKey();
//                                                        faculty.setFacultyPushId(key);
//
//                                                        // checking if the hod decided to add the faculty to any of the classes
//                                                        if(addedClasses.size() == 0){
//                                                            // hod did not add the faculty to any of the classes
//                                                            // just updating the staff to the database
//                                                            firebaseDatabase.getReference()
//                                                                    .child(FACULTIES)
//                                                                    .child(key)
//                                                                    .setValue(faculty);
//                                                        }
//                                                        else{
//                                                            Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });
//                            }
//                        }
//
//                        new name().nameCheck();

                        // by here the inputs are valid
                        // creating a faculty object
                        Faculty faculty = new Faculty(facultyName, facultyGender, departmentValue);

                        String key = firebaseDatabase.getReference().push().getKey();
                        faculty.setFacultyPushId(key);

                        // checking if the hod decided to add the faculty to any of the classes
                        if(addedClasses.size() == 0){
                            // hod did not add the faculty to any of the classes
                            // just updating the staff to the database
                            firebaseDatabase.getReference()
                                    .child(FACULTIES)
                                    .child(key)
                                    .setValue(faculty)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getContext(), "Faculty Added", Toast.LENGTH_SHORT).show();

                                                // passing the hod to All faculty members fragment
                                                // resetting the fields
                                                name.setText("");
                                            }
                                        }
                                    });
                        }
                        else{
                            // first updating the staff to the database
                            firebaseDatabase.getReference()
                                    .child(FACULTIES)
                                    .child(key)
                                    .setValue(faculty)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                // updating the array list of faculties of the classes
                                                for(int i = 0; i < addedClasses.size(); i++){
                                                    String key = addedClassesPushID.get(i);

                                                    firebaseDatabase.getReference()
                                                            .child(CLASSES)
                                                            .child(key)
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    Class classx = snapshot.getValue(Class.class);

                                                                    // adding the current faculty to the faculty members
                                                                    ArrayList<String> list = classx.getFacultyMembers();

                                                                    // adding to list, if the staff is not present already
                                                                    if(! (list.contains(facultyName))){
                                                                        list.add(facultyName);

                                                                        Map<String, Object> map = new HashMap<String, Object>(){
                                                                            {
                                                                                put("facultyMembers", list);
                                                                            }
                                                                        };

                                                                        // updating this list in the database
                                                                        firebaseDatabase
                                                                                .getReference()
                                                                                .child(CLASSES)
                                                                                .child(key)
                                                                                .updateChildren(map);

                                                                        // resetting the fields
                                                                        name.setText("");
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                }

                new check().check();
            }
        });

        return view;
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(View view){
        // recycler view
        recyclerView = view.findViewById(R.id.hod_faculty_rec_view);

        // button
        addFaculty = view.findViewById(R.id.hod_faculty_add_btn);

        // radio group
        gender = view.findViewById(R.id.hod_faculty_gender_group);
        designation = view.findViewById(R.id.hod_faculty_designation);
        department = view.findViewById(R.id.hod_faculty_department);

        // edit text
        name = view.findViewById(R.id.hod_faculty_name_edit_text);
    }
}