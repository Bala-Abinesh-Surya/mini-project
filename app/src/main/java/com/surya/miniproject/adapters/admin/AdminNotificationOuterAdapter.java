package com.surya.miniproject.adapters.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surya.miniproject.R;
import com.surya.miniproject.adapters.NotificationAdapter;
import com.surya.miniproject.models.Notification;

import java.util.ArrayList;

public class AdminNotificationOuterAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<ArrayList<Notification>> notifications;

    // Constructor
    public AdminNotificationOuterAdapter(Context context, ArrayList<ArrayList<Notification>> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_notifications_list, parent, false);
        return new AdminNotificationsOuterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == AdminNotificationsOuterViewHolder.class){
            // displaying the data
            ArrayList<Notification> notification = notifications.get(position);

            // data
            ((AdminNotificationsOuterViewHolder) holder).date.setText(notification.get(0).getDate());

            // setting up the adapter fpr thr recycler view
            NotificationAdapter adapter = new NotificationAdapter(notification, context);
            ((AdminNotificationsOuterViewHolder) holder).recyclerView.setAdapter(adapter);
            ((AdminNotificationsOuterViewHolder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    // view holder class
    private static class AdminNotificationsOuterViewHolder extends RecyclerView.ViewHolder{
        // UI Elements
        private final TextView date;
        private final RecyclerView recyclerView;

        public AdminNotificationsOuterViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text view
            date = itemView.findViewById(R.id.admin_notification_date);
            recyclerView = itemView.findViewById(R.id.admin_notification_inner_rec_view);
        }
    }
}
