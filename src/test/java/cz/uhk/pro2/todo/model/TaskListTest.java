package cz.uhk.pro2.todo.model;

import org.junit.Test;

import java.util.Calendar;

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
    public void getUndoneTasksCount() {
        TaskList taskList = new TaskList();
        taskList.addTask(new Task("Task1", Calendar.getInstance().getTime(), false));
        taskList.addTask(new Task("Task2", Calendar.getInstance().getTime(), true));
        taskList.addTask(new Task("Task3", Calendar.getInstance().getTime(), true));
        int undoneTasksCount = taskList.getUndoneTasksCount();
        assertEquals(1, undoneTasksCount);
    }

}