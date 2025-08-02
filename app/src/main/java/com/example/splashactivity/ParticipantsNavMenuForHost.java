package com.example.splashactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsNavMenuForHost extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<EventModel> eventList;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    MaterialToolbar materialToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_nav_menu_for_host);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.navy_blue));
        }


        materialToolbar = findViewById(R.id.materialToolbar);
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHostEventHomeScreen();
            }
        });




        recyclerView = findViewById(R.id.RecyclerViewForParticipants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        adapter = new EventAdapter(eventList, this);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        fetchEventNames();


    }

    private void fetchEventNames() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) return;
        String userEmail = currentUser.getEmail();

        databaseReference.child("eventDetailsForHost")
                .orderByChild("email").equalTo(userEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> eventNames = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String eventName = snapshot.child("eventName").getValue(String.class);
                            if (eventName != null) {
                                eventNames.add(eventName);
                            }
                        }
                        fetchParticipants(eventNames);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }

    private void fetchParticipants(List<String> eventNames) {
        if (eventNames.isEmpty()) {
            showToast();
            return;
        }

        int eventsToFetch = eventNames.size();
        final int[] eventsFetched = {0};

        for (String eventName : eventNames) {
            List<ParticipantModel> participants = new ArrayList<>();
            EventModel event = new EventModel(eventName, participants);

            databaseReference.child("AcceptedEvents").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean hasParticipants = false;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                            String event = eventSnapshot.getValue(String.class);
                            if (eventName.equals(event)) {
                                String uid = snapshot.getKey();
                                if (uid != null) {
                                    hasParticipants = true;
                                    fetchUserName(uid, participants);
                                }
                            }
                        }
                    }

                    if (hasParticipants) {
                        eventList.add(event);
                        adapter.notifyDataSetChanged();
                    }

                    eventsFetched[0]++;
                    if (eventsFetched[0] == eventsToFetch) {
                        if (eventList.isEmpty()) {
                            showToast();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });
        }
    }

    private void fetchUserName(String uid, List<ParticipantModel> participants) {
        databaseReference.child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        if (name != null) {
                            participants.add(new ParticipantModel(name));
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }

    private void navigateToHostEventHomeScreen() {
        Intent intent = new Intent(this, HostEventHomeScreen.class);
        startActivity(intent);
        finish();
    }

    private void showToast() {
        Toast.makeText(this, "No participants data available.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ParticipantsNavMenuForHost.this, HostEventHomeScreen.class);
        startActivity(intent);
        finish(); // Optional: finish the current activity if you don't want it in the back stack
    }

}
