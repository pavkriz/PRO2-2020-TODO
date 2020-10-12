package main.java.cz.uhk.pro2.todo.model;

import java.time.LocalDate;

public class Task {
    private String description;
    private LocalDate dueDate;
    private Boolean done;

    public Task(){
    }

    public Task(String description, LocalDate dueDate, Boolean done) {
        this.description = description;
        this.dueDate = dueDate;
        this.done = done;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
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
