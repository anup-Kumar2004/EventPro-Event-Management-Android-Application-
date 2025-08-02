package com.example.splashactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Model2Adapter extends RecyclerView.Adapter<Model2Adapter.Model2ViewHolder> {

    private List<Model2> eventList;
    private Context context;

    public Model2Adapter(List<Model2> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public Model2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item3, parent, false);
        return new Model2ViewHolder(view, context, this);
    }

    @Override
    public void onBindViewHolder(@NonNull Model2ViewHolder holder, int position) {
        Model2 event = eventList.get(position);
        holder.bind(event, position);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class Model2ViewHolder extends RecyclerView.ViewHolder {
        private TextView eventNameTextView;
        private TextView eventDateTextView;
        private TextView eventDetailsTextView;
        private MaterialButton acceptInviteButton;
        private MaterialButton declineInviteButton;
        private Model2Adapter adapter;
        private Context context;

        public Model2ViewHolder(@NonNull View itemView, Context context, Model2Adapter adapter) {
            super(itemView);
            this.context = context;
            this.adapter = adapter;
            eventNameTextView = itemView.findViewById(R.id.eventName);
            eventDateTextView = itemView.findViewById(R.id.eventDate);
            eventDetailsTextView = itemView.findViewById(R.id.eventDetails);
            acceptInviteButton = itemView.findViewById(R.id.accept_invite_button);
            declineInviteButton = itemView.findViewById(R.id.decline_invite_button);
        }

        public void bind(Model2 event, int position) {
            eventNameTextView.setText(event.getEventName());
            eventDateTextView.setText(event.getEventDate());
            eventDetailsTextView.setText(event.getEventDetails());

            acceptInviteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addEventToDatabase(event.getEventName(), position, "AcceptedEvents");
                    Toast.makeText(context, "Invite Accepted", Toast.LENGTH_SHORT).show();
                }
            });

            declineInviteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addEventToDatabase(event.getEventName(), position, "DeclinedEvents");
                    Toast.makeText(context, "Invite Declined", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void addEventToDatabase(String eventName, int position, String nodeName) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userEventsRef = databaseReference.child(nodeName).child(userId);

            // Fetch existing events count
            userEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long eventCount = snapshot.getChildrenCount();
                    String newEventName = "event" + (eventCount + 1);

                    // Add the new event with the generated name
                    userEventsRef.child(newEventName).setValue(eventName);

                    // Remove item from RecyclerView
                    adapter.eventList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, adapter.eventList.size());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle potential errors here
                }
            });
        }
    }

    public void startListening() {
        // Add logic if needed to start listening for changes
    }

    public void stopListening() {
        // Add logic if needed to stop listening for changes
    }
}
