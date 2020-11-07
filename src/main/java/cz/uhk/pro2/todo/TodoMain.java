package cz.uhk.pro2.todo;

import cz.uhk.pro2.todo.gui.TasksTableModel;
import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TodoMain extends JFrame {
    private JButton btnAdd = new JButton("Přidat úkol");
    private JButton btnRemove = new JButton("Smazat úkol");
    private JButton btnExport = new JButton("Exportovat do JSON");
    private JPanel pnlNorth = new JPanel();
    private JPanel pnlSouth = new JPanel();
    private TaskList taskList = new TaskList();
    private TasksTableModel tasksTableModel = new TasksTableModel(taskList);
    private JTable tbl = new JTable(tasksTableModel);
    private JLabel lblUndoneTasks = new JLabel("Počet nesplněných úkolů:");
    private JFileChooser fileChooser = new JFileChooser();
    private Gson gson = new Gson();

    public TodoMain() throws HeadlessException {
        setTitle("TODO app");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnRemove);
        pnlNorth.add(btnExport);
        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        pnlSouth.add(lblUndoneTasks);
        add(pnlSouth, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        btnAdd.addActionListener(e -> addTask());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            Date d = sdf.parse("28.10.2020 13:00");
            Date now = new Date();
            long diffMilis = d.getTime() - now.getTime();
            taskList.addTask(new Task("Naučit se Javu", d, true));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        btnRemove.addActionListener(e -> removeTask());
        btnExport.addActionListener(e -> exportToJson());
        taskList.addTask(new Task("Naučit se Javu", new Date(), false));
        taskList.addTask(new Task("Jit se proběhnout", new Date(), false));
        taskList.addTask(new Task("Vyvařit roušku", new Date(), false));
        Timer timer  = new Timer(1000, e -> {
            setTitle(new Date().toString());
        });
        timer.start();
        updateUndoneTasksLabel();
        fileChooser.setFileFilter(new FileNameExtensionFilter("json file", "json"));
        fileChooser.setSelectedFile(new File("tasks.json"));
    }

    private void addTask() {
        String description = JOptionPane.showInputDialog("Zadejte úkol:");
        taskList.addTask(new Task(description, new Date(), false));
        tasksTableModel.fireTableDataChanged();
        updateUndoneTasksLabel();
    }

    private void removeTask() {
        // Tlacitko pro smazani vybraneho radku
        int selectedRowCount = tbl.getSelectedRowCount();
        if (selectedRowCount == 1) {
            taskList.removeTask(tbl.getSelectedRow());
        } else {
            JOptionPane.showMessageDialog(this,"Nebyl vybrán žádný úkol.");
            return;
        }
        tasksTableModel.fireTableDataChanged();
        updateUndoneTasksLabel();
    }

    private void updateUndoneTasksLabel() {
        // Label, ktery bude zobrazovat pocet nesplnenych tasku
        lblUndoneTasks.setText("Počet nesplněných úkolů: " + taskList.getUndoneTasksCount());
    }

    private void exportToJson() {
        // Tlacitko na ulozeni seznamu tasku do JSON souboru
        // [{ description:"Naucit se Javu",.... },{  },{  }]
        int returnValue = fileChooser.showSaveDialog(this);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.append(gson.toJson(taskList.getTasks()));
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
