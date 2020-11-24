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
    private JPanel pnlNorth = new JPanel();
    //private TaskList taskList = new TaskList();
    private TaskDao taskDao = new TaskDao();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskDao);
    private JTable tbl = new JTable(tasksTableModel);
    private JLabel lblUndoneTasks = new JLabel("Počet nesplněných úkolů: ");

    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(lblUndoneTasks);
        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pack();
        btnAdd.addActionListener(e -> addTask());
//        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
//        try {
//            Date d = sdf.parse("28.10.2020 13:00");
//            Date now = new Date();
//            long diffMilis = d.getTime() - now.getTime();
//            taskList.addTask(new Task("Naučit se Javu", d, true));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        taskList.addTask(new Task("Jit se proběhnout", new Date(), false));
//        taskList.addTask(new Task("Vyvařit roušku", new Date(), false));
//        Timer timer  = new Timer(1000, e -> {
//            setTitle(new Date().toString());
//        });
//        timer.start();
    }

    private void addTask() {
        // TODO 13.10.2020 DU1
        // zeptame se uzivatele
        // vytvorime task a pridame do seznamu
        // notifikujeme tabulku, ze doslo ze zmene dat
        //taskDao.save(task);
        tasksTableModel.reloadData();
    }

    // TODO 20.10.2020 DU1
    // Tlacitko pro smazani vybraneho radku

    // TODO 20.10.2020 DU2
    // Label, ktery bude zobrazovat pocet nesplnenych tasku

    // TODO 20.10.2020 DU3
    // Tlacitko na ulozeni seznamu tasku do JSON souboru
    // [{ description:"Naucit se Javu",.... },{  },{  }]

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
