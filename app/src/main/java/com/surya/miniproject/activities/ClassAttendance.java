package com.surya.miniproject.activities;

import static com.surya.miniproject.activities.DashBoard.facultyDepartment;
import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.constants.Strings.APP_DEFAULTS;
import static com.surya.miniproject.constants.Strings.ATTENDANCE;
import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.CLASS_ADVISOR;
import static com.surya.miniproject.constants.Strings.CLASS_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_PUSH_ID;
import static com.surya.miniproject.constants.Strings.FACULTY_GENDER;
import static com.surya.miniproject.constants.Strings.FACULTY_IS_AN_HOD;
import static com.surya.miniproject.constants.Strings.MALE;
import static com.surya.miniproject.constants.Strings.REQUESTS;
import static com.surya.miniproject.constants.Strings.REQUEST_ONE_TIME;
import static com.surya.miniproject.constants.Strings.TODAY_IS_A_LEAVE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.activities.hod.StaffDetailsActiity;
import com.surya.miniproject.adapters.AttendanceAdapter;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.CurrentClass;
import com.surya.miniproject.models.Request;
import com.surya.miniproject.models.Student;
import com.surya.miniproject.utility.Functions;

import java.lang.ref.WeakReference;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ClassAttendance extends AppCompatActivity {

    // UI Elements
    private RecyclerView recyclerView;
    private TextView name;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Attendance> result = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    public static Attendance todayAttendance;

    private String classPushId;
    private String className;
    private String classAdvisor;

    // adapter
    private AttendanceAdapter attendanceAdapter;

    public static ArrayList<Student> students = new ArrayList<Student>();
    private boolean attendanceEditedForToday = false;

    private Request req;

    // Back Button Functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // inflating the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // clearing the menu if any exists
        menu.clear();

        if( (getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getBoolean(FACULTY_IS_AN_HOD, false))
            || (classAdvisor.equals(facultyName)) ){
            // inflating the menu, if the faculty is the HOD or the class advisor
            getMenuInflater().inflate(R.menu.class_menu, menu);
        }
        else{
            // if the staff is not an HOD or the class advisor
            // checking if the staff has requested for the attendance already
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference()
                    .child(REQUESTS)
                    .child(facultyDepartment)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                // going through the requests
                                boolean alreadyRequested = false;

                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                    Request request = snapshot1.getValue(Request.class);

                                    if(request.getClassName().equals(className)){
                                        if(request.getRequestSender().equals(facultyName)){
                                            // means, this faculty has already requested for this class attendance
                                            req = request;
                                            alreadyRequested = true;
                                            break;
                                        }
                                    }
                                }

                                if(alreadyRequested){
                                    // inflating the request accept menu
                                    getMenuInflater().inflate(R.menu.request_status_menu, menu);
                                }
                                else{
                                    // inflating the request access menu
                                    getMenuInflater().inflate(R.menu.request_menu, menu);
                                }
                            }
                            else{
                                // no requests have been made by any staff to any staff
                                // inflating the request access menu
                                getMenuInflater().inflate(R.menu.request_menu, menu);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        return super.onCreateOptionsMenu(menu);
    }

    // menu items clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // get staff details menu
        if(item.getItemId() == R.id.menu_staff_details){
            // passing the user to the staff details of a class activity
            // passing the classPushId as the intent
            Intent intent = new Intent(this, StaffDetailsActiity.class);
            intent.putExtra(CLASS_PUSH_ID, classPushId);
            intent.putExtra(CLASS_NAME, className);
            startActivity(intent);
        }
        else if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        else if(item.getItemId() == R.id.menu_request_access){
            // showing the request bottom sheet
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ClassAttendance.this, R.style.BottomSheetDialogTheme);
            View view = LayoutInflater.from(ClassAttendance.this)
                    .inflate(R.layout.requesting_access_bottpm_sheet, (ConstraintLayout) findViewById(R.id.requesting_access_layout));

            class elements{
                private final Button button;
                private final TextView staffName, warnText, classText;
                private final Spinner spinner;
                private final Request request;
                private final ImageView imageView;
                private final ArrayAdapter<String> adapter;

                private final String[] requestString = new String[]{
                        "Only 1 Time",
                        "Many More Time"
                };

                // Constructor
                public elements(View view) {
                    // initialising the UI Elements
                    // button
                    button = view.findViewById(R.id.request_access);

                    // image view
                    imageView = view.findViewById(R.id.request_staff_image);

                    // setting up the image
                    if(getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getString(FACULTY_GENDER, null).equals(MALE)){
                        imageView.setImageResource(R.drawable.male);
                    }
                    else{
                        // gender may be female, or Rather Not Say
                        imageView.setImageResource(R.drawable.female);
                    }

                    // text view
                    staffName = view.findViewById(R.id.request_staff_name);
                    warnText = view.findViewById(R.id.request_warn);
                    classText = view.findViewById(R.id.request_class_text);

                    // spinner
                    // setting up the ArrayAdapter for the spinner
                    adapter = new ArrayAdapter(ClassAttendance.this, android.R.layout.simple_spinner_dropdown_item, requestString);

                    spinner = view.findViewById(R.id.request_spinner);
                    spinner.setAdapter(adapter);

                    // setting up the facultyName and the classText
                    staffName.setText(facultyName);
                    classText.setText("You are requesting for " + className + " attendance");

                    // setting up the request object
                    request = new Request(
                            facultyName,
                            classAdvisor,
                            REQUEST_ONE_TIME,
                            className,
                            new Functions().date(),
                            false
                    );

                    warnText.setText(request.requestWarnText());

                    // on item selected listener for the spinner
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position == 0){
                                onNothingSelected(parent);
                            }
                            else if(position == 1){
                                // Many More Time selected
                                request.setRequestType(REQUEST_ONE_TIME);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // nothing selected
                            request.setRequestType(REQUEST_ONE_TIME);
                        }
                    });

                    // on click listener for the button
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // updating the request in the firebase database
                            ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();
                            NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();

                            if(mobile == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTED){
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                String key = firebaseDatabase.getReference().push().getKey();

                                // setting up the request push id
                                request.setRequestPushId(key);
                                request.setRequestTimedOut(false);

                                firebaseDatabase.getReference()
                                        .child(REQUESTS)
                                        .child(facultyDepartment)
                                        .child(key)
                                        .setValue(request)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                bottomSheetDialog.dismiss();
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(ClassAttendance.this, "Turn on the Mobile Data/Wifi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            new elements(view);

            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        }
        else if(item.getItemId() == R.id.menu_request_status){
            // inflating the request status bottom sheet
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
            View view = LayoutInflater.from(ClassAttendance.this)
                    .inflate(R.layout.request_status_bottom_sheet, (ConstraintLayout) findViewById(R.id.request_status_layout));

            class elements{
                private final View view;
                private final TextView staffName, statusText;
                private final ImageView imageView;

                // Constructor
                public elements(View view) {
                    this.view = view;

                    // initialising the UI Elements
                    // text view
                    staffName = view.findViewById(R.id.request_status_staff_name);
                    statusText = view.findViewById(R.id.request_status_text);

                    // image view
                    imageView = view.findViewById(R.id.request_status_staff_image);

                    // setting up the image
                    if(getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getString(FACULTY_GENDER, null).equals(MALE)){
                        imageView.setImageResource(R.drawable.male);
                    }
                    else{
                        // gender may be female, or Rather Not Say
                        imageView.setImageResource(R.drawable.female);
                    }

                    // setting up the texts
                    staffName.setText(facultyName);
                    statusText.setText(req.requestStatusText());
                }
            }

            new elements(view);

            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_attendance);

        // getting the data from the intent
        className = getIntent().getStringExtra(CLASS_NAME);
        classPushId = getIntent().getStringExtra(CLASS_PUSH_ID);
        classAdvisor = getIntent().getStringExtra(CLASS_ADVISOR);
        String department = getIntent().getStringExtra(CLASS_DEPARTMENT);

        // creating a CurrentClass object, for future reference
        CurrentClass currentClass = new CurrentClass(className, department, classPushId, classAdvisor);

        // method to initialise the UI Elements
        initialiseUIElements(classAdvisor);

        // setting the name
        name.setText(className);

        // setting the title of the Action Bar
        getSupportActionBar().setTitle(className);

        // enabling the home button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();
//        NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();
//
//        // getting the students list from the database
//        String todayDate = new Functions().date();
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseDatabase.getReference()
//                .child(CLASSES)
//                .child(classPushId)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            // clearing the students array list
//                            students.clear();
//
//                            Class classx = snapshot.getValue(Class.class);
//                            students = classx.getStudents();
//
//                            CurrentClass.currentClassFacultyMember = classx.getFacultyMembers();
//                        }
//
//                        firebaseDatabase.getReference()
//                                .child(ATTENDANCE)
//                                .child(className)
//                                .child(LocalDateTime.now().getMonth()+"-"+LocalDateTime.now().getYear())
//                                .addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        if(snapshot.exists()){
//                                            // clearing the hash table
//                                            result.clear();
//                                            dates.clear();
//                                            int index = 0;
//
//                                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
//                                                Attendance a = snapshot1.getValue(Attendance.class);
//                                                dates.add(snapshot1.getKey());
//
//                                                if(snapshot1.getKey().equals(todayDate)){
//                                                    // attendance has been updated for today, already by this staff
//                                                    attendanceEditedForToday = true;
//
//                                                    // adding today's attendance
//                                                    todayAttendance = a;
//
//                                                    floatingActionButton.setImageResource(R.drawable.ic_edit);
//                                                }
//
//                                                // adding the hash table to the array list
//                                                result.add(index, a);
//
//                                                index++;
//                                            }
//                                        }
//
//                                        if(! attendanceEditedForToday){
//                                            floatingActionButton.setImageResource(R.drawable.ic_add);
//                                        }
//
//                                        // setting up the recycler view
//                                        attendanceAdapter = new AttendanceAdapter(ClassAttendance.this, className, result, dates);
//                                        AttendanceAdapter.students = students;
//                                        recyclerView.setAdapter(attendanceAdapter);
//                                        recyclerView.setLayoutManager(new LinearLayoutManager(ClassAttendance.this));
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

        // on click listener for the floating action button
        // button visible only to the class advisor, that too only in the working days
        // in leaves, it will not be visible
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(attendanceEditedForToday){
                    // attendance for today has been already been updated
                    // so, passing the faculty to the AttendanceEditing activity
                    Intent intent = new Intent(ClassAttendance.this, AttendanceEditing.class);
                    intent.putExtra(CLASS_NAME, className);
                    intent.putExtra(CLASS_DEPARTMENT, department);
                    startActivity(intent);
                }
                else{
                    // attendance for today, has not yet been uploaded
                    // passing the user to the AttendanceMarking activity
                    Intent intent = new Intent(ClassAttendance.this, AttendanceMarking.class);
                    intent.putExtra(CLASS_NAME, className);
                    intent.putExtra(CLASS_DEPARTMENT, department);
                    startActivity(intent);
                }
            }
        });

        // fetching the attendance
        new FetchAttendanceAsyncTask(this, this).execute(FirebaseDatabase.getInstance());
    }

    private static class FetchAttendanceAsyncTask extends AsyncTask<FirebaseDatabase, Integer, Integer>{
        private final WeakReference<ClassAttendance> weakReference;
        private final Context context;

        // Constructor
        public FetchAttendanceAsyncTask(ClassAttendance attendance, Context context) {
            weakReference = new WeakReference<ClassAttendance>(attendance);
            this.context = context;
        }

        @Override
        protected Integer doInBackground(FirebaseDatabase... firebaseDatabases) {
            FirebaseDatabase firebaseDatabase = firebaseDatabases[0];
            ClassAttendance attendance = weakReference.get();

            // getting the students list from the database
            String todayDate = new Functions().date();
            firebaseDatabase.getReference()
                    .child(CLASSES)
                    .child(attendance.classPushId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                // clearing the students array list
                                students.clear();

                                Class classx = snapshot.getValue(Class.class);
                                students = classx.getStudents();

                                CurrentClass.currentClassFacultyMember = classx.getFacultyMembers();
                            }

                            firebaseDatabase.getReference()
                                    .child(ATTENDANCE)
                                    .child(attendance.className)
                                    .child(LocalDateTime.now().getMonth()+"-"+LocalDateTime.now().getYear())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                // clearing the hash table
                                                attendance.result.clear();
                                                attendance.dates.clear();
                                                int index = 0;

                                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                                    Attendance a = snapshot1.getValue(Attendance.class);
                                                    attendance.dates.add(snapshot1.getKey());

                                                    if(snapshot1.getKey().equals(todayDate)){
                                                        // attendance has been updated for today, already by this staff
                                                        attendance.attendanceEditedForToday = true;

                                                        // adding today's attendance
                                                        todayAttendance = a;

                                                        attendance.floatingActionButton.setImageResource(R.drawable.ic_edit);
                                                    }

                                                    // adding the hash table to the array list
                                                    attendance.result.add(index, a);

                                                    index++;
                                                }
                                            }

                                            // snapshots may have the attendance data in different order
                                            // say, like 1 2 15 16 17 3 4 5 (dates)
                                            // these dates hve to be rearranged
                                            class ArrangeLists{
                                                // Constructor
                                                public ArrangeLists(){
                                                    // going through the dates array list
                                                    for(int i = 0; i < attendance.dates.size(); i++){
                                                        for(int j = i+1; j < attendance.dates.size(); j++){
                                                            int day1 = Integer.parseInt(attendance.dates.get(i).split("-")[0]);
                                                            int day2 = Integer.parseInt(attendance.dates.get(j).split("-")[0]);

                                                            // condition is day2 should always be greater than day1
                                                            if(day1 > day2){
                                                                /*
                                                                 *
                                                                 * exchanging the dates
                                                                 *
                                                                 * */
                                                                String temp = attendance.dates.get(j);

                                                                // storing the i data in the j
                                                                attendance.dates.remove(j);
                                                                attendance.dates.add(j, attendance.dates.get(i));

                                                                // storing the j data in the i
                                                                attendance.dates.remove(i);
                                                                attendance.dates.add(i, temp);

                                                                /*
                                                                 *
                                                                 * exchanging the attendance data
                                                                 *
                                                                 * */
                                                                Attendance temporary = attendance.result.get(j);

                                                                // storing the i data in the j
                                                                attendance.result.remove(j);
                                                                attendance.result.add(j, attendance.result.get(i));

                                                                // storing the j data in the i
                                                                attendance.result.remove(i);
                                                                attendance.result.add(i, temporary);
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            new ArrangeLists();

                                            // if today's attendance has not been updated yet, setting the add icon as the floating button image
                                            if(! attendance.attendanceEditedForToday){
                                                attendance.floatingActionButton.setImageResource(R.drawable.ic_add);
                                            }

                                            // setting up the recycler view
                                            attendance.attendanceAdapter = new AttendanceAdapter(context, attendance.className, attendance.result, attendance.dates);
                                            AttendanceAdapter.students = students;
                                            attendance.recyclerView.setAdapter(attendance.attendanceAdapter);
                                            attendance.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            return null;
        }
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(String classAdvisor) {
        // recycler view
        recyclerView = findViewById(R.id.class_attendance_rec_view);

        // text view
        name = findViewById(R.id.class_attebdance_name);

        // floating action button
        floatingActionButton = findViewById(R.id.add_attendance_btn);

        // hiding the floating action button for all the faculty members but for the class advisor and that too nly in the working days
        if(getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getBoolean(TODAY_IS_A_LEAVE, false)){
            // leave
            // so hiding the button, in spite of everyone
            floatingActionButton.setVisibility(View.GONE);

            // showing the toast to the faculty that today is a holiday
            Toast.makeText(this, "Today is a holiday!", Toast.LENGTH_SHORT).show();
        }
        else{
            // working day
            // so making the button visible only to the class advisor
            if(classAdvisor.equals(facultyName)){
                floatingActionButton.setVisibility(View.VISIBLE);
            }
            else{
                floatingActionButton.setVisibility(View.GONE);
            }
        }
    }
}