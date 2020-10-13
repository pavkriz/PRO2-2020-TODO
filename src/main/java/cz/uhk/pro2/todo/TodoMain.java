package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoMain extends JFrame {

    private final JButton btnAddTask = new JButton("Přidat úkol");

    private final JPanel pnlNorth = new JPanel();

    private final TaskList taskList = new TaskList();

    private final TasksTableModel tasksTableModel = new TasksTableModel(taskList);

    private final JTable tbl = new JTable(tasksTableModel);

    public TodoMain() throws HeadlessException {
        setTitle("TODO aplikace");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAddTask);
        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();
        btnAddTask.addActionListener(e -> addTask());
    }


    private void addTask() {
        String description = JOptionPane.showInputDialog(this, "Zadej název úkolu");
        if (description == null) {
            return;
        }
        String dateString = JOptionPane.showInputDialog(this, "Zadej do kdy se má splnit (formát DD.MM.YYYY)");
        if (dateString == null) {
            return;
        }
        SimpleDateFormat dateParser = new SimpleDateFormat("dd.MM.yyyy");
        Date date;
        try {
            date = dateParser.parse(dateString);
        } catch (Throwable throwable) {
            JOptionPane.showMessageDialog(this, "Zadal si špatně datum, takže máš smůlu");
            return;
        }

        Object isFinishedObject = JOptionPane.showInputDialog(this, "Byl už úkol splněn?",
                "Splění úkolu", JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"ANO", "NE"}, "NE");
        if (isFinishedObject == null) {
            return;
        }
        boolean isFinished = isFinishedObject.equals("ANO");

        Task task = new Task(description, date, isFinished);
        taskList.addTask(task);
        tbl.updateUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoMain windows = new TodoMain();
            windows.setVisible(true);
        });

    }
}
