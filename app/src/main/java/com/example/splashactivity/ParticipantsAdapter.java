package com.example.splashactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantViewHolder> {

    private List<ParticipantModel> participantsList;

    public ParticipantsAdapter(List<ParticipantModel> participantsList) {
        this.participantsList = participantsList;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participants_item, parent, false);
        return new ParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        ParticipantModel participant = participantsList.get(position);
        holder.participantName.setText(participant.getName());
    }

    @Override
    public int getItemCount() {
        return participantsList.size();
    }

    static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        TextView participantName;

        ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            participantName = itemView.findViewById(R.id.participantName);
        }
    }
}
