package com.surya.miniproject.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surya.miniproject.R;
import com.surya.miniproject.models.Student;

import java.util.ArrayList;
import java.util.Hashtable;

public class TodayAttendanceAdapter extends RecyclerView.Adapter {

    private final Context context;
    public static Hashtable<String, String> attendance;
    private final ArrayList<Student> students;
    private final int purpose;
    public static boolean forHereToEditTheAttendance = false;
    private final Button update;
    private final RecyclerView recyclerView;
    private final TextView noAbsenteesText;
    private ArrayList<Student> absentees = new ArrayList<>();

    // Constructor
    public TodayAttendanceAdapter(Context context, ArrayList<Student> students, Hashtable<String, String> attendance, int purpose, Button button, RecyclerView recyclerView, TextView noAbsenteesText) {
        this.context = context;
        this.students = students;
        TodayAttendanceAdapter.attendance = attendance;
        this.purpose = purpose;
        this.update = button;
        this.recyclerView = recyclerView;
        this.noAbsenteesText = noAbsenteesText;

        // making the button invisible
        update.setVisibility(View.GONE);
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

            new BackGround();

            // on click listener for the image view
            ((TodayAttendanceViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(purpose == 1){
                        // class advisor is here to edit the attendance may be...
                        if(forHereToEditTheAttendance){
                            // checking the status of the attendance now
                            String studentName = students.get(position).getStudentName();

                            class Edit{
                                public Edit(){
                                    // clearing the absentees array list
                                    absentees.clear();

                                    for(Student student : students){
                                        if(attendance.get(student.getStudentName()).equals("A")){
                                            absentees.add(student);
                                        }
                                    }

                                    if(absentees.size() == 0){
                                        // no absentees there
                                        // so making the noAbsentText visible
                                        noAbsenteesText.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                    else{
                                        // absentees are there
                                        // so setting up the adapter for the absentees recycler view
                                        noAbsenteesText.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);

                                        AbsenteesListAdapter adapter = new AbsenteesListAdapter(context, absentees);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    }
                                }
                            }

                            if(attendance.get(studentName).equals("P")){
                                // changing it to A
                                attendance.put(studentName, "A");
                            }
                            else{
                                // current is A
                                // changing it to P
                                attendance.put(studentName, "P");
                            }

                            new BackGround();
                            new Edit();
                        }
                        else{
                            // showing an alert to the class advisor, to check if the faculty is here to edit the attendance
                            class Alert{
                                public Alert(){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                            .setCancelable(false)
                                            .setMessage("Do you want to edit the attendance ?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    forHereToEditTheAttendance = true;
                                                    // showing the button
                                                    update.setVisibility(View.VISIBLE);
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    forHereToEditTheAttendance = false;
                                                    dialog.dismiss();
                                                }
                                            });

                                    AlertDialog dialog = builder.create();
                                    dialog.setTitle("Edit the attendance");
                                    dialog.show();
                                }
                            }

                            new Alert();
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
