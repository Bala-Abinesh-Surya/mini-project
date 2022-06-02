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

import java.util.ArrayList;

public class PresentAbsentAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<String> attendance;

    // Constructor
    public PresentAbsentAdapter(Context context, ArrayList<String> attendance) {
        this.context = context;
        this.attendance = attendance;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance, parent, false);
        return new PresentAbsentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == PresentAbsentViewHolder.class){
            // displaying the data
            ((PresentAbsentViewHolder) holder).result.setText(attendance.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return attendance.size();
    }

    // view holder class
    public class PresentAbsentViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private TextView result;

        public PresentAbsentViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialing the UI Elements
            // text view
            result = itemView.findViewById(R.id.attendance_text);
        }
    }
}
