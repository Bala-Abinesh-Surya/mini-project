package com.surya.miniproject.adapters.hod;

import static com.surya.miniproject.fragments.hod.HODAddStaffsFragment.addedClasses;
import static com.surya.miniproject.fragments.hod.HODAddStaffsFragment.addedClassesPushID;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.surya.miniproject.R;
import com.surya.miniproject.models.Class;

import java.util.ArrayList;

public class AddFacultyAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Class> classes;

    // Constructor
    public AddFacultyAdapter(Context context, ArrayList<Class> classes) {
        this.context = context;
        this.classes = classes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.classes_check_list, parent, false);
        return new AddFacultyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == AddFacultyViewHolder.class){
            // displaying the data
            Class classx = classes.get(position);

            // class name
            ((AddFacultyViewHolder) holder).className.setText(classx.getClassName());

            // check box listener
            ((AddFacultyViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        // adding the class to the addedClasses array list, if not present
                        if(! (addedClasses.contains(classx))){
                            addedClasses.add(classx);
                            addedClassesPushID.add(classx.getClassPushId());
                        }
                    }
                    else{
                        // removing the class from the addedClasses array list
                        if(addedClasses.contains(classx)){
                            addedClasses.remove(classx);
                            addedClassesPushID.add(classx.getClassPushId());
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    // view holder class
    public class AddFacultyViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private TextView className;
        private CheckBox checkBox;

        public AddFacultyViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            className = itemView.findViewById(R.id.hod_add_faculty_staff_name);

            // check box
            checkBox = itemView.findViewById(R.id.hod_add_faculty_check);
        }
    }
}
