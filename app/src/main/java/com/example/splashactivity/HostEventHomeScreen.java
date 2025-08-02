package com.example.splashactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.appbar.MaterialToolbar;

public class HostEventHomeScreen extends AppCompatActivity {

    DrawerLayout drawerLayout;
    MaterialToolbar materialToolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_event_home_screen);

        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.navy_blue));
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        Button btnLive = findViewById(R.id.btnLive);
        btnLive.setOnClickListener(v -> fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, liveEventsFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit());

        drawerLayout = findViewById(R.id.drawerLayout);
        materialToolbar = findViewById(R.id.materialToolbar);
        frameLayout = findViewById(R.id.frameLayout);
        navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                HostEventHomeScreen.this, drawerLayout, materialToolbar, R.string.drawer_close, R.string.drawer_open);
        drawerLayout.addDrawerListener(toggle);

        // Initialize FirebaseAuth and get the current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Initialize FirebaseDatabase and get the reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        // Initialize TextViews from the nav_header.xml
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.userName);
        TextView userEmailTextView = headerView.findViewById(R.id.userEmail);

        // Set the email of the logged-in user
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            userEmailTextView.setText(userEmail);

            // Get the user's UID
            String uid = currentUser.getUid();

            // Query the user's name from the database
            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Get the name field from the snapshot
                        String name = dataSnapshot.child("name").getValue(String.class);
                        userNameTextView.setText(name);
                    } else {
                        // Handle case where user data doesn't exist
                        userNameTextView.setText("Unknown User");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors
                    Toast.makeText(HostEventHomeScreen.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Initialize GoogleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.eventsNavMenu) {
                // No need to do anything, stay on the same screen
            } else if (id == R.id.participantsNavMenu) {
                // Redirect to HostParticipantsNavMenu activity
                startActivity(new Intent(HostEventHomeScreen.this, ParticipantsNavMenuForHost.class));
            } else if (id == R.id.inviteNavMenu) {
                // Redirect to HostInviteNavMenu activity
                startActivity(new Intent(HostEventHomeScreen.this, InviteNavMenuForHost.class));
            } else if (id == R.id.joinAEventNavMenu) {
                // Redirect to ChoiceScreen activity
                startActivity(new Intent(HostEventHomeScreen.this, ChoiceScreen.class));
            } else if (id == R.id.logoutNavMenu) {
                // Log out from Firebase and Google Sign-In
                FirebaseAuth.getInstance().signOut();
                googleSignInClient.signOut().addOnCompleteListener(task -> {

                    // Redirect to the login screen
                    Intent intent = new Intent(HostEventHomeScreen.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(HostEventHomeScreen.this, "LOGGED OUT", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
            // Close the drawer after handling the click
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HostEventHomeScreen.this, ChoiceScreen.class);
        startActivity(intent);
        finish(); // Optional: finish the current activity if you don't want it in the back stack
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}
