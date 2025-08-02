package com.example.splashactivity;

public class EventDataStoring3 {

    String emailOfInvitees , emailOfHost;

    public String getEmailOfHost() {
        return emailOfHost;
    }

    public void setEmailOfHost(String emailOfHost) {
        this.emailOfHost = emailOfHost;
    }

    public EventDataStoring3(String emailOfInvitees) {
        this.emailOfInvitees = emailOfInvitees;
    }

    public String getEmailOfInvitees() {
        return emailOfInvitees;
    }

    public void setEmailOfInvitees(String emailOfInvitees , String emailOfHost) {
        this.emailOfInvitees = emailOfInvitees;
        this.emailOfHost = emailOfHost;
    }
}
