package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.table.AbstractTableModel;

public class TasksTableModel extends AbstractTableModel {
    private TaskList taskList;

    public TasksTableModel(TaskList taskList) {
        this.taskList = taskList;
    } // reference na tasklist je nutná kvuli metodam

    @Override
    public int getRowCount() {
        return taskList.getTasks().size();
    } // pro velikost kolekce je size(), pro velikost pole([]) je length() a v databazich se pouziva count :-)

    @Override
    public int getColumnCount() {
        return 3;
    } // pocet dle poctu atributu v TASK

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = taskList.getTasks().get(rowIndex);
        switch (columnIndex) {
            case 0: return task.getDescription(); // return nejen vraci hodnotu, ale i konci metodu! proto neni treba vypisovat vsude break
            case 1: return task.getDueDate();
            case 2: return task.isDone();
        }
        return ""; // tohle by se nemelo volat - případně tam přidáme throw new Exception a metodu
    }

    @Override
    public String getColumnName(int column) {
        String[] nazvy = new String[] {"Nazev","Datum","Vyrizeno"};
        return nazvy[column];
    }
}
