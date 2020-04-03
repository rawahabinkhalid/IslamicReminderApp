package com.example.habitreminder.Data;

public class JournalData extends ReminderData {

    private String jDescription;
    private String dateJournal;
    private String timestamp;


    public JournalData() {
    }

    public JournalData(String jDescriptions, String dateJournal, String timestamp) {
        this.jDescription=jDescriptions;
        this.dateJournal=dateJournal;
        this.timestamp = timestamp;
        }

    public String getjDescription() {
        return jDescription;
    }

    public void setjDescription(String jDescription) {
        this.jDescription = jDescription;
    }

    public String getDateJournal() {
        return dateJournal;
    }

    public void setDateJournal(String dateJournal) {
        this.dateJournal = dateJournal;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}