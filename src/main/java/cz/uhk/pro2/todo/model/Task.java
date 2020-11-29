package cz.uhk.pro2.todo.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    private long daysLeft;
    private long id;
    private String description;
    private Date dueDate;
    private boolean done;

    public Task() {
    }

    public Task(String description, Date dueDate, boolean done, long daysLeft) {
        this.description = description;
        this.dueDate = dueDate;
        this.done = done;
        this.daysLeft = daysLeft;
    }

    public Task(long id, String description, Date dueDate, boolean done) {
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
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

    public void setDaysLeft(long daysLeft) {
        this.daysLeft = daysLeft;
    }

    public long getDaysLeft() {
        return daysLeft;
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


    /*public long getDaysLeft(Task task) {
        dueDate = task.getDueDate();
        Date now = new Date();
        return (dueDate.getTime() - now.getTime())/86400000;           //zitrek hazi 0
    }*/
}
