package cz.uhk.pro2.todo.model;

import com.google.gson.Gson;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {

    public List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void addTask(Task t) {
        tasks.add(t);
    }

    public void removeTask(Task t) {
        tasks.remove(t);
    }

    public String getUndoneTasksCount() {
        long undoneTasks = tasks.stream().filter(t -> !t.isDone()).count();
        return String.valueOf(undoneTasks);
    }

    public String toJson() {
        return new Gson().toJson(tasks);
    }

    public Task getTaskIndex(int index) {
        return tasks.get(index);
    }
}
