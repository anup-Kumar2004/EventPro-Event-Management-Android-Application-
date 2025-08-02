package com.example.splashactivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinJoinedEventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;
    private List<Event> eventList;
    private DatabaseReference databaseReference;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    public JoinJoinedEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_joined_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.joindRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(eventList);
        recyclerView.setAdapter(eventsAdapter);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        fetchEventsFromFirebase();
    }

    private void fetchEventsFromFirebase() {
        // Assuming `currentUser.getUid()` returns the current user ID
        String userId = currentUser.getUid();

        // Fetch event names from "AcceptedEvents"
        DatabaseReference acceptedEventsRef = databaseReference.child("AcceptedEvents").child(userId);
        acceptedEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> eventNameMap = new HashMap<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String eventName = dataSnapshot.getValue(String.class);
                    eventNameMap.put(eventName, eventName);
                }
                fetchEventDetails(eventNameMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void fetchEventDetails(Map<String, String> eventNameMap) {
        DatabaseReference eventDetailsRef = databaseReference.child("eventDetailsForHost");
        eventDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String eventName = dataSnapshot.child("eventName").getValue(String.class);
                    if (eventNameMap.containsKey(eventName)) {
                        Event event = new Event(
                                eventName,
                                dataSnapshot.child("fromDate").getValue(String.class),
                                dataSnapshot.child("toDate").getValue(String.class),
                                dataSnapshot.child("fromTime").getValue(String.class),
                                dataSnapshot.child("toTime").getValue(String.class),
                                dataSnapshot.child("eventDetails").getValue(String.class),
                                dataSnapshot.child("email").getValue(String.class)  // Fetch email field
                        );
                        eventList.add(event);
                    }
                }
                eventsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
