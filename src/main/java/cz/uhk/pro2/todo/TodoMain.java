package cz.uhk.pro2.todo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoMain extends JFrame {
    private final JButton btnAdd = new JButton("Přidat úkol");
    private final JButton btnRmv = new JButton("Odebrat úkol");
    private final JButton btnSaveJSON = new JButton("Uložit do JSONu");
    private final JButton btnLoadJSON = new JButton("Nahrát z JSONu");
    private final JButton btnSaveCSV = new JButton("Uložit do CSV");
    private final JButton btnLoadCSV = new JButton("Nahrát z CSV");
    private final JPanel pnlNorth = new JPanel();
    private final JPanel pnlSouth = new JPanel();
    private TaskList taskList = new TaskList();
    private TaskDao taskDao = new TaskDao();
    private final TasksTableModel tasksTableModel = new TasksTableModel(taskDao);
    private JTable tbl = new JTable(tasksTableModel);
    public JLabel lblUndoneTasks = new JLabel();

    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pnlSouth.add(btnSaveJSON);
        pnlSouth.add(btnSaveCSV);
        pnlSouth.add(btnLoadJSON);
        pnlSouth.add(btnLoadCSV);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnRmv);
        add(pnlNorth, BorderLayout.NORTH);
        add(pnlSouth,BorderLayout.SOUTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();

        btnSaveJSON.addActionListener(e -> saveToJSON(taskList));
        btnLoadJSON.addActionListener(e -> loadFromJSON());
        btnSaveCSV.addActionListener(e -> saveToCSV(taskList));
        btnLoadCSV.addActionListener(e -> loadFromCSV());
        btnAdd.addActionListener(e -> addTask());
        btnRmv.addActionListener(e -> removeTask());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date d1 = new Date();
        Date d2 = new Date();
        Date d3 = new Date();
        try {
            d1 = dateFormat.parse("24.12.2020 18:00");
            d2 = dateFormat.parse("31.12.2020 23:59");
            d3 = dateFormat.parse("5.12.2020 19:30");
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        taskList.addTask(new Task("Naučit se Javu", d1, false));
        taskList.addTask(new Task("Jit se proběhnout", d2, false));
        taskList.addTask(new Task("Vyvařit roušku", d3, false));

        refreshLabel(lblUndoneTasks);
        pnlNorth.add(lblUndoneTasks);

        Timer timer = new Timer(1000,e -> { // timer je framework, tedy vyšší úroveň, třída Thread/interface Runnable je opravdu low-level multithreading
            setTitle(new Date().toString());
        });
        timer.start();
        Timer aktualizace = new Timer(100,e -> refreshLabel(lblUndoneTasks)); // navic timer, ktery refreshuje label, kvuli editovatelnym bunkam v table modelu
        aktualizace.start();
        Timer dueTimeUpdater = new Timer(10000,e -> {
            for (Task task : taskList.getTasks()) {
                tasksTableModel.countDown(task.getDueDate().getTime());
            }
            tasksTableModel.fireTableDataChanged();
        });
        dueTimeUpdater.start();
    }

    private void loadFromCSV() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("tasks.csv"),StandardCharsets.UTF_8))) {
            String s;
            java.util.List<Task> loadedTasks = new ArrayList<>();
            int i = 1;
            while ((s = br.readLine()) != null) {
                String[] columns = s.split(";");
                if (columns.length == 3) {
                    Task task = new Task();
                    task.setDescription(columns[0]);
                    SimpleDateFormat formatter = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
                    Date date = formatter.parse(columns[1]);
                    task.setDueDate(date);
                    task.setDone(Boolean.parseBoolean(columns[2]));
                    loadedTasks.add(task);
                    i++;
                } else {
                    System.out.println("INVALID column count on row " + i);
                }
            }
            TaskList noveUlohy = new TaskList(loadedTasks);
            tasksTableModel.setTaskList(noveUlohy);
            tasksTableModel.fireTableDataChanged();
            this.taskList = noveUlohy;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void saveToCSV(TaskList t) {
        this.taskList = t;
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("tasks.csv"), StandardCharsets.UTF_8))) {
            for (Task task : taskList.getTasks()) {
                pw.print(task.getDescription());
                pw.print(";");
                SimpleDateFormat formatter = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
                String dueDate = formatter.format(task.getDueDate());
                pw.print(dueDate);
                pw.print(";");
                pw.print(task.isDone());
                pw.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadFromJSON() {
        Gson gson = new Gson();
        try (Reader fileReader = new FileReader("tasks.json")) {
            taskList = gson.fromJson(fileReader,TaskList.class);
            tasksTableModel.setTaskList(taskList); // dodělány v TaskTableModelu getter a setter pro tasklist kvuli změnám v nahrávání
            tasksTableModel.fireTableDataChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToJSON(TaskList t) {
        this.taskList = t;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        try (Writer fileWriter = new FileWriter("tasks.json")){
            gson.toJson(t,fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeTask() {
        try {
            taskList.removeTask(tbl.getSelectedRow());
            tbl.addNotify();
            refreshLabel(lblUndoneTasks);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Nejprve vyberte ulohu!");
        }
    }

    private void addTask() {
        Task newTask = new Task("",new Date(),false);
        newTask.setDescription(JOptionPane.showInputDialog("Nazev nove ulohy: "));
        taskList.addTask(newTask);
        refreshLabel(lblUndoneTasks);
        tbl.addNotify(); // došlo ke změně data
    }

    public void refreshLabel(JLabel label) {
        label.setText("Počet nesplněných úkolů: " + taskList.getUndoneTasks());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoMain window = new TodoMain();
            window.setVisible(true);
        });
    }
    //pres git: VCS->Git->Fetch, a pak mergnout zmeny z master do vasi vetve
    // VCS -> Git -> Branches -> Master -> Merge Into Current

    //vicevlaknove - rozhrani Swing - Swingworker (na Androidu Background task, na jinych OS jinak zvane atd.)
    /*
    př - stahuju soubor a chci aplikaci zaroveň používat
    neco se opakuje - Swing - Timer

    Thread - synchronizace vláken je poměrně obtížná, doporučuje se používat nějaký framework a ne naši vlastní implementaci
    třída AtomicInteger - dělaný na práci s více vlákny
    knihovny třetích stran - thread safe nebo ne - knihovny se zapnutou synchronizací - nedělá se defaultně, protože synchronizace může být velmi náročná na výpočetní výkon
     */
}
