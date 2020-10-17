package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
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
        setLocationRelativeTo(null);
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
        Task newTask = new Task("",new Date(),false);
        newTask.setDescription(JOptionPane.showInputDialog("Nazev nove ulohy: "));
        taskList.addTask(newTask);
        tbl.addNotify(); // došlo ke změně dat
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoMain window = new TodoMain();
            window.setVisible(true);
        });
    }

    //pres git: VCS->Git->Fetch, a pak mergnout zmeny z master do vasi vetve
    // VCS -> Git -> Branches -> Master -> Merge Into Current
}
