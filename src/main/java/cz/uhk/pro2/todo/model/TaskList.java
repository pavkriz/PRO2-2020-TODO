package cz.uhk.pro2.todo.model;

import java.util.ArrayList;
import java.util.List;

public class TaskList {


    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }
    public int getUndoneTasksCount(){
       //return tasks.stream().filter(t -> !t.isDone().count());
           return 0;
    }


    public void addTask(Task t ){
        tasks.add(t);

    }
    public void removeTask(Task t)
    {
        tasks.remove(t);
    }


}
