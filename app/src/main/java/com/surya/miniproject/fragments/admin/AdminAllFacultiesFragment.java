package com.surya.miniproject.fragments.admin;

import static com.surya.miniproject.constants.Strings.FACULTIES;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.adapters.admin.AllFacultyMembersAdapter;
import com.surya.miniproject.models.Faculty;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AdminAllFacultiesFragment extends Fragment {

    // UI Elements
    private RecyclerView recyclerView;

    private StaffDetailsBottomSheet staffDetailsBottomSheet;

    public interface StaffDetailsBottomSheet{
        View GiveStaffDetailsBottomSheet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_all_faculties, container, false);

        // initialising UI Elements
        // recycler view
        recyclerView = view.findViewById(R.id.admin_all_faculty_rec_view);

        new AllFacultyMembersAsyncTask(this).execute(FirebaseDatabase.getInstance());

        return view;
    }

    private static class AllFacultyMembersAsyncTask extends AsyncTask<FirebaseDatabase, Integer, Integer>{
        private final WeakReference<AdminAllFacultiesFragment> weakReference;

        // Constructor
        public AllFacultyMembersAsyncTask(AdminAllFacultiesFragment fragment){
            weakReference = new WeakReference<AdminAllFacultiesFragment>(fragment);
        }

        @Override
        protected Integer doInBackground(FirebaseDatabase... firebaseDatabases) {
            FirebaseDatabase firebaseDatabase = firebaseDatabases[0];
            AdminAllFacultiesFragment fragment = weakReference.get();
            ArrayList<Faculty> facultyMembers = new ArrayList<>();

            // fetching for all the faculty members in the database
            firebaseDatabase.getReference()
                    .child(FACULTIES)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                // clearing the facultyMembers array list
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Faculty faculty = dataSnapshot.getValue(Faculty.class);

                                    if(! (faculty.getFacultyName().equals("DUMMY"))){
                                        // for the safety
                                        // adding the faculty to the facultyMembers array list
                                        facultyMembers.add(faculty);
                                    }
                                }

                                // setting up the adapter for the recycler view
                                AllFacultyMembersAdapter adapter = new AllFacultyMembersAdapter(fragment.getContext(), facultyMembers, firebaseDatabase);
                                fragment.recyclerView.setAdapter(adapter);
                                adapter.setBottomSheetView(fragment.staffDetailsBottomSheet.GiveStaffDetailsBottomSheet());
                                fragment.recyclerView.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            return null;
        }
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

        staffDetailsBottomSheet = (StaffDetailsBottomSheet) activity;
    }
}