package cz.uhk.pro2.todo;

import com.google.gson.Gson;
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

public class  TodoMain extends  JFrame{



    private final JButton btnAdd = new JButton("Add Task");
    private final JPanel pnlNorth = new JPanel();

    private final TaskDao taskDao = new TaskDao();
    private final TaskList taskList = new TaskList(taskDao.findAll());
    private final TaskTableModel taskTableModel = new TaskTableModel(taskList);
    private final JTable jTable = new JTable(taskTableModel);
    private final JLabel undoneLabel = new JLabel("Nesplněné Todo");
    private TaskTableModel tasksTableModel = new TaskTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);
    private Label lblUndone = new Label("Počet tasku co jsi nedodělal: 0");
    private JButton saveJSON = new JButton("Uložit");
    private final JButton btnLoadFile = new JButton("Otevřít soubor");
    private final JButton btnSave = new JButton("Uložit soubor");
    JButton loadBtn = new JButton("Otevřít");
    private JButton btnDel = new JButton("Smazat z TODO");



        private final Timer timer = new Timer(1000, e -> {
            undoneLabel.setText(taskList.getUndoneTasks());
            taskTableModel.notify();
        });

    public TodoMain() throws HeadlessException {
        setTitle("Aplikace mých TODO");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnDel);
        pnlNorth.add(saveJSON);
        pnlNorth.add(loadBtn);
        pnlNorth.add(undoneLabel);
        pnlNorth.add(btnLoadFile);
        pnlNorth.add(btnSave);
        btnDel.addActionListener(e -> delTask());
        saveJSON.addActionListener(e -> saveFile());
        loadBtn.addActionListener(e -> loadFile());
        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(jTable), BorderLayout.CENTER);
        pack();

        btnAdd.addActionListener(e -> {
            try {
                addTask();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        });
        btnLoadFile.addActionListener(e -> loadFile());

        btnSave.addActionListener(e -> saveFile());
    }


    private void addTask() throws ParseException {
        //add Task to TaskList
        Task task = new Task(
                getId(), //Get Task ID
                getDescription(), //Get Task Description
                getDate(),  //Get Task Date
                getIsDone()); //Get if Task is Done
        taskList.addTask(task);

        //Notify on Table Change
        jTable.addNotify();
        undoneLabel.setText(taskList.getUndoneTasks());
        taskTableModel.fireTableDataChanged();
        taskDao.save(task);
        for(int i =0; i<taskList.getTasks().size();i++){
            System.out.println(taskList.getTasks().get(i));
        }
    }
    private void getUpdatedInfo() {
        lblUndone.setText("Počet nedokončených tasků: " + taskList.getUndoneTasks());
    }

    private void delTask(){
        int selected = tbl.getSelectedRow();
        if(selected != -1) {
            tasksTableModel.removeRow(selected);
            taskList.removeTask(taskList.getTasks().get(selected));
            tbl.clearSelection();
            taskDao.delete(jTable.getSelectedRow());
            tbl.addNotify();


            getUpdatedInfo();
            JOptionPane.showMessageDialog(null, "Řádek vymazán!");
        }
    }

    private String getDescription() {
        return JOptionPane.showInputDialog("Napiš sem tvoje TODO");
    }

    private Date getDate() {
        Date date = new Date();
        return date;
    }

    private boolean getIsDone() {
        int strDone = JOptionPane.showConfirmDialog(null,
                "Je toto TODO splněno?",
                "Vyber",
                JOptionPane.YES_NO_OPTION);
        switch (strDone){
            case 0:
                return true;
            case 1:
                return false;
            default:
                JOptionPane.showMessageDialog(null, "ERROR", "CHYBA", JOptionPane.ERROR_MESSAGE);
        }
        return false;
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
    private void saveFile() {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        initFileChooser(fileChooser);

        fileChooser.setDialogTitle("Uložení souboru");
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getSelectedFileWithExtension(fileChooser);
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
    private long getId() {
        long id = 0;
        if (taskList.getTasks().size() > 0) {
            id = taskList.getTasks().get(taskList.getTasks().size() - 1).getId();
        }
        return id++;
    }




    private void loadJSON(File fileToLoad) {
        try {
            Gson gson = new Gson();
            taskList.deleteTasks();
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
    private void loadCSV(File fileToLoad) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(fileToLoad));

            String r;
            int row = 0;
            taskList.deleteTasks();
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
    private void loadFile() {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        initFileChooser(fileChooser);
        fileChooser.setDialogTitle("Select file.");
        int userSelection = fileChooser.showOpenDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = getSelectedFileWithExtension(fileChooser);
            if (fileToLoad.getAbsolutePath().endsWith(".csv")) {
                loadCSV(fileToLoad);
                JOptionPane.showMessageDialog(null, "File loaded: " + fileToLoad.getAbsolutePath());
            }
            if (fileToLoad.getAbsolutePath().endsWith(".json")) {
                loadJSON(fileToLoad);
                JOptionPane.showMessageDialog(null, "File loaded: " + fileToLoad.getAbsolutePath());
            }


        getUpdatedInfo();
        }
    }
            public static void main(String[] args) {
                SwingUtilities.invokeLater(() -> {
                    TodoMain windows = new TodoMain();
                    windows.setVisible(true);

                });
            }
    }