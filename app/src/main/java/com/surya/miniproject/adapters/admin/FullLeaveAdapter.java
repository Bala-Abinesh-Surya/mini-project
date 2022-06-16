package com.surya.miniproject.adapters.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.surya.miniproject.R;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class FullLeaveAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final ArrayList<Integer> leaves;

    // Constructor
    public FullLeaveAdapter(Context context, ArrayList<Integer> leaves) {
        this.context = context;
        this.leaves = leaves;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendce_marking_list, parent, false);
        return new FullLeaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == FullLeaveViewHolder.class){
            // displaying the data
            // date
            ((FullLeaveViewHolder) holder).date.setText(leaves.get(position) + "");

            // image view
            if(LocalDateTime.now().getDayOfMonth() > leaves.get(position)){
                ((FullLeaveViewHolder) holder).circle.setImageResource(R.drawable.circle_grey);
            }
            else{
                ((FullLeaveViewHolder) holder).circle.setImageResource(R.drawable.circle_red);
            }
        }
    }

    @Override
    public int getItemCount() {
        return leaves.size();
    }

    // view holder class
    private static class FullLeaveViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private final TextView date;
        private final ImageView circle;

        public FullLeaveViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            date = itemView.findViewById(R.id.marking_no);

            // image view
            circle = itemView.findViewById(R.id.marking_circle);
        }
    }
}
