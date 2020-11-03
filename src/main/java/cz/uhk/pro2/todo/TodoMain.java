package cz.uhk.pro2.todo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

public class TodoMain extends JFrame {
    private final JButton btnAdd = new JButton("Přidat úkol");
    private final JButton btnRmv = new JButton("Odebrat úkol");
    private final JButton btnSaveJSON = new JButton("Uložit do JSONu");
    private final JPanel pnlNorth = new JPanel();
    private final JPanel pnlSouth = new JPanel();
    private TaskList taskList = new TaskList();
    private final TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private final JTable tbl = new JTable(tasksTableModel);
    public JLabel lblUndoneTasks = new JLabel();

    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pnlSouth.add(btnSaveJSON);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnRmv);
        add(pnlNorth, BorderLayout.NORTH);
        add(pnlSouth,BorderLayout.SOUTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();
        btnSaveJSON.addActionListener(e -> saveToJSON(taskList));
        btnAdd.addActionListener(e -> addTask());
        btnRmv.addActionListener(e -> removeTask());

        taskList.addTask(new Task("Naučit se Javu", new Date(), false));
        taskList.addTask(new Task("Jit se proběhnout", new Date(), false));
        taskList.addTask(new Task("Vyvařit roušku", new Date(), false));

        refreshLabel(lblUndoneTasks);
        pnlNorth.add(lblUndoneTasks);

        Timer timer = new Timer(1000,e -> { // timer je framework, tedy vyšší úroveň, třída Thread/interface Runnable je opravdu low-level multithreading
            setTitle(new Date().toString());
        });
        timer.start();
        Timer aktualizace = new Timer(100,e -> refreshLabel(lblUndoneTasks)); // navic timer, ktery refreshuje label, kvuli editovatelnym bunkam v table modelu
        aktualizace.start();
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
