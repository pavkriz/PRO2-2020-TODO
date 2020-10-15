package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TaskTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class  TodoMain extends  JFrame{

    private final TaskList taskList = new TaskList();

    private final JButton btnAdd = new JButton("Add Task");
    private final JPanel pnlNorth = new JPanel();

    private final TaskTableModel taskTableModel = new TaskTableModel(taskList);
    private final JTable jTable = new JTable(taskTableModel);

    public TodoMain() throws HeadlessException {
        taskList.addTask(new Task("Learn Java 'cause you cannot C#!", new Date(), false));
        taskList.addTask(new Task("Go run 'cause you are fat!", new Date(), false));
        taskList.addTask(new Task("Boil all the mask to finish this task!", new Date(), false));

        setTitle("TODO App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);

        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(jTable), BorderLayout.CENTER);
        pack();

        btnAdd.addActionListener(e -> {
            try {
                addTask();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        });
    }

    private void addTask() throws ParseException {
        //add Task to TaskList
        taskList.addTask(new Task(
                getDescription(), //Get Task Description
                getDate(),  //Get Task Date
                getIsDone())); //Get if Task is Done

        //Notify on Table Change
        jTable.addNotify();

        for(int i =0; i<taskList.getTasks().size();i++){
            System.out.println(taskList.getTasks().get(i));
        }
    }

    private String getDescription() {
        return JOptionPane.showInputDialog("Put task description here.");
    }

    private Date getDate() {
        Date date = new Date();
        String strDate = JOptionPane.showInputDialog("Write due date. (format: dd.mm.yyyy => example 01.01.2000)");
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private boolean getIsDone() {
        int strDone = JOptionPane.showConfirmDialog(null,
                "Is this task done?.",
                "Select",
                JOptionPane.YES_NO_OPTION);
        switch (strDone){
            case 0:
                return true;
            case 1:
                return false;
            default:
                JOptionPane.showMessageDialog(null, "ErrorMsg", "Failure", JOptionPane.ERROR_MESSAGE);
        }
        return false;
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
