package com.example.habitreminder.Data;

public class ReminderData {
    private  String  habit_title;
    private String time;

    public ReminderData() {
    }

    public ReminderData(String habit_title, String time) {
        this.habit_title = habit_title;
        this.time = time;
    }

    public String getHabit_title() {
        return habit_title;
    }

    public void setHabit_title(String habit_title) {
        this.habit_title = habit_title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}