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

    // menu items clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // back button in the action bar
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
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
        //CurrentClass currentClass = new CurrentClass(className, department, classPushId, classAdvisor);

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
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(attendanceEditedForToday){
//                    // attendance for today has been already been updated
//                    // so, passing the faculty to the AttendanceEditing activity
//                    Intent intent = new Intent(ClassAttendance.this, AttendanceEditing.class);
//                    intent.putExtra(CLASS_NAME, className);
//                    intent.putExtra(CLASS_DEPARTMENT, department);
//                    startActivity(intent);
//                }
//                else{
//                    // attendance for today, has not yet been uploaded
//                    // passing the user to the AttendanceMarking activity
//                    Intent intent = new Intent(ClassAttendance.this, AttendanceMarking.class);
//                    intent.putExtra(CLASS_NAME, className);
//                    intent.putExtra(CLASS_DEPARTMENT, department);
//                    startActivity(intent);
//                }
//            }
//        });

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
        floatingActionButton.setVisibility(View.GONE);

        // hiding the floating action button for all the faculty members but for the class advisor and that too nly in the working days
        if(getSharedPreferences(APP_DEFAULTS, Context.MODE_PRIVATE).getBoolean(TODAY_IS_A_LEAVE, false)){
            // leave
            // so hiding the button, in spite of everyone
            floatingActionButton.setVisibility(View.GONE);

            // showing the toast to the faculty that today is a holiday
            Toast.makeText(this, "Today is a holiday!", Toast.LENGTH_SHORT).show();
        }
//        else{
//            // working day
//            // so making the button visible only to the class advisor
//            if(classAdvisor.equals(facultyName)){
//                floatingActionButton.setVisibility(View.VISIBLE);
//            }
//            else{
//                floatingActionButton.setVisibility(View.GONE);
//            }
//        }
    }
}