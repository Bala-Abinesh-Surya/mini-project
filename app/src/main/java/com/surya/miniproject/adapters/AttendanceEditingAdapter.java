package com.surya.miniproject.adapters;

import static com.surya.miniproject.adapters.AttendanceAdapter.students;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surya.miniproject.R;
import com.surya.miniproject.activities.ClassAttendance;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Student;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;

public class AttendanceEditingAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<String> attendance = new ArrayList<>();
    private RecyclerView absentees;
    private TextView absenteesNum;
    private TextView noAbsenteesText;
    private Attendance today;
    private ArrayList<Student> absentList = new ArrayList<>();
    private Button update;

    // json string that needs to be updated in the firebase
    public static String updatedJson = "";

    // Constructor
    public AttendanceEditingAdapter(Context context, RecyclerView recyclerView, TextView absenteesNumber, TextView noAbsenteesText, Button update) {
        this.context = context;
        this.absentees = recyclerView;
        this.absenteesNum = absenteesNumber;
        this.noAbsenteesText = noAbsenteesText;
        this.update = update;

        // initialising today's attendance
        today = ClassAttendance.todayAttendance;

        // initialising the absentees list
        initialiseAbsenteesList();

        // disabling the update button
        update.setEnabled(false);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendce_marking_list, parent, false);
        return new AttendanceEditingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // displaying the data
        if(holder.getClass() == AttendanceEditingViewHolder.class){
            Student student = students.get(position);

            // regNo
            String regNo = student.getStudentRegNo();
            ((AttendanceEditingViewHolder) holder).regNo.setText(regNo.substring(regNo.length() - 3));

            // setting the initial colors
            // Red for A
            // green for G
            if(attendance.get(position).equals("P")){
                ((AttendanceEditingViewHolder) holder).imageView.setBackgroundResource(R.drawable.circle);
            }
            else{
                // attendance is Absent
                ((AttendanceEditingViewHolder) holder).imageView.setBackgroundResource(R.drawable.circle_red);
            }

            // on click listener for the image view
            ((AttendanceEditingViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // changing the attendance to P, if already A
                    // to A, if already P
                    if(attendance.get(position).equals("P")){
                        // changing the drawable of the image view - red color
                        ((AttendanceEditingViewHolder) holder).imageView.setBackgroundResource(R.drawable.circle_red);

                        // changing to A
                        attendance.remove(position);
                        attendance.add(position, "A");

                        // adding the student in the absentees array list
                        addIfNotAlreadyExists(student);

                        // setting up the adapter for thr absentees recycler view
                        absentees.setVisibility(View.VISIBLE);

                        AbsenteesListAdapter absenteesListAdapter = new AbsenteesListAdapter(context, absentList);
                        absentees.setAdapter(absenteesListAdapter);
                        absentees.setLayoutManager(new LinearLayoutManager(context));

                        // making the recycler view, absentees number text visible
                        absenteesNum.setVisibility(View.VISIBLE);

                        // hiding the no absentees text
                        noAbsenteesText.setVisibility(View.GONE);

                        // changing the number of absentees
                        absenteesNum.setText("Number of Absentees : " + absentList.size());

                        // enabling the button
                        update.setEnabled(true);

                        // updating the json string
                        updateJson();
                    }
                    else{
                        // changing the drawable of the image view - green color
                        ((AttendanceEditingViewHolder) holder).imageView.setBackgroundResource(R.drawable.circle);

                        // changing to P
                        attendance.remove(position);
                        attendance.add(position, "P");

                        // removing student from absentees list
                        absentList.remove(student);

                        // initialising the absentees recycler view, if the absentees list is not empty
                        if(absentList.size() > 0){
                            // setting up the adapter for thr absentees recycler view
                            absentees.setVisibility(View.VISIBLE);

                            AbsenteesListAdapter absenteesListAdapter = new AbsenteesListAdapter(context, absentList);
                            absentees.setAdapter(absenteesListAdapter);
                            absentees.setLayoutManager(new LinearLayoutManager(context));

                            // making the recycler view, absentees number text visible
                            absenteesNum.setVisibility(View.VISIBLE);

                            // hiding the no absentees text
                            noAbsenteesText.setVisibility(View.GONE);

                            // changing the number of absentees
                            absenteesNum.setText("Number of Absentees : " + absentList.size());
                        }
                        else{
                            // there are no absentees, hiding the absentees number text and the rccycler view
                            noAbsenteesText.setVisibility(View.VISIBLE);

                            absenteesNum.setVisibility(View.GONE);
                            absentees.setVisibility(View.GONE);
                        }

                        // enabling the button
                        update.setEnabled(true);

                        // updating the json string
                        updateJson();
                    }
                }
            });

            // initialising the absentees recycler view, if the absentees list is not empty
            if(absentList.size() > 0){
                // setting up the adapter for thr absentees recycler view
                AbsenteesListAdapter absenteesListAdapter = new AbsenteesListAdapter(context, absentList);
                absentees.setAdapter(absenteesListAdapter);
                absentees.setLayoutManager(new LinearLayoutManager(context));

                // making the recycler view, absentees number text visible
                absentees.setVisibility(View.VISIBLE);
                absenteesNum.setVisibility(View.VISIBLE);

                // hiding the no absentees text
                noAbsenteesText.setVisibility(View.GONE);

                // changing the number of absentees
                absenteesNum.setText("Number of Absentees : " + absentList.size());
            }
            else{
                // there are no absentees, hiding the absentees number text and the rccycler view
                noAbsenteesText.setVisibility(View.VISIBLE);

                absenteesNum.setVisibility(View.GONE);
                absentees.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    // view holder class
    public class AttendanceEditingViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private ImageView imageView;
        private TextView regNo;

        public AttendanceEditingViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // image view
            imageView = itemView.findViewById(R.id.marking_circle);

            // text view
            regNo = itemView.findViewById(R.id.marking_no);
        }
    }

    // method to initialise the absentees list
    private void initialiseAbsenteesList(){
        // getting the json string from the today's attendance
        String json = today.getJson();

        // converting the json to HashTable<String, String>
        Type type = new TypeToken<Hashtable<String, String>>(){}.getType();
        Hashtable<String, String> table = new Gson().fromJson(json, type);

        // going through the students list
        int x = 0;
        for(int i = 0; i < students.size(); i++){
            Student student = students.get(i);
            String name = student.getStudentName();

            if(table.get(name).equals("A")){
                // adding the student to the absentees list
                absentList.add(student);
            }

            // adding the present state, either P or A, to the absentees array list
            attendance.add(i, table.get(name));

            x++;
        }
    }

    // method to check if the student object is already present in the array list
    // if present, student object should not be added once again
    private void addIfNotAlreadyExists(Student student){
        if(! (absentList.contains(student))){
            absentList.add(student);
        }
    }

    // method to update updatedJson, every time the faculty edits the attendance
    private void updateJson(){
        // going through the ArrayList<String> and students array list tp create a hash table
        Hashtable<String, String> map = new Hashtable<>();

        for(int i = 0; i < attendance.size(); i++){
            map.put(students.get(i).getStudentName(), attendance.get(i));
        }

        // converting the hash table to json string
        updatedJson = new Gson().toJson(map);
    }
}
