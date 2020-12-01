package cz.uhk.pro2.todo.model;

import java.util.Date;

public class Task {
    private long id;
    private String description;
    private Date date;

    public Task() {
    }

    private boolean done;

    public Task(long id, String description, Date date, boolean done) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.done = done;
    }


    public Task(String description, Date date, boolean done) {
        this.description = description;
        this.date = date;
        this.done = done;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return "Task { \n" +
                "id = '" + id + '\'' +
                ", description = '" + description + '\''+
                ", dueDate = " + date +
                ", done = " + done +
                ", dueDate" +
                '}';

    }
}