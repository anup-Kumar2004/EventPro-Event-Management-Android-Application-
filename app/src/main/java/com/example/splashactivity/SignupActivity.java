package com.example.splashactivity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//change

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class SignupActivity extends AppCompatActivity {

    private EditText signupName, signupEmail, signupPassword, signupReTypePassword;
    private FirebaseAuth auth;
    private Button registerButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        signupName = findViewById(R.id.register_name);
        signupEmail = findViewById(R.id.register_email);
        signupPassword = findViewById(R.id.register_type_password);
        signupReTypePassword = findViewById(R.id.register_retype_password);
        registerButton = findViewById(R.id.register_button);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = signupName.getText().toString();
                final String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();
                String reType_password = signupReTypePassword.getText().toString();

                // Check if any field is empty
                if (name.isEmpty() || email.isEmpty() || reType_password.isEmpty()) {
                    // Display error message for empty fields
                    Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if passwords match
                if (!password.equals(reType_password)) {
                    // Passwords don't match, show error
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if email is in valid format (ending with "@gmail.com")
                String emailPattern = "[a-zA-Z0-9._-]+@gmail\\.com";
                if (!email.matches(emailPattern)) {
                    // Display error message for invalid email format
                    Toast.makeText(SignupActivity.this, "Invalid email format. Please enter a valid Gmail address.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(password.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                } else{
                    auth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String uid = auth.getCurrentUser().getUid(); // Get user's UID
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid); // Reference to user's data in Firebase
                                userRef.child("name").setValue(name);

                                startActivity(new Intent(SignupActivity.this,ChoiceScreen.class));
                            }else {
                                Toast.makeText(SignupActivity.this, "Signup Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });


    }

    public void navigateToSignupScreen(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}