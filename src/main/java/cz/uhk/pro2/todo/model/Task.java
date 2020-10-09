package cz.uhk.pro2.todo.model;

public class Task {
    private String description;
    private Boolean done;

    public String getDescription() {
        return description;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Task(){

    }
}
