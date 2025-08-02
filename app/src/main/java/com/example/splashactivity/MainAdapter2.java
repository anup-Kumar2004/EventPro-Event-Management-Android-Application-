package com.example.splashactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class MainAdapter2 extends RecyclerView.Adapter<MainAdapter2.ViewHolder> {

    private List<MainModel> eventList;
    private FirebaseAuth auth;

    public MainAdapter2(List<MainModel> eventList) {
        this.eventList = eventList;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainModel mainModel = eventList.get(position);
        holder.abc.setText(mainModel.getEventName());
        holder.cde.setText(mainModel.getFromDate());
        holder.def.setText(mainModel.getEventDetails());

        holder.joinEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    String uid = auth.getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("AcceptedEvents").child(uid);

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long count = dataSnapshot.getChildrenCount();
                            String newEventKey = "event" + (count + 1);
                            ref.child(newEventKey).setValue(mainModel.getEventName())
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(v.getContext(), "Event Joined", Toast.LENGTH_SHORT).show();
                                        removeItem(currentPosition);
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Failed to Join Event", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle potential errors
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView abc, cde, def;
        View joinEventButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            abc = itemView.findViewById(R.id.abc);
            cde = itemView.findViewById(R.id.cde);
            def = itemView.findViewById(R.id.def);
            joinEventButton = itemView.findViewById(R.id.joinEventButtonInJoinUser);
        }
    }

    private void removeItem(int position) {
        eventList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, eventList.size());
    }
}
