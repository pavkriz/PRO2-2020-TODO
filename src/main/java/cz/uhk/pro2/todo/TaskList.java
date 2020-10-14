package cz.uhk.pro2.todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {

    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks(){
        return Collections.unmodifiableList(tasks); //vrati nemenny list
    }

    public TaskList taskList;

    public void TasksTableModel(TaskList taskList) {
        this.taskList = taskList;
    }

    public void addTask(Task t){
        tasks.add(t);
    }

    public void removeTask(Task t){
        tasks.remove(t);
    }

    public int getUndoneTasksCount() {
        //TODO
        return taskList.getUndoneTasksCount();
    }
}
