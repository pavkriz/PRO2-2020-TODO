package cz.uhk.pro2.todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TaskList {

    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks(){
        return Collections.unmodifiableList(tasks); //vrati nemenny list
    }

    public void addTask(Task t){
        tasks.add(t);
    }

    public void removeTask(Task t){
        tasks.remove(t);
    }
}
