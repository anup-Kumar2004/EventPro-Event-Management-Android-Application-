package com.example.splashactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> eventList;

    public EventsAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_joined_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventName.setText(event.getEventName());
        holder.eventDetails.setText(event.getEventDetails());
        holder.fromDate.setText(event.getFromDate());
        holder.toDate.setText(event.getToDate());
        holder.fromTime.setText(event.getFromTime());
        holder.toTime.setText(event.getToTime());
        holder.emailText.setText(event.getEmail());  // Bind the email field
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName, eventDetails, fromDate, toDate, fromTime, toTime, emailText;  // New field

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDetails = itemView.findViewById(R.id.eventDetails);
            fromDate = itemView.findViewById(R.id.fromDate);
            toDate = itemView.findViewById(R.id.toDate);
            fromTime = itemView.findViewById(R.id.fromTime);
            toTime = itemView.findViewById(R.id.toTime);
            emailText = itemView.findViewById(R.id.emailText);  // Initialize the new field
        }
    }
}
