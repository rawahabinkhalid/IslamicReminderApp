package com.example.habitreminder.Data;

public class AddFrequencyData {
    private String advance;
    private String intermediate;
    private String beginner;
    private String habitName;
    private String subHabitsName;


    public AddFrequencyData() {
    }

    public AddFrequencyData(String advance, String intermediate, String beginner, String habitName, String subHabitsName) {
        this.advance = advance;
        this.intermediate = intermediate;
        this.beginner = beginner;
        this.habitName = habitName;
        this.subHabitsName = subHabitsName;
    }

    public String getAdvance() {
        return advance;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public String getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(String intermediate) {
        this.intermediate = intermediate;
    }

    public String getBeginner() {
        return beginner;
    }

    public void setBeginner(String beginner) {
        this.beginner = beginner;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public String getSubHabitsName() {
        return subHabitsName;
    }

    public void setSubHabitsName(String subHabitsName) {
        this.subHabitsName = subHabitsName;
    }
}
