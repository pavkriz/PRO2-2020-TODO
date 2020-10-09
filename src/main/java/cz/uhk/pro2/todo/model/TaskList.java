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

    public int getUndoneTasksCount(){
            int a;
            int b = 0;

            for (a = 0; a < tasks.size(); a++ ){
                if(!tasks[a].isDone()) {
                b++;
                }
                }
            return b;

    }
}
