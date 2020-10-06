package cz.uhk.pro2.todo.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UndoneTest {

    @Test
    public void testUndoneTasks() {
        TaskList tasks = new TaskList();
        Task t = new Task();
        t.setDone(false);
        tasks.addTask(t);
    }
}
