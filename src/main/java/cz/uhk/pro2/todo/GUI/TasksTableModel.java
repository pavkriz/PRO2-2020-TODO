package cz.uhk.pro2.todo.GUI;

import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TasksTableModel extends AbstractTableModel {
    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    private TaskList taskList;

    public TasksTableModel(TaskList taskList) {
        this.taskList = taskList;
        Timer tm = new Timer(10000, e -> fireTableDataChanged());
        tm.start();
    }

    @Override
    public int getRowCount() {
        return taskList.getTasks().size();
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
                return task.getDueDate();

            case 2:
                return task.isDone();
            case 3:
                Date now = new Date();
                long diffMilis = task.getDueDate().getTime() - now.getTime();
                if (TimeUnit.MILLISECONDS.toDays(diffMilis) == 0) {
                    if (TimeUnit.MILLISECONDS.toHours(diffMilis) == 0) {
                        if (TimeUnit.MILLISECONDS.toMinutes(diffMilis) == 0) {
                            return TimeUnit.MILLISECONDS.toSeconds(diffMilis) + " Seconds";
                        } else {
                            return TimeUnit.MILLISECONDS.toMinutes(diffMilis) + " Minutes";
                        }
                    } else {
                        return TimeUnit.MILLISECONDS.toHours(diffMilis) + " Hours";
                    }
                } else {
                    return TimeUnit.MILLISECONDS.toDays(diffMilis) + " Days";
                }
            default:
                System.out.println("Chyba?");
                break;
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Popis";
            case 1:
                return "Datum";
            case 2:
                return "IsDone?";
            case 3:
                return "Zbývající čas";
            default:
                System.out.println("Chyba");
                break;
        }
        return "Chyba 2";
    }

    public void removeRow(int selectedRow) {
        fireTableRowsDeleted(selectedRow, selectedRow);
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
        switch (columnIndex) {
            case 2:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 2) { // done
            Task task = taskList.getTasks().get(rowIndex);
            task.setDone((Boolean) aValue);
        }
    }
}
