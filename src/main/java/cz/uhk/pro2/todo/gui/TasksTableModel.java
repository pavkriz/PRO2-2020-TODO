package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.model.Task;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class TasksTableModel extends AbstractTableModel {
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

    public long daysLeftUpdate(Task task){
        Timer timer  = new Timer(10000, e -> {
            Date dueDate = task.getDueDate();
            Date now = new Date();
            task.setDaysLeft((dueDate.getTime() - now.getTime())/86400000);
            fireTableDataChanged();
        });
        timer.start();
        return task.getDaysLeft();
    }


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
            case 3: return daysLeftUpdate(task);
            default: break;
        }
        return ""; // tohle by se nemelo volat
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Description";
            case 1: return "Due date";
            case 2: return "Done";
            case 3: return "Days left:";
            default: break;
        }
        return "Nadpis";
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
            //fireTableCellUpdated(rowIndex, 0); // informujeme tabulku, ze se zmenil i sloupec 0, pokud bychom v nem zobrazovali priznak DONE
        }
    }

    public void reloadData() {
        tasks = taskDao.findAll(); // vytahneme data z DB
        fireTableDataChanged();
    }
}
