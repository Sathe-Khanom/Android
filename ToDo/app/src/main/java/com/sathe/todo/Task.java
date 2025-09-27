package com.sathe.todo;

public class Task {


    private int id;
    private String title;
    private boolean isDone;
    private String date;  // new field (store as String for easy display)

    // Constructor without ID (new tasks)
    public Task(String title, String date) {
        this.title = title;
        this.isDone = false;
        this.date = date;
    }

    // Constructor with ID (for DB tasks)
    public Task(int id, String title, boolean isDone, String date) {
        this.id = id;
        this.title = title;
        this.isDone = isDone;
        this.date = date;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isDone() { return isDone; }
    public void setDone(boolean done) { isDone = done; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

}
