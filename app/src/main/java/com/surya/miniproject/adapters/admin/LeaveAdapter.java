package com.surya.miniproject.adapters.admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surya.miniproject.R;
import com.surya.miniproject.fragments.admin.AdminMarkLeaves;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LeaveAdapter extends RecyclerView.Adapter{

    private final Context context;
    public static ArrayList<Integer> status;
    private final ArrayList<Integer> original;
    private RecyclerView leaves;
    private ArrayList<Integer> leavesArrayList = new ArrayList<>();
    private int purpose = 0;

    // Constructor
    public LeaveAdapter(Context context, ArrayList<Integer> status, RecyclerView recyclerView) {
        this.context = context;
        LeaveAdapter.status = status;
        this.leaves = recyclerView;

        // initialising leaves array list
        initialiseLeaveData();

        this.original = (ArrayList<Integer>) leavesArrayList.clone();

        // setting up the adapter for the leaves recycler view
        FullLeaveAdapter adapter = new FullLeaveAdapter(context, leavesArrayList);
        leaves.setAdapter(adapter);
        leaves.setLayoutManager(new GridLayoutManager(context, 5));
    }

    public LeaveAdapter(Context context, ArrayList<Integer> status){
        this.context = context;
        LeaveAdapter.status = status;
        this.purpose = 1;

        // initialising leaves array list
        initialiseLeaveData();

        this.original = (ArrayList<Integer>) leavesArrayList.clone();
    }

    // setter methods
    public void setLeaves(RecyclerView leaves) {
        this.leaves = leaves;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendce_marking_list, parent, false);
        return new LeaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // displaying the data
        if(holder.getClass() == LeaveViewHolder.class){
            // date
            ((LeaveViewHolder) holder).date.setText((position + 1)+"");

            // changing the background of the image view
            // if the day is working day, green else red
            class Background{
                // Constructor
                public Background(){
                    if((position + 1) < LocalDateTime.now().getDayOfMonth()){
                        ((LeaveViewHolder) holder).circle.setImageResource(R.drawable.circle_grey);
                    }
                    else{
                        if(status.get(position) == 0){
                            if(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), position + 1, 0, 1).getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                                ((LeaveViewHolder) holder).circle.setImageResource(R.drawable.circle_red);
                            }
                            else{
                                ((LeaveViewHolder) holder).circle.setImageResource(R.drawable.circle_leave);
                            }
                        }
                        else{
                            ((LeaveViewHolder) holder).circle.setImageResource(R.drawable.circle);
                        }
                    }
                }
            }

            new Background();

            // on click listener for the image view
            ((LeaveViewHolder) holder).circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(purpose == 0){
                        if((position + 1) < LocalDateTime.now().getDayOfMonth()){
                            Toast.makeText(context, "It's past time", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), position + 1, 0, 1).getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                                Toast.makeText(context, "It's is a Sunday", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(status.get(position) == 0){
                                    // leave
                                    // changing it to working day - 1
                                    status.remove(position);
                                    status.add(position, 1);
                                }
                                else{
                                    // working day
                                    // changing it to leave - 0
                                    status.remove(position);
                                    status.add(position, 0);
                                }

                                initialiseLeaveData();

                                // setting up the adapter for the leaves recycler view
                                FullLeaveAdapter adapter = new FullLeaveAdapter(context, leavesArrayList);
                                leaves.setAdapter(adapter);
                                leaves.setLayoutManager(new GridLayoutManager(context, 5));

                                // changing the background of the image
                                new Background();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return status.size();
    }

    // view holder class
    private static class LeaveViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private final TextView date;
        private final ImageView circle;

        public LeaveViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            date = itemView.findViewById(R.id.marking_no);

            // image view
            circle = itemView.findViewById(R.id.marking_circle);
        }
    }

    // method to prepare the leaves array list data
    private void initialiseLeaveData(){
        // clearing the leave array list
        leavesArrayList.clear();

        for(int i = 1; i <= status.size(); i++){
            if(status.get(i-1) == 0){
                // 0 means leave
                // so adding the date to the dates array list
                leavesArrayList.add(i);
            }
        }
    }
}
