package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JButton btnRemove = new JButton("Odstranit úkol");

    private JPanel pnlNorth = new JPanel();
    private TaskList taskList = new TaskList();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);
    private JFrame f;

    private JLabel lblUndoneTasks = new JLabel("");


    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnRemove);

        pnlNorth.add(lblUndoneTasks);

        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();

        btnAdd.addActionListener(e -> addTask());
        btnRemove.addActionListener(e -> removeTask(tbl.getSelectedRow()));

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

        // notifikujeme tabulku, ze doslo ze zmene dat
        tasksTableModel.fireTableDataChanged();

        updateVariables();
    }

    // TODO 20.10.2020 DU1
    // Tlacitko pro smazani vybraneho radku

    private void removeTask(int rowIndex) {

        taskList.removeTask(rowIndex);
        // notifikujeme tabulku, ze doslo ze zmene dat
        tasksTableModel.fireTableDataChanged();

        updateVariables();
    }

    // TODO 20.10.2020 DU2
    // Label, ktery bude zobrazovat pocet nesplnenych tasku

    public void updateVariables() {
        lblUndoneTasks.setText("Počet nesplněných úkolů: " + taskList.getUndoneTasksCount());
    }

    // TODO 20.10.2020 DU3
    // Tlačítko na uložení seznamu tasku do JSON souboru
    // Předem definovaný název json v projektu
    // Výsledek: [{ description: "nadf" , ...},{},{}]
    // kouknout se na prednasky
    // Editovat pom.xml -> Maven + External Libraries


    public static void main(String[] args) {
        //System.out.println("Hello!!!");
        SwingUtilities.invokeLater(() -> {
            TodoMain window = new TodoMain();
            window.setVisible(true);
        });
    }
}
