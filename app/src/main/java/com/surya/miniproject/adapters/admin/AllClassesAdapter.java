package com.surya.miniproject.adapters.admin;

import static com.surya.miniproject.constants.Strings.ATTENDANCE;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.surya.miniproject.R;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.Student;
import com.surya.miniproject.utility.Functions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;

public class AllClassesAdapter extends RecyclerView.Adapter {
    /*Adapter used for two purposes, same layout though*/

    private final Context context;
    private final ArrayList<Class> classes;
    private final FirebaseDatabase firebaseDatabase;
    private final int purpose;
    private View bottomSheetView;

    // Constructor
    public AllClassesAdapter(Context context, ArrayList<Class> classes, FirebaseDatabase firebaseDatabase, int purpose) {
        this.context = context;
        this.classes = classes;
        this.firebaseDatabase = firebaseDatabase;
        this.purpose = purpose;
    }

    // setter methods
    public void setBottomSheetView(View bottomSheetView) {
        this.bottomSheetView = bottomSheetView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_classes_list, parent, false);
        return new AllClassesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /*
        *
        *
        * Used for two purposes
        *
        *
        * Purpose - 0:
        *   - To just show the list of classes to the admin and the status of the today's attendance to the admin
        *
        *
        *
        * Purpose - 1:
        *   - To show the list of classes to the faculty and to generate statistics
        *
        * */
        if(holder.getClass() == AllClassesViewHolder.class){
            // displaying the data
            Class myClass = classes.get(position);

            class Image{
                public Image(){
                    if(purpose == 0){
                        // show the bell icon
                      //  ((AllClassesViewHolder) holder).notify.setBackgroundResource(R.drawable.ic_notify);
                        ((AllClassesViewHolder) holder).notify.setVisibility(View.GONE);
                    }
                    else{
                        // purpose is 1
                        // setting the export icon
                        ((AllClassesViewHolder) holder).notify.setImageResource(R.drawable.stat);
                        ((AllClassesViewHolder) holder).notify.setImageTintList(null);

                        // also hiding the status image view, coz not needed for this purpose
                        ((AllClassesViewHolder) holder).status.setVisibility(View.GONE);
                    }
                }
            }

            new Image();

            // name
            ((AllClassesViewHolder) holder).className.setText(myClass.getClassName());

            // class advisor
            ((AllClassesViewHolder) holder).advisor.setText(myClass.getClassAdvisor());

            // status image
            // checking if the attendance for today has been updated for the class
//            firebaseDatabase.getReference()
//                    .child(ATTENDANCE)
//                    .child(myClass.getClassName())
//                    .child(LocalDateTime.now().getMonth() + "-" + LocalDateTime.now().getYear())
//                    .child(new Functions().date())
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.exists()){
//                                // attendance has already been taken for today
//                                ((AllClassesViewHolder) holder).status.setImageResource(R.drawable.circle);
//
//                                // hiding the notify image, because the attendance is taken
//                                ((AllClassesViewHolder) holder).notify.setVisibility(View.GONE);
//                            }
//                            else{
//                                // snapshot does not exist
//                                // so attendance is not yet taken
//                                ((AllClassesViewHolder) holder).status.setImageResource(R.drawable.circle_red);
//
//                                // making the notify image visible, because the attendance is not yet taken
//                                ((AllClassesViewHolder) holder).notify.setVisibility(View.VISIBLE);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });

            // notify image
            ((AllClassesViewHolder) holder).notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(purpose == 0){

                    }
                    else{
                        // purpose is 1
                        // stats for a class
                        // inflating the stat bottom sheet
                        class Sheet{
                            public Sheet(){
                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                                bottomSheetDialog.setContentView(bottomSheetView);
                                bottomSheetDialog.setCancelable(false);
                                bottomSheetDialog.show();

                                // setting up the texts
                                Class classx = classes.get(position);
                             //   ((TextView) bottomSheetView.findViewById(R.id.stat_bs_class_name)).setText(classx.getClassName());
                                ((TextView) bottomSheetView.findViewById(R.id.stat_bs_class_Advisor)).setText(classx.getClassName());
                                ((TextView) bottomSheetView.findViewById(R.id.stat_bs_dep)).setText(classx.getClassDepartment());

                                // on click listener for the dismiss button
                                ((Button) bottomSheetView.findViewById(R.id.stat_bs_dismiss_btn)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ViewGroup) bottomSheetView.getParent()).removeView(bottomSheetView);
                                        bottomSheetDialog.dismiss();
                                    }
                                });

                                // details
                                final int[] percentage = {50}; // default percentage
                                final String[] range = {"Above"};

                                // item selected listener the radio group
                                ((RadioGroup) bottomSheetView.findViewById(R.id.stat_bs_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        if(group.getCheckedRadioButtonId() == R.id.stat_bs_rd_below){
                                            // below selected
                                            range[0] = "Below";
                                        }
                                        else{
                                            // Above must have been selected
                                            range[0] = "Above";
                                        }
                                    }
                                });

                                // on click listener for the generate button
                                ((Button) bottomSheetView.findViewById(R.id.stat_bs_btn)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // fetch the statistics
                                        class Fetch{
                                            // checking for the faculty percentage input
                                            public Fetch(){
                                                if(((EditText) bottomSheetView.findViewById(R.id.stat_bs_edit_text)).getText().toString().equals("")){
                                                    // means faculty did not enter any percentage
                                                    percentage[0] = 50;
                                                }
                                                else{
                                                    // faculty entered some value
                                                    percentage[0] = Integer.parseInt(((EditText) bottomSheetView.findViewById(R.id.stat_bs_edit_text)).getText().toString());
                                                }

                                                ArrayList<Attendance> attendances = new ArrayList<>();

                                                // fetching the data
                                                firebaseDatabase.getReference()
                                                        .child(ATTENDANCE)
                                                        .child(classx.getClassName())
                                                        .child(LocalDateTime.now().getMonth() + "-" + LocalDateTime.now().getYear()) // current month data
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.exists()){
                                                                    // clearing the attendance array list
                                                                    attendances.clear();
                                                                    Hashtable<String, ArrayList<String>> statistics = new Hashtable<>();

                                                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                                        attendances.add(dataSnapshot.getValue(Attendance.class));
                                                                    }

                                                                    Log.d("vikram", attendances.size()+"");

                                                                    // calculating the necessary data from the given attendance array list
                                                                    for(Attendance attendance : attendances){
                                                                        // converting the json string in the attendance object to Hashtable
                                                                        attendance.setLocalTable();

                                                                        // getting the attendance data from the hash table
                                                                        for(Student student : classx.getStudents()){
                                                                            // if the statistics contains the current student name
                                                                            if(! (statistics.contains(student.getStudentName()))){
                                                                                // putting the student name and adding a new array list
                                                                                statistics.put(student.getStudentName(), new ArrayList<String>(){
                                                                                    {
                                                                                        if(add(attendance.getTable().get(student.getStudentName()))){

                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                            else{
                                                                                //Log.d("vikram", "not executed at all");
                                                                                // already student name is there in the hash table
                                                                                // so just adding the attendance value to the existing array list
                                                                               // statistics.get(student.getStudentName()).add(attendance.getTable().get(student.getStudentName()));
                                                                                ArrayList<String> temp = statistics.get(student.getStudentName());
                                                                                temp.add(attendance.getTable().get(student.getStudentName()));

                                                                                statistics.put(student.getStudentName(), temp);
                                                                            }
                                                                        }

                                                                        Log.d("vikram", new Gson().toJson(statistics));
                                                                    }

                                                                    // calculating the percentage data for each data
                                                                    for(Student student : classx.getStudents()){
                                                                        String studentName = student.getStudentName();

                                                                        ArrayList<String> data = statistics.get(studentName);
                                                                       // Log.d("vikram", studentName + "+" + data.toString());
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                            }
                                        }

                                        new Fetch();
                                    }
                                });
                            }
                        }

                        new Sheet();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    // view holder class
    private static class AllClassesViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private final TextView className, advisor;
        private final ImageView status, notify;
        private final ConstraintLayout constraintLayout;

        public AllClassesViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            className = itemView.findViewById(R.id.admin_all_classes_name);
            advisor = itemView.findViewById(R.id.admin_all_classes_class_advisor);

            // image view
            status = itemView.findViewById(R.id.admin_all_classes_status);
            notify = itemView.findViewById(R.id.admin_all_classes_notify);

            // constraint layout
            constraintLayout = itemView.findViewById(R.id.admin_all_classes_layout);
        }
    }
}
