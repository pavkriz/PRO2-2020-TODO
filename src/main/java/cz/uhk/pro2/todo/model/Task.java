package cz.uhk.pro2.todo.model;

import java.util.Date;

public class Task {
    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return Date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public void setDate(java.util.Date date) {
        Date = date;
    }



    private java.util.Date Date;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    private boolean done;

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", Date=" + Date +
                ", done=" + done +
                '}';
    }

}
