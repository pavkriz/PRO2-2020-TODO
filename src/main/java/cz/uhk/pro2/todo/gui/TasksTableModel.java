package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.table.AbstractTableModel;
import java.util.Date;

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
        return 4;
    } // pocet dle poctu atributu v TASK + zbyvajici cas

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = taskList.getTasks().get(rowIndex);
        switch (columnIndex) {
            case 0: return task.getDescription() + (task.isDone() ? " DONE" : ""); // return nejen vraci hodnotu, ale i konci metodu! proto neni treba vypisovat vsude break
            case 1: return task.getDueDate();
            case 2: return task.isDone();
            case 3: return new Date();
        }
        return ""; // tohle by se nemelo volat - případně tam přidáme throw new Exception a metodu
    }

    @Override
    public String getColumnName(int column) {
        String[] nazvy = new String[] {"Nazev","Datum","Vyrizeno","Zbyva casu"};
        return nazvy[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 2:
                return Boolean.class; // tzv sell renderer - zaskrtavaci policko - defaultní pro table
            default: return Object.class;
        }
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) { // ke změně udaju z tabulky
        switch (columnIndex) {
            case 2: return true;
            default: return false;
        }
    }
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) { // aValue ... jakakoli hodnota (puvodni metoda ma typ var)
        if (columnIndex == 2) {
            Task task = taskList.getTasks().get(rowIndex);
            task.setDone((Boolean) aValue); //nutno přetypovat na Boolean, jinak to Java nebere
            /*
            task.setDescription(task.getDescription() + " DONE");
            fireTableCellUpdated(rowIndex,0); davam vedet tabulce
             */
            fireTableCellUpdated(rowIndex,0);
        }
    } // editovatelná tabulka

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }
    //todo Novy sloupec, ktery bude ukazovat, kolik dni zbyva do dokonceni ukolu (dueDate) + tento udaj kazdych 10 sekund aktualizovat (Timer, fireTableDataCHange(),...)
    //simpledateformat
    //SimpleDateFomrat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    //Date d = sdf.parse(zadej datum dle formatu);
    //d.getTime() .. kolik ms od 1.1.1970
    //long diffMillis = d.getTime() - now.getTime() - za jak dlouho v milisekundách nastane datum d

}
