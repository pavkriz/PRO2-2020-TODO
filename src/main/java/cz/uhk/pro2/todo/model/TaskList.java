package cz.uhk.pro2.todo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {
    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void addTask(Task t) {
        tasks.add(t);
    }

    public void removeTask(Task t) {
        tasks.remove(t);
    }
    public void setTasks(List<Task> list){
        this.tasks=list;
    }
    public int getSize(){
        return tasks.size();
    }
    public int getUndoneTasksCount() {
        int pocet=0;
        for(Task t : tasks){
            if(!(t.isDone())){
                pocet++;
            }
        }
        
        return pocet; 
    }
}
