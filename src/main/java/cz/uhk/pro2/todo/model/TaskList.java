package cz.uhk.pro2.todo.model;

import cz.uhk.pro2.todo.TodoMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {

    private List<Task> taskList = new ArrayList<>();

    public void addTask(Task t) {
        taskList.add(t);
    }
    public void removeTask(int i) { // pro dalsi tlacitka
        taskList.remove(i);
    }
    public List<Task> getTasks() {
        return Collections.unmodifiableList(taskList);
    }

    public int getUndoneTasks() {
        int undoneTasks = 0;
        for (Task task : taskList) {
            if (!task.isDone()) {
                undoneTasks++;
            }
        }
        return undoneTasks;
    }
}
