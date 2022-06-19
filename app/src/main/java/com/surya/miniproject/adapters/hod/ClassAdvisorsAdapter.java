package com.surya.miniproject.adapters.hod;

import static com.surya.miniproject.constants.Strings.CLASS_ADVISOR;
import static com.surya.miniproject.constants.Strings.CLASS_NAME;
import static com.surya.miniproject.constants.Strings.CLASS_PUSH_ID;
import static com.surya.miniproject.constants.Strings.FACULTIES;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.activities.hod.ClassAdvisorsPickActivity;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;

public class ClassAdvisorsAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final ArrayList<Class> classes;

    // Constructor
    public ClassAdvisorsAdapter(Context context, ArrayList<Class> classes) {
        this.context = context;
        this.classes = classes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_class_advisors_list, parent, false);
        return new ClassAdvisorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // displaying the data
        if(holder.getClass() == ClassAdvisorsViewHolder.class){
            Class classx = classes.get(position);

            // class name
            ((ClassAdvisorsViewHolder) holder).classNameText.setText(classx.getClassName());

            // class advisor name
            ((ClassAdvisorsViewHolder) holder).classAdvisorsText.setText(classx.getClassAdvisor());

            // on click listener for thr edit image
            ((ClassAdvisorsViewHolder) holder).edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // showing an alert to the hod
                    class Alert{
                        private final AlertDialog.Builder builder;

                        public Alert(){
                            // initialising the builder
                            builder = new AlertDialog.Builder(context);

                            // setting up the builder
                            builder.setMessage("Do you want to assign some other faculty as the Class Advisor?")
                                    .setCancelable(false)
                                    .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // just dismissing the dialog
                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // hod clicked yes
                                            // passing the hod to the ClassAdvisorsPickActivity
                                            Intent intent = new Intent(context, ClassAdvisorsPickActivity.class);
                                            intent.putExtra(CLASS_NAME, classx.getClassName());
                                            intent.putExtra(CLASS_ADVISOR, classx.getClassAdvisor());
                                            intent.putExtra(CLASS_PUSH_ID, classx.getClassPushId());
                                            context.startActivity(intent);
                                        }
                                    });
                        }

                        private void show(){
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setTitle("Edit the Class Advisor");
                            alertDialog.show();
                        }
                    }

                    new Alert().show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    // view holder class
    private static class ClassAdvisorsViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private final TextView classNameText, classAdvisorsText;
        private final ImageView edit;

        public ClassAdvisorsViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            classNameText = itemView.findViewById(R.id.hod_class_advisor_class_name);
            classAdvisorsText = itemView.findViewById(R.id.hod_class_advisor_name);

            // image view
            edit = itemView.findViewById(R.id.hod_class_advisor_edit);
        }
    }
}
