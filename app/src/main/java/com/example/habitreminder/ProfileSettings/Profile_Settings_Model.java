package com.example.habitreminder.ProfileSettings;

public class Profile_Settings_Model {
    String Name;
    String Description;
    String Status;
    String Type;

    public Profile_Settings_Model(String name, String description, String status, String type) {
        Name = name;
        Description = description;
        Status = status;
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
