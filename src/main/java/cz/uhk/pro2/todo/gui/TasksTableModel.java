package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;

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
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Task task = taskList.getTasks().get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");

        switch (columnIndex) {
            case 0: return task.getDescription();
            case 1: return sdf.format(task.getDueDate());
            case 2: return task.isDone()?"x":"";
        }
        return ""; // tohle by se nemelo volat
    }

    @Override
    public String getColumnName(int column) {
        // TODO DU2
        // Přes switch se rozhodne, který sloupec bude pojmenován
        switch (column) {
            case 0: return "Popis";
            case 1: return "Platnost";
            case 2: return "Splnění";
        }
        return ""; // tohle by se nemelo volat
    }

}
