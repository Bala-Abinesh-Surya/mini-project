package com.surya.miniproject.activities;

import static com.surya.miniproject.activities.ClassAttendance.todayAttendance;
import static com.surya.miniproject.activities.DashBoard.facultyDepartment;
import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.constants.Strings.ATTENDANCE;
import static com.surya.miniproject.constants.Strings.CLASS_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.NOTIFICATIONS;
import static com.surya.miniproject.constants.Strings.NOTIFICATION_EDIT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.AttendanceEditingAdapter;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Notification;
import com.surya.miniproject.utility.Functions;

import java.time.LocalDateTime;

public class AttendanceEditing extends AppCompatActivity {

    // UI Elements
    private RecyclerView editRecyclerView, absenteesRecyclerView;
    private TextView editClassName, editClassDate, editClassNumber, noAbsentees, editedBy;
    private Button edit;

    // Back Button Functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_editing);

        // method to initialise the UI Elements
        initialiseUIElements();

        // getting the data from the intents
        String className = getIntent().getStringExtra(CLASS_NAME);
        String department = getIntent().getStringExtra(CLASS_DEPARTMENT);

        // setting the title of the Action Bar
        getSupportActionBar().setTitle(className);

        // changing the name
        editClassName.setText(className);
        editedBy.setText(facultyName);
        editClassDate.setText("Attendance of " + new Functions().date());

        // setting up the attendance recycler view
        AttendanceEditingAdapter attendanceEditingAdapter = new AttendanceEditingAdapter(this,
                absenteesRecyclerView,
                editClassNumber,
                noAbsentees,
                edit
        );
        editRecyclerView.setAdapter(attendanceEditingAdapter);
        editRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        // on click listener for the attendance update button
        // will be enabled, only when the staff made some changes
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creating an Attendance object
                Attendance attendance = new Attendance(className, new Functions().date());
                attendance.setEditedBy(facultyName);
                attendance.setJson(AttendanceEditingAdapter.updatedJson);

                ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();
                NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();

                if (mobile == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTED) {
                    // either mobile data or the Wifi is turned on
                    // updating the object in the database
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference()
                            .child(ATTENDANCE)
                            .child(className)
                            .child(LocalDateTime.now().getMonth()+"-"+LocalDateTime.now().getYear())
                            .child(new Functions().date())
                            .setValue(attendance)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        // creating a notification object
                                        Notification notification = new Notification(facultyDepartment, facultyName, className, new Functions().date());
                                        notification.setCategory(NOTIFICATION_EDIT);

                                        // uploading the notification in the database
                                        firebaseDatabase.getReference()
                                                        .child(NOTIFICATIONS)
                                                                .child(department)
                                                                        .push()
                                                                                .setValue(notification)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if(task.isSuccessful()){
                                                                                                    // passing the user back to the ClassAttendance activity
                                                                                                    onBackPressed();
                                                                                                    Toast.makeText(AttendanceEditing.this, "Attendance Updated", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                    }
                                    else{
                                        Toast.makeText(AttendanceEditing.this, "Try Again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    // telling the faculty tp turn on the mobile data/wifi
                    Toast.makeText(AttendanceEditing.this, "Turn on the Mobile Data/Wifi to edit the attendance", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(){
        // recycler view
        editRecyclerView = findViewById(R.id.edit_class_attendance);
        absenteesRecyclerView = findViewById(R.id.edit_class_absentees);

        // button
        edit = findViewById(R.id.edit_btn);

        // text views
        editClassName = findViewById(R.id.edit_class_name);
        editClassDate = findViewById(R.id.edit_class_date);
        editClassNumber = findViewById(R.id.edit_class_absentees_num);
        noAbsentees = findViewById(R.id.edit_class_no_absent);
        editedBy = findViewById(R.id.class_edit_staff);
    }
}