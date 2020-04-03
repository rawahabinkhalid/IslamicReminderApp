package com.example.habitreminder.Data;

public class SubHabits {
    private int Frequency_Advanced;
    private int Frequency_Beginner;
    private int Frequency_Intermediate;
    private String Name;
    private String Notification;

    public SubHabits() {
    }

    public SubHabits(int frequency_Advanced, int frequency_Beginner, int frequency_Intermediate, String name, String notification) {
        Frequency_Advanced = frequency_Advanced;
        Frequency_Beginner = frequency_Beginner;
        Frequency_Intermediate = frequency_Intermediate;
        Name = name;
        Notification = notification;
    }

    public int getFrequency_Advanced() {
        return Frequency_Advanced;
    }

    public void setFrequency_Advanced(int frequency_Advanced) {
        Frequency_Advanced = frequency_Advanced;
    }

    public int getFrequency_Beginner() {
        return Frequency_Beginner;
    }

    public void setFrequency_Beginner(int frequency_Beginner) {
        Frequency_Beginner = frequency_Beginner;
    }

    public int getFrequency_Intermediate() {
        return Frequency_Intermediate;
    }

    public void setFrequency_Intermediate(int frequency_Intermediate) {
        Frequency_Intermediate = frequency_Intermediate;
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
}
