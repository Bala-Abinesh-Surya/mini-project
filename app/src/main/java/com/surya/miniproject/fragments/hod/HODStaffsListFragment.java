package com.surya.miniproject.fragments.hod;

import static com.surya.miniproject.constants.Strings.CSE_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.FACULTIES;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.activities.hod.HODPanel;
import com.surya.miniproject.adapters.hod.PanelStaffsListAdapter;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;

public class HODStaffsListFragment extends Fragment {

    // UI Elements
    /// UI Elements
    private RecyclerView recyclerView;

    private PanelStaffsListAdapter adapter;
    private ArrayList<Faculty> faculties = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_h_o_d_staffs_list, container, false);

        // method to initialise the UI Elements
        initialiseUIElements(view);

        // fetching the staffs list from firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference()
                .child(FACULTIES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // clearing the faculties array list
                            faculties.clear();

                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                Faculty faculty = snapshot1.getValue(Faculty.class);

                                if(faculty.getFacultyDepartment().equals(CSE_DEPARTMENT)){
                                    faculties.add(faculty);
                                }
                            }

                            // setting up the adapter for the recycler view
                            adapter = new PanelStaffsListAdapter(getContext(), faculties);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(View view){
        // recycler view
        recyclerView = view.findViewById(R.id.hod_panel_rec_view);
    }
}