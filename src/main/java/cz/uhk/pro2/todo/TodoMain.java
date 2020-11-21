package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;


import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JPanel pnlNorth = new JPanel();
    private JPanel pnlSouth = new JPanel();
    private static TaskList taskList = new TaskList();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);
    private JLabel lblUndoneTasks = new JLabel("Undone tasks: ");
    private JButton btnDel = new JButton("Odstranit úkol");
    private JButton btnSaveJSON = new JButton("Ulozit do JSON");
    private JButton btnLoadJSON = new JButton("Nacist z JSON");
    private JButton btnSaveCsv = new JButton("Ulozit do CSV");
    private JButton btnLoadCsv = new JButton("Nacist z CSV");

    public TodoMain() throws HeadlessException, IOException, ParseException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(lblUndoneTasks);
        pnlNorth.add(btnDel);

        add(pnlNorth, BorderLayout.NORTH);
        pnlSouth.add(btnSaveCsv);
        pnlSouth.add(btnLoadCsv);
        pnlSouth.add(btnSaveJSON);
        pnlSouth.add(btnLoadJSON);
        add(pnlSouth, BorderLayout.SOUTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();
        btnLoadCsv.addActionListener(e-> {
            try {
                loadCsv();
            } catch (FileNotFoundException e4) {
                // TODO Auto-generated catch block
                e4.printStackTrace();
            } catch (IOException e4) {
                // TODO Auto-generated catch block
                e4.printStackTrace();
            }
        });
        btnSaveCsv.addActionListener(e -> {
            try {
                saveCsv();
            } catch (IOException e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            }
        });
        btnAdd.addActionListener(e -> {
            try {
                addTask();
            } catch (ParseException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        });
        btnDel.addActionListener(e -> deleteTask());
        btnSaveJSON.addActionListener(e -> {
            try {
                saveJson();
            } catch (IOException e1) {
                System.out.println(e1);
            }
        });
        btnLoadJSON.addActionListener(e -> loadJson());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date d = sdf.parse("25.11.2020 23:59");
        taskList.addTask(new Task("Naučit se Javu", d, false));
        taskList.addTask(new Task("Jit se proběhnout", d, false));
        taskList.addTask(new Task("Vyvařit roušku", d, false));
        lblUndoneTasks.setText("Undone tasks: " + taskList.getUndoneTasksCount());
        Timer timer = new Timer(10000, e -> {
            tbl.revalidate();
            tbl.repaint();

        });
        timer.start();
    }

    private void saveCsv() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try (FileWriter fw = new FileWriter("tasks.csv")) {
            PrintWriter pw = new PrintWriter(fw);
            for (Task t : taskList.getTasks()) {
                pw.println(t.getDescription() + "," + sdf.format(t.getDueDate()) + "," + t.isDone());

            }
            fw.close();
        }

    }

    private void loadCsv() throws FileNotFoundException,IOException{
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        
        try(FileReader fr = new FileReader("tasks.csv")){
            taskList.setTasks(new ArrayList<>());
            String radek;
            BufferedReader br = new BufferedReader(fr);
            while((radek=br.readLine())!=null){
                if(radek.equals(""))break;
                else{
                    String[]data=radek.split(",");
                    taskList.addTask(new Task(data[0],sdf.parse(data[1]),Boolean.parseBoolean(data[2])));
                }
            }   
        } catch (ParseException e) {
            System.out.println("ParseException loadCsv");
        }
        lblUndoneTasks.setText("Undone tasks: " + taskList.getUndoneTasksCount());
        tbl.revalidate();
        tbl.repaint();
    }
    private void saveJson() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        JSONArray jsonList = new JSONArray();
        try (FileWriter fw = new FileWriter("tasks.json")) {
            for (Task t : taskList.getTasks()) {
                JSONObject taskDetails = new JSONObject();
                taskDetails.put("Description", t.getDescription());
                taskDetails.put("Due date", sdf.format(t.getDueDate()));
                taskDetails.put("Is done", String.valueOf(t.isDone()));
                JSONObject task = new JSONObject();
                task.put("Task",taskDetails);
                jsonList.add(task);

            }
            fw.write(jsonList.toJSONString());
        }

    }
    private void loadJson(){
        System.out.println("LOADING");
        taskList.setTasks(new ArrayList<>());
        JSONParser parser = new JSONParser();
        try{
            
            Object o =parser.parse(new FileReader("tasks.json"));
            JSONArray a = (JSONArray) o;
            a.forEach(task->parseTask((JSONObject)task));
            lblUndoneTasks.setText("Undone tasks: " + taskList.getUndoneTasksCount());
       
       
       
        }
        catch(IOException e){
            System.out.println(e);
        }
        catch (org.json.simple.parser.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        
        
        
    }
  
    private void parseTask(JSONObject jsontask){
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        JSONObject task = (JSONObject) jsontask.get("Task");
        String des= (String)task.get("Description");
        String date= (String)task.get("Due date");
        String isDone = (String)task.get("Is done");
        System.out.println(des + " " + date + " " + isDone);
        try {
            taskList.addTask(new Task(des, sdf.parse(date), Boolean.parseBoolean(isDone)));
        } catch (ParseException e) {
            System.out.println(date);
        }
        tbl.revalidate();
        tbl.repaint();    
       
        
    }
   
    private void deleteTask() {
        
        if(tbl.getSelectedRow()!=-1 &&tbl.getSelectedRow()!=taskList.getSize()){
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
