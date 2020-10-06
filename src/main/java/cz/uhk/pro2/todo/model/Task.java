package cz.uhk.pro2.todo.model;

import java.time.LocalDate;
import java.util.Date;

public class Task {

    private String description;
    private LocalDate dueDate;
    private boolean done;

    public Task(String description, LocalDate dueDate, Boolean done) {
        this.description = description;
        this.dueDate = dueDate;
        this.done = done;

    }

    public Task() {
    }


    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return description.concat(dueDate.toString());
    }
}

//dole branches - remote master - update !!!
//potom teprve local merge - smartmerge (alt dole kliknout na remotemaster pravym - merge into current - smartmerge)!!
