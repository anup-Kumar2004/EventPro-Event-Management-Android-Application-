package com.example.splashactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.SharedPreferences;

public class userNameForGoogleSignInOptions extends AppCompatActivity {


    private EditText editText;
    private ImageView nextButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name_for_google_sign_in_options);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        editText = findViewById(R.id.editText);
        nextButton = findViewById(R.id.nextButton);

        // Set click listener on the nextButton
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserName();
            }
        });
    }

    private void saveUserName() {
        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Get UID and the name entered by the user
            String uid = currentUser.getUid();
            String name = editText.getText().toString().trim();

            // Validate the name
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the name under the user's UID
            mDatabase.child("users").child(uid).child("name").setValue(name)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Navigate to ChoiceScreen activity
                            Intent intent = new Intent(userNameForGoogleSignInOptions.this, ChoiceScreen.class);
                            startActivity(intent);
                            finish(); // Finish the current activity
                        } else {
                            // Handle failure
                            Log.e("FirebaseError", "Failed to save user name", task.getException());
                            Toast.makeText(this, "Failed to save name, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
