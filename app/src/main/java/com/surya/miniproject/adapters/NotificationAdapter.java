package com.surya.miniproject.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.surya.miniproject.R;
import com.surya.miniproject.models.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter {

    private ArrayList<Notification> notifications;
    private Context context;

    // Constructor
    public NotificationAdapter(ArrayList<Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;

        // reversing the notification array list
        reverseNotificationArrayList();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notifications_layout, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == NotificationViewHolder.class){
            // displaying the data
            Notification notification = notifications.get(position);

            // notification message
            ((NotificationViewHolder) holder).message.setText(notification.returnNotificationMessage());
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    // view holder class
    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private final TextView message;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            message = itemView.findViewById(R.id.notification_msg);
        }
    }

    // method to reverse the notification array list
    private void reverseNotificationArrayList(){
        ArrayList<Notification> temp = new ArrayList<>(notifications.size());

        // going through the notifications array list in reverse
        for(int i = notifications.size() - 1; i >= 0; i--){
            int j = (notifications.size() - 1) - i;

            temp.add(j, notifications.get(i));
        }

        notifications = temp;
    }
}
