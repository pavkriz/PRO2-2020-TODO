package cz.uhk.pro2.todo;

import com.google.gson.Gson;
import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JButton btnRemove = new JButton("Smazat úkol");
    private JButton btnImportJson = new JButton("Import JSON");
    private JButton btnExportJson = new JButton("Export JSON");
    private JButton btnImportCsv = new JButton("Import CSV");
    private JButton btnExportCsv = new JButton("Export CSV");
    private JPanel pnlNorth = new JPanel();
    private JPanel pnlSouth = new JPanel();
    private TaskDao taskDao = new TaskDao();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskDao);
    private JTable tbl = new JTable(tasksTableModel);
    private JLabel lblUndoneTasks = new JLabel("Počet nesplněných úkolů:");
    private JFileChooser jsonFileChooser = new JFileChooser();
    private JFileChooser csvFileChooser = new JFileChooser();
    private Gson gson = new Gson();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnRemove);
        pnlNorth.add(btnImportJson);
        pnlNorth.add(btnExportJson);
        pnlNorth.add(btnImportCsv);
        pnlNorth.add(btnExportCsv);
        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pnlSouth.add(lblUndoneTasks);
        add(pnlSouth, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        btnAdd.addActionListener(e -> addTask());
        btnRemove.addActionListener(e -> removeTask());
        btnImportJson.addActionListener(e -> importFromJson());
        btnExportJson.addActionListener(e -> exportToJson());
        btnImportCsv.addActionListener(e -> importFromCsv());
        btnExportCsv.addActionListener(e -> exportToCsv());
        Timer timer  = new Timer(1000, e -> {
            setTitle(new Date().toString());
        });
        timer.start();
        tasksTableModel.addTableModelListener(e -> {
            if(e.getColumn() == 2) {
                updateUndoneTasksLabel();
            }
        });
        updateUndoneTasksLabel();
        jsonFileChooser.setFileFilter(new FileNameExtensionFilter("json file", "json"));
        jsonFileChooser.setSelectedFile(new File("tasks.json"));
        csvFileChooser.setFileFilter(new FileNameExtensionFilter("Comma separated values", "csv"));
        csvFileChooser.setSelectedFile(new File("tasks.csv"));

        new Timer(1000, e -> tasksTableModel.updateDueDateColumn()).start();
    }

    private void addTask() {
        // TODO 13.10.2020 DU1
        // zeptame se uzivatele
        // vytvorime task a pridame do seznamu
        // notifikujeme tabulku, ze doslo ze zmene dat
        //taskDao.save(task);
        JTextField txtTask = new JTextField();
        JTextField txtDueDate = new JTextField(sdf.format(new Date()));
        JPanel pnlAddTask = new JPanel(new GridLayout(2,2));
        pnlAddTask.add(new JLabel("Ukol:"));
        pnlAddTask.add(txtTask);
        pnlAddTask.add(new JLabel("Splnit do:"));
        pnlAddTask.add(txtDueDate);

        int clickedButton = JOptionPane.showConfirmDialog(null, pnlAddTask, "Zadejte úkol:", JOptionPane.OK_CANCEL_OPTION);
        if(clickedButton == JOptionPane.OK_OPTION) {
            try {
                taskDao.save(new Task(txtTask.getText(), sdf.parse(txtDueDate.getText()), false));
                tasksTableModel.reloadData();
                updateUndoneTasksLabel();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        tasksTableModel.reloadData();
    }

    private void removeTask() {
        // Tlacitko pro smazani vybraneho radku
        int selectedRowCount = tbl.getSelectedRowCount();
        if (selectedRowCount == 1) {
            Task t = tasksTableModel.getTask(tbl.getSelectedRow());
            taskDao.delete(t.getId());
        } else {
            JOptionPane.showMessageDialog(this,"Nebyl vybrán žádný úkol.");
            return;
        }
        tasksTableModel.reloadData();
    }

    private void updateUndoneTasksLabel() {
        // Label, ktery bude zobrazovat pocet nesplnenych tasku
        lblUndoneTasks.setText("Počet nesplněných úkolů: " + tasksTableModel.getUndoneTasksCount());
    }

    private void importFromJson() {
        try {
            int result = jsonFileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = jsonFileChooser.getSelectedFile();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                Task[] tasks = gson.fromJson(bufferedReader, Task[].class);
                tasksTableModel.loadTasks(Arrays.asList(tasks));
                updateUndoneTasksLabel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportToJson() {
        // Tlacitko na ulozeni seznamu tasku do JSON souboru
        // [{ description:"Naucit se Javu",.... },{  },{  }]
        int returnValue = jsonFileChooser.showSaveDialog(this);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File file = jsonFileChooser.getSelectedFile();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.append(gson.toJson(tasksTableModel.getTasks()));
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void importFromCsv() {
        int returnValue = csvFileChooser.showOpenDialog(this);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File file = csvFileChooser.getSelectedFile();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                List<Task> tasks = new ArrayList();
                String line;
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(",");
                    if (split.length != 3) throw new RuntimeException("Invalid CSV format");
                    tasks.add(new Task(split[0], sdf.parse(split[1]), Boolean.parseBoolean(split[2])));
                }
                tasksTableModel.loadTasks(tasks);
                tasksTableModel.reloadData();
                updateUndoneTasksLabel();
            } catch (IOException | ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void exportToCsv() {
        int returnValue = csvFileChooser.showSaveDialog(this);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File file = csvFileChooser.getSelectedFile();
                StringBuilder csvBuilder = new StringBuilder("description,dueDate,done");
                tasksTableModel.getTasks().forEach(task -> {
                    csvBuilder.append(System.lineSeparator());
                    csvBuilder.append((String.format("%s,%s,%s", task.getDescription(), sdf.format(task.getDueDate()), task.isDone())));
                });
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.append(csvBuilder.toString());
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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
