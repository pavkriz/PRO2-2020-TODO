package cz.uhk.pro2.todo;

import java.util.Date;

public class Task {
    String description;
    Date dueDate;
    boolean done;
    int index;

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", done=" + done +
                ", index=" + index +
                '}';
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

    public int getIndex() {
        return index;
    }
}
