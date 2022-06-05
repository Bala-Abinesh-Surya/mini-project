package com.surya.miniproject.adapters.hod;

import static com.surya.miniproject.constants.Strings.MALE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.surya.miniproject.R;
import com.surya.miniproject.models.Faculty;

import java.util.ArrayList;

public class PanelStaffsListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Faculty> faculties;

    // Constructor
    public PanelStaffsListAdapter(Context context, ArrayList<Faculty> faculties) {
        this.context = context;
        this.faculties = faculties;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hod_staffs_list, parent, false);
        return new PanelStaffsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // displaying the data
        if(holder.getClass() == PanelStaffsListViewHolder.class){
            Faculty faculty = faculties.get(position);

            // name
            ((PanelStaffsListViewHolder) holder).name.setText(faculty.getFacultyName());

            // user name
            ((PanelStaffsListViewHolder) holder).username.setText("@"+faculty.getFacultyUserName());

            // image view
            if(faculty.getFacultyGender().equals(MALE)){
                ((PanelStaffsListViewHolder) holder).imageView.setImageResource(R.drawable.male);
            }
            else{
                ((PanelStaffsListViewHolder) holder).imageView.setImageResource(R.drawable.female);
            }
        }
    }

    @Override
    public int getItemCount() {
        return faculties.size();
    }

    // view holder class
    public class PanelStaffsListViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private TextView name, username;
        private ImageView imageView;

        public PanelStaffsListViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // image view
            imageView = itemView.findViewById(R.id.hod_panel_profile);

            // text views
            name = itemView.findViewById(R.id.hod_panel_name);
            username = itemView.findViewById(R.id.hod_panel_user_name);
        }
    }
}
