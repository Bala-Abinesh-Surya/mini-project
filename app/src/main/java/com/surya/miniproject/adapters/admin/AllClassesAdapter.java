package com.surya.miniproject.adapters.admin;

import static com.surya.miniproject.constants.Strings.ATTENDANCE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.models.Class;
import com.surya.miniproject.utility.Functions;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AllClassesAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final ArrayList<Class> classes;
    private final FirebaseDatabase firebaseDatabase;

    // Constructor
    public AllClassesAdapter(Context context, ArrayList<Class> classes, FirebaseDatabase firebaseDatabase) {
        this.context = context;
        this.classes = classes;
        this.firebaseDatabase = firebaseDatabase;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_classes_list, parent, false);
        return new AllClassesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == AllClassesViewHolder.class){
            // displaying the data
            Class myClass = classes.get(position);

            // name
            ((AllClassesViewHolder) holder).className.setText(myClass.getClassName());

            // class advisor
            ((AllClassesViewHolder) holder).advisor.setText(myClass.getClassAdvisor());

            // status image
            // checking if the attendance for today has been updated for the class
            firebaseDatabase.getReference()
                    .child(ATTENDANCE)
                    .child(myClass.getClassName())
                    .child(LocalDateTime.now().getMonth() + "-" + LocalDateTime.now().getYear())
                    .child(new Functions().date())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                // attendance has already been taken for today
                                ((AllClassesViewHolder) holder).status.setImageResource(R.drawable.circle);

                                // hiding the notify image, because the attendance is taken
                                ((AllClassesViewHolder) holder).notify.setVisibility(View.GONE);
                            }
                            else{
                                // snapshot does not exist
                                // so attendance is not yet taken
                                ((AllClassesViewHolder) holder).status.setImageResource(R.drawable.circle_red);

                                // making the notify image visible, because the attendance is not yet taken
                                ((AllClassesViewHolder) holder).notify.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            // notify image
            ((AllClassesViewHolder) holder).notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    // view holder class
    private static class AllClassesViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private final TextView className, advisor;
        private final ImageView status, notify;
        private final ConstraintLayout constraintLayout;

        public AllClassesViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            className = itemView.findViewById(R.id.admin_all_classes_name);
            advisor = itemView.findViewById(R.id.admin_all_classes_class_advisor);

            // image view
            status = itemView.findViewById(R.id.admin_all_classes_status);
            notify = itemView.findViewById(R.id.admin_all_classes_notify);

            // constraint layout
            constraintLayout = itemView.findViewById(R.id.admin_all_classes_layout);
        }
    }
}
