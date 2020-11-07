package cz.uhk.pro2.todo.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Task {

    private String description;
    private Date dueDate;
    private boolean done;

    public Task() {
    }

    public Task(String description, Date dueDate, boolean done) {
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

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", done=" + done +
                '}';
    }

    public String getTimeLeft() {
        if(this.done) {
            return "Splněno";
        }

        long diff = this.dueDate.getTime() - new Date().getTime();
        if(diff <= 0) {
            return "-";
        }
        if(diff <= 60000) {
            return String.format("Zbývá %d sekund", TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS));
        }
        if(diff <= 3600000) {
            return String.format("Zbývá %d minut", TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS));
        }
        if(diff <= 86400000) {
            return String.format("Zbývá %d hodin", TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS));
        }
        return String.format("Zbývá %d dní", TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }
}
