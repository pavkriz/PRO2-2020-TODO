package cz.uhk.pro2.todo.model;

import java.util.Date;

public class Task {
    private long id;
    private String description;
    private Date dueDate;
    private boolean done;

    public Task() {
        description = "tmp";
        dueDate = new Date();
        done = false;
        id = 0;
    }

    public Task(String description, Date dueDate, boolean done) {
        this.description = description;
        this.dueDate = dueDate;
        this.done = done;
    }

    public Task(long id, String description, Date dueDate, boolean done) {
        this(description, dueDate, done);
        this.id = id;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", done=" + done +
                '}';
    }
}
