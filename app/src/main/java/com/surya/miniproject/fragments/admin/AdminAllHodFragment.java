package com.surya.miniproject.fragments.admin;

import static com.surya.miniproject.constants.Strings.FACULTIES;
import static com.surya.miniproject.constants.Strings.FACULTY_USER_NAME;
import static com.surya.miniproject.constants.Strings.HOD;

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
import com.surya.miniproject.adapters.admin.AllFacultyMembersAdapter;
import com.surya.miniproject.models.Faculty;
import com.surya.miniproject.models.HOD;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AdminAllHodFragment extends Fragment {

    // UI Elements
    private RecyclerView recyclerView;

    private BottomSheet bottomSheet;

    public interface BottomSheet{
        View giveStaffBottomSheet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_all_hod, container, false);

        // initialising the UI Elements
        // recycler view
        recyclerView = view.findViewById(R.id.admin_all_hod_rec_view);

        new AllHodAsyncTask(this).execute(FirebaseDatabase.getInstance());

        return view;
    }

    // fetching for the HOD list in the firebase
    private static class AllHodAsyncTask extends AsyncTask<FirebaseDatabase, Integer, Integer>{
        private final WeakReference<AdminAllHodFragment> weakReference;

        // Constructor
        public AllHodAsyncTask(AdminAllHodFragment fragment){
            weakReference = new WeakReference<AdminAllHodFragment>(fragment);
        }

        @Override
        protected Integer doInBackground(FirebaseDatabase... firebaseDatabases) {
            FirebaseDatabase firebaseDatabase = firebaseDatabases[0];
            AdminAllHodFragment fragment = weakReference.get();
            ArrayList<Faculty> facultyMembers = new ArrayList<>();

            // fetching the HOD list
            firebaseDatabase.getReference()
                    .child(HOD)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                // clearing the facultyMembers array list
                                facultyMembers.clear();

                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    com.surya.miniproject.models.HOD hod = dataSnapshot.getValue(HOD.class);

                                    // going through all the faculty Members in the database
                                    firebaseDatabase.getReference()
                                            .child(FACULTIES)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                                            Faculty faculty = dataSnapshot1.getValue(Faculty.class);

                                                            if(! (faculty.getFacultyName().equals("DUMMY"))){
                                                                // for the safety
                                                                if(faculty.getFacultyName().equals(hod.getName())){
                                                                    facultyMembers.add(faculty);

                                                                    Log.d("vikra", faculty.getFacultyName());
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                }

                                // setting up the adapter for the recycler view
                                AllFacultyMembersAdapter adapter = new AllFacultyMembersAdapter(fragment.getContext(), facultyMembers, firebaseDatabase);
                                adapter.setBottomSheetView(fragment.bottomSheet.giveStaffBottomSheet());
                                fragment.recyclerView.setAdapter(adapter);
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

        bottomSheet = (BottomSheet) activity;
    }
}