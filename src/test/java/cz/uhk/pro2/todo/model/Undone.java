package cz.uhk.pro2.todo.model;

import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Undone {
    @Test
    public void testUndoneTasks() {
        TaskList list = new TaskList();
        Task o = new Task();
        o.setDone(false);
        list.addTask(o);
        assertEquals(1,list.getUndoneTasks());

    }
}
