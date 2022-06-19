package com.surya.miniproject.fragments;

import static com.surya.miniproject.activities.DashBoard.facultyDepartment;
import static com.surya.miniproject.constants.Strings.NOTIFICATIONS;
import static com.surya.miniproject.pool.ThreadPool.executorService;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.NotificationAdapter;
import com.surya.miniproject.models.Notification;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    // UI Elements
    private RecyclerView recyclerView;

    private ArrayList<Notification> notifications = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // method to initialise the UI Elements
        initialiseUIElements(view);

        Toast.makeText(this.getContext(), "Recent 15 notifications are shown!", Toast.LENGTH_SHORT).show();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager conMan = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();
                NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();

                if (mobile == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTED) {
                    // listening for the notifications in the database
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference()
                            .child(NOTIFICATIONS)
                            .child(facultyDepartment)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        // clearing the notifications array list
                                        notifications.clear();

                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                            Notification notification = snapshot1.getValue(Notification.class);

                                            notifications.add(notification);
                                        }

                                        class RecentNotifications{
                                            public RecentNotifications(){
                                                ArrayList<Notification> notificationsx = new ArrayList<>();

                                                for(int i = notifications.size() - 1; i > notifications.size() - 15; i--){
                                                    notificationsx.add(notifications.get(i));
                                                }

                                                notifications.clear();

                                                // reversing this notificationsx array list and adding to notifications
                                                for(int i = notificationsx.size() - 1; i >= 0; i --){
                                                    notifications.add(notificationsx.get(i));
                                                }
                                            }
                                        }

                                        // getting the latest 15 notifications
                                        new RecentNotifications();

                                        // setting up the recycler view
                                        NotificationAdapter notificationAdapter = new NotificationAdapter(notifications, getContext());
                                        recyclerView.setAdapter(notificationAdapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
                else{
                    // notifying the faculty to turn on the Mobile data/wifi
                    Toast.makeText(getContext(), "Turn on Mobile Data/Wifi to get notifications", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(View view){
        // recycler view
        recyclerView = view.findViewById(R.id.notifications_rec_view);
    }
}