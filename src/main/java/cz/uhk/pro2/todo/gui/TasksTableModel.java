package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.model.Task;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TasksTableModel extends AbstractTableModel {
    private String[] columnNames = {"Úkol", "Splnit do", "Splněno", "Zbývá"};
    private TaskDao taskDao;
    private List<Task> tasks; // docasne uloziste tasku, abychom se porad neptali DB

    public TasksTableModel(TaskDao taskDao) {
        this.taskDao = taskDao;
        reloadData();
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    // TODO DU 27.10.2020 Zobrazovat dalsi sloupec s poctem dni, klolik zbyva do dokonceni ukolu (dueDate)
    //               + Kazdych 10 sekund tento udaj aktualizovat

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);
        switch (columnIndex) {
            case 0: return task.getDescription(); // + (task.isDone() ? " DONE" : ""); // alternativne zobrazujeme jeste priznak DONE
            case 1: return task.getDueDate();
            case 2: return task.isDone();
            case 3: return task.getTimeLeft();
        }
        return ""; // tohle by se nemelo volat
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 2: return Boolean.class;
            default: return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 2: return true;
            default: return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 2) { // done
            Task task = tasks.get(rowIndex);
            task.setDone((Boolean) aValue);
            // ulozit do DB zmeneny zaznam
            taskDao.save(task);
            fireTableCellUpdated(rowIndex, columnIndex);
            //fireTableCellUpdated(rowIndex, 0); // informujeme tabulku, ze se zmenil i sloupec 0, pokud bychom v nem zobrazovali priznak DONE
        }

    }

    public void updateDueDateColumn() {
        for (int i = 0; i < this.tasks.size(); i++) {
            this.fireTableCellUpdated(i, 3);
        }
    }

    public void reloadData() {
        tasks = taskDao.findAll(); // vytahneme data z DB
        fireTableDataChanged();
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public Task getTask(int row) {
        return tasks.get(row);
    }

    public int getUndoneTasksCount() {
        int count = 0;

        for (Task t : tasks) {
            if (!t.isDone()) count++;
        }
        return count;
    }

    public void loadTasks(List<Task> tasks) {
        this.tasks.forEach(taskDao::delete);
        this.tasks = tasks;
        tasks.forEach(taskDao::save);
        reloadData();
    }
}
