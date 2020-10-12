package cz.uhk.pro2.todo.model;

import java.util.Date;

public class Task {

    private String description;
    private Date date;

    public Task() {
    }

    private boolean done;


    public Task(String description, Date date, boolean done) {
        this.description = description;
        this.date = date;
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String toString(){
        return null;
    }
}
