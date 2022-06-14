package com.surya.miniproject.adapters.admin;

import static com.surya.miniproject.constants.Strings.HOD;
import static com.surya.miniproject.constants.Strings.MALE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surya.miniproject.R;
import com.surya.miniproject.models.Faculty;
import com.surya.miniproject.models.HOD;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AllFacultyMembersAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final ArrayList<Faculty> facultyMembers;
    private final FirebaseDatabase firebaseDatabase;
    private View bottomSheetView;

    // Constructor
    public AllFacultyMembersAdapter(Context context, ArrayList<Faculty> facultyMembers, FirebaseDatabase firebaseDatabase) {
        this.context = context;
        this.facultyMembers = facultyMembers;
        this.firebaseDatabase = firebaseDatabase;
    }

    // setter methods
    public void setBottomSheetView(View bottomSheetView) {
        this.bottomSheetView = bottomSheetView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_all_faculty_members, parent, false);
        return new AllFacultyMembersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == AllFacultyMembersViewHolder.class){
            // displaying the data
            Faculty faculty = facultyMembers.get(position);
            final boolean[] facultyIsAnHod = {false};

            // name
            ((AllFacultyMembersViewHolder) holder).facultyName.setText(faculty.getFacultyName());

            // department
            ((AllFacultyMembersViewHolder) holder).department.setText(faculty.getFacultyDepartment());

            // gender image
            // if the faculty is the HOD, setting the delegate image
            // else, setting the image based on the gender
            firebaseDatabase.getReference()
                    .child(HOD)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    com.surya.miniproject.models.HOD hod = dataSnapshot.getValue(HOD.class);

                                    if(hod.getDepartment().equals(faculty.getFacultyDepartment())){
                                        if(hod.getName().equals(faculty.getFacultyName())){
                                            // faculty is the HOD
                                            facultyIsAnHod[0] = true;

                                            ((AllFacultyMembersViewHolder) holder).gender.setImageResource(R.drawable.delegate);
                                        }
                                        else{
                                            // faculty is not the HOD
                                            facultyIsAnHod[0] = false;

                                            if(faculty.getFacultyGender().equals(MALE)){
                                                ((AllFacultyMembersViewHolder) holder).gender.setImageResource(R.drawable.male);
                                            }
                                            else{
                                                // Female or Rather Not Say
                                                ((AllFacultyMembersViewHolder) holder).gender.setImageResource(R.drawable.female);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            // on click listener for the constraint layout
            ((AllFacultyMembersViewHolder) holder).constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // showing the bottom sheet
                    class BottomSheet{
                        // Constructor
                        public BottomSheet(){
                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                            bottomSheetDialog.setCancelable(false);
                            bottomSheetDialog.setContentView(bottomSheetView);
                            bottomSheetDialog.show();

                            // setting up the texts
                            // name
                            ((TextView) bottomSheetView.findViewById(R.id.admin_all_faculty_bottom_name)).setText(faculty.getFacultyName());

                            // user name
                            ((TextView) bottomSheetView.findViewById(R.id.admin_all_faculty_bottom_username)).setText(faculty.getFacultyUserName());

                            // password
                            ((TextView) bottomSheetView.findViewById(R.id.admin_all_faculty_bottom_password)).setText(faculty.getFacultyPassword());

                            // push id
                            ((TextView) bottomSheetView.findViewById(R.id.admin_all_faculty_bottom_pushId)).setText(faculty.getFacultyPushId());

                            // on click listener for the button
                            ((Button) bottomSheetView.findViewById(R.id.admin_all_faculty_bottom_dismiss)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // dismissing the dialog
                                    ((ViewGroup) bottomSheetView.getParent()).removeView(bottomSheetView);
                                    bottomSheetDialog.dismiss();
                                }
                            });

                            // image
                            // if the faculty is an HOD, then delegate image
                            // else, based on gender
                            if(facultyIsAnHod[0]){
                                // faculty is an HOD
                                ((ImageView) bottomSheetView.findViewById(R.id.admin_all_faculty_bottom_image)).setImageResource(R.drawable.delegate);
                            }
                            else{
                                // faculty is not an HOD
                                if(faculty.getFacultyGender().equals(MALE)){
                                    ((ImageView) bottomSheetView.findViewById(R.id.admin_all_faculty_bottom_image)).setImageResource(R.drawable.male);
                                }
                                else{
                                    // Female or Rather Not Say
                                    ((ImageView) bottomSheetView.findViewById(R.id.admin_all_faculty_bottom_image)).setImageResource(R.drawable.female);
                                }
                            }
                        }
                    }

                    new BottomSheet();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return facultyMembers.size();
    }

    // view holder class
    private static class AllFacultyMembersViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private final TextView facultyName;
        private final TextView department;
        private final ImageView gender;
        private final ConstraintLayout constraintLayout;

        public AllFacultyMembersViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            facultyName = itemView.findViewById(R.id.admin_all_faculty_name);
            department = itemView.findViewById(R.id.admin_all_faculty_department);

            // image view
            gender = itemView.findViewById(R.id.admin_all_faculty_image);

            // constraint layout
            constraintLayout = itemView.findViewById(R.id.admin_all_faculty_layout);
        }
    }
}
