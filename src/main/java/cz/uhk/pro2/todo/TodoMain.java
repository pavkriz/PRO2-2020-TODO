package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TaskTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.Date;

public class  TodoMain extends  JFrame{

    private final TaskList taskList = new TaskList();

    private final JButton btnAdd = new JButton("Add Task");
    private final JPanel pnlNorth = new JPanel();

    private final TaskTableModel taskTableModel = new TaskTableModel(taskList);
    private final JTable jTable = new JTable(taskTableModel);

    public TodoMain() throws HeadlessException {
        taskList.addTask(new Task("Uč se JAVU", new Date(), false));
        taskList.addTask(new Task("Jdi si zaběhat!", new Date(), false));
        taskList.addTask(new Task("Jdi spát", new Date(), false));

        setTitle("Aplikace mých TODO");
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
        return JOptionPane.showInputDialog("Napiš sem tvoje TODO");
    }

    private Date getDate() {
        Date date = new Date();
        return date;
    }

    private boolean getIsDone() {
        int strDone = JOptionPane.showConfirmDialog(null,
                "Je toto TODO splněno?",
                "Vyber",
                JOptionPane.YES_NO_OPTION);
        switch (strDone){
            case 0:
                return true;
            case 1:
                return false;
            default:
                JOptionPane.showMessageDialog(null, "ERROR", "CHYBA", JOptionPane.ERROR_MESSAGE);
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