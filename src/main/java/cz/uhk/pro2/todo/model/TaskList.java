package cz.uhk.pro2.todo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {
    private List<Task> tasks = new ArrayList<>();



    public List<Task> getTasks(){
        return Collections.unmodifiableList(tasks);
    }

    public void addTask(Task t){
        tasks.add(t);
    }

    public void removeTask(Task t){
        tasks.remove(t);
    }

    public void removeTask(int position){
        tasks.remove(position);
    }

    public int getUndoneTasksCount(){
        //TODO 13.10.
        //return (int) tasks.stream().filter(t -> t.isDone()).count();
        int undoneTaskCount = 0;
        for (Task task : tasks)
            if (!task.isDone()) undoneTaskCount++;
        return undoneTaskCount;
    }

    public int getTasksCount(){
        return tasks.size();
    }

    public void removeAll() {
        tasks = null;
    }

}
