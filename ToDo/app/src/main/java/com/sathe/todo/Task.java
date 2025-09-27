package com.sathe.todo;

public class Task {

    private String title;
    private boolean isDone;

    public Task(String title) {
        this.title = title;
        this.isDone = false;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

}
