package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JPanel pnlNorth = new JPanel();
    private TaskList taskList = new TaskList();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);

    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();
        btnAdd.addActionListener(e -> addTask());
        taskList.addTask(new Task("Naučit se Javu", new Date(), false));
        taskList.addTask(new Task("Jit se proběhnout", new Date(), false));
        taskList.addTask(new Task("Vyvařit roušku", new Date(), false));
    }

    private void addTask() {
        String description = JOptionPane.showInputDialog("Zadejte úkol:");
        taskList.addTask(new Task(description, new Date(), false));
        tasksTableModel.fireTableDataChanged();

    }

    public static void main(String[] args) {
        //System.out.println("Hello!!!");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TodoMain window = new TodoMain();
                window.setVisible(true);
            }
        });
    }
}
