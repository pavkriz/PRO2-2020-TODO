package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TasksTableModel extends AbstractTableModel {
    private TaskDao taskDao;
    private List<Task> tasks; // docasne uloziste tasku, abychom se porad neptali DB

    public TasksTableModel(TaskDao taskDao) {
        this.taskDao = taskDao;
        reloadData();
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    // TODO DU 27.10.2020 Zobrazovat dalsi sloupec s poctem dni, klolik zbyva do dokonceni ukolu (dueDate)
    //               + Kazdych 10 sekund tento udaj aktualizovat

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");

        switch (columnIndex) {
            case 0: return task.getDescription(); // + (task.isDone() ? " DONE" : ""); // alternativne zobrazujeme jeste priznak DONE
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
        // Přes switch se rozhodne, který sloupec bude pojmenován
        switch (column) {
            case 0: return "Popis";
            case 1: return "Platnost";
            case 2: return "Splnění";
            case 3: return "Zbývající čas (dny)";
        }
        return ""; // tohle by se nemelo volat
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 2: return Boolean.class;
            default: return Object.class;
        }
    }

    public void reloadData() {
        tasks = taskDao.findAll(); // vytahneme data z DB
        fireTableDataChanged();
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
            Task task = tasks.get(rowIndex);
            task.setDone((Boolean) aValue);
            // ulozit do DB zmeneny zaznam
            taskDao.save(task);
            //fireTableCellUpdated(rowIndex, 0); // informujeme tabulku, ze se zmenil i sloupec 0, pokud bychom v nem zobrazovali priznak DONE
        }
    }

    /*
    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }
     */
}
