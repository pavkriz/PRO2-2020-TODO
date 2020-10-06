package cz.uhk.pro2.todo.model;

import java.util.Date;

public class Task {

    private String description;
    private Date dueDate;
    private boolean done;

    public Task () {
    }

    public Task (String description, Date dueDate, boolean done) {
        this.description = description;
        this.dueDate = dueDate;
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
