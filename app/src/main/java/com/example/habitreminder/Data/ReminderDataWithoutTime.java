package com.example.habitreminder.Data;

public class ReminderDataWithoutTime {
    private  String  Name;
    private String TypeOfReminder;
    private String DefaultDate;

    public ReminderDataWithoutTime(String name, String typeOfReminder, String defaultDate) {
        Name = name;
        TypeOfReminder = typeOfReminder;
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

    public String getDefaultDate() {
        return DefaultDate;
    }

    public void setDefaultDate(String defaultDate) {
        DefaultDate = defaultDate;
    }
}
