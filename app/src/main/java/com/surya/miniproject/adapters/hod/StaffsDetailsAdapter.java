package com.surya.miniproject.adapters.hod;

import static com.surya.miniproject.activities.DashBoard.facultyDepartment;
import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.constants.Strings.CLASSES;
import static com.surya.miniproject.constants.Strings.FACULTIES;
import static com.surya.miniproject.constants.Strings.MALE;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Currency;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.activities.hod.ClassAdvisorsPickActivity;
import com.surya.miniproject.activities.hod.HODPanel;
import com.surya.miniproject.details.Data;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.CurrentClass;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StaffsDetailsAdapter extends RecyclerView.Adapter {
    /*Using this adapter for four causes, same layout*/

    private final Context context;
    private final ArrayList<String> facultyMembers;
    private int purpose;
    private final FirebaseDatabase firebaseDatabase;
    private String className;
    private String classPudId;
    private ConstraintLayout constraintLayout;

    // Constructor
    public StaffsDetailsAdapter(Context context, ArrayList<String> facultyMembers, FirebaseDatabase firebaseDatabase, String className, String classPushId) {
        this.context = context;
        this.facultyMembers = facultyMembers;
        this.firebaseDatabase = firebaseDatabase;
        this.className = className;
        this.classPudId = classPushId;
    }

    public StaffsDetailsAdapter(Context context, ArrayList<String> facultyMembers, FirebaseDatabase firebaseDatabase){
        this.context = context;
        this.facultyMembers = facultyMembers;
        this.firebaseDatabase = firebaseDatabase;
    }

    public StaffsDetailsAdapter(ConstraintLayout constraintLayout, Context context, ArrayList<String> facultyMembers, String className, String classPushId, FirebaseDatabase database){
        this.context = context;
        this.facultyMembers = facultyMembers;
        this.classPudId = classPushId;
        this.className = className;
        this.firebaseDatabase = database;
        this.constraintLayout = constraintLayout;
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
            /*Using this adapter for four purposes*/
            /*
            *
            * Purpose 1 :
            *   - To display the staffs, and removing the staffs from the faculty members for a class
            *
            * Purpose 2 :
            *   - To display the staffs, and add the staff to the faculty members for a class
            *
            * Purpose 3 :
            *   - To remove the faculty from the whole system
            *
            * Purpose - 4:
            *   - To pick a class advisor for a class
            *
            * */

            class SetImage{
                // setting the image based on the purpose
                /*
                 *
                 * Purpose 1 && Purpose 3 - to remove the staff from a class/the entire system
                 *   - So pencil image
                 *
                 * Purpose 2 && Purpose 4 to add a staff for a class/assign a new class advisor for a class
                 *   - So Add image
                 *
                 * */
                private void setImage(ImageView imageView, int purpose){
                    if(purpose == 1 || purpose == 3){
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
                            private final AlertDialog.Builder builder;
                            private final String staffName;
                            private final String className;

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
                    else if(purpose == 2){
                        // purpose is 2
                        class alert{
                            private final AlertDialog.Builder builder;

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
                    else if(purpose == 3){
                        // purpose is 3
                        // removing the faculty from the system
                        class alert{
                            private final AlertDialog.Builder builder;

                            // Constructor
                            public alert(){
                                // initialising the builder
                                builder = new AlertDialog.Builder(context);

                                // setting up the builder
                                builder.setMessage("Do you want to remove the " + faculty + " from the system ?")
                                        .setCancelable(false)
                                        .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // dismissing the dialog
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // hod chose yes
                                                // so removing the faculty
                                                class Check{
                                                    public Check(){
                                                        final boolean[] canBeRemoved = {true};
                                                        // going through all the classes amd checking if the faculty is handling any subject for any class or a class advisor
                                                        firebaseDatabase.getReference()
                                                                .child(CLASSES)
                                                                .addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if(snapshot.exists()){
                                                                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                                                Class classx = dataSnapshot.getValue(Class.class);

                                                                                // checking if the faculty is the class advisor
                                                                                if(classx.getClassAdvisor().equals(faculty)){
                                                                                    canBeRemoved[0] = false;
                                                                                    break;
                                                                                }

                                                                                // checking if the faculty is handling any subjects for the class
                                                                                if(classx.getFacultyMembers().contains(faculty)){
                                                                                    canBeRemoved[0] = false;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }

                                                                        if(! (canBeRemoved[0])){
                                                                            Toast.makeText(context, faculty + " cannot be removed!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        else{
                                                                            // the faculty can be removed
                                                                            // means, the faculty is not a class advisor and not handling any subjects for any other classes
                                                                            firebaseDatabase.getReference()
                                                                                    .child(FACULTIES)
                                                                                    .addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            if(snapshot.exists()){
                                                                                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                                                                    Faculty faculty1 = dataSnapshot.getValue(Faculty.class);
                                                                                                    String key = dataSnapshot.getKey();

                                                                                                    if(! (faculty1.getFacultyName().equals("DUMMY"))){
                                                                                                        // for the safety purposes
                                                                                                        if(faculty1.getFacultyName().equals(faculty)){
                                                                                                            firebaseDatabase.getReference()
                                                                                                                    .child(FACULTIES)
                                                                                                                    .child(key)
                                                                                                                    .removeValue()
                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            Toast.makeText(context, "Faculty Removed!", Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    });
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
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                    }
                                                }

                                                new Check();
                                            }
                                        });
                            }

                            private void show(){
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setTitle("Remove a faculty");
                                alertDialog.show();
                            }
                        }

                        if(faculty.equals(facultyName)){
                            Toast.makeText(context, "Cannot remove yourself!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            new alert().show();
                        }
                    }
                    else{
                        // purpose is 4
                        // assigning a new faculty member as the class's class advisor
                        class alert{
                            // showing an alert dialog to the hod
                            private final AlertDialog.Builder builder;

                            // Constructor
                            public alert(){
                                // initialising up the builder
                                builder = new AlertDialog.Builder(context);

                                // setting up the builder
                                builder.setMessage("Assign " + faculty + " as the " + className + "'s class advisor ?")
                                        .setCancelable(false)
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // assigning the selected faculty as the class advisor
                                                Map<String, Object> map = new HashMap<String, Object>(){
                                                    {
                                                        put("classAdvisor", faculty);
                                                    }
                                                };

                                                firebaseDatabase.getReference()
                                                        .child(CLASSES)
                                                        .child(classPudId)
                                                        .updateChildren(map)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(context, "Class Advisor Changed", Toast.LENGTH_SHORT).show();
                                                                    ClassAdvisorsPickActivity.classAdvisorPicked = true;
                                                                }
                                                                else{
                                                                    // class advisor not updated
                                                                    Toast.makeText(context, "Class Advisor not updated", Toast.LENGTH_SHORT).show();
                                                                    ClassAdvisorsPickActivity.classAdvisorPicked = true;
                                                                }
                                                            }
                                                        });
                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // just dismissing the dialog
                                                dialog.dismiss();
                                            }
                                        });
                            }

                            private void show(){
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setTitle("Edit the Class Advisor");
                                alertDialog.show();
                            }
                        }

                        new alert().show();
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
