package com.example.habitreminder.Data;

public class Data {
    private String UID;
    private String Name;
    private String Email;
    private String UserType;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Data(){

    }

    public Data(String UID, String name, String email, String userType) {
        this.UID = UID;
        this.Name = name;
        this.Email = email;
        this.UserType = userType;
    }
}
