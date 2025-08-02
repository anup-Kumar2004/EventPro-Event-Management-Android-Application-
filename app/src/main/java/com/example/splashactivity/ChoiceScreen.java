package com.example.splashactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChoiceScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choice_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Find the CardViews
        androidx.cardview.widget.CardView hostEventCard = findViewById(R.id.host_event_card);
        androidx.cardview.widget.CardView joinEventCard = findViewById(R.id.join_event_card);

        // Set OnClickListener for host event card
        hostEventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity for hosting an event
                Intent intent = new Intent(ChoiceScreen.this, HostEventHomeScreen.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for join event card
        joinEventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity for joining an event
                Intent intent = new Intent(ChoiceScreen.this, JoinEventHomeScreen.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please Log Out if you want to navigate to the signup screen", Toast.LENGTH_SHORT).show();
    }
}
