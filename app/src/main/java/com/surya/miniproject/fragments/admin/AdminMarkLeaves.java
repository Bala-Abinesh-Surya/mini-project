package com.surya.miniproject.fragments.admin;

import static com.surya.miniproject.constants.Strings.LEAVES;

import static java.time.DayOfWeek.SUNDAY;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.admin.LeaveAdapter;

import java.lang.ref.WeakReference;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminMarkLeaves extends Fragment {

    // UI Elements
    private RecyclerView workingDays, leaves;
    private TextView monthText, leaveText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_mark_leaves, container, false);

        // initialising the UI Elements
        // recycler view
        workingDays = view.findViewById(R.id.admin_leave_marking_days_rec_view);
        leaves = view.findViewById(R.id.admin_leave_marking_leave_rec_view);

        // text view
        monthText = view.findViewById(R.id.admin_leave_marking_month);
        leaveText = view.findViewById(R.id.admin_leave_marking_leave_text);

        // setting the monthText
        monthText.setText("for " + LocalDateTime.now().getMonth()+ " - " + LocalDateTime.now().getYear());

        new LeaveAsyncTask(this).execute(FirebaseDatabase.getInstance());

        // on click listener for the update button
        view.findViewById(R.id.admin_leave_marking_update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>(){
                    {
                        put("leaves", LeaveAdapter.status);
                    }
                };

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference()
                        .child(LEAVES)
                        .child(LocalDateTime.now().getYear()+"")
                        .child(LocalDateTime.now().getMonth()+"")
                        .updateChildren(map);

                Toast.makeText(getContext(), "Leaves Updated", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private static class LeaveAsyncTask extends AsyncTask<FirebaseDatabase, Integer, Integer> {
        private final WeakReference<AdminMarkLeaves> weakReference;

        // Constructor
        public LeaveAsyncTask(AdminMarkLeaves markLeaves){
            weakReference = new WeakReference<AdminMarkLeaves>(markLeaves);
        }

        @Override
        protected Integer doInBackground(FirebaseDatabase... firebaseDatabases) {
            FirebaseDatabase firebaseDatabase = firebaseDatabases[0];
            AdminMarkLeaves markLeaves = weakReference.get();

            // fetching for the leaves in the database
            firebaseDatabase.getReference()
                    .child(LEAVES)
                    .child(LocalDateTime.now().getYear()+"")
                    .child(LocalDateTime.now().getMonth()+"")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){

                            }
                            else{
                                // snapshot does not exist
                                // means, leaves for this month are not marked yet
                                class Leave{
                                    // Constructor
                                    public Leave(){
                                        // determining the number of days in the month and initialising the array list accordingly
                                        int year = LocalDateTime.now().getYear();
                                        boolean leapYear = false;

                                        /*
                                         *
                                         *
                                         * What is a leap year? To be a leap year, the year number must be divisible by four – except for end-of-century years,
                                         * which must be divisible by 400. This means that the year 2000 was a leap year, although 1900 was not.
                                         *  2020, 2024 and 2028 are all leap years.
                                         *
                                         *
                                         * */

                                        if(year % 100 == 0){
                                            leapYear = year % 400 == 0;
                                        }
                                        else{
                                            leapYear = year % 4 == 0;
                                        }

                                        int numberOfDays = LocalDateTime.now().getMonth().length(leapYear);
                                        ArrayList<Integer> leaves = new ArrayList<>(numberOfDays);

                                        // setting up the dummy data in this array list
                                        // for all sundays, put 0 (leave)
                                        // else, for all days, put 1 (working day)
                                        for(int i = 1; i <= numberOfDays; i++){
                                            if(LocalDateTime.of(2022, LocalDateTime.now().getMonth(), i, 0, 1).getDayOfWeek().equals(DayOfWeek.values()[6])){
                                                leaves.add(0);
                                            }
                                            else{
                                                leaves.add(1);
                                            }
                                        }

                                        // setting up the adapter for the recycler view
                                        LeaveAdapter adapter = new LeaveAdapter(markLeaves.getContext(), leaves, markLeaves.leaves);
                                        adapter.setLeaves(markLeaves.leaves);
                                        markLeaves.workingDays.setAdapter(adapter);
                                        markLeaves.workingDays.setLayoutManager(new GridLayoutManager(markLeaves.getContext(), 5));
                                    }
                                }

                                new Leave();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            return null;
        }
    }
}