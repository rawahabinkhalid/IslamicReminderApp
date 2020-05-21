package com.example.habitreminder.ProfileSettings;

public class Profile_Settings_Model {
    String Name;
    String Description;
    Boolean Status;
    String Type;

    public Profile_Settings_Model(String name, String description, Boolean status, String type) {
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

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
