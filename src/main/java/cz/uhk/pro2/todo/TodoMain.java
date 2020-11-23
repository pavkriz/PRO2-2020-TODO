package cz.uhk.pro2.todo;

import com.google.gson.*;
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

    private final TaskList taskList = new TaskList();

    private final JButton btnAdd = new JButton("Add Task");
    private final JButton btnRemove = new JButton("Remove Task");
    private final JButton btnLoadCSV = new JButton("Load CSV file");
    private final JPanel pnlNorth = new JPanel();
    private final JFrame frame = new JFrame();

    private final TaskTableModel taskTableModel = new TaskTableModel(taskList);
    private final JTable jTable = new JTable(taskTableModel);
    private final JLabel undoneLabel = new JLabel("Undone Tasks");

    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final Gson gson = gsonBuilder.create();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public TodoMain() throws HeadlessException {

        Timer timer  = new Timer(1000, e -> {
            undoneLabel.setText(taskList.getUndoneTasks());
            taskTableModel.fireTableDataChanged();
        });
        timer.start();

        loadJson();

        setTitle("TODO App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnRemove);
        pnlNorth.add(undoneLabel);
        pnlNorth.add(btnLoadCSV);

        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(jTable), BorderLayout.CENTER);
        pack();

        btnAdd.addActionListener(e -> addTask());

        btnRemove.addActionListener(e -> removeTask());

        btnLoadCSV.addActionListener(e -> loadCSV());
    }

    private void removeTask() {
        taskList.removeTask(jTable.getSelectedRow());
        jTable.addNotify();
        save();
    }

    private void loadJson() {
        JsonParser parser = new JsonParser();
        try {
            Object obj = parser.parse(new FileReader("taskList.json"));

            JsonObject jsonObject = (JsonObject) obj;

            JsonArray companyList = (JsonArray) jsonObject.get("tasks");

            for (int i = 0; i < companyList.size(); i++) {
                taskList.addTask(
                        new Task(
                                companyList.get(i).getAsJsonObject().get("description").toString().replace('"', ' ').trim(),
                                dateParse(
                                        companyList.get(i).getAsJsonObject().get("date").toString().replace('"', ' ').trim()
                                ),
                                Boolean.parseBoolean(
                                        companyList.get(i).getAsJsonObject().get("done").toString().replace('"', ' ').trim()
                                )
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCSV() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filterJson = new FileNameExtensionFilter("Json file", "json");
        FileNameExtensionFilter filterCSV = new FileNameExtensionFilter("CSV file", "csv");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(filterJson);
        fileChooser.addChoosableFileFilter(filterCSV);
        fileChooser.setDialogTitle("Select CSV file.");
        int userSelection = fileChooser.showOpenDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = getFileExtension(fileChooser);
            if (fileToLoad.getAbsolutePath().endsWith(".csv")) {
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
                JOptionPane.showMessageDialog(null, "Soubor načten: " + fileToLoad.getAbsolutePath());
            }
            if (fileToLoad.getAbsolutePath().endsWith(".json")) {
                try {
                    Gson gson = new Gson();
                    taskList.deleteAllTasks();
                    BufferedReader bfr = new BufferedReader(new FileReader(fileToLoad));
                    TaskList newTaskList = gson.fromJson(bfr, TaskList.class);
                    for (Task t : newTaskList.getTasks()) {
                        taskList.addTask(t);
                    }
                    bfr.close();
                    JOptionPane.showMessageDialog(null, "Soubor načten: " + fileToLoad.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        jTable.addNotify();
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
            // if not, append the first extension from the selected filter
            file = new File(file.toString() + '.' + extensions[0]);
        }
        return file;
    }

    private void addTask(){
        try {
            //add Task to TaskList
            taskList.addTask(new Task(
                    getDescription(), //Get Task Description
                    getDate(),  //Get Task Date
                    getIsDone())); //Get if Task is Done

            //Notify on Table Change
            jTable.addNotify();
            undoneLabel.setText(taskList.getUndoneTasks());
            //Save to JSON file
            save();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void save() {
        /*gsonBuilder.setPrettyPrinting();
        try (Writer file = new FileWriter("taskList.json")) {
            gson.toJson(taskList,file);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filterJson = new FileNameExtensionFilter("Json file", "json");
        FileNameExtensionFilter filterCSV = new FileNameExtensionFilter("CSV file", "csv");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(filterJson);
        fileChooser.addChoosableFileFilter(filterCSV);
        fileChooser.setDialogTitle("Dialog k uložení souboru");
        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getFileExtension(fileChooser);
            if (fileToSave.getAbsolutePath().endsWith(".json")) {
                try {
                    Gson gson = new Gson();
                    FileWriter fileWriter = new FileWriter(fileToSave);
                    gson.toJson(taskList, fileWriter);
                    fileWriter.flush();
                    fileWriter.close();
                    JOptionPane.showMessageDialog(null, "Soubor uložen: " + fileToSave.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileToSave.getAbsolutePath().endsWith(".csv")) {
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
                JOptionPane.showMessageDialog(null, "Soubor uložen: " + fileToSave.getAbsolutePath());
            }
        }
    }

    private String getDescription() {
        return JOptionPane.showInputDialog("Put task description here.");
    }

    private Date getDate() throws ParseException {
        String strDate = JOptionPane.showInputDialog("Write due date. (01.01.2000 12:30)");
        Date date = null;
        try{
            date = sdf.parse(strDate);
        }catch(Exception e){
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
        switch (strDone){
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
