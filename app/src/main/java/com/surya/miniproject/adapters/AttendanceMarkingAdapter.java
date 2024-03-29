package com.surya.miniproject.adapters;

import static com.surya.miniproject.adapters.AttendanceAdapter.students;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surya.miniproject.R;
import com.surya.miniproject.models.Student;

import java.util.ArrayList;

public class AttendanceMarkingAdapter extends RecyclerView.Adapter {

    private Context context;
    public static ArrayList<String> attendance;
    private RecyclerView recyclerView;
    private ArrayList<Student> absentees;
    private TextView absentText;
    private TextView absenteesNumber;

    // Constructor
    public AttendanceMarkingAdapter(Context context, RecyclerView recyclerView, TextView absenteesText, TextView number) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.absentText = absenteesText;
        this.absenteesNumber = number;

        // initialising the dummy attendance
        attendance = makeDummyAttendance(ClassesListAdapter.studentsArrayList.size());

        absentees = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendce_marking_list, parent, false);
        return new AttendanceMarkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == AttendanceMarkingViewHolder.class){
            // displaying the data
            Student student = ClassesListAdapter.studentsArrayList.get(position);

            // regNo
            String regNo = student.getStudentRegNo();
            ((AttendanceMarkingViewHolder) holder).regNo.setText(regNo.substring(regNo.length() - 3));

            class BackGround{
                public BackGround(){
                    if(attendance.get(position).equals("P")){
                        // changing the drawable file - green color
                        ((AttendanceMarkingViewHolder) holder).imageView.setBackgroundResource(R.drawable.circle);
                    }
                    else{
                        // changing the drawable file - red color
                        ((AttendanceMarkingViewHolder) holder).imageView.setBackgroundResource(R.drawable.circle_red);
                    }
                }
            }

            new BackGround();

            // on click listener for the image view
            // to mark either P or A
            ((AttendanceMarkingViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // checking if the attendance is marked as "P", if so changing it to "A"
                    if(attendance.get(position).equals("P")){
                        // changing it to "A"
                        attendance.remove(position);
                        attendance.add(position, "A");

                        new BackGround();

                        // adding the student to the absentees array list
                        addIfNotAlreadyExists(student);

                        // making the recycler view visible and absentees text GONE
                        absentText.setVisibility(View.GONE);
                        absenteesNumber.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);

                        // setting up the adapter for the absentees recycler view
                        AbsenteesListAdapter absenteesListAdapter = new AbsenteesListAdapter(context, absentees);
                        recyclerView.setAdapter(absenteesListAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));

                        // absentees number text
                        absenteesNumber.setText("Number of Absentees " + absentees.size());
                    }
                    else{
                        // changing it to "P"
                        attendance.remove(position);
                        attendance.add(position, "P");

                        new BackGround();

                        // removing the student
                        absentees.remove(student);

                        // if the size of the absentees is 0, hiding the recycler view
                        if(absentees.size() == 0){
                            // making the absentees text visible
                            absentText.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            absenteesNumber.setVisibility(View.GONE);
                        }
                        else{
                            absentText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            absenteesNumber.setVisibility(View.VISIBLE);

                            // setting up the adapter for the absentees recycler view
                            AbsenteesListAdapter absenteesListAdapter = new AbsenteesListAdapter(context, absentees);
                            recyclerView.setAdapter(absenteesListAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));

                            // absentees number text
                            absenteesNumber.setText("Number of Absentees " + absentees.size());
                        }
                    }
                }
            });

            // to show ths studentName
            ((AttendanceMarkingViewHolder) holder).imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, student.getStudentName(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ClassesListAdapter.studentsArrayList.size();
    }

    // view holder class
    public class AttendanceMarkingViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private ImageView imageView;
        private TextView regNo;

        public AttendanceMarkingViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // image view
            imageView = itemView.findViewById(R.id.marking_circle);

            // text view
            regNo = itemView.findViewById(R.id.marking_no);
        }
    }

    // method to initialise a dummy attendance
    private ArrayList<String> makeDummyAttendance(int size){
        ArrayList<String> dummy = new ArrayList<>(size);

        for(int i = 0; i < size; i++){
            dummy.add(i, "P");
        }

        return dummy;
    }

    // method to check if the student object is already present in the array list
    // if present, student object should not be added once again
    private void addIfNotAlreadyExists(Student student){
        if(! (absentees.contains(student))){
            absentees.add(student);
        }
    }
}
