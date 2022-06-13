package com.surya.miniproject.adapters;

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
import com.surya.miniproject.R;
import com.surya.miniproject.details.Data;

public class DevelopersListAdapter extends RecyclerView.Adapter {

    private final Context context;
    private Data data;
    private final View bottomSheetView;

    // Constructor
    public DevelopersListAdapter(Context context, View view) {
        this.context = context;
        this.bottomSheetView = view;

        // initialising data object
        data = new Data();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.developers_list, parent, false);
        return new DevelopersListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == DevelopersListViewHolder.class){
            // displaying the data
            // developer_name@@@about@@@reg_no@@@role
            String[] temp = data.DEVELOPER_DETAILS[position].split("@@@");

            // name
            ((DevelopersListViewHolder) holder).name.setText(temp[0]);

            // about
            ((DevelopersListViewHolder) holder).about.setText(temp[1]);

            // image view
            if(position == 0){
                // shenoi sir
                // setting the male image
                ((DevelopersListViewHolder) holder).imageView.setImageResource(R.drawable.male);
            }
            else if(position <= 2){
                // two members, Abinilla and Harsha
                // setting the female image
                ((DevelopersListViewHolder) holder).imageView.setImageResource(R.drawable.female);
            }
            else{
                // last two members, Abino and Myself
                // setting the male image
                ((DevelopersListViewHolder) holder).imageView.setImageResource(R.drawable.male);
            }

            // on click listener for the constraint layout
            ((DevelopersListViewHolder) holder).constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // inflating the developers bottom sheet
                    class BottomSheet{
                        private final BottomSheetDialog bottomSheetDialog;

                        // Constructor
                        public BottomSheet() {
                            bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                            bottomSheetDialog.setCancelable(false);
                            bottomSheetDialog.setContentView(bottomSheetView);
                            bottomSheetDialog.show();
                        }

                        // bottom sheet essentials
                        public void essentials(){
                            // setting up the UI Elements
                            // name
                            ((TextView) bottomSheetView.findViewById(R.id.developers_sheet_name)).setText(temp[0]);

                            // role
                            ((TextView) bottomSheetView.findViewById(R.id.developers_sheet_role)).setText(temp[3]);

                            // reg no
                            ((TextView) bottomSheetView.findViewById(R.id.developers_sheet_reg_no)).setText(temp[2]);

                            // image view
                            if(position == 0){
                                // shenoi sir
                                ((ImageView) bottomSheetView.findViewById(R.id.developers_sheet_image)).setImageResource(R.drawable.male);

                                // also modifying the text above the dismiss button
                                ((TextView) bottomSheetView.findViewById(R.id.developers_sheet_batch_text)).setText("CSE Department");
                            }
                            else if(position <= 2){
                                // harsha and abinilla
                                ((ImageView) bottomSheetView.findViewById(R.id.developers_sheet_image)).setImageResource(R.drawable.female);

                                // also modifying the text above the dismiss button
                                ((TextView) bottomSheetView.findViewById(R.id.developers_sheet_batch_text)).setText("CSE Department - 2019 Batch");
                            }
                            else{
                                // myself and abino
                                ((ImageView) bottomSheetView.findViewById(R.id.developers_sheet_image)).setImageResource(R.drawable.male);

                                // also modifying the text above the dismiss button
                                ((TextView) bottomSheetView.findViewById(R.id.developers_sheet_batch_text)).setText("CSE Department - 2019 Batch");
                            }

                            // on click listener for the button
                            ((Button) bottomSheetView.findViewById(R.id.developers_sheet_dismiss)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // dismissing the dialog
                                    ((ViewGroup) bottomSheetView.getParent()).removeView(bottomSheetView);
                                    bottomSheetDialog.dismiss();
                                }
                            });
                        }
                    }

                    new BottomSheet().essentials();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.DEVELOPER_DETAILS.length;
    }

    // View Holder class
    private static class DevelopersListViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private final TextView name, about;
        private final ImageView imageView;
        private final ConstraintLayout constraintLayout;

        public DevelopersListViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            name = itemView.findViewById(R.id.developers_list_name);
            about = itemView.findViewById(R.id.developers_list_about);

            // image view
            imageView = itemView.findViewById(R.id.developers_list_image);

            // constraint layout
            constraintLayout = itemView.findViewById(R.id.developers_layout);
        }
    }
}
