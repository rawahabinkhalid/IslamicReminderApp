package com.example.habitreminder.Data;

public class ReminderData {
    private  String  Name;
    private String TypeOfReminder;
    private String Time;

    public ReminderData() {
    }

    public ReminderData(String name, String typeOfReminder, String time) {
        Name = name;
        TypeOfReminder = typeOfReminder;
        Time = time;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTypeOfReminder() {
        return TypeOfReminder;
    }

    public void setTypeOfReminder(String typeOfReminder) {
        TypeOfReminder = typeOfReminder;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}