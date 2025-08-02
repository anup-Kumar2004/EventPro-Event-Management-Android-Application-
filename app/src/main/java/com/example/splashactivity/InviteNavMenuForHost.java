package com.example.splashactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InviteNavMenuForHost extends AppCompatActivity {

    EditText m1;
    Spinner spinner;
    ArrayList<String> spinnerList;
    DatabaseReference spinnerRef;
    ArrayAdapter<String> adapterSpinner;

    Button b;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite_nav_menu_for_host);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.navy_blue));
        }

        mAuth = FirebaseAuth.getInstance();

        b = findViewById(R.id.inviteKroButton);
        spinner = findViewById(R.id.spinnerId);
        spinnerRef = FirebaseDatabase.getInstance().getReference("eventDetailsForHost");

        spinnerList = new ArrayList<>();
        adapterSpinner = new ArrayAdapter<>(InviteNavMenuForHost.this, android.R.layout.simple_spinner_item, spinnerList);
        adapterSpinner.setDropDownViewResource(R.layout.spinner_dropdown_bg);
        spinner.setAdapter(adapterSpinner);

        float offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, getResources().getDisplayMetrics());
        spinner.setDropDownVerticalOffset((int) offset);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process();
            }
        });

        showData();
        checkAndRemoveOrphanedEvents();
    }

    private void showData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        String userEmail = currentUser.getEmail();

        spinnerRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String eventName = eventSnapshot.child("eventName").getValue(String.class);
                    if (eventName != null) {
                        spinnerList.add(eventName);
                    }
                }
                adapterSpinner.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void process() {
        m1 = findViewById(R.id.emailForInviteScreen);
        String emailOfInvitees = m1.getText().toString().trim();
        String selectedEvent = spinner.getSelectedItem().toString();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        String uid = currentUser.getUid();
        String emailOfHost = currentUser.getEmail();

        if (emailOfInvitees.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please provide receiver's email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (emailOfInvitees.equalsIgnoreCase(emailOfHost)) {
            Toast.makeText(getApplicationContext(), "You cannot invite yourself.", Toast.LENGTH_SHORT).show();
            return;
        }

        String encodedEmailOfInvitees = encodeEmail(emailOfInvitees);
        String encodedEmailOfHost = encodeEmail(emailOfHost);
        String combinedEmailKey = encodedEmailOfInvitees + " | " + encodedEmailOfHost;

        String sanitizedEventKey = selectedEvent.replaceAll("[.#$\\[\\]]", "_");

        DatabaseReference eventDetailsRef = FirebaseDatabase.getInstance().getReference("eventDetailsForHost").child(sanitizedEventKey);
        eventDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String eventName = snapshot.child("eventName").getValue(String.class);
                    String eventDate = snapshot.child("fromDate").getValue(String.class);
                    String eventDetails = snapshot.child("eventDetails").getValue(String.class);

                    if (selectedEvent.equals(eventName)) {
                        DatabaseReference node = FirebaseDatabase.getInstance().getReference("HostNeJinkoInviteKraHaiUnkaData")
                                .child(combinedEmailKey)
                                .child(sanitizedEventKey);

                        node.child("eventName").setValue(selectedEvent);
                        node.child("eventDate").setValue(eventDate);
                        node.child("eventDetails").setValue(eventDetails);
                        node.child("uidOfHost").setValue(uid)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        m1.setText("");
                                        Toast.makeText(getApplicationContext(), "Invite Sent", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to invite", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "Selected event does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Event details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to retrieve event details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAndRemoveOrphanedEvents() {
        DatabaseReference hostNeJinkoInviteKraHaiUnkaDataRef = FirebaseDatabase.getInstance().getReference("HostNeJinkoInviteKraHaiUnkaData");

        hostNeJinkoInviteKraHaiUnkaDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot inviteSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot eventSnapshot : inviteSnapshot.getChildren()) {
                        String eventName = eventSnapshot.child("eventName").getValue(String.class);

                        if (eventName != null) {
                            checkEventInEventDetailsForHost(eventName, inviteSnapshot.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void checkEventInEventDetailsForHost(String eventName, String combinedEmailKey) {
        DatabaseReference eventDetailsForHostRef = FirebaseDatabase.getInstance().getReference("eventDetailsForHost");
        eventDetailsForHostRef.orderByChild("eventName").equalTo(eventName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("HostNeJinkoInviteKraHaiUnkaData")
                            .child(combinedEmailKey)
                            .child(eventName.replaceAll("[.#$\\[\\]]", "_"));
                    eventRef.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private String encodeEmail(String email) {
        return email.replace(".", ",");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HostEventHomeScreen.class));
        finish();
    }
}
