package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.table.AbstractTableModel;
import java.util.Date;

public class TasksTableModel extends AbstractTableModel {
    private TaskList taskList;
    private final TaskDao taskDao;

    public TasksTableModel(TaskDao taskDao) {
        this.taskDao = taskDao;
        reloadData();
    }

    @Override
    public int getRowCount() {
        return taskList.getTasksCount();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = taskList.getTasks().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return task.getDescription();
            case 1:
                return task.getDueDateFormat("dd.MM.yyyy");
            case 2:
                return task.isDone();
            case 3:
                return task.getRemainingTime();
            default:
                return "NULL";
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Tasks?";
            case 1:
                return "Deadline";
            case 2:
                return "Done";
            case 3:
                return "Remaining time";
            default:
                return "NULL";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 2:
                return Boolean.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 2) {
            Task task = taskList.getTasks().get(rowIndex);
            task.setDone((Boolean) aValue);
            taskDao.save(task);
        }
    }

    public void reloadData() {
        taskList = taskDao.findAll();
    }

    public void addTask(Task t) {
        taskDao.save(t);
        taskList.addTask(t);
    }

    public void deleteTask(Task t) {
        taskDao.delete(t);
        taskList.removeTask(t);
    }

    public void deleteTask(String description, Date dueDate, boolean isDone) {
        Task task = taskList.getByData(description, dueDate, isDone);
        if (task != null) {
            deleteTask(task);
        }
    }

    public Task getByIndex(int index) {
        return taskList.getByIndex(index);
    }
}
