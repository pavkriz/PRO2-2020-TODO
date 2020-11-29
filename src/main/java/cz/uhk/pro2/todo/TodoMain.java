package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JPanel pnlNorth = new JPanel();
    public TaskList taskList = new TaskList();
    //private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    //private TaskList taskList = new TaskList();
    private TaskDao taskDao = new TaskDao();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskDao);
    private JTable tbl = new JTable(tasksTableModel);
    private JButton btnRemove = new JButton("Odstranit úkol");
    private JButton btnToJson = new JButton("Ulož úkoly do JSON");
    private JLabel lblUndoneTasks = new JLabel();
    private Task task = new Task();

    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(lblUndoneTasks);
        pnlNorth.add(btnRemove);
        pnlNorth.add(btnToJson);

        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();

        btnAdd.addActionListener(e -> addTask());
        btnRemove.addActionListener(e -> removeTask());
//        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
//        try {
//            Date d = sdf.parse("28.10.2020 13:00");
//            Date now = new Date();
//            long diffMilis = d.getTime() - now.getTime();
//            taskList.addTask(new Task("Naučit se Javu", d, true));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        taskList.addTask(new Task("Jit se proběhnout", new Date(), false));
//        taskList.addTask(new Task("Vyvařit roušku", new Date(), false));
          Timer timer  = new Timer(1000, e -> {
              setTitle(new Date().toString());
          });
          timer.start();
    }

    private void addTask() {
        String description = JOptionPane.showInputDialog("Description: ");
        String stringDueDate = JOptionPane.showInputDialog("Due date: (yyyy-MM-dd) ");
        long daysLeft = 0;
        task = new Task(description,parseDate(stringDueDate),false, daysLeft);
        taskDao.save(task);
        tbl.addNotify();
        tasksTableModel.reloadData();
    }

    public Date parseDate(String stringDueDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(stringDueDate);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return null;        //tohle zmenit
    }


    // TODO 20.10.2020 DU1
    // Tlacitko pro smazani vybraneho radku

    public void removeTask(){
        if(tbl.getSelectedRowCount() == 1){
            taskDao.delete(tbl.getSelectedRow()+1);      ///TODO upravit maze druhy radek

            //tbl.addNotify();
            tasksTableModel.reloadData();
        } else
            JOptionPane.showMessageDialog(null,"Vyberte jeden úkol!");
    }


    private String getUndoneTasks() {
        return taskList.getUndoneTasksCount();
    }

    private void toJson(){
        String json = taskList.toJson();
        JOptionPane.showMessageDialog(null,json);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TodoMain window = new TodoMain();
                window.setVisible(true);
            }
        });
    }
}
