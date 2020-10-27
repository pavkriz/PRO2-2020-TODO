package cz.uhk.pro2.todo;

import com.google.gson.Gson;
import cz.uhk.pro2.todo.GUI.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JPanel pnlNorth = new JPanel();
    private TaskList taskList = new TaskList();
    private JPanel pnlSouth = new JPanel();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);
    private JButton btnDel = new JButton("Odstranit úkol");
    private Label lblUndone = new Label("Počet nedokončených tasků: 0");
    private JButton saveJSON = new JButton("Uložit");


    public TodoMain() throws HeadlessException{
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlSouth.add(btnDel);
        pnlNorth.add(lblUndone);
        pnlSouth.add(saveJSON);
        add(pnlSouth, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();
        btnAdd.addActionListener(e -> addTask());
        btnDel.addActionListener(e -> delTask());
        saveJSON.addActionListener(e -> saveFile()); //json save
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            taskList.addTask(new Task("default", sdf.parse("12.12.2020 12:00"), true));
            taskList.addTask(new Task("default2", sdf.parse("12.12.2021 12:00"), true));
            taskList.addTask(new Task("default3", sdf.parse("12.12.2020 18:00"), true));
            taskList.addTask(new Task("default4", sdf.parse("27.10.2020 15:00"), true));
            taskList.addTask(new Task("default5", sdf.parse("27.10.2020 13:50"), true));
        } catch (Exception c) {
            c.printStackTrace();
        }
    }


    private void saveFile() {
        try {
            Gson gson = new Gson();
            FileWriter fw = new FileWriter("tasks.json");
            gson.toJson(taskList, fw);
            fw.flush();
            fw.close();

            JOptionPane.showMessageDialog(null, "Uloženo do kořen. adresáře!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //deleteButton
    //Label undone tasks
    //button to save into JSON


    private void addTask(){
        String desc = JOptionPane.showInputDialog("Zadej popis tasku");
        String sdate = JOptionPane.showInputDialog("Zadej datum ve formátu dd.mm.yyyy hh:mm");

        Date date;
        try{
            date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(sdate);
        }catch(Exception e){
            date = new Date();
        }

        boolean done;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Je task hotový?");
        done = dialogResult == JOptionPane.YES_OPTION;


        taskList.addTask(new Task(desc,date,done));
        getUpdatedInfo();
        tbl.addNotify();
    }

    private void delTask(){
        int selected = tbl.getSelectedRow();
        if(selected != -1) {
            tasksTableModel.removeRow(selected);
            taskList.removeTask(taskList.getTasks().get(selected));
            tbl.clearSelection();
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
}
