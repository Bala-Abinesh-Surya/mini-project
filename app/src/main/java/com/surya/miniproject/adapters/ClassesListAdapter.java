package com.surya.miniproject.adapters;

import static com.surya.miniproject.activities.DashBoard.facultyName;
import static com.surya.miniproject.constants.Strings.CLASS_ADVISOR;
import static com.surya.miniproject.constants.Strings.CLASS_DEPARTMENT;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_PUSH_ID;

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
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.surya.miniproject.R;
import com.surya.miniproject.activities.ClassAttendance;
import com.surya.miniproject.activities.TodayAttendance;
import com.surya.miniproject.models.Attendance;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.CurrentClass;
import com.surya.miniproject.models.Student;

import java.util.ArrayList;
import java.util.Hashtable;

public class ClassesListAdapter extends RecyclerView.Adapter{

    private ArrayList<Class> classes;
    private Context context;
    private Hashtable<String, String> advisors;

    public static ArrayList<Student> studentsArrayList;

    // Constructor
    public ClassesListAdapter(ArrayList<Class> classes, Context context, Hashtable<String, String> advisors) {
        this.classes = classes;
        this.context = context;
        this.advisors = advisors;
    }

    public ClassesListAdapter(){

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_list, parent, false);
        return new ClassesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == ClassesListViewHolder.class){
            // displaying the data
            Class classx = classes.get(position);

            // className
            ((ClassesListViewHolder) holder).className.setText(classx.getClassName());

            // on click listener for the constraint layout
            ((ClassesListViewHolder) holder).constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // passing the user to the ClassAttendance activity
                    Intent intent = new Intent(context, ClassAttendance.class);
                    // passing the className, pushId, class advisor, class department as the intent
                    intent.putExtra(CLASS_NAME, classx.getClassName());
                    intent.putExtra(CLASS_PUSH_ID, classx.getClassPushId());
                    intent.putExtra(CLASS_ADVISOR, classx.getClassAdvisor());
                    intent.putExtra(CLASS_DEPARTMENT, classx.getClassDepartment());
                    context.startActivity(intent);
                }
            });

            // checking if the currently signed in faculty is the class advisor of this class
            // if so, making the tick VISIBLE
            if(facultyName.equals(advisors.get(classx.getClassName()))){
                ((ClassesListViewHolder) holder).tick.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    // view holder class
    public class ClassesListViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private TextView className;
        private ConstraintLayout constraintLayout;
        private ImageView tick;

        public ClassesListViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            className = itemView.findViewById(R.id.class_list_name);

            // constraint layout
            constraintLayout = itemView.findViewById(R.id.class_list_layout);

            // image view
            tick = itemView.findViewById(R.id.tick);

            // hiding the tick by default
            tick.setVisibility(View.GONE);
        }
    }
}
