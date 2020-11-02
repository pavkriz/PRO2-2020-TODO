package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = taskList.getTasks().get(rowIndex);

        //switch-case
        switch (columnIndex){
            case 0: return task.getDescription();
            case 1:
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                String strDate = dateFormat.format(task.getDate());
                return strDate;
            case 2: return task.isDone();
            case 3:
                return getDueTime(task);
            default: return "";
        }
    }

    private Object getDueTime(Task task) {
        Date currentTime = new Date();
        Date date = task.getDate();
        long dueDate = date.getTime() - currentTime.getTime();
        String time = "";
        if(TimeUnit.MILLISECONDS.toDays(dueDate) > 0){
            time = time + TimeUnit.MILLISECONDS.toDays(dueDate) + " days ";
        }
        time = time +
                TimeUnit.MILLISECONDS.toHours(dueDate)%24 + ":" +
                TimeUnit.MILLISECONDS.toMinutes(dueDate)%60 + ":" +
                TimeUnit.MILLISECONDS.toSeconds(dueDate)%60;
        return time;
    }

    @Override
    public String getColumnName(int column) {
        String [] columnNames = new String[getColumnCount()];
        String[] str = new Task().toString().split("[,=}{]");
        int tmp =0;
        for (int i = 1; i < str.length; i+=2 ){
            columnNames[tmp] = str[i];
            tmp++;
        }
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
            Task task = taskList.getTasks().get(rowIndex);
            task.setDone((Boolean) aValue);
        }
    }
}
