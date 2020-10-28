package cz.uhk.pro2.todo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoMain extends JFrame {

    private final JButton btnAddTask = new JButton("Přidat úkol");

    private final JButton btnRemoveTask = new JButton("Odebrat úkol");

    private final JButton btnSaveToFile = new JButton("Ulož data do souboru");

    private final JLabel lblUnfinishedTasks = new JLabel("Počet nesplněných úkolů: 0");

    private final JPanel pnlNorth = new JPanel();

    private final TaskList taskList = new TaskList();

    private final TasksTableModel tasksTableModel = new TasksTableModel(taskList);

    private final JTable tbl = new JTable(tasksTableModel);

    private Timer updateTimer;

    public TodoMain() throws HeadlessException {
        setTitle("TODO aplikace");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAddTask);
        pnlNorth.add(btnRemoveTask);
        pnlNorth.add(lblUnfinishedTasks);
        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        add(btnSaveToFile, BorderLayout.SOUTH);
        pack();
        btnAddTask.addActionListener(e -> {
            addTask();
            updateUnfinishedTasks();
        });
        btnRemoveTask.addActionListener(e -> {
            removeHighlightedTask();
            updateUnfinishedTasks();
        });
        btnSaveToFile.addActionListener(e -> saveToFile());
        initializeTimers();
    }

    private void initializeTimers()
    {
        updateTimer = new Timer(10000, c -> tbl.updateUI());
        updateTimer.start();
    }

    private void updateUnfinishedTasks() {
        lblUnfinishedTasks.setText("Počet nesplněných úkolů: " + taskList.getUndoneTasksCount());
    }

    private void removeHighlightedTask() {
        int selectedRow = tbl.getSelectedRow();
        if (selectedRow != -1) {
            taskList.removeTask(selectedRow);
        }
        tbl.updateUI();
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON soubory", "json"));
        fileChooser.setDialogTitle("Vyberte, kam uložit soubor s daty");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                FileWriter fw = new FileWriter(fileToSave);
                fw.write(gson.toJson(taskList));
                fw.close();
            }
            catch (Throwable throwable) {
                JOptionPane.showMessageDialog(this, "Nepodařilo se uložit soubor.");
            }
        }
    }

    private void addTask() {
        String description = JOptionPane.showInputDialog(this, "Zadej název úkolu");
        if (description == null) {
            return;
        }
        String dateString = JOptionPane.showInputDialog(this, "Zadej do kdy se má splnit (formát DD.MM.RRRR HH:MM)");
        if (dateString == null) {
            return;
        }
        SimpleDateFormat dateParser = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date;
        try {
            date = dateParser.parse(dateString);
        } catch (Throwable throwable) {
            JOptionPane.showMessageDialog(this, "Zadal si špatně datum, takže máš smůlu");
            return;
        }

        Object isFinishedObject = JOptionPane.showInputDialog(this, "Byl už úkol splněn?",
                "Splnění úkolu", JOptionPane.QUESTION_MESSAGE,
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
