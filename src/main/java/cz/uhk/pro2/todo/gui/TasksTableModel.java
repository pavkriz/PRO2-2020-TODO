package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.table.AbstractTableModel;

public class TasksTableModel extends AbstractTableModel {

    private final TaskList taskList;

    public TasksTableModel(TaskList taskList) {
        this.taskList = taskList;
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
                return "Co mám udělat";
            case 1:
                return "Datum splatnosti";
            case 2:
                return "Je vyřešen";
            case 3:
                return "Kolik času zbývá";
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
        //return super.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex == 2) { // isDone
            Task task = taskList.getTasks().get(0);
            task.setDone((Boolean)aValue);
        }
    }
}
