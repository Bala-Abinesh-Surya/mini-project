package com.surya.miniproject.background;

import static com.surya.miniproject.constants.Strings.LEAVES;
import static com.surya.miniproject.constants.Strings.TODAY_IS_A_LEAVE;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.models.Leave;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class IsTodayALeaveAsyncTask extends AsyncTask<FirebaseDatabase, Integer, Integer> {
    private final SharedPreferences.Editor editor;

    // Constructor
    public IsTodayALeaveAsyncTask(SharedPreferences.Editor editor){
        this.editor = editor;
    }

    @Override
    protected Integer doInBackground(FirebaseDatabase... firebaseDatabases) {
        FirebaseDatabase firebaseDatabase = firebaseDatabases[0];

        // checking if today is leave from the database
        firebaseDatabase.getReference()
                .child(LEAVES)
                .child(LocalDateTime.now().getYear()+"")
                .child(LocalDateTime.now().getMonth()+"")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // means the admin has updated the working days and leaves for this month
                            Leave leave = snapshot.child("leaves").getValue(Leave.class);

                            ArrayList<Integer> leaves = leave.getLeaves();

                            // if the current day's value is 0, then holiday
                            // else, working day
                            editor.putBoolean(TODAY_IS_A_LEAVE, leaves.get(LocalDateTime.now().getDayOfMonth() - 1) == 0);

                            editor.apply();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return null;
    }
}
