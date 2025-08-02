package com.example.splashactivity;

import java.util.List;

public class EventModel {
    private String eventName;
    private List<ParticipantModel> participants;

    public EventModel() {
    }

    public EventModel(String eventName, List<ParticipantModel> participants) {
        this.eventName = eventName;
        this.participants = participants;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<ParticipantModel> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantModel> participants) {
        this.participants = participants;
    }
}
