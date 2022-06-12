package com.surya.miniproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.surya.miniproject.R;
import com.surya.miniproject.models.Student;

import java.util.ArrayList;

public class AbsenteesListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Student> absentees;

    // Constructor
    public AbsenteesListAdapter(Context context, ArrayList<Student> absentees) {
        this.context = context;
        this.absentees = absentees;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.absentees_list, parent, false);
        return new AbsenteesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == AbsenteesListViewHolder.class){
            // displaying the data
            Student student = absentees.get(position);

            // name
            ((AbsenteesListViewHolder) holder).studentName.setText(student.getStudentName());

            // regNo
            ((AbsenteesListViewHolder) holder).regNo.setText(student.getStudentRegNo());

            // changing the color of the constraint layout alternatively
            if(position % 2 == 0){
                ((AbsenteesListViewHolder) holder).constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.table_row_1));
            }
            else{
                ((AbsenteesListViewHolder) holder).constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.table_row_2));
            }
        }
    }

    @Override
    public int getItemCount() {
        return absentees.size();
    }

    // view holder class
    public static class AbsenteesListViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private TextView regNo, studentName;
        private ConstraintLayout constraintLayout;

        public AbsenteesListViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            regNo = itemView.findViewById(R.id.absentee_no);
            studentName = itemView.findViewById(R.id.absentee_name);

            // constraint layout
            constraintLayout = itemView.findViewById(R.id.absentee_layout);
        }
    }
}
