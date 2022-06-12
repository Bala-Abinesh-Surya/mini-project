package com.surya.miniproject.fragments;

import static com.surya.miniproject.activities.DashBoard.facultyDepartment;
import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.constants.Strings.REQUESTS;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.surya.miniproject.adapters.RequestsAdapter;
import com.surya.miniproject.models.Request;

import java.util.ArrayList;

public class RequestsFragment extends Fragment {

    // UI Elements
    private RecyclerView  recyclerView;

    // adapter
    private RequestsAdapter adapter;

    private BottomSheetViewSettingInterface bottomSheetViewSettingInterface;

    public interface BottomSheetViewSettingInterface{
        View setRequestAcceptingView();
        View setRequestAcceptedView();
    }

    private ArrayList<Request> requestArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        // method to initialise the UI Elements
        initialiseUIElements(view);

        // fetching the requests for the faculty from the firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference()
                .child(REQUESTS)
                .child(facultyDepartment)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // clearing the requests array list
                            requestArrayList.clear();

                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                Request request = snapshot1.getValue(Request.class);
                                Log.d("vikram", request.toString());

                                // if the receiver of the request is the faculty, adding the request to the array list
                                if(request.getRequestReceiver().equals(facultyName)){
                                    requestArrayList.add(request);
                                }
                            }

                            // setting up the adapter for the recycler view
                            adapter = new RequestsAdapter(getContext(), requestArrayList, firebaseDatabase);
                            adapter.setRequestAcceptView(bottomSheetViewSettingInterface.setRequestAcceptingView());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

        bottomSheetViewSettingInterface = (BottomSheetViewSettingInterface) activity;
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(View view){
        // recycler view
        recyclerView = view.findViewById(R.id.requests_rec_view);
    }
}