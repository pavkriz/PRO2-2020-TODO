package cz.uhk.pro2.todo.model;

public class Task {
    private String description;

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    private boolean done;
}
