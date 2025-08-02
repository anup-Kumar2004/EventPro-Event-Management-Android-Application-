package com.example.splashactivity;

public class HelperClass2 {tring name, email, password, reType_password, eventKaNaam;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReType_password() {
        return reType_password;
    }



    public void setReType_password(String reType_password) {
        this.reType_password = reType_password;
    }

    public String geteventkanaam() {return eventKaNaam;}

    public void seteventkanaam(String eventKaNaam){this.eventKaNaam = eventKaNaam;}

    public HelperClass(String name, String email, String password, String reType_password , String eventKaNaam) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.reType_password = reType_password;
        this.eventKaNaam = eventKaNaam;
    }

    public HelperClass() {
    }
}
