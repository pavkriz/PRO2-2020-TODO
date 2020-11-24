package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JPanel pnlNorth = new JPanel();
    public TaskList taskList = new TaskList();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);
    private JButton btnRemove = new JButton("Odstranit úkol");
    private JButton btnToJson = new JButton("Ulož úkoly do JSON");
    private JLabel lblUndoneTasks = new JLabel();

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
        btnToJson.addActionListener(e -> toJson());

        taskList.addTask(new Task("Naučit se Javu", new Date(), false));
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            Date d = sdf.parse("28.10.2020 13:00");
            Date now = new Date();
            long diffMilis = d.getTime() - now.getTime();
            taskList.addTask(new Task("Naučit se Javu", d, true));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        taskList.addTask(new Task("Jit se proběhnout", new Date(), false));
        taskList.addTask(new Task("Vyvařit roušku", new Date(), false));

        lblUndoneTasks.setText("Počet nesplněných úkolů: " + getUndoneTasks());       //existuje neco jako setLong
        Timer timer  = new Timer(1000, e -> {
            setTitle(new Date().toString());
        });
        timer.start();
    }

    private void addTask() {
        String description = JOptionPane.showInputDialog("Description: ");
        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        boolean done = false;       // vytvorime task a pridame do seznamu
        taskList.addTask(new Task(description,date,done));
        tbl.addNotify();        // notifikujeme tabulku, ze doslo ze zmene dat
    }

    // TODO 20.10.2020 DU1
    // Tlacitko pro smazani vybraneho radku

    public void removeTask(){
        System.out.println(tbl.getSelectedRow());
        if(tbl.getSelectedRowCount() == 1){
            taskList.removeTask(taskList.getTaskIndex(tbl.getSelectedRow()));
            tbl.addNotify();
            //tbl.removeNotify();       //proc nefunguje?
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
