package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JPanel pnlNorth = new JPanel();
    private TaskList taskList = new TaskList();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);
    private JLabel lblUndoneTasks = new JLabel("Undone tasks: ");
    private JButton btnDel = new JButton("Odstranit úkol");
    private JButton btnSave = new JButton("Ulozit do JSON");

    public TodoMain() throws HeadlessException, IOException, ParseException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(lblUndoneTasks);
        pnlNorth.add(btnDel);
        pnlNorth.add(btnSave);
        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();
        btnAdd.addActionListener(e -> {
            try {
                addTask();
            } catch (ParseException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        });
        btnDel.addActionListener(e -> deleteTask());
        btnSave.addActionListener(e -> {
            try {
                saveJson();
            } catch (IOException e1) {
                System.out.println(e1);
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date d = sdf.parse("25.11.2020 23:59");
        taskList.addTask(new Task("Naučit se Javu", d, false));
        taskList.addTask(new Task("Jit se proběhnout", d, false));
        taskList.addTask(new Task("Vyvařit roušku", d, false));
        lblUndoneTasks.setText("Undone tasks: " + taskList.getUndoneTasksCount());
        Timer timer  = new Timer(10000, e -> {
            tbl.revalidate();
            tbl.repaint();
            
        });
        timer.start();
    }

    private void saveJson() throws IOException {
        JSONArray jsonList = new JSONArray();
        try (FileWriter fw = new FileWriter("tasks.json")) {
            for (Task t : taskList.getTasks()) {
                JSONObject task = new JSONObject();
                task.put("Description", t.getDescription());
                task.put("Due date", t.getDueDate());
                task.put("Is done", t.isDone());
                jsonList.add(task);

            }
            fw.write(jsonList.toJSONString());
        }

    }

    private void deleteTask() {
        if(tbl.getSelectedRow()!=-1){
        taskList.removeTask(taskList.getTasks().get(tbl.getSelectedRow()));
        lblUndoneTasks.setText("Undone tasks: " + taskList.getUndoneTasksCount());
        tbl.revalidate();
        tbl.repaint();}
    }

    private void addTask() throws ParseException {
        // TODO DU1
        // zeptame se uzivatele
        // vytvorime task a pridame do seznamu
        // notifikujeme tabulku, ze doslo ze zmene dat
        JOptionPane jo = new JOptionPane();
        String des = jo.showInputDialog("Zadejte description");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date d = new Date();
         try {
            String date=jo.showInputDialog("Zadejte datum ve tvaru: 'dd.MM.yyyy HH:mm'");
            d = sdf.parse(date);
        } catch (HeadlessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        if (des != "" || des != null) {
            taskList.addTask(new Task(des, d, false));
        }
        lblUndoneTasks.setText("Undone tasks: " + taskList.getUndoneTasksCount());
        tbl.revalidate();
        tbl.repaint();

    }

    public static void main(String[] args) {
        // System.out.println("Hello!!!");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TodoMain window;
                try {
                    window = new TodoMain();
                    window.setVisible(true);
                } catch (HeadlessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        });
    }
}
