package com.example.splashactivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class eventDetailsPart2 extends AppCompatActivity {

    TextView dateformat ;
    TextView textView44443;
    TextView timeformatID2;
    TextView timeformat ;

    int year;
    int month;
    int day;
    int hour;
    int minute;

    DatabaseReference eventDetailsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_part2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.navy_blue));
        }

        dateformat = findViewById(R.id.dateformatID);
        textView44443 = findViewById(R.id.textView44443);
        timeformatID2 = findViewById(R.id.timeformatID2);
        timeformat = findViewById(R.id.timeformatID);

        eventDetailsRef = FirebaseDatabase.getInstance().getReference().child("eventDetailsForHost");


        Calendar calendar = Calendar.getInstance();

        // Set OnClickListener for dateformat TextView
        dateformat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(eventDetailsPart2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Use 'view' parameter to get the selected date
                        calendar.set(year, month, dayOfMonth);
                        dateformat.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });




        // Set OnClickListener for timeformat TextView
        timeformat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                // Create a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(eventDetailsPart2.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        timeformat.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                    }

                }, hour, minute, true);

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        textView44443.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(eventDetailsPart2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Use 'view' parameter to get the selected date
                        calendar.set(year, month, dayOfMonth);
                        textView44443.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        // Set OnClickListener for timeformatID2 TextView
        timeformatID2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                // Create a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(eventDetailsPart2.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        timeformatID2.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                    }

                }, hour, minute, true);

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });
    }

    public void process2(View view) {
        String fromDate = dateformat.getText().toString();
        String fromTime = timeformat.getText().toString();
        String toDate = textView44443.getText().toString();
        String toTime = timeformatID2.getText().toString();

        // Check if any field is empty or contains the hint text
        if (fromDate.equals("Select Date") || fromDate.trim().isEmpty() ||
                fromTime.equals("Select Time") || fromTime.trim().isEmpty() ||
                toDate.equals("Select Date") || toDate.trim().isEmpty() ||
                toTime.equals("Select Time") || toTime.trim().isEmpty()) {
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the original (unsanitized) event name from Intent
        String rawEventName = getIntent().getStringExtra("eventName");

        if (rawEventName == null || rawEventName.isEmpty()) {
            Toast.makeText(this, "Event name not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sanitize it for Firebase key usage (same as in previous file)
        String eventName = rawEventName.replaceAll("[.#$\\[\\]/]", "_");

        // Push data to Firebase under the sanitized eventName node
        DatabaseReference eventRef = eventDetailsRef.child(eventName);
        eventRef.child("fromDate").setValue(fromDate);
        eventRef.child("fromTime").setValue(fromTime);
        eventRef.child("toDate").setValue(toDate);
        eventRef.child("toTime").setValue(toTime)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(eventDetailsPart2.this, "Date and time details saved to Firebase for " + rawEventName, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(eventDetailsPart2.this, HostEventHomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(eventDetailsPart2.this, "Failed to save date and time details", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
    }


}
