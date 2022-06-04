package com.surya.miniproject.activities;

import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.adapters.AttendanceMarkingAdapter.attendance;
import static com.surya.miniproject.constants.Strings.ATTENDANCE;
import static com.surya.miniproject.constants.Strings.CLASS_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.NOTIFICATIONS;
import static com.surya.miniproject.constants.Strings.NOTIFICATION_UPDATE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.AttendanceMarkingAdapter;
import com.surya.miniproject.details.Data;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Notification;
import com.surya.miniproject.utility.Functions;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

public class AttendanceMarking extends AppCompatActivity {

    // UI Elements
    private TextView classNameText, editBy, absentText, absenteesNumber;
    private RecyclerView recyclerView, absentees;
    private Button update;

    // adapter
    private AttendanceMarkingAdapter attendanceMarkingAdapter;

    // Back button functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_marking);

        // getting the data from the intent
        String className = getIntent().getStringExtra(CLASS_NAME);
        String department = getIntent().getStringExtra(CLASS_DEPARTMENT);

        // method to initialise the UI Elements
        initialiseUIElements();

        // setting the title of the Action bar
        getSupportActionBar().setTitle(className);

        // changing the name
        classNameText.setText(className);
        editBy.setText(facultyName);

        // setting up the recycler view
        attendanceMarkingAdapter = new AttendanceMarkingAdapter(AttendanceMarking.this, absentees, absentText, absenteesNumber);
        recyclerView.setAdapter(attendanceMarkingAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(AttendanceMarking.this, 5));

        // on click listener for the update button
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creating an Attendance object
                Attendance a = new Attendance(className, new Functions().date());

                class Compute{
                    private String json(){
                        // computing the hashtable
                        Hashtable<String, String> table = new Hashtable<>();

                        for(int i = 0; i < ClassAttendance.students.size(); i++){
                            table.put(ClassAttendance.students.get(i).getStudentName(), attendance.get(i));
                        }

                        // converting the hashtable into json string
                        return new Gson().toJson(table);
                    }
                }

                a.setJson(new Compute().json());
                a.setEditedBy(facultyName);

                ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();
                NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();

                if (mobile == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTED) {
                    // updating the attendance in the database
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference()
                            .child(ATTENDANCE)
                            .child(className)
                            .child(LocalDateTime.now().getMonth()+"") // LocalDateTime.now().getMonth()+""
                            .child(new Functions().date())
                            .setValue(a)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        // creating a notification
                                        Notification notification = new Notification(department, facultyName, className, new Functions().date());
                                        notification.setCategory(NOTIFICATION_UPDATE);

                                        // uploading the notification in the database
                                        firebaseDatabase.getReference()
                                                .child(NOTIFICATIONS)
                                                .child(department)
                                                .push()
                                                .setValue(notification)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        // do exactly what the back button does
                                                        onBackPressed();
                                                    }
                                                });

                                    }
                                    else{
                                        // attendance has not been updated
                                        Toast.makeText(AttendanceMarking.this, "Failed... Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    // telling the user to turn on the mobile data/wifi
                    Toast.makeText(AttendanceMarking.this, "Turn on the Mobile Data/Wifi to Update", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // method to initialise the UI Elements
    private void initialiseUIElements() {
        // text view
        classNameText = findViewById(R.id.marking_class_name);
        editBy = findViewById(R.id.marking_edit_by);
        absentText = findViewById(R.id.no_absent_text);
        absenteesNumber = findViewById(R.id.number_absentees);

        // recycler view
        recyclerView = findViewById(R.id.marking_rec_view);
        absentees = findViewById(R.id.marking_absent_rec_view);

        // button
        update = findViewById(R.id.marking_update);

        // hiding the absentees recycler view and absentees number at first
        absentText.setVisibility(View.GONE);
        absenteesNumber.setVisibility(View.GONE);
    }
}