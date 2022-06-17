package com.surya.miniproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.surya.miniproject.R;
import com.surya.miniproject.models.Student;

import java.util.ArrayList;
import java.util.Hashtable;

public class TodayAttendanceAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final Hashtable<String, String> attendance;
    private final ArrayList<Student> students;

    // Constructor
    public TodayAttendanceAdapter(Context context, ArrayList<Student> students, Hashtable<String, String> attendance) {
        this.context = context;
        this.students = students;
        this.attendance = attendance;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendce_marking_list, parent, false);
        return new TodayAttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == TodayAttendanceViewHolder.class){
            // displaying the data
            // regNo
            String reg = students.get(position).getStudentRegNo();
            ((TodayAttendanceViewHolder) holder).regNo.setText(reg.substring(reg.length() - 3));

            // back ground for the image view
            class BackGround{
                public BackGround(){
                    if(attendance.get(students.get(position).getStudentName()).equals("P")){
                        // green color drawable
                        ((TodayAttendanceViewHolder) holder).imageView.setImageResource(R.drawable.circle);
                    }
                    else{
                        // red drawable color
                        ((TodayAttendanceViewHolder) holder).imageView.setImageResource(R.drawable.circle_red);
                    }
                }
            }

            //new BackGround();
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    // view holder class
    private static class TodayAttendanceViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private final TextView regNo;
        private final ImageView imageView;

        public TodayAttendanceViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            regNo = itemView.findViewById(R.id.marking_no);

            // image view
            imageView = itemView.findViewById(R.id.marking_circle);
        }
    }
}
