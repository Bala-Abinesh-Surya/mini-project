package com.surya.miniproject.adapters.hod;

import static com.surya.miniproject.constants.Strings.FACULTIES;
import static com.surya.miniproject.constants.Strings.MALE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.details.Data;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;

public class StaffsDetailsAdapter extends RecyclerView.Adapter {
    /*Using this adapter for two causes, same layout*/

    private Context context;
    private ArrayList<String> facultyMembers;
    private int purpose;
    private FirebaseDatabase firebaseDatabase;

    // Constructor
    public StaffsDetailsAdapter(Context context, ArrayList<String> facultyMembers, FirebaseDatabase firebaseDatabase) {
        this.context = context;
        this.facultyMembers = facultyMembers;
        this.firebaseDatabase = firebaseDatabase;
    }

    // setter methods
    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.staffs_list, parent, false);
        return new StaffsDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == StaffsDetailsViewHolder.class){
            /*Using this adapter for two purposes*/
            /*
            *
            * Purpose 1 :
            *   - To display the staffs, and removing the staffs from the faculty members for a class
            *
            * Purpose 2 :
            *
            *
            * */

            String facultyName = facultyMembers.get(position);

            // staff name
            ((StaffsDetailsViewHolder) holder).staffName.setText(facultyName);

            // on click listener for the edit image
            ((StaffsDetailsViewHolder) holder).edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(purpose == 1){

                    }
                    else{
                        // purpose is 2
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return facultyMembers.size();
    }

    // view holder class
    public class StaffsDetailsViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private TextView staffName;
        private ImageView edit;

        public StaffsDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            staffName = itemView.findViewById(R.id.staff_details_name);

            // image views
            edit = itemView.findViewById(R.id.stafff_details_edit);
        }
    }
}
