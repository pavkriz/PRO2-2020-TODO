package cz.uhk.pro2.todo.model;

import java.util.Date;

public class Task {
    private String description;
    private Date dueDate;
    private boolean done;

    public Task() {
    }

    public Task(String description, Date dueDate, boolean done) {
        this.description = description;
        this.dueDate = dueDate; // pro datum - tridu simpledateformat - umi zparsovat nejaky retezec na datum a cas - pripadne vyrobit datePicker - jako kalendar
        this.done = done;
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
        return description.concat(dueDate.toString().concat(String.valueOf(done)));
    }
}
