package main.java.cz.uhk.pro2.todo;

import main.java.cz.uhk.pro2.todo.GUI.TasksTableModel;
import main.java.cz.uhk.pro2.todo.model.Task;
import main.java.cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JPanel pnlNorth = new JPanel();
    private TaskList taskList = new TaskList();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);

    public TodoMain() throws HeadlessException{
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        add(pnlNorth,BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();
        btnAdd.addActionListener(e -> addTask());
    }

    private void addTask(){
        //úkol
        String desc = JOptionPane.showInputDialog("Zadej popis tasku");
        String sdate = JOptionPane.showInputDialog("Zadej datum ve formátu dd/mm/YYYY");

        Date date;
        try{
            date = new SimpleDateFormat("dd/MM/yyyy").parse(sdate);
        }catch(Exception e){
            date = new Date();
        }

        boolean done;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Je task hotový?");
        done = dialogResult == JOptionPane.YES_OPTION;

        taskList.addTask(new Task(desc,date,done));
        tbl.addNotify();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoMain windows = new TodoMain();
            windows.setVisible(true);

        });
    }
}
