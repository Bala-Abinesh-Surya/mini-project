package com.surya.miniproject.adapters;

import static com.surya.miniproject.activities.DashBoard.facultyDepartment;
import static com.surya.miniproject.constants.Strings.REQUESTS;

import android.content.Context;
import android.content.pm.LabeledIntent;
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
import com.google.firebase.database.FirebaseDatabase;
import com.surya.miniproject.R;
import com.surya.miniproject.models.Request;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final ArrayList<Request> requestArrayList;
    private View requestAcceptView;
    private final FirebaseDatabase firebaseDatabase;

    // Constructor
    public RequestsAdapter(Context context, ArrayList<Request> requestArrayList, FirebaseDatabase firebaseDatabase) {
        this.context = context;
        this.requestArrayList = requestArrayList;
        this.firebaseDatabase = firebaseDatabase;
    }

    // setter methods
    public void setRequestAcceptView(View requestAcceptView) {
        this.requestAcceptView = requestAcceptView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.requests_list, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == RequestViewHolder.class){
            // displaying the data
            Request request = requestArrayList.get(position);

            // request text
            if(request.isAccepted()){
                ((RequestViewHolder) holder).requestText.setText(request.requestAcceptedText());
            }
            else{
                ((RequestViewHolder) holder).requestText.setText(request.requestText());
            }

            // date
            ((RequestViewHolder) holder).date.setText(request.getRequestedDate());

            // image view
            if(request.isAccepted()){
                // request is accepted
                ((RequestViewHolder) holder).imageView.setImageResource(R.drawable.request_accepted);
            }
            else{
                // request is not accepted
                ((RequestViewHolder) holder).imageView.setImageResource(R.drawable.question);
            }

            // on click listener for the layout
            ((RequestViewHolder) holder).constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(! (request.isAccepted())){
                        // request is not accepted yet
                        // inflating the request accepting bottom sheet
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                        bottomSheetDialog.setContentView(requestAcceptView);
                        bottomSheetDialog.setCancelable(false);
                        bottomSheetDialog.show();

                        class elements{
                            private final View view;
                            private final Button button, later;
                            private final TextView acceptText, timeline;

                            // Constructor
                            public elements(View view) {
                                this.view = view;

                                // initialising the UI Elements
                                // button
                                button = view.findViewById(R.id.request_accept_btn);
                                later = view.findViewById(R.id.request_accept_later);

                                // text view
                                acceptText = view.findViewById(R.id.request_accept_text);
                                timeline = view.findViewById(R.id.request_accept_timeline);

                                acceptText.setText(request.requestText());
                                timeline.setText(request.timelineText());

                                // on click listener for the accept button
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // updating the status of the request to request accepted
                                        request.setAccepted(true);

                                        // updating the request to the firebase
                                        firebaseDatabase.getReference()
                                                .child(REQUESTS)
                                                .child(facultyDepartment)
                                                .child(request.getRequestPushId())
                                                .setValue(request);

                                        // dismissing the dialogue
                                        ((ViewGroup) requestAcceptView.getParent()).removeView(requestAcceptView);
                                        bottomSheetDialog.dismiss();
                                    }
                                });

                                // on click listener for the accept later button
                                later.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // dismissing the dialogue
                                        ((ViewGroup) requestAcceptView.getParent()).removeView(requestAcceptView);
                                        bottomSheetDialog.dismiss();
                                    }
                                });
                            }
                        }

                        new elements(requestAcceptView);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    // view holder class
    private class RequestViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private final ImageView imageView;
        private final TextView requestText, date;
        private final ConstraintLayout constraintLayout;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            // method to initialise the UI Elements
            // image view
            imageView = itemView.findViewById(R.id.request_approved_image);

            // text view
            requestText = itemView.findViewById(R.id.request_text);
            date = itemView.findViewById(R.id.request_date);

            // constraint layout
            constraintLayout = itemView.findViewById(R.id.request_layout);
        }
    }
}
