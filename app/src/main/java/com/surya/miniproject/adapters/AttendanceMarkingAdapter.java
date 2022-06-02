package com.surya.miniproject.adapters;

import static com.surya.miniproject.adapters.AttendanceAdapter.students;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        attendance = makeDummyAttendance(students.size());

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
            Student student = students.get(position);

            // regNo
            String regNo = student.getStudentRegNo();
            ((AttendanceMarkingViewHolder) holder).regNo.setText(regNo.substring(regNo.length() - 3));

            // on click listener for the image view
            ((AttendanceMarkingViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // checking if the attendance is marked as "P", if so changing it to "A"
                    if(attendance.get(position).equals("P")){
                        // changing it to "A"
                        attendance.add(position, "A");
                        // changing the drawable file - red color
                        ((AttendanceMarkingViewHolder) holder).imageView.setBackgroundResource(R.drawable.circle_red);

                        // adding the student to the absentees array list
                        absentees.add(student);

                        absentees.size();

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
                        attendance.add(position, "P");
                        // changing the drawable file - green color
                        ((AttendanceMarkingViewHolder) holder).imageView.setBackgroundResource(R.drawable.circle);

                        // removing the student
                        absentees.remove(student);

                        // if the size of the absentees is 0, hiding the recyler view
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
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
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
}
