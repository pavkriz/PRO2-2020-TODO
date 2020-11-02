package cz.uhk.pro2.todo;

import com.google.gson.*;
import cz.uhk.pro2.todo.gui.TaskTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class  TodoMain extends  JFrame{

    private final TaskList taskList = new TaskList();

    private final JButton btnAdd = new JButton("Add Task");
    private final JButton btnRemove = new JButton("Remove Task");
    private final JPanel pnlNorth = new JPanel();

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

        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(jTable), BorderLayout.CENTER);
        pack();

        btnAdd.addActionListener(e -> addTask());

        btnRemove.addActionListener(e -> removeTask());
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
        gsonBuilder.setPrettyPrinting();
        try (Writer file = new FileWriter("taskList.json")) {
            gson.toJson(taskList,file);
        } catch (IOException e) {
            e.printStackTrace();
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
