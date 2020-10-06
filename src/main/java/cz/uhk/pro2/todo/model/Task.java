package cz.uhk.pro2.todo.model;

import java.util.Date;

public class Task {
    private String description;
    private Date dneDate;
    private boolean done;

    public Task(String description, Date dneDate, boolean done) {
        this.description = description;
        this.dneDate = dneDate;
        this.done = done;
    }
    public Task(){

    }

    @Override
    public String toString(){
        String popis = description + ", " + dneDate.toString() + ", ";
        popis += (done)? "true" : "false";
        return  popis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDneDate() {
        return dneDate;
    }

    public void setDneDate(Date dneDate) {
        this.dneDate = dneDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
