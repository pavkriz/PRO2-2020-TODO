package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskTableModel extends AbstractTableModel {

    private final TaskList taskList;

    public TaskTableModel(TaskList taskList) {
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

        //switch-case
        switch (columnIndex){
            case 0: return task.getDescription();
            case 1:
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String strDate = dateFormat.format(task.getDate());
                return strDate;
                //return task.getDate();
            case 2: return task.isDone();
        }
        return "WRONG!";
    }

    @Override
    public String getColumnName(int column) {
        String [] columnNames = new String[getColumnCount()];
        String[] str = new Task().toString().split(",|=|\\{");
        int tmp =0;
        for (int i = 1; i < str.length; i+=2 ){
            columnNames[tmp] = str[i];
            tmp++;
        }
        return columnNames[column];
    }
}
