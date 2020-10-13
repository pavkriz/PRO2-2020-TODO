package main.java.cz.uhk.pro2.todo.GUI;

import main.java.cz.uhk.pro2.todo.model.Task;
import main.java.cz.uhk.pro2.todo.model.TaskList;

import javax.swing.table.AbstractTableModel;

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
        switch(columnIndex){
            case 0:
                return task.getDescription();

            case 1:
                return task.getDueDate();

            case 2:
                return task.isDone();

            default:
                System.out.println("Chyba?");
                break;
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0:
                return "Popis";
            case 1:
                return "Datum";
            case 2:
                return "IsDone?";
            default:
                System.out.println("Chyba");
                break;
        }
        return "Chyba 2";
    }
}
