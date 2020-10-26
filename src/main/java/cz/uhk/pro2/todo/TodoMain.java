package cz.uhk.pro2.todo;

import com.google.gson.*;
import com.google.gson.internal.GsonBuildConfig;
import cz.uhk.pro2.todo.gui.TaskTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

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

    public TodoMain() throws HeadlessException {
        loadJson();
        undoneLabel.setText(taskList.getUndoneTasks());

        setTitle("TODO App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnRemove);
        pnlNorth.add(undoneLabel);


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

        btnRemove.addActionListener(e -> {
            try{
                taskList.removeTask(jTable.getSelectedRow());
                jTable.addNotify();
                save();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
    }

    private void loadJson() {
        JsonParser parser = new JsonParser();
        try {
            Object obj = parser.parse(new FileReader("taskList.json"));

            JsonObject jsonObject = (JsonObject) obj;

            JsonArray companyList = (JsonArray) jsonObject.get("tasks");

            for(int i = 0; i < companyList.size() ;i++){
                taskList.addTask(
                        new Task(
                                companyList.get(i).getAsJsonObject().get("description").toString().replace('"',' ').trim(),
                                dateParse(
                                        companyList.get(i).getAsJsonObject().get("date").toString().replace('"',' ').trim()
                                ),
                                Boolean.parseBoolean(
                                        companyList.get(i).getAsJsonObject().get("done").toString().replace('"',' ').trim()
                                )
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTask() throws ParseException {
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

    private Date getDate() {
        Date date = new Date();
        String strDate = JOptionPane.showInputDialog("Write due date. (format: dd.mm.yyyy => example 01.01.2000)");
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Date dateParse(String strDate) throws ParseException {
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(strDate);
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
