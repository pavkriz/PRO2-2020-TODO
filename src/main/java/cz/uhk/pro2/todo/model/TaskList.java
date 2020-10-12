package cz.uhk.pro2.todo.model;

import java.util.ArrayList;
import java.util.List;

public class TaskList {

    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task){
        tasks.add(task);
    }

    public  void removeTask(Task task){
        tasks.remove(task);
    }

    public List<Task> getTasks(){

        return null;
    }

    public int getUndoneTasks(){
        int count = 0;
        for(Task task : tasks){
            if(task.isDone()){
                count++;
            }
        }

        //tasks.stream().filter(task -> task.isDone());
        return count;
    }
}
