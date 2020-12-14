package cz.uhk.pro2.todo.gui;

import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TasksTableModel extends AbstractTableModel {   // transformuje data z TaskListu do tabulkové podoby
    private TaskDao taskDao;
    private List<Task> tasks;   // docasne uloziste tasku, abychom se porad neptali DB

    public TasksTableModel(TaskDao taskDao){
        this.taskDao = taskDao;
        reloadData();  //vytahneme data z DB
    }


    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {   //počet sloupců, atributů
        return 4;
    }


    //TODO 27.10. zobrazovat další sloupec s poctem dní, kolik zbývá do sokoncení úkolu (dueDate)
    //      každých 10 sekund tento aktualizovat udaj

    //nastavení, jaké údaje se budou zobrazovat ve sloupcích
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                Task task = tasks.get(rowIndex);
                switch (columnIndex){
                    case 0: return task.getDescription();   // + (task.isDone() ? " DONE" : ""); // alternativne zobrazujeme jeste priznak DONE
                    case 1: return task.getDueDate();
                    case 2: return task.isDone();
                    case 3:
                        Date dueDate = task.getDueDate();
                        Date nowDate = new Date();
                        float difDate = dueDate.getTime() - nowDate.getTime();
                        return (int) (difDate / (1000*60*60*24));
        }
        return "";     //nemělo by se volat
    }


    @Override
    public String getColumnName(int column) {
        //TODO 2 13.10.
        String[] columnlbl = {"Popis úkolu", "Čas splnění", "Splněno", "Času zbývá"};
        return columnlbl[column];
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 2: return Boolean.class;
            default: return Object.class;
        }
    }


    public void reloadData(){
        tasks = TaskDao.findAll();  // vytahneme data z DB
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 2: return true;
            default: return false;
        }
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 2){      //true
            Task task = tasks.get(rowIndex);
            task.setDone((boolean)aValue);
            // ulozit do DB zmeneny zaznam
            taskDao.save(task);
            //fireTableCellUpdated(rowindex, 0); //informujeme tabulku, že se změnil i sloupec 0, pokud bychom v nem zobrazovali priznak DONE
        }
    }


    public void setTaskList(TaskList taskList){
        this.tasks = taskList.getTasks();
    }
}