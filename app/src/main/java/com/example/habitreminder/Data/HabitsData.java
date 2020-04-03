package com.example.habitreminder.Data;

import java.util.List;

public class HabitsData {
    private String Name;
    private String Notification;
    private List<SubHabits> subHabits;

    public HabitsData() {
    }

    public HabitsData(String name, String notification, List<SubHabits> subHabits) {
        Name = name;
        Notification = notification;
        this.subHabits = subHabits;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public List<SubHabits> getSubHabits() {
        return subHabits;
    }

    public void setSubHabits(List<SubHabits> subHabits) {
        this.subHabits = subHabits;
    }
}
