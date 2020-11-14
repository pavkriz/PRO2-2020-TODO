package cz.uhk.pro2.todo.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Task {
    private String description;
    private Date dueDate;
    private boolean done;


    public Task(String description, Date dueDate, boolean done) {
        setDescription(description);
        setDueDate(dueDate);
        setDone(done);
    }

    public Task() {

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

    public String getDueDateFormat(String format) {
        SimpleDateFormat dateParser = new SimpleDateFormat(format);
        return dateParser.format(dueDate);
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

    public String getRemainingTime() {
        if (isDone()) {
            return "Úkol vyřešen";
        }

        long seconds = (getDueDate().getTime() - (new Date()).getTime()) / 1000;
        if (seconds < 0) {
            return "Úkol nesplněn v čas";
        }
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
        return String.format("%d dní %d hodin %d minut %d sekund", day, hours, minute, second);
    }

    @Override
    public String toString() {
        return "description: " + description + " dueDate: " + dueDate + " done: " + done;
    }

    public Object[] toCSVString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        return new Object[]{description, df.format(dueDate), done};
    }
}
