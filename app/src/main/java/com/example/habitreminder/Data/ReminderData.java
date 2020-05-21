package com.example.habitreminder.Data;

public class ReminderData {
    private  String  Name;
    private String TypeOfReminder;
    private String Time;
    private String DefaultDate;

    public ReminderData() {
    }

    public ReminderData(String name, String typeOfReminder, String time, String defaultDate) {
        Name = name;
        TypeOfReminder = typeOfReminder;
        Time = time;
        DefaultDate = defaultDate;
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

    public String getDefaultDate() {
        return DefaultDate;
    }

    public void setDefaultDate(String defaultDate) {
        DefaultDate = defaultDate;
    }
}