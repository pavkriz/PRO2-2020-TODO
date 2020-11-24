package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TasksTableModel extends AbstractTableModel {

    private TaskList taskList;

    public TasksTableModel(TaskList taskList) {
        this.taskList = taskList;
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");

        switch (columnIndex) {
            case 0: return task.getDescription();
            case 1: return sdf.format(task.getDueDate());
            case 2: return task.isDone();
            case 3:
                Date dueDate = task.getDueDate();
                Date nowDate = new Date();

                float difDate = dueDate.getTime() - nowDate.getTime();

                return (int) (difDate / (1000*60*60*24));


        }
        return ""; // tohle by se nemelo volat
    }

    @Override
    public String getColumnName(int column) {
        // TODO DU2
        // Přes switch se rozhodne, který sloupec bude pojmenován.
        switch (column) {
            case 0: return "Popis";
            case 1: return "Platnost";
            case 2: return "Splnění";
            case 3: return "Zbývající čas (dny)";
        }
        return "";
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
            Task task = taskList.getTasks().get(rowIndex);
            task.setDone((Boolean) aValue);
            //fireTableCellUpdated(rowIndex, 0);
        }
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }
}
