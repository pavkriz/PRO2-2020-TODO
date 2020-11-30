package cz.uhk.pro2.todo.model;

import java.util.Date;

public class Task {
    private long id;
    private String description;
    private Date dueDate;
    private Boolean done;

    public Task() {

    }

    public Task(String description, Date dueDate, Boolean done) {
        this.description = description;
        this.dueDate = dueDate;
        this.done = done;
        this.id = 0;
    }

    public Task(long id, String description, Date dueDate, boolean done) {
        this(description, dueDate, done);
        this.id = id;
    }


    public Boolean isDone() {
        return done;
    }

    public long getId() {
        return id;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", done=" + done +
                '}';
    }

}
