package cz.uhk.pro2.todo;
import com.google.gson.Gson;
import cz.uhk.pro2.todo.gui.TaskTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class  TodoMain extends  JFrame{

    private final TaskList taskList = new TaskList();

    private final JButton btnAdd = new JButton("Add Task");
    private final JPanel pnlNorth = new JPanel();


    private final TaskTableModel taskTableModel = new TaskTableModel(taskList);
    private final JTable jTable = new JTable(taskTableModel);
    private TaskTableModel tasksTableModel = new TaskTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);
    private Label lblUndone = new Label("Počet tasku co jsi nedodělal: 0");
    private JButton saveJSON = new JButton("Uložit");
    private JButton btnDel = new JButton("Smazat z TODO");

    public TodoMain() throws HeadlessException {
        taskList.addTask(new Task("Uč se JAVU", new Date(), false));
        taskList.addTask(new Task("Jdi si zaběhat!", new Date(), false));
        taskList.addTask(new Task("Jdi spát", new Date(), false));

        setTitle("Aplikace mých TODO");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnDel);
        pnlNorth.add(saveJSON);
        btnDel.addActionListener(e -> delTask());
        saveJSON.addActionListener(e -> saveFile());
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
    }

    private void addTask() throws ParseException {
        //add Task to TaskList
        taskList.addTask(new Task(
                getDescription(), //Get Task Description
                getDate(),  //Get Task Date
                getIsDone())); //Get if Task is Done

        //Notify on Table Change
        jTable.addNotify();

        for(int i =0; i<taskList.getTasks().size();i++){
            System.out.println(taskList.getTasks().get(i));
        }
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
    private void saveFile() {
        try {
            Gson gson = new Gson();
            FileWriter fw = new FileWriter("todo.json");
            gson.toJson(taskList, fw);
            fw.flush();
            fw.close();

            JOptionPane.showMessageDialog(null, "TODO bylo uloženo do kořenového adresáře");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getUpdatedInfo() {
        lblUndone.setText("Počet nedokončených tasků: " + taskList.getUndoneTasks());
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