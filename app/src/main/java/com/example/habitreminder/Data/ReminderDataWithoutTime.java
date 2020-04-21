package com.example.habitreminder.Data;

public class ReminderDataWithoutTime {
    private  String  Name;
    private String TypeOfReminder;

    public ReminderDataWithoutTime(String name, String typeOfReminder) {
        Name = name;
        TypeOfReminder = typeOfReminder;
    }

    public ReminderDataWithoutTime() {
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
}
