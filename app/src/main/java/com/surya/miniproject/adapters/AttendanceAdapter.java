package com.surya.miniproject.adapters;

import static com.surya.miniproject.constants.Strings.ATTENDANCE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.models.Student;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter {

    private Context context;
    private String className;

    public static ArrayList<Student> students;

    // Constructor
    public AttendanceAdapter(Context context, String className) {
        this.context = context;
        this.className = className;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.students_list, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == AttendanceViewHolder.class){
            if(position == 0){
                ((AttendanceViewHolder) holder).studentName.setText("Name");
                ((AttendanceViewHolder) holder).studentRegNo.setText("No.");
            }
            else{
                // displaying the data
                Student student = students.get(position - 1);

                // name
                ((AttendanceViewHolder) holder).studentName.setText(student.getStudentName());

                // reg no
                String regNo = student.getStudentRegNo();
                ((AttendanceViewHolder) holder).studentRegNo.setText(regNo.substring(regNo.length() - 3));

                // setting up the adapter for the recycler view
                // getting the attendance from the database

            }

            // changing the color of the constraint layout alternatively
            if(position % 2 == 0){
                ((AttendanceViewHolder) holder).constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.table_row_1));
            }
            else{
                ((AttendanceViewHolder) holder).constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.table_row_2));
            }
        }
    }

    @Override
    public int getItemCount() {
        return students.size() + 1;
    }

    // Attendance view holder class
    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        // UI Elements
        private TextView studentRegNo, studentName;
        private ConstraintLayout constraintLayout;
        private RecyclerView recyclerView;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            studentName = itemView.findViewById(R.id.student_list_name);
            studentRegNo = itemView.findViewById(R.id.student_list_reg_no);

            // constraint layout
            constraintLayout = itemView.findViewById(R.id.student_list_layout);

            // recycler view
            recyclerView = itemView.findViewById(R.id.student_list_rec_view);
        }
    }
}
