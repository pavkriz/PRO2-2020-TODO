package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TasksTableModel;

import javax.swing.*;
import java.awt.*;

public class TodoMain extends JFrame{

    private JButton btnAdd = new JButton("Přidat úkol");
    private JPanel pnlNorth = new JPanel();
    private TasksTableModel tasksTableModel = new TasksTableModel();
    private JTable tbl = new JTable(tasksTableModel);

    public TodoMain() throws HeadlessException{
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        add(pnlNorth, BorderLayout.NORTH);
        add(tbl, BorderLayout.CENTER);
        pack();
        btnAdd.addActionListener(e -> addTask());
    }

    private void addTask(){
        //TODO
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
