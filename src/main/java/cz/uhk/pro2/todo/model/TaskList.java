package cz.uhk.pro2.todo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskList {

    private List<Task> taskList = new ArrayList<>();
    public TaskList() {}

    public TaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

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
    public Task getByData(String description, Date dueDate, boolean isDone) {
        List<Task> tasks = taskList.stream().filter(c -> c.getDescription().equals(description) &&
                c.getDueDate().equals(dueDate) &&
                c.isDone() == isDone).collect(Collectors.toList());
        if(tasks.size() > 0) {
            return tasks.get(0);
        }
        return null;
    }
    public Task getByIndex(int index) {
        return taskList.get(index);
    }
}
