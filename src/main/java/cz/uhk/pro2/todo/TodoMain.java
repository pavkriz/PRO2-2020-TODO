package cz.uhk.pro2.todo;

import com.google.gson.Gson;
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
import java.util.Date;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JButton btndel = new JButton("Smazat úkol");
    private JButton btntoJSON = new JButton("Převeď na JSON");
    private JButton btnFromJSON = new JButton("Načti z JSON");
    private JButton btnToCSV = new JButton("Převeď na CSV");
    private JButton btnFromCSV = new JButton("Načti z CSV");


    private JPanel pnlNorth = new JPanel();
    private TaskList taskList = new TaskList();
    private TaskDao taskDao = new TaskDao();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskDao);
    private JTable tbl = new JTable(tasksTableModel);
    private JLabel lblUndoneTasks = new JLabel("Počet nesplněných úkolů: " + taskList.getUndoneTasksCount());
    private JFrame f;

    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(pnlNorth, BorderLayout.NORTH);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btndel);
        pnlNorth.add(btntoJSON);
        pnlNorth.add(btnFromJSON);
        pnlNorth.add(btnToCSV);
        pnlNorth.add(btnFromCSV);
        pnlNorth.add(lblUndoneTasks);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();

        taskList.addTask(new Task("Naučit se Javu", new Date(), false));
    /*   SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            Date d = sdf.parse("18.11.2020 13:00");
            Date now = new Date();
            long diffMills = d.getTime() - now.getTime();
            taskList.addTask(new Task("Naučit je Javu", d, true));
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

        //vytvořené vzorové tasky
        taskList.addTask(new Task("Jít na procházku", new Date(), false));
        taskList.addTask(new Task("Najíst se", new Date(), false));

        //timer pro GUI
        Timer timer = new Timer(1000, e -> {
            setTitle(new Date().toString());
        });
        timer.start();
*/

        // TODO 4 27.10.
        //  Zobrazovat dalsi sloupec s poctem dni, klolik zbyva do dokonceni ukolu (dueDate)
        //  Kazdych 10 sekund tento udaj aktualizovat
        Timer dokonceni = new Timer(10000, e -> {
            for (int i = 0; i < taskList.getUndoneTasksCount(); i++) {
                tasksTableModel.fireTableCellUpdated(i, 3);
            }
        });
        dokonceni.start();
        updateVariables();


        //listenery pro tlačítka
        btnAdd.addActionListener(e -> addTask());
        btndel.addActionListener(e -> removeTask(tbl.getSelectedRow()));
        btntoJSON.addActionListener(e -> saveAsJSON());
        btnFromJSON.addActionListener(e -> loadJSON());
        btnToCSV.addActionListener(e -> saveCSV());
        btnFromCSV.addActionListener(e-> loadCSV());
    }

    private void addTask() {
        // TODO 1 13.10.
        //   zeptáme se uživatele
        //   vytvoříme task a pridame do seznamu
        //   notifikujeme tabulku, ze doslo ke zmene dat
        f = new JFrame();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm HH:mm");

        //tázání se uživatele
                String ukol = JOptionPane.showInputDialog(f, "Zadej další úkol");
                String datum = JOptionPane.showInputDialog(f, "Zadej datum dokončení ve formátu dd.mm HH:mm");
                //nastavení času dokončení a přidání tasku do seznamu
                Date dueDate;
                try {
                    //přeformátování získaného datumu a následné přidání do nového tasku -> seznamu
                    dueDate = sdf.parse(datum);
                    taskList.addTask(new Task(ukol, dueDate, false));

        } catch (ParseException e) {
            taskList.addTask(new Task());
        } finally {
            updateVariables();
        }
    }

    private void removeTask(int rowIndex) {
        // TODO 2 20.10.
        //  přidat tlačítko pro smazání vybraného řádku
        //  budeme potřebovat tlačítko, Listener, zjistit, který řádek je vybraný
        //  Vybraný řádek smazat (podle indexu) a notifikovat tabulku jako při přidávání tasku
        taskList.removeTask(rowIndex);
        updateVariables();
    }

    public void updateVariables() {
        // TODO 2 20.10.
        //  label, který bude zobrazovat pocet nesplněných tasků - aktualizovat
        //  přes setText na JLabelu zaktualizovat co v něm je napsáno
        tasksTableModel.fireTableDataChanged();                                             //notifikace tabulky, že došlo ke změně dat
        lblUndoneTasks.setText("Nespleněné úkoly: " + taskList.getUndoneTasksCount());      //změna labelu s počtem undone tasků
    }

    private void saveAsJSON() {
        //TODO 3 20.10.
        //  tlačítko na uložení seznamu tasku do JSON souboru
        if (taskList.getTasksCount() == 0) System.out.println("Seznam nemá žádné úkoly");
        else {
            try {
                Gson gson = new Gson();
                Writer w = new FileWriter("SeznamUkolu.json");
                // Převod taskListu jako String formátu json
                gson.toJson(taskList, w);

                // Writer pošle data to souboru flush() a pak zavře Writer pomocí close()
                w.flush();
                w.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
                //vytisknutí 1 řádku do souboru
                w.print(p.getDescription());
                w.print(";");
                w.print(p.getDueDate());
                w.print(";");
                w.print(p.isDone());
                w.print(";");
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
                    Date dueDate = new SimpleDateFormat("dd.MM.yy HH:mm").parse(cols[1]);
                    taskList.addTask(new Task(cols[0], dueDate, cols[2].equals("true")));
                } else {
                    System.out.println("WARNING: špatný počet sloupců na řádku " + i);
                }
                i++;
            }
            tasksTableModel.setTaskList(taskList);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
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

