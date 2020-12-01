package cz.uhk.pro2.todo;

import com.google.gson.*;
import cz.uhk.pro2.todo.database.TaskDao;
import cz.uhk.pro2.todo.gui.TaskTableModel;
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

    private final JButton btnAdd = new JButton("Add Task");
    private final JButton btnRemove = new JButton("Remove Task");
    private final JButton btnLoadFile = new JButton("Load file");
    private final JButton btnSave = new JButton("Save file");
    private final JPanel pnlNorth = new JPanel();
    private final JFrame frame = new JFrame();


    private final TaskDao taskDao = new TaskDao();
    private final TaskList taskList = new TaskList(taskDao.findAll());
    private final TaskTableModel taskTableModel = new TaskTableModel(taskList);
    private final JTable jTable = new JTable(taskTableModel);
    private final JLabel undoneLabel = new JLabel("Undone Tasks");

    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final Gson gson = gsonBuilder.create();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private final Timer timer = new Timer(1000, e -> {
        undoneLabel.setText(taskList.getUndoneTasks());
        taskTableModel.notify();
    });

    public TodoMain() throws HeadlessException {

        setTitle("TODO App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnRemove);
        pnlNorth.add(undoneLabel);
        pnlNorth.add(btnLoadFile);
        pnlNorth.add(btnSave);

        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(jTable), BorderLayout.CENTER);
        pack();

        btnAdd.addActionListener(e -> addTask());

        btnRemove.addActionListener(e -> removeTask());

        btnLoadFile.addActionListener(e -> loadFile());

        btnSave.addActionListener(e -> save());
    }

    private void removeTask() {
        taskList.removeTask(jTable.getSelectedRow());
        taskDao.delete(jTable.getSelectedRow());
        jTable.addNotify();
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        initFileChooser(fileChooser);
        fileChooser.setDialogTitle("Select file.");
        int userSelection = fileChooser.showOpenDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = getFileExtension(fileChooser);
            if (fileToLoad.getAbsolutePath().endsWith(".csv")) {
                loadCSV(fileToLoad);
                JOptionPane.showMessageDialog(null, "File loaded: " + fileToLoad.getAbsolutePath());
            }
            if (fileToLoad.getAbsolutePath().endsWith(".json")) {
                loadJSON(fileToLoad);
                JOptionPane.showMessageDialog(null, "File loaded: " + fileToLoad.getAbsolutePath());
            }
        }
        jTable.addNotify();
    }

    private void loadCSV(File fileToLoad) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(fileToLoad));

            String r;
            int row = 0;
            taskList.deleteAllTasks();
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
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadJSON(File fileToLoad) {
        try {
            Gson gson = new Gson();
            taskList.deleteAllTasks();
            BufferedReader bfr = new BufferedReader(new FileReader(fileToLoad));
            TaskList newTaskList = gson.fromJson(bfr, TaskList.class);
            for (Task t : newTaskList.getTasks()) {
                taskList.addTask(t);
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFileExtension(JFileChooser fileChooser) {
        File file = fileChooser.getSelectedFile();
        if (fileChooser.getFileFilter() instanceof FileNameExtensionFilter) {
            String[] extensions = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions();
            String nameLower = file.getName().toLowerCase();
            for (String ext : extensions) {
                if (nameLower.endsWith('.' + ext.toLowerCase())) {
                    return file;
                }
            }
            file = new File(file.toString() + '.' + extensions[0]);
        }
        return file;
    }

    private void addTask() {
        try {
            Task task = new Task(
                    getId(), //Get Task ID
                    getDescription(), //Get Task Description
                    getDate(),  //Get Task Date
                    getIsDone());
            //add Task to TaskList
            taskList.addTask(task); //Get if Task is Done

            //Notify on Table Change
            jTable.addNotify();
            undoneLabel.setText(taskList.getUndoneTasks());
            //Save to JSON file
            //save();
            taskTableModel.fireTableDataChanged();
            taskDao.save(task);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    private void save() {
        JFileChooser fileChooser = new JFileChooser();
        initFileChooser(fileChooser);

        fileChooser.setDialogTitle("Save file");
        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getFileExtension(fileChooser);
            if (fileToSave.getAbsolutePath().endsWith(".json")) {
                saveJSON(fileToSave);
                JOptionPane.showMessageDialog(null, "File saved: " + fileToSave.getAbsolutePath());
            }
            if (fileToSave.getAbsolutePath().endsWith(".csv")) {
                saveCSV(fileToSave);
                JOptionPane.showMessageDialog(null, "File saved: " + fileToSave.getAbsolutePath());
            }
        }
    }

    private void initFileChooser(JFileChooser fileChooser) {
        FileNameExtensionFilter filterJson = new FileNameExtensionFilter("Json file", "json");
        FileNameExtensionFilter filterCSV = new FileNameExtensionFilter("CSV file", "csv");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(filterJson);
        fileChooser.addChoosableFileFilter(filterCSV);
    }

    private void saveCSV(File fileToSave) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileToSave));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            for (Task tsk : taskList.getTasks()) {
                bufferedWriter.write(tsk.getDescription() + ";" + simpleDateFormat.format(tsk.getDate()) + ";" + tsk.isDone() + "\n");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveJSON(File fileToSave) {
        try {
            Gson gson = new Gson();
            FileWriter fileWriter = new FileWriter(fileToSave);
            gson.toJson(taskList, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getId() {
        long id = 0;
        if (taskList.getTasks().size() > 0) {
            id = taskList.getTasks().get(taskList.getTasks().size() - 1).getId();
        }
        return id++;
    }

    private String getDescription() {
        return JOptionPane.showInputDialog("Put task description here.");
    }

    private Date getDate() throws ParseException {
        String strDate = JOptionPane.showInputDialog("Write due date. (01.01.2000 12:30)");
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (Exception e) {
            getDate();
        }
        return date;
    }

    private Date dateParse(String strDate) throws ParseException {
        Date date = sdf.parse(strDate);
        return date;
    }

    private boolean getIsDone() {
        int strDone = JOptionPane.showConfirmDialog(null,
                "Is this task done?.",
                "Select",
                JOptionPane.YES_NO_OPTION);
        switch (strDone) {
            case 0:
                return true;
            case 1:
                return false;
            default:
                JOptionPane.showMessageDialog(null, "ErrorMsg", "Failure", JOptionPane.ERROR_MESSAGE);
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
