package cz.uhk.pro2.todo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {

    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks); // TODO vrati nemenny list
    }

    public void addTask(Task t) {
        tasks.add(t);
    }

    public void removeTask(Task t) {
        tasks.remove(t);
    }

    public int getUndoneTestsCount() {
        int count = 0;

        for (Task t: tasks) {
            if (t.isDone()) count++;
        }
        return count;

        // lambda
        // return tasks.stream().filter(t -> !t.isDone()).count();
    }

}
