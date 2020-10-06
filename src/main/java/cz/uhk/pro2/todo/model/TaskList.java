package cz.uhk.pro2.todo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {

    private List<Task> tasks;


    public TaskList(){
        tasks = new ArrayList<Task>();
    }

    public void addTask(Task t){
        this.tasks.add(t);
    }

    public void removeTask(int index){
        this.tasks.remove(index);
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public int getUndoneTasksCount() {
        int nUndoneTasks = 0;
        for (Task t: tasks) {
            if(t.isDone()) nUndoneTasks++;
        }
        return nUndoneTasks;

    }
}
