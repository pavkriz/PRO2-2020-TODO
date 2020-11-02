package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicInteger;


public class TodoMain extends JFrame {

    private JButton btnAdd = new JButton("Přidat úkol");
    private JButton btnDelete = new JButton("Odebrat vybraný úkol");
    private JButton btnSave = new JButton("Uložit seznam");
    private JPanel pnlNorth = new JPanel();
    private TaskList taskList = new TaskList();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);
    private JLabel lblUndoneTasks = new JLabel("Pocet nesplnených úkolů: 0");

    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnDelete);
        pnlNorth.add(btnSave);
        pnlNorth.add(lblUndoneTasks);
        add(pnlNorth, BorderLayout.NORTH);
        add(tbl, BorderLayout.CENTER);
        btnAdd.addActionListener(e ->{
            addTask();
            updateUndoneTasks();
        });
        btnDelete.addActionListener(e ->{
            deleteTask();
            updateUndoneTasks();
        });
        btnSave.addActionListener(e -> saveTasks());
        pack();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            Date d = sdf.parse("28.10.2020 13:00");
            Date now = new Date();
            long diffMilis = d.getTime() - now.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timer timer  = new Timer(1000, e -> {
            setTitle(new Date().toString());
        });
        timer.start();
    }

    private void addTask() {
        String desc = JOptionPane.showInputDialog("Zadej popis tasku");
        String sdate = JOptionPane.showInputDialog("Zadej datum ve formátu dd/mm/YYYY");

        Date date;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(sdate);
        } catch (Exception e) {
            date = new Date();
        }

        boolean done;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Je task hotový?");
        done = dialogResult == JOptionPane.YES_OPTION;

        taskList.addTask(new Task(desc, date, done));
        tbl.addNotify();
        tbl.updateUI();
    }

    private void deleteTask(){
        int selectRow = tbl.getSelectedRow();
        if(selectRow != -1){
            taskList.removeTask(selectRow);
        }
        tbl.updateUI();
    }

    private void updateUndoneTasks() {
        lblUndoneTasks.setText("Počet nesplněných úkolů: " + taskList.getUndoneTasksCount());
    }

    private void saveTasks() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON soubory", "json"));
        fileChooser.setDialogTitle("Vyberte, kam uložit soubor s daty");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                FileWriter fw = new FileWriter(fileToSave);
                fw.write(gson.toJson(taskList));
                fw.close();
            }
            catch (Throwable throwable) {
                JOptionPane.showMessageDialog(this, "Soubor se nepodařilo uložit");
            }
        }
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

