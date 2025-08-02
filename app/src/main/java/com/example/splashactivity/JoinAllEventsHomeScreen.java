package com.example.splashactivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JoinAllEventsHomeScreen extends Fragment {

    RecyclerView recyclerView2;
    MainAdapter2 mainAdapter2;
    FirebaseAuth auth;
    List<String> acceptedEvents;
    String currentUserEmail;

    public JoinAllEventsHomeScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_all_events_home_screen, container, false);


        auth = FirebaseAuth.getInstance();
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));

        acceptedEvents = new ArrayList<>();
        currentUserEmail = auth.getCurrentUser().getEmail();

        fetchAcceptedEvents();

        return view;
    }

    private void fetchAcceptedEvents() {
        String uid = auth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("AcceptedEvents").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String eventName = snapshot.getValue(String.class);
                            acceptedEvents.add(eventName);
                        }
                        fetchEventDetails();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle potential errors
                    }
                });
    }

    private void fetchEventDetails() {
        Query query = FirebaseDatabase.getInstance().getReference().child("eventDetailsForHost");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MainModel> filteredEvents = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MainModel event = snapshot.getValue(MainModel.class);
                    if (event != null && !acceptedEvents.contains(event.getEventName()) && !event.getEmail().equals(currentUserEmail)) {
                        filteredEvents.add(event);
                    }
                }
                if (filteredEvents.isEmpty()) {
                    Toast.makeText(getActivity(), "No new events data available right now ", Toast.LENGTH_LONG).show();
                } else {
                    setupRecyclerView(filteredEvents);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    private void setupRecyclerView(List<MainModel> filteredEvents) {
        mainAdapter2 = new MainAdapter2(filteredEvents);
        recyclerView2.setAdapter(mainAdapter2);
        mainAdapter2.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
