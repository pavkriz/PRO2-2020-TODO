package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class TodoMain extends JFrame{

    private JButton btnAdd = new JButton("Přidat úkol");
    private JPanel pnlNorth = new JPanel();
    private TaskList taskList = new TaskList();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);

    public TodoMain() throws HeadlessException{
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        add(pnlNorth, BorderLayout.NORTH);
        add(tbl, BorderLayout.CENTER);
        btnAdd.addActionListener(e -> addTask());
       /*
        taskList.addTask(new Task("Naucit se Javu", new Date(), false));
        taskList.addTask(new Task("Behat", new Date(), false));
        taskList.addTask(new Task("Nakoupit", new Date(), false));
        taskList.addTask(new Task("Vyvarit rousku", new Date(), false));
        taskList.addTask(new Task("Naucit se Javu", new Date(), false));
       */
        pack();
    }

    private void addTask(){
        //TODO
        String taskString = JOptionPane.showInputDialog("Zadej ulohu");
        taskList.addTask(new Task(taskString, new Date(), false));
        //notifikujeme tabulku ze doslo ke zmene dat
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
