package cz.uhk.pro2.todo;

import com.google.gson.Gson;
import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JButton btnRemove = new JButton("Odstranit úkol");
    private JButton btnConvert = new JButton("Převeď na JSON");

    private JPanel pnlNorth = new JPanel();
    private TaskList taskList = new TaskList();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);
    private JFrame f;

    private JLabel lblUndoneTasks = new JLabel("");


    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600,600));

        pnlNorth.add(btnAdd);
        pnlNorth.add(btnRemove);
        pnlNorth.add(btnConvert);

        pnlNorth.add(lblUndoneTasks);

        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();

        btnAdd.addActionListener(e -> addTask());
        btnRemove.addActionListener(e -> removeTask(tbl.getSelectedRow()));
        btnConvert.addActionListener(e -> saveAsJSON());

        taskList.addTask(new Task("Naučit se Javu", new Date(), false));
        taskList.addTask(new Task("Jit se proběhnout", new Date(), false));
        taskList.addTask(new Task("Vyvařit roušku", new Date(), false));

        updateVariables();
    }

    private void addTask() {
        // TODO 13.10. 2020 DU1

        f = new JFrame();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm HH:mm");

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
            e.printStackTrace();
        }

        updateVariables();
    }

    // TODO 20.10.2020 DU1
    // Tlacitko pro smazani vybraneho radku

    private void removeTask(int rowIndex) {

        taskList.removeTask(rowIndex);
        updateVariables();
    }

    // TODO 20.10.2020 DU2
    // Label, ktery bude zobrazovat pocet nesplnenych tasku

    public void updateVariables() {
        // notifikujeme tabulku, ze doslo ze zmene dat
        tasksTableModel.fireTableDataChanged();
        // Změní label s množstvím nesplněných úkolů
        lblUndoneTasks.setText("Nesplněné úkoly: " + taskList.getUndoneTasksCount());
    }

    // TODO 20.10.2020 DU3
    // Tlačítko na uložení seznamu tasku do JSON souboru
    // Předem definovaný název json v projektu
    // Výsledek: [{ description: "nadf" , ...},{},{}]

    private void saveAsJSON() {
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


    public static void main(String[] args) {
        //System.out.println("Hello!!!");
        SwingUtilities.invokeLater(() -> {
            TodoMain window = new TodoMain();
            window.setVisible(true);
        });
    }
}
