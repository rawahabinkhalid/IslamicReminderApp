package com.example.habitreminder.Data;

import java.util.List;

public class CalendarData {
    private List<String> end_date;
    private List<String> title;
    private List<String> start_date;
    private List<String> description;

    public CalendarData() {
    }

    public List<String> getEnd_date() {
        return end_date;
    }

    public void setEnd_date(List<String> end_date) {
        this.end_date = end_date;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getStart_date() {
        return start_date;
    }

    public void setStart_date(List<String> start_date) {
        this.start_date = start_date;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }
}
