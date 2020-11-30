package cz.uhk.pro2.todo;

import com.google.gson.Gson;
import cz.uhk.pro2.todo.GUI.TasksTableModel;
import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoMain extends JFrame {
    private TaskDao taskDao = new TaskDao();
    private TaskList taskList = new TaskList(taskDao.findAll());
    private TasksTableModel tasksTableModel = new TasksTableModel(taskDao);
    private JTable tbl = new JTable(tasksTableModel);
    private Label lblUndone = new Label("Počet nedokončených tasků: 0");


    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JButton btnAdd = new JButton("Přidat úkol");
        JPanel pnlNorth = new JPanel();
        pnlNorth.add(btnAdd);
        JPanel pnlSouth = new JPanel();
        JButton btnDel = new JButton("Odstranit úkol");
        pnlSouth.add(btnDel);
        pnlNorth.add(lblUndone);
        JButton saveBtn = new JButton("Uložit");
        JButton loadBtn = new JButton("Načíst");
        pnlSouth.add(saveBtn);
        pnlSouth.add(loadBtn);
        add(pnlSouth, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();
        btnAdd.addActionListener(e -> addTask());
        btnDel.addActionListener(e -> delTask());
        saveBtn.addActionListener(e -> saveFile()); // save btn
        loadBtn.addActionListener(e -> loadCSV());
        //SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        /*try {
            taskList.addTask(new Task("default", sdf.parse("12.12.2020 12:00"), true));
            taskList.addTask(new Task("default2", sdf.parse("12.12.2021 12:00"), true));
            taskList.addTask(new Task("default3", sdf.parse("12.12.2020 18:00"), true));
            taskList.addTask(new Task("default4", sdf.parse("27.11.2020 15:00"), true));
            taskList.addTask(new Task("default5", sdf.parse("27.11.2020 13:50"), true));
        } catch (Exception c) {
            c.printStackTrace();
        }*/
        getUpdatedInfo();
    }

    public static File getSelectedFileWithExtension(JFileChooser c) {
        File file = c.getSelectedFile();
        if (c.getFileFilter() instanceof FileNameExtensionFilter) {
            String[] exts = ((FileNameExtensionFilter) c.getFileFilter()).getExtensions();
            String nameLower = file.getName().toLowerCase();
            for (String ext : exts) { // check if it already has a valid extension
                if (nameLower.endsWith('.' + ext.toLowerCase())) {
                    return file; // if yes -> return file
                }
            }
            // if not, append the first extension from the selected filter
            file = new File(file.toString() + '.' + exts[0]);
        }
        return file;
    }

    private void saveFile() {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filterJson = new FileNameExtensionFilter("Json file", "json");
        FileNameExtensionFilter filterCSV = new FileNameExtensionFilter("CSV file", "csv");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(filterJson);
        fileChooser.addChoosableFileFilter(filterCSV);
        fileChooser.setDialogTitle("Dialog k uložení souboru");
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getSelectedFileWithExtension(fileChooser);
            if (fileToSave.getAbsolutePath().endsWith(".json")) {
                try {
                    Gson gson = new Gson();
                    FileWriter fw = new FileWriter(fileToSave);
                    gson.toJson(taskList, fw);
                    fw.flush();
                    fw.close();
                    JOptionPane.showMessageDialog(null, "Soubor uložen: " + fileToSave.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileToSave.getAbsolutePath().endsWith(".csv")) {
                try {
                    BufferedWriter bwriter = new BufferedWriter(new FileWriter(fileToSave));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    for (Task t : taskList.getTasks()) {
                        bwriter.write(t.getDescription() + ";" + sdf.format(t.getDueDate()) + ";" + t.isDone() + "\n");
                    }
                    bwriter.flush();
                    bwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Soubor uložen: " + fileToSave.getAbsolutePath());
            }
        }
    }

    private void addTask(){
        String desc = JOptionPane.showInputDialog("Zadej popis tasku");
        String sdate = JOptionPane.showInputDialog("Zadej datum ve formátu dd.mm.yyyy hh:mm");

        Date date;
        try {
            date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(sdate);
        } catch (Exception e) {
            date = new Date();
        }

        boolean done;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Je task hotový?");
        done = dialogResult == JOptionPane.YES_OPTION;

        Task t = new Task(desc, date, done);
        taskDao.save(t);
        taskList.addTask(t);
        System.out.println("Task ID: " + t.getId());
        tasksTableModel.reloadData();
        tbl.addNotify();
        getUpdatedInfo();
    }

    private void delTask(){
        int selected = tbl.getSelectedRow();
        if(selected != -1) {
            Task t;
            t = taskList.getTasks().get(selected);
            taskDao.delete(t);
            taskList.removeTask(t);
            tbl.clearSelection();
            tasksTableModel.reloadData();
            tbl.addNotify();
            getUpdatedInfo();
            JOptionPane.showMessageDialog(null, "Řádek vymazán!");
        }
    }

    private void getUpdatedInfo() {
        lblUndone.setText("Počet nedokončených tasků: " + taskList.getUndoneTasks());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoMain windows = new TodoMain();
            windows.setVisible(true);
        });
    }

    private void loadCSV() {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filterJson = new FileNameExtensionFilter("Json file", "json");
        FileNameExtensionFilter filterCSV = new FileNameExtensionFilter("CSV file", "csv");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(filterJson);
        fileChooser.addChoosableFileFilter(filterCSV);
        fileChooser.setDialogTitle("Vyber CSV soubor k načtení");
        int userSelection = fileChooser.showOpenDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = getSelectedFileWithExtension(fileChooser);
            if (fileToLoad.getAbsolutePath().endsWith(".csv")) {
                try {
                    BufferedReader bfr = new BufferedReader(new FileReader(fileToLoad));

                    String r;
                    int row = 0;
                    taskList.clear();
                    while ((r = bfr.readLine()) != null) {
                        String[] columns = r.split(";");
                        if (columns.length == 3) {
                            Date dueDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(columns[1]);
                            taskList.addTask(new Task(columns[0], dueDate, Boolean.parseBoolean(columns[2])));
                        } else {
                            System.out.println("Bad column count on row #" + row);
                        }
                        row++;
                    }
                    tasksTableModel.loadTasks(taskList);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Soubor načten: " + fileToLoad.getAbsolutePath());
            }
            if (fileToLoad.getAbsolutePath().endsWith(".json")) {
                try {
                    Gson gson = new Gson();
                    taskList.clear();
                    BufferedReader bfr = new BufferedReader(new FileReader(fileToLoad));
                    TaskList newTl = gson.fromJson(bfr, TaskList.class);
                    for (Task t : newTl.getTasks()) {
                        taskList.addTask(t);
                    }
                    bfr.close();
                    tasksTableModel.loadTasks(taskList);
                    JOptionPane.showMessageDialog(null, "Soubor načten: " + fileToLoad.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        tasksTableModel.reloadData();
        getUpdatedInfo();
        tbl.addNotify();
    }
}
