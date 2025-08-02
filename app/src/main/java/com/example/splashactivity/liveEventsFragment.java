package com.example.splashactivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splashactivity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class liveEventsFragment extends Fragment {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    FirebaseAuth auth;
    FloatingActionButton fab;

    public liveEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_events, container, false);

        auth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.floatingActionButton2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get currently signed-in user's email
        String currentUserEmail = auth.getCurrentUser().getEmail();

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("eventDetailsForHost")
                        .orderByChild("email").equalTo(currentUserEmail), MainModel.class)
                .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        // Set OnClickListener for the floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define the intent to navigate to another screen
                Intent intent = new Intent(getActivity(), typeEventName.class);
                startActivity(intent); // Start the activity
            }
        });

        // Add a scroll listener to the RecyclerView
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.isShown()) {
                    // Scrolling down
                    fab.hide();
                } else if (dy < 0 && !fab.isShown()) {
                    // Scrolling up
                    fab.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        mainAdapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        mainAdapter.stopListening();
        super.onStop();
    }
}
