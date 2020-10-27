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
        return taskList.getTasks().size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = taskList.getTasks().get(rowIndex);
        switch (columnIndex) {
            case 0: return task.getDescription();
            case 1: return task.getDueDate();
            case 2: return task.isDone();
            default: break;                     //je to dobře?/proč
        }
        return ""; // tohle by se nemelo volat
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Description";
            case 1: return "Due date";
            case 2: return "Done";
            default: break;
        }
        return "Nadpis";
    }
}
