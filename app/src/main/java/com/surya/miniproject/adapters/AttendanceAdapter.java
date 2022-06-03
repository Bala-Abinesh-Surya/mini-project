package com.surya.miniproject.adapters;

import static com.surya.miniproject.constants.Strings.ATTENDANCE;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surya.miniproject.R;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Student;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;

public class AttendanceAdapter extends RecyclerView.Adapter {

    private Context context;
    private String className;

    public static ArrayList<Student> students;
    private ArrayList<String> dates;
    private ArrayList<Hashtable<String, String>> tableList = new ArrayList<>();

    // Constructor
    public AttendanceAdapter(Context context, String className, ArrayList<Attendance> results, ArrayList<String> dates) {
        this.context = context;
        this.className = className;
        this.dates = dates;

        // computing the table
        for(Attendance a : results){
            Type type = new TypeToken<Hashtable<String, String>>(){}.getType();
            Hashtable<String, String> table = new Gson().fromJson(a.getJson(), type);

            tableList.add(table);
        }
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

                if(! (dates.size() == 0)){
                    PresentAbsentAdapter presentAbsentAdapter = new PresentAbsentAdapter(context, dates);
                    ((AttendanceViewHolder) holder).recyclerView.setAdapter(presentAbsentAdapter);
                    ((AttendanceViewHolder) holder).recyclerView.setLayoutManager(new GridLayoutManager(context, dates.size()));
                }
            }
            else{
                // displaying the data
                Student student = students.get(position - 1);

                // name
                String studentName = student.getStudentName();
                ((AttendanceViewHolder) holder).studentName.setText(studentName);

                // reg no
                String regNo = student.getStudentRegNo();
                ((AttendanceViewHolder) holder).studentRegNo.setText(regNo.substring(regNo.length() - 3));

                // setting up the recycler view
                ArrayList<String> present = new ArrayList<>();
                for(Hashtable<String, String> a : tableList){
                    present.add(a.get(studentName));
                }

                if(present.size() == 0){
                    // attendance for this month has not been uploaded yet
                   // Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                else{
                    PresentAbsentAdapter presentAbsentAdapter = new PresentAbsentAdapter(context, present);
                    ((AttendanceViewHolder) holder).recyclerView.setAdapter(presentAbsentAdapter);
                    ((AttendanceViewHolder) holder).recyclerView.setLayoutManager(new GridLayoutManager(context, present.size()));
                }
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
