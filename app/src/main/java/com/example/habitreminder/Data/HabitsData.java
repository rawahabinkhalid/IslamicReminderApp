package com.example.habitreminder.Data;

import java.util.List;

public class HabitsData {
    private String Key;
    private String Name;
    private String Notification;
    private List<SubHabits> subHabits;

    public HabitsData(String key, String name, String notification, List<SubHabits> subHabits) {
        Key = key;
        Name = name;
        Notification = notification;
        this.subHabits = subHabits;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
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
