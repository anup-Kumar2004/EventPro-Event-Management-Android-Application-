package com.example.splashactivity;

public class EventDataStoring {

    String email,eventName,eventDetails ;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public EventDataStoring(String email, String eventName, String eventDetails) {
        this.email = email;
        this.eventName = eventName;
        this.eventDetails = eventDetails;
    }
}
