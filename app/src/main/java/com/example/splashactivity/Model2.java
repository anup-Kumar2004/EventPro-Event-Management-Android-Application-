package com.example.splashactivity;

public class Model2 {
    private String eventName;
    private String eventDate;
    private String eventDetails;

    public Model2() {
        // No-argument constructor required for Firebase
    }

    public Model2(String eventName, String eventDate, String eventDetails) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventDetails = eventDetails;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }
}
