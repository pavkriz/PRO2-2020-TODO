package cz.uhk.pro2.todo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoMain extends JFrame {
    private final JButton btnAddTask = new JButton("Přidat úkol");
    private final JButton btnRemoveTask = new JButton("Odebrat úkol");
    private final JButton btnSaveToFile = new JButton("Ulož data do souboru");
    private final JLabel lblUnfinishedTasks = new JLabel("Počet nesplněných úkolů: 0");
    private final JMenuBar menuBar = new JMenuBar();
    private final JPanel pnlNorth = new JPanel();
    private TaskDao taskDao = new TaskDao();
    private TaskList taskList = new TaskList();
    private final TasksTableModel tasksTableModel = new TasksTableModel(taskDao);
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
        //add(btnSaveToFile, BorderLayout.SOUTH);
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
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Soubor");
        menuBar.add(menu);
        JMenuItem m1 = new JMenuItem("Otevřít");
        JMenuItem m2 = new JMenuItem("Uložit");
        m2.addActionListener(c -> saveToFile());
        m1.addActionListener(c -> readFromFile());
        menu.add(m1);
        menu.add(m2);
    }

    private void initializeTimers() {
        updateTimer = new Timer(10000, c -> tbl.updateUI());
        updateTimer.start();
    }

    private void updateUnfinishedTasks() {
        lblUnfinishedTasks.setText("Počet nesplněných úkolů: " + taskList.getUndoneTasksCount());
    }

    private void removeHighlightedTask() {
        int selectedRow = tbl.getSelectedRow();
        if (selectedRow != -1) {
            Task selectedTask = tasksTableModel.getByIndex(selectedRow);
            tasksTableModel.deleteTask(selectedTask);
        }
        tbl.updateUI();
    }

    private void saveToFile() {
        JFileChooser fileChooser = createFileDialog();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                FileWriter fw = new FileWriter(fileToSave);
                switch (FilenameUtils.getExtension(fileToSave.getName()).toLowerCase()) {
                    case "":
                    default:
                        JOptionPane.showMessageDialog(this, "Musíš zadat extension!");
                        return;
                    case "json":
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
                        fw.write(gson.toJson(taskList));
                        break;
                    case "csv":
                        CSVPrinter csvPrinter = new CSVPrinter(fw, CSVFormat.DEFAULT.withHeader("Description", "DueDate", "Done"));
                        for (Task task : taskList.getTasks()) {
                            csvPrinter.printRecord(task.toCSVString());
                        }
                        csvPrinter.flush();
                        break;
                }
                fw.close();
            } catch (Throwable throwable) {
                JOptionPane.showMessageDialog(this, "Nepodařilo se uložit soubor.");
            }
        }
    }

    private JFileChooser createFileDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON soubory", "json"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV soubory", "csv"));
        fileChooser.setDialogTitle("Vyberte, kam uložit soubor s daty");

        return fileChooser;
    }

    private void readFromFile() {
        JFileChooser fileChooser = createFileDialog();
        File selectedFile;
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION || !(selectedFile = fileChooser.getSelectedFile()).exists()) {
            return;
        }
        String extension = FilenameUtils.getExtension(selectedFile.getAbsolutePath()).toLowerCase();
        switch (extension) {
            case "json":
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
                try {
                    TaskList localTaskList = gson.fromJson(new FileReader(selectedFile.getAbsolutePath()), TaskList.class);
                    taskList.clear();
                    taskList.add(localTaskList);
                    tbl.updateUI();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "csv":
                try (
                        Reader reader = Files.newBufferedReader(Paths.get(selectedFile.getAbsolutePath()));
                        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                                .withFirstRecordAsHeader()
                                .withIgnoreHeaderCase()
                                .withTrim())
                ) {
                    taskList.clear();
                    for (CSVRecord csvRecord : csvParser) {
                        // Accessing values by the names assigned to each column
                        String description = csvRecord.get("Description");
                        String dueDateString = csvRecord.get("DueDate");
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
                        Date dueDate = df.parse(dueDateString);
                        boolean isDone = Boolean.parseBoolean(csvRecord.get("Done"));
                        tasksTableModel.addTask(new Task(description, dueDate, isDone));
                        //taskList.addTask();
                    }
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        updateUnfinishedTasks();

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
        //taskList.addTask(task);
        tasksTableModel.addTask(task);
        tbl.updateUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoMain windows = new TodoMain();
            windows.setVisible(true);
        });

    }
}
