package cz.uhk.pro2.todo.model;

import org.junit.Test;

import java.time.LocalDate;

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
        Task t1 = new Task("1s", LocalDate.now(),false);
        Task t2 = new Task("2nd",LocalDate.now(),true);
        Task t3 = new Task("3rd",LocalDate.now(),false);

        assertEquals(0,tasks.getUndoneTasks());

        tasks.addTask(t1); // jednotkový test pro metodu
        tasks.addTask(t2);
        tasks.addTask(t3);

        assertEquals(2,tasks.getUndoneTasks());
    }
}