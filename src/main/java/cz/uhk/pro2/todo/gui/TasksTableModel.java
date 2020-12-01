package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TasksTableModel extends AbstractTableModel {
    private TaskList taskList;
    private TaskDao taskDao;
    private List<Task> tasks;
    public TasksTableModel(TaskDao taskDao) {
        this.taskDao = taskDao;
        reloadData();
    }
    public TasksTableModel(TaskList taskList) {
        this.taskList = taskList;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    /*
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = taskList.getTasks().get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        
        Date date = new Date();
        switch (columnIndex) {
            case 0: return task.getDescription();
            case 1: return sdf.format(task.getDueDate());
            case 2: return task.isDone();
            case 3:return (task.getDueDate().getTime()-date.getTime())/ 86400000;
        }
        return ""; // tohle by se nemelo volat
    }*/
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);
        switch (columnIndex) {
            case 0: return task.getDescription(); // + (task.isDone() ? " DONE" : ""); // alternativne zobrazujeme jeste priznak DONE
            case 1: return task.getDueDate();
            case 2: return task.isDone();
        }
        return ""; // tohle by se nemelo volat
    }
    public Task getTaskAt(int rowIndex){
        return tasks.get(rowIndex);
    }
    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0:return "Description";
            case 1:return "Due date";
            case 2:return "Is task done?";
            case 3:return "Remaining days";
            default: return "";
        } // default
        
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
