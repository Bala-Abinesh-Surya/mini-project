package com.surya.miniproject.fragments.admin;

import static com.surya.miniproject.constants.Strings.NOTIFICATIONS;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.admin.AdminNotificationOuterAdapter;
import com.surya.miniproject.models.Notification;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AdminNotificationsFragment extends Fragment {

    // UI Elements
    private RecyclerView recyclerView;

    private ArrayList<ArrayList<Notification>> notifications = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_notifications, container, false);

        // method to initialise the UI Elements
        initialiseUIElements(view);

        NotificationsFetcher notificationsFetcher = new NotificationsFetcher(this);
        notificationsFetcher.execute(FirebaseDatabase.getInstance());

        return view;
    }

    private static class NotificationsFetcher extends AsyncTask<FirebaseDatabase, Integer, ArrayList<ArrayList<Notification>>>{
        private ArrayList<ArrayList<Notification>> notifications;
        private final WeakReference<AdminNotificationsFragment> weakReference;

        // Constructor
        public NotificationsFetcher(AdminNotificationsFragment fragment) {
            weakReference = new WeakReference<AdminNotificationsFragment>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // initialising the array list
            notifications = new ArrayList<>();
        }

        @Override
        protected ArrayList<ArrayList<Notification>> doInBackground(FirebaseDatabase... firebaseDatabases) {
            FirebaseDatabase firebaseDatabase = firebaseDatabases[0];

            AdminNotificationsFragment fragment = weakReference.get();

            // going through all the notifications
            firebaseDatabase.getReference()
                    .child(NOTIFICATIONS)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                // clearing the notifications array list
                                notifications.clear();

                                class duplicate{
                                    public void check(Notification notification){
                                        if(notifications.size() == 0){
                                            // empty array list
                                            notifications.add(new ArrayList<Notification>(){
                                                {
                                                    add(notification);
                                                }
                                            });
                                        }
                                        else{
                                            // list is not empty
                                            // going through the list
                                            boolean added = false;
                                            for(ArrayList<Notification> notificationsArrayList : notifications){
                                                Notification nx = notificationsArrayList.get(0);

                                                if(nx.getDate().equals(notification.getDate())){
                                                    // adding the notification the existing array list
                                                    notificationsArrayList.add(nx);
                                                    added = true;
                                                    break;
                                                }
                                            }

                                            if(! added){
                                                // adding the notification the new array list in the notifications array list
                                                notifications.add(new ArrayList<Notification>(){
                                                    {
                                                        add(notification);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }

                                duplicate duplicate = new duplicate();

                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                    for(DataSnapshot snapshot11 : snapshot1.getChildren()){
                                        Notification notification = snapshot11.getValue(Notification.class);

                                        duplicate.check(notification);
                                    }
                                }

                                // setting up the recycler view
                                AdminNotificationOuterAdapter adapter = new AdminNotificationOuterAdapter(fragment.getContext(), notifications);
                                fragment.recyclerView.setAdapter(adapter);
                                fragment.recyclerView.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            return notifications;
        }
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(View view){
        // recycler view
        recyclerView = view.findViewById(R.id.admin_notifications_rec_view);
    }
}