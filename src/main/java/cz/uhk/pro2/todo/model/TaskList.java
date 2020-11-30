package cz.uhk.pro2.todo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskList {
    private final List<Task> tasks = new ArrayList<>();
    public TaskList() {}
    public void addTask(Task task) {
        tasks.add(task);
    }
    public void removeTask(int index) {
        tasks.remove(index);
    }
    public void removeTask(Task t) {
        tasks.remove(t);
    }
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }
    public int getUndoneTasksCount() {
        return (int) tasks.stream().filter(c -> !c.isDone()).count();
    }
    public int getTasksCount() {
        return tasks.size();
    }
    public void clear() {
        tasks.clear();
    }

    public void add(TaskList taskList) {
        if (taskList == null) {
            return;
        }
        tasks.addAll(taskList.getTasks());
    }

    public Task getByData(String description, Date dueDate, boolean isDone) {
        List<Task> taskList = tasks.stream().filter(c -> c.getDescription().equals(description) &&
                                                            c.getDueDate().equals(dueDate) &&
                                                            c.isDone() == isDone).collect(Collectors.toList());
        if(taskList.size() > 0) {
            return taskList.get(0);
        }
        return null;
    }
    public Task getByIndex(int index) {
        return tasks.get(index);
    }
}
