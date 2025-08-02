package com.example.splashactivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.ViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position, @NonNull MainModel mainModel) {
        holder.eventNamingInRecycleView.setText(mainModel.getEventName());
        holder.eventDateInRecycleView.setText(mainModel.getFromDate());
        holder.eventDetailsInRecycleView.setText(mainModel.getEventDetails());

        holder.deletingEventButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Do you want to delete this item?")
                    .setMessage("Once you delete, it will be permanently deleted from our database")
                    .setCancelable(false)
                    .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("DELETE", (dialog, which) -> {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference().child("eventDetailsForHost")
                                .child(Objects.requireNonNull(getRef(holder.getBindingAdapterPosition()).getKey()));

                        reference.removeValue().addOnSuccessListener(aVoid -> {
                            Toast.makeText(v.getContext(), "Deleted Permanently", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    });
            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(dialog1 -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(v.getContext().getResources().getColor(android.R.color.holo_red_dark));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(v.getContext().getResources().getColor(android.R.color.holo_blue_dark));
            });
            dialog.show();
        });

        holder.editEventButton.setOnClickListener(v -> showEditDialog(v, holder.getBindingAdapterPosition()));
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(itemView);
    }

    private void showEditDialog(View v, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = LayoutInflater.from(v.getContext());
        View dialogView = inflater.inflate(R.layout.dialog_edit_event, null);
        builder.setView(dialogView);

        EditText emailEditText = dialogView.findViewById(R.id.t1);
        EditText eventNameEditText = dialogView.findViewById(R.id.t2);
        EditText eventDetailsEditText = dialogView.findViewById(R.id.t3);
        TextView fromDateTextView = dialogView.findViewById(R.id.t4);
        TextView fromTimeTextView = dialogView.findViewById(R.id.t5);
        TextView toDateTextView = dialogView.findViewById(R.id.t6);
        TextView toTimeTextView = dialogView.findViewById(R.id.t7);

        MainModel mainModel = getItem(position);
        emailEditText.setText(mainModel.getEmail());
        eventNameEditText.setText(mainModel.getEventName());
        eventDetailsEditText.setText(mainModel.getEventDetails());
        fromDateTextView.setText(mainModel.getFromDate());
        fromTimeTextView.setText(mainModel.getFromTime());
        toDateTextView.setText(mainModel.getToDate());
        toTimeTextView.setText(mainModel.getToTime());

        fromDateTextView.setOnClickListener(view -> showDatePickerDialog(view.getContext(), fromDateTextView));
        fromTimeTextView.setOnClickListener(view -> showTimePickerDialog(view.getContext(), fromTimeTextView));
        toDateTextView.setOnClickListener(view -> showDatePickerDialog(view.getContext(), toDateTextView));
        toTimeTextView.setOnClickListener(view -> showTimePickerDialog(view.getContext(), toTimeTextView));

        builder.setPositiveButton("UPDATE", (dialog, which) -> {
            String email = emailEditText.getText().toString().trim();
            String updatedEventName = eventNameEditText.getText().toString().trim();
            String eventDetails = eventDetailsEditText.getText().toString().trim();
            String fromDate = fromDateTextView.getText().toString().trim();
            String fromTime = fromTimeTextView.getText().toString().trim();
            String toDate = toDateTextView.getText().toString().trim();
            String toTime = toTimeTextView.getText().toString().trim();

            MainModel updatedModel = new MainModel(updatedEventName, eventDetails, fromDate, email);
            updatedModel.setFromTime(fromTime);
            updatedModel.setToDate(toDate);
            updatedModel.setToTime(toTime);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseRef = database.getReference().child("eventDetailsForHost");

            String oldEventKey = Objects.requireNonNull(getRef(position).getKey());
            String oldSanitizedKey = oldEventKey.replaceAll("[.#$\\[\\]]", "_");
            String newSanitizedKey = updatedEventName.replaceAll("[.#$\\[\\]]", "_");

            if (!oldSanitizedKey.equals(newSanitizedKey)) {
                databaseRef.child(oldSanitizedKey).removeValue().addOnSuccessListener(unused -> {
                    databaseRef.child(newSanitizedKey).setValue(updatedModel).addOnSuccessListener(unused2 -> {
                        Toast.makeText(v.getContext(), "Event renamed and updated successfully", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(v.getContext(), "Failed to create new event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    Toast.makeText(v.getContext(), "Failed to delete old event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                databaseRef.child(oldSanitizedKey).setValue(updatedModel).addOnSuccessListener(unused -> {
                    Toast.makeText(v.getContext(), "Event updated successfully", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(v.getContext(), "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(v.getContext().getResources().getColor(android.R.color.holo_red_light));
    }

    private void showDatePickerDialog(Context context, TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedDay + " " + getMonthName(selectedMonth) + " " + selectedYear;
            textView.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(Context context, TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, selectedHour, selectedMinute) -> {
            String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
            textView.setText(selectedTime);
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private String getMonthName(int month) {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month];
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventNamingInRecycleView, eventDetailsInRecycleView, eventDateInRecycleView;
        ImageView deletingEventButton , editEventButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNamingInRecycleView = itemView.findViewById(R.id.eventNamingInRecycleView);
            eventDetailsInRecycleView = itemView.findViewById(R.id.eventDetailsInRecycleView);
            eventDateInRecycleView = itemView.findViewById(R.id.eventDateInRecycleView);
            deletingEventButton = itemView.findViewById(R.id.deletingEventButton);
            editEventButton = itemView.findViewById(R.id.editEventButton);
        }
    }
}
