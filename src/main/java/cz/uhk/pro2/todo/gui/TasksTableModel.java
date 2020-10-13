package cz.uhk.pro2.todo.gui;

import javax.swing.table.AbstractTableModel;

public class TasksTableModel extends AbstractTableModel{
    @Override
    public int getRowCount() {
        return 3;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        return "ABC";
    }
}
