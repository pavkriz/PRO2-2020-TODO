package cz.uhk.pro2.todo.model;

import java.time.LocalDate;
import java.util.Date;

public class Task {
    private String description;
    private Date dueDate;
    private Boolean done;

    public Task(){
    }

    public Task(String description, Date dueDate, Boolean done) {
        this.description = description;
        this.dueDate = dueDate;
        this.done = done;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", done=" + done +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }


}
