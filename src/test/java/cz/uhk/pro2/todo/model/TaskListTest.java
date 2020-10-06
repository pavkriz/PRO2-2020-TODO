package cz.uhk.pro2.todo.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TaskListTest {
    @Test
    public void testAddTask() {
        TaskList l = new TaskList();
        Task t = new Task();
        assertEquals(0, l.getTasks().size());
        l.addTask(t);
        assertEquals(1, l.getTasks().size());
        assertEquals(t, l.getTasks().get(0));
    }

    @Test
    public void getUndoneTasks() {
            TaskList tasks = new TaskList();
            Task t = new Task();
            t.setDone(false);
            tasks.addTask(t); // jednotkový test pro metodu
        }
    }
}