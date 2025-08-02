package com.example.splashactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class typeEventName extends AppCompatActivity {

    EditText t1, t2, t3;
    FloatingActionButton floatingActionButton9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_event_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.navy_blue));
        }

        // Initialize EditText fields
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);

        // Get current user email and set it to t1 EditText
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserEmail = currentUser.getEmail();
            if (currentUserEmail != null) {
                t1.setText(currentUserEmail);
            }
        }

        floatingActionButton9 = findViewById(R.id.floatingActionButton9);
        floatingActionButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process();
            }
        });
    }

    public void process() {
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);

        String email = t1.getText().toString().trim();
        String rawEventName = t2.getText().toString().trim();
        String eventDetails = t3.getText().toString().trim();

        // Sanitize event name for Firebase key usage
        String eventName = rawEventName.replaceAll("[.#$\\[\\]/]", "_");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String currentUserEmail = currentUser.getEmail();
            if (!email.equals(currentUserEmail)) {
                Toast.makeText(getApplicationContext(), "Please enter your registered email", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            return;
        }

        if (rawEventName.isEmpty() || eventDetails.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase db2 = FirebaseDatabase.getInstance();
        DatabaseReference node = db2.getReference("eventDetailsForHost");

        node.child(eventName).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "An event with the same name already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    // Store original event name in Firebase
                    EventDataStoring obj1 = new EventDataStoring(email, rawEventName, eventDetails);

                    node.child(eventName).setValue(obj1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    t1.setText("");
                                    t2.setText("");
                                    t3.setText("");
                                    Toast.makeText(getApplicationContext(), "Details inserted", Toast.LENGTH_SHORT).show();

                                    Intent sendIntent = new Intent(typeEventName.this, eventDetailsPart2.class);
                                    sendIntent.putExtra("eventName", rawEventName);
                                    startActivity(sendIntent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed to insert details", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to check event name", Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(typeEventName.this, HostEventHomeScreen.class);
        startActivity(intent);
        finish(); // Optional: finish the current activity if you don't want it in the back stack
    }
}
