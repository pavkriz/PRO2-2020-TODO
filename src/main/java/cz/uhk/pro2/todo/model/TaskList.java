package cz.uhk.pro2.todo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class TaskList {

    private final List<Task> tasks;

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        tasks = new ArrayList<>();
    }


    public void addTask(Task task){
        tasks.add(task);
    }

    public  void removeTask(Task task){
        tasks.remove(task);
    }

    public List<Task> getTasks(){
        return Collections.unmodifiableList(tasks);
    }

    public String getUndoneTasks(){
        int count = 0;
        for(Task task : tasks){
            if(!task.isDone()){
                count++;
            }
        }
        String str = String.valueOf(count);
        //tasks.stream().filter(task -> task.isDone());
        return str;
    }
    public void deleteTasks() {
        tasks.clear();
    }

}
