package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.dao.TaskDao;
import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JButton btnRemove = new JButton("Odstranit úkol");
    private JButton btnToJSON = new JButton("Převeď na JSON");
    private JButton btnFromJSON = new JButton("Načti z JSON");
    private JButton btnToCSV = new JButton("Převeď na CSV");
    private JButton btnFromCSV = new JButton("Načti z CSV");


    private JPanel pnlNorth = new JPanel();
    //private TaskList taskList = new TaskList();
    private TaskDao taskDao = new TaskDao();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskDao);
    private JPanel pnlSouth = new JPanel();

    private TaskList taskList = new TaskList();
    //private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);
    private JFrame f;

    private JLabel lblUndoneTasks = new JLabel("Počet nesplněných úkolů: ");

    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600,600));

        pnlNorth.add(btnAdd);
        pnlNorth.add(btnRemove);
        pnlNorth.add(lblUndoneTasks);

        pnlSouth.add(btnToJSON);
        pnlSouth.add(btnFromJSON);
        pnlSouth.add(btnToCSV);
        pnlSouth.add(btnFromCSV);


        add(pnlNorth, BorderLayout.NORTH);
        add(pnlSouth, BorderLayout.SOUTH);

        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();

        btnAdd.addActionListener(e -> addTask());
        btnRemove.addActionListener(e -> removeTask(tbl.getSelectedRow()));

        btnToJSON.addActionListener(e -> saveJSON());
        btnFromJSON.addActionListener(e -> loadJSON());
        btnToCSV.addActionListener(e -> saveCSV());
        btnFromCSV.addActionListener(e-> loadCSV());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
        try {
            Date d = sdf.parse("30.10.20 13:00");
            Date now = new Date();
            taskList.addTask(new Task("Naučit se Javu", d, true));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        taskList.addTask(new Task("Jit se proběhnout", new Date(), false));
        taskList.addTask(new Task("Vyvařit roušku", new Date(), false));

        Timer hodiny  = new Timer(1000, e -> setTitle(new Date().toString()));
        hodiny.start();


        // TODO 27.10.2020 DU4
        // Zobrazovat dalsi sloupec s poctem dni, klolik zbyva do dokonceni ukolu (dueDate)
        // Kazdych 10 sekund tento udaj aktualizovat

        Timer casDokonceni = new Timer(10000, e -> {
            for (int i = 0; i < taskList.getTasksCount(); i++)
                tasksTableModel.fireTableCellUpdated(i,3);
        });
        casDokonceni.start();

        updateVariables();
    }

    private void addTask() {
        // TODO 13.10.2020 DU1

        f = new JFrame();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");

        // zeptame se uzivatele
        String jmeno = JOptionPane.showInputDialog(f,"Zadej jméno úkolu");
        String datum = JOptionPane.showInputDialog(f,"Zadej konečné datum formátu dd.mm HH:mm");

        Date dueDate;
        try {
            // Přeformátujeme uživatel přidaný string na daný formát
            dueDate = sdf.parse(datum);
            // vytvorime task a pridame do seznamu
            taskList.addTask(new Task(jmeno, dueDate, false));

        } catch (ParseException e) {
            taskList.addTask(new Task());
        }
        finally {
            updateVariables();
        }

    }

    // 0.10.2020 DU1
    // Tlacitko pro smazani vybraneho radku

    private void removeTask(int rowIndex) {

        taskList.removeTask(rowIndex);
        updateVariables();
    }

    // TODO 20.10.2020 DU2
    // Label, ktery bude zobrazovat pocet nesplnenych tasku

    public void updateVariables() {
        // notifikujeme tabulku, ze doslo ze zmene dat
        //taskDao.save(task);
        tasksTableModel.reloadData();
        tasksTableModel.fireTableDataChanged();
        // Změní label s množstvím nesplněných úkolů
        lblUndoneTasks.setText("Nesplněné úkoly: " + taskList.getUndoneTasksCount());
    }

    // TODO 20.10.2020 DU3
    // Tlačítko na uložení seznamu tasku do JSON souboru
    // Předem definovaný název json v projektu
    // Výsledek: [{ description: "nadf" , ...},{},{}]

    private void saveJSON() {
        if (taskList.getTasksCount() == 0) System.out.println("Seznam neobsahuje žádné úkoly.");
        else {
            try {
                Gson gson = new Gson();
                Writer w = new FileWriter("SeznamUkolu.json");

                // Převede taskList jako String formátu json
                gson.toJson(taskList, w);

                // Writer "navalí" data to souboru pomoci metody flush() a následně zavře Writer metodou close()
                w.flush();
                w.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO 10.11 DU
    // Nacitani z JSONu a ukladani a nacitani CSV

    private void loadJSON() {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader("SeznamUkolu.json");
            taskList = gson.fromJson(reader, TaskList.class);
            tasksTableModel.setTaskList(taskList);
            reader.close();

            updateVariables();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCSV() {
        try (PrintWriter w =
                     new PrintWriter(
                             new OutputStreamWriter(
                                     new FileOutputStream("SeznamUkolu.csv"), StandardCharsets.UTF_8
                             )
                     )
        ) {
            for (Task p : taskList.getTasks()) {
                // vytiskneme 1 radek do souboru
                w.print(p.getDescription());
                w.print(";");
                w.print(p.getDueDate());
                w.print(";");
                w.print(p.isDone());
                w.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCSV() {
        try (BufferedReader r =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream("SeznamUkolu.csv"), StandardCharsets.UTF_8))) {
            String s;
            int i = 1;
            taskList.removeAll();
            taskList = new TaskList();

            while ((s = r.readLine()) != null) {
                // precteme jeden radek a vyrobime z nej instanci osoby a pridame ji do seznamu
                String[] cols = s.split(";");

                if (cols.length == 3) {

                    Date dueDate  = new SimpleDateFormat("dd.MM.yy HH:mm").parse(cols[1]);

                    taskList.addTask(
                            new Task(cols[0],
                            dueDate,
                            cols[2].equals("true")));
                } else {
                    System.out.println("WARNING: wrong column count on row " + i);
                }
                i++;
            }
            tasksTableModel.setTaskList(taskList);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //System.out.println("Hello!!!");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TodoMain window = new TodoMain();
                window.setVisible(true);
            }
        });
    }
}
