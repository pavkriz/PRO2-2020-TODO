package cz.uhk.pro2.todo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {
    private List<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void addTask(Task t) {
        tasks.add(t);
    }

    public void removeTask(Task t) {
        tasks.remove(t);
    }

    public int getUndoneTasks() {
        int utasks = 0;
        for (Task task : tasks) {
            if (!task.isDone()) {
                utasks++;
            }
        }
        return utasks;
    }

    public void clear() {
        tasks.clear();
    }
}
