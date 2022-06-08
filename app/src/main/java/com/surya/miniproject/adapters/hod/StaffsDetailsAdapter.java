package com.surya.miniproject.adapters.hod;

import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.FACULTIES;
import static com.surya.miniproject.constants.Strings.MALE;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Currency;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.details.Data;
import com.surya.miniproject.models.CurrentClass;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StaffsDetailsAdapter extends RecyclerView.Adapter {
    /*Using this adapter for two causes, same layout*/

    private Context context;
    private ArrayList<String> facultyMembers;
    private int purpose;
    private FirebaseDatabase firebaseDatabase;
    private String className;
    private String classPudId;

    // Constructor
    public StaffsDetailsAdapter(Context context, ArrayList<String> facultyMembers, FirebaseDatabase firebaseDatabase, String className, String classPushId) {
        this.context = context;
        this.facultyMembers = facultyMembers;
        this.firebaseDatabase = firebaseDatabase;
        this.className = className;
        this.classPudId = classPushId;
    }

    // setter methods
    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.staffs_list, parent, false);
        return new StaffsDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == StaffsDetailsViewHolder.class){
            /*Using this adapter for two purposes*/
            /*
            *
            * Purpose 1 :
            *   - To display the staffs, and removing the staffs from the faculty members for a class
            *
            * Purpose 2 :
            *   - To display the staffs, and add the staff to the faculty members for a class
            *
            * */

            class SetImage{
                // setting the image based on the purpose
                /*
                 *
                 * Purpose 1 - to remove the staff from a class
                 *   - So pencil image
                 *
                 * Purpose 2 - to add a staff for a class
                 *   - So Add image
                 *
                 * */
                private void setImage(ImageView imageView, int purpose){
                    if(purpose == 1){
                        imageView.setImageResource(R.drawable.ic_edit);
                    }
                    else{
                        // purpose is 2
                        imageView.setImageResource(R.drawable.ic_add);
                    }
                }
            }

            // setting the image
            new SetImage().setImage(((StaffsDetailsViewHolder) holder).edit, purpose);

            String faculty = facultyMembers.get(position);

            // staff name
            ((StaffsDetailsViewHolder) holder).staffName.setText(faculty);

            // on click listener for the edit image
            ((StaffsDetailsViewHolder) holder).edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(purpose == 1){
                        // Removing a staff
                        class alert {
                            private AlertDialog.Builder builder;
                            private String staffName;
                            private String className;

                            // Constructor
                            public alert(String staffName, String className) {
                                this.staffName = staffName;
                                this.className = className;

                                // initialising the builder
                                builder = new AlertDialog.Builder(context);

                                // setting up the builder
                                builder.setMessage("Removing " + staffName + " from " + className + " ?")
                                        .setCancelable(false)
                                        .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // removing the staff
                                                Map<String, Object> map = new HashMap<String, Object>(){
                                                    {
                                                        put("facultyMembers", facultyMembers);
                                                    }
                                                };

                                                firebaseDatabase.getReference()
                                                        .child(CLASSES)
                                                        .child(classPudId)
                                                        .updateChildren(map)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(context, staffName + " removed", Toast.LENGTH_SHORT).show();
                                                                dialog.cancel();
                                                            }
                                                        });
                                            }
                                        });
                            }

                            private void show(){
                                // checking if the faculty is remove him/her
                                if(facultyName.equals(faculty)){
                                    Toast.makeText(context, "Cannot remove yourself!!!", Toast.LENGTH_SHORT).show();
                                }

                                // checking if the staff is the class advisor
                                else if(CurrentClass.classAdvisor.equals(staffName)){
                                    Toast.makeText(context, "Cannot remove the Class Advisor!!!", Toast.LENGTH_SHORT).show();
                                }

                                else{
                                    // removing the staff
                                    remove(faculty);

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.setTitle("Removing a Faculty");
                                    alertDialog.show();
                                }
                            }
                        }

                        alert alert = new alert(faculty, className);
                        alert.show();
                    }
                    else{
                        // purpose is 2
                        class alert{
                            private AlertDialog.Builder builder;

                            // Constructor
                            public alert() {
                                // initialising the builder
                                builder = new AlertDialog.Builder(context);

                                // setting up the builder
                                builder.setMessage("Adding " + faculty + " to the " + className + " ?")
                                        .setCancelable(false)
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // adding the faculty to the CurrentClass facultyMembers
                                                if(! (CurrentClass.currentClassFacultyMember.contains(faculty))){
                                                    CurrentClass.currentClassFacultyMember.add(faculty);
                                                }

                                                Map<String, Object> map = new HashMap<String, Object>(){
                                                    {
                                                        put("facultyMembers", CurrentClass.currentClassFacultyMember);
                                                    }
                                                };

                                                // updating the database
                                                firebaseDatabase.getReference()
                                                        .child(CLASSES)
                                                        .child(classPudId)
                                                        .updateChildren(map);

                                                // creating a dummy updating and deletion
                                                Faculty faculty1 = new Faculty();
                                                faculty1.setFacultyName("DUMMY");
                                                String key = firebaseDatabase.getReference().push().getKey();
                                                firebaseDatabase.getReference()
                                                        .child(FACULTIES)
                                                        .child(key)
                                                        .setValue(faculty1)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                firebaseDatabase.getReference()
                                                                        .child(FACULTIES)
                                                                        .child(key)
                                                                        .removeValue();
                                                            }
                                                        });
                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // removing the staff name from the CurrentClass faculty members
                                                // just for safety
                                                if(CurrentClass.currentClassFacultyMember.contains(faculty)){
                                                    CurrentClass.currentClassFacultyMember.remove(faculty);
                                                }

                                                dialog.cancel();
                                            }
                                        });
                            }

                            private void show(){
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setTitle("Adding a Faculty");
                                alertDialog.show();
                            }
                        }

                        alert alert = new alert();
                        alert.show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return facultyMembers.size();
    }

    // view holder class
    public class StaffsDetailsViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private TextView staffName;
        private ImageView edit;

        public StaffsDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            staffName = itemView.findViewById(R.id.staff_details_name);

            // image views
            edit = itemView.findViewById(R.id.stafff_details_edit);
        }
    }

    // method to remove an entry from array list
    private void remove(String facultyName){
        if(facultyMembers.contains(facultyName)){
            facultyMembers.remove(facultyName);

            // updating the CurrentClass faculty members
            CurrentClass.currentClassFacultyMember = facultyMembers;
        }
    }
}
