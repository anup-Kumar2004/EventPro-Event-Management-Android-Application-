package com.example.splashactivity;

public class MainModel {

    String eventName;
    String eventDetails;
    String fromDate;
    String fromTime;
    String toDate;
    String toTime;
    String email;

    // Constructor with all parameters
    public MainModel(String eventName, String eventDetails, String fromDate, String fromTime, String toDate, String toTime, String email) {
        this.eventName = eventName;
        this.eventDetails = eventDetails;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.email = email;
    }

    // Default constructor
    public MainModel() {
    }

    public MainModel(String eventName, String eventDetails, String fromDate, String email) {
        this.eventName = eventName;
        this.eventDetails = eventDetails;
        this.fromDate = fromDate;

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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
