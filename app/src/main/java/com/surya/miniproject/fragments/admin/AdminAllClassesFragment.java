package com.surya.miniproject.fragments.admin;

import static com.surya.miniproject.constants.Strings.CLASSES;

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
import com.surya.miniproject.adapters.admin.AllClassesAdapter;
import com.surya.miniproject.models.Class;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AdminAllClassesFragment extends Fragment {

    // UI Elements
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_all_classes, container, false);

        // UI Elements
        // recycler view
        recyclerView = view.findViewById(R.id.admin_all_faculty_rec_view);

        new AllClassesAsyncTask(this).execute(FirebaseDatabase.getInstance());

        return view;
    }

    private static class AllClassesAsyncTask extends AsyncTask<FirebaseDatabase, Integer, Integer>{
        private final WeakReference<AdminAllClassesFragment> weakReference;

        // Constructor
        public AllClassesAsyncTask(AdminAllClassesFragment fragment){
            weakReference = new WeakReference<AdminAllClassesFragment>(fragment);
        }

        @Override
        protected Integer doInBackground(FirebaseDatabase... firebaseDatabases) {
            FirebaseDatabase firebaseDatabase = firebaseDatabases[0];
            ArrayList<Class> classes = new ArrayList<>();
            AdminAllClassesFragment fragment = weakReference.get();

            // fetching all classes from the database
            firebaseDatabase.getReference()
                    .child(CLASSES)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                // clearing the classes array list
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Class myClass = dataSnapshot.getValue(Class.class);

                                    // adding the myClass to the classes array list
                                    classes.add(myClass);
                                }

                                // setting up the adapter for the recycler view
                                AllClassesAdapter adapter = new AllClassesAdapter(fragment.getContext(), classes, firebaseDatabase, 0);
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
}