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
    public void testGetUndoneTasksCount() {
        TaskList l = new TaskList();
        Task t = new Task();
        Task t2 = new Task();
        Task t3 = new Task();
        t2.setDone(true);
        l.addTask(t);
        l.addTask(t2);
        l.addTask(t3);
        assertEquals(2, l.getUndoneTasksCount());
    }
}