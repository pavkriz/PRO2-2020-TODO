package cz.uhk.pro2.todo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {
    private final List<Task> tasks = new ArrayList<>();


    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(int index) {
        tasks.remove(index);
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }
}
