package com.example.splashactivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JoinInviteEventsFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private DatabaseReference acceptedEventsReference;
    private DatabaseReference declinedEventsReference;
    private RecyclerView recyclerView3;
    private Model2Adapter model2Adapter;
    private List<Model2> eventList;
    private Set<String> processedEventNames;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_invite_events, container, false);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        recyclerView3 = view.findViewById(R.id.recyclerViewForInviteCameFromHost);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventList = new ArrayList<>();
        processedEventNames = new HashSet<>();

        if (currentUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("HostNeJinkoInviteKraHaiUnkaData");
            acceptedEventsReference = FirebaseDatabase.getInstance().getReference("AcceptedEvents").child(currentUser.getUid());
            declinedEventsReference = FirebaseDatabase.getInstance().getReference("DeclinedEvents").child(currentUser.getUid());

            // Fetch accepted events first
            acceptedEventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot acceptedEventsSnapshot) {
                    for (DataSnapshot eventSnapshot : acceptedEventsSnapshot.getChildren()) {
                        String eventName = eventSnapshot.getValue(String.class);
                        processedEventNames.add(eventName);
                    }

                    // Fetch declined events next
                    declinedEventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot declinedEventsSnapshot) {
                            for (DataSnapshot eventSnapshot : declinedEventsSnapshot.getChildren()) {
                                String eventName = eventSnapshot.getValue(String.class);
                                processedEventNames.add(eventName);
                            }

                            // Now fetch events from HostNeJinkoInviteKraHaiUnkaData
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    eventList.clear(); // Clear the list before updating

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String key = snapshot.getKey();
                                        String emailPart = key.split(" \\| ")[0]; // Split and get the first part
                                        String formattedEmail = convertCommaToDot(emailPart);

                                        if (formattedEmail.equals(currentUser.getEmail())) {
                                            for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                                                Model2 event = eventSnapshot.getValue(Model2.class);
                                                if (event != null && !processedEventNames.contains(event.getEventName())) {
                                                    eventList.add(event); // Add each event to the list
                                                }
                                            }
                                        }
                                    }

                                    // Check if the event list is empty and show a toast message
                                    if (eventList.isEmpty()) {
                                        Toast.makeText(getContext(), "No new invited events data.", Toast.LENGTH_SHORT).show();
                                    }

                                    // Update the adapter with the new list
                                    model2Adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("JoinInviteEvents", "Database error: " + databaseError.getMessage());
                                }
                            });

                            // Initialize the adapter with the event list and context
                            model2Adapter = new Model2Adapter(eventList, getContext());
                            recyclerView3.setAdapter(model2Adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("JoinInviteEvents", "Database error: " + databaseError.getMessage());
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("JoinInviteEvents", "Database error: " + databaseError.getMessage());
                }
            });
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (model2Adapter != null) {
            model2Adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (model2Adapter != null) {
            model2Adapter.stopListening();
        }
    }

    private String convertCommaToDot(String email) {
        return email.replace(',', '.');
    }
}
