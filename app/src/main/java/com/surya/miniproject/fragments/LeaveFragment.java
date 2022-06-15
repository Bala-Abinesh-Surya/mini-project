package com.surya.miniproject.fragments;

import static com.surya.miniproject.constants.Strings.LEAVES;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.admin.LeaveAdapter;
import com.surya.miniproject.models.Leave;

import java.lang.ref.WeakReference;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LeaveFragment extends Fragment {

    // UI Elements
    private RecyclerView recyclerView;
    private TextView notFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leave, container, false);

        // UI Elements
        // recycler view
        recyclerView = view.findViewById(R.id.leave_rec_view);

        // text view
        notFound = view.findViewById(R.id.leave_no_data_found);
        // month text
        ((TextView) view.findViewById(R.id.leave_month_text)).setText(LocalDateTime.now().getMonth() + " - " + LocalDateTime.now().getYear());

        new LeaveAsyncTask(this).execute(FirebaseDatabase.getInstance());

        return view;
    }

    private static class LeaveAsyncTask extends AsyncTask<FirebaseDatabase, Integer, Integer>{
        private final WeakReference<LeaveFragment> weakReference;

        // Constructor
        public LeaveAsyncTask(LeaveFragment fragment){
            this.weakReference = new WeakReference<LeaveFragment>(fragment);
        }

        @Override
        protected Integer doInBackground(FirebaseDatabase... firebaseDatabases) {
            FirebaseDatabase firebaseDatabase = firebaseDatabases[0];
            LeaveFragment fragment = weakReference.get();

            // fetching the data from the database
            firebaseDatabase.getReference()
                    .child(LEAVES)
                    .child(LocalDateTime.now().getYear()+"")
                    .child(LocalDateTime.now().getMonth()+"")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                // this month's data is in the firebase
                                // so making the recycler view visible and no data found text hidden
                                fragment.notFound.setVisibility(View.GONE);
                                fragment.recyclerView.setVisibility(View.VISIBLE);

                                Leave leave = snapshot.child("leaves").getValue(Leave.class);

                                ArrayList<Integer> leaves = leave.getLeaves();

                                // setting up the adapter for the recycler view
                                LeaveAdapter adapter = new LeaveAdapter(fragment.getContext(), leaves);
                                fragment.recyclerView.setAdapter(adapter);
                                fragment.recyclerView.setLayoutManager(new GridLayoutManager(fragment.getContext(), 5));
                            }
                            else{
                                // snapshot does not exist
                                // so hiding the recycler view and showing the no data found text
                                fragment.recyclerView.setVisibility(View.GONE);
                                fragment.notFound.setVisibility(View.VISIBLE);
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