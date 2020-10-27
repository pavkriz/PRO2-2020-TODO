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

    public TodoMain() throws HeadlessException, IOException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(lblUndoneTasks);
        pnlNorth.add(btnDel);
        pnlNorth.add(btnSave);
        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();
        btnAdd.addActionListener(e -> addTask());
        btnDel.addActionListener(e -> deleteTask());
        btnSave.addActionListener(e -> {
            try {
                saveJson();
            } catch (IOException e1) {
                System.out.println(e1);
            }
        });
        taskList.addTask(new Task("Naučit se Javu", new Date(), false));
        taskList.addTask(new Task("Jit se proběhnout", new Date(), false));
        taskList.addTask(new Task("Vyvařit roušku", new Date(), false));
        lblUndoneTasks.setText("Undone tasks: " + taskList.getUndoneTasksCount());
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

        taskList.removeTask(taskList.getTasks().get(tbl.getSelectedRow()));
        lblUndoneTasks.setText("Undone tasks: " + taskList.getUndoneTasksCount());
        tbl.revalidate();
        tbl.repaint();
    }

    private void addTask() {
        // TODO DU1
        // zeptame se uzivatele
        // vytvorime task a pridame do seznamu
        // notifikujeme tabulku, ze doslo ze zmene dat
        JOptionPane jo = new JOptionPane();
        String des = jo.showInputDialog("Zadejte description");
        if (des != "" || des != null) {
            taskList.addTask(new Task(des, new Date(), false));
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
                }
                
            }
        });
    }
}
