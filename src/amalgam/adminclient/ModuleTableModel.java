package amalgam.adminclient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import amalgam.util.Module;

class ModuleTableModel extends AbstractTableModel {
    private boolean failedSet = false;
    private final String[] columnNames = {"Module","Description"};
    private Vector data;
    public ModuleTableModel() {
        this.data = new Vector();
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void addRow(Module a) {
        data.add(a);
        this.fireTableRowsInserted(data.indexOf(a), data.indexOf(a));
    }

    public Module getModule(int x) {
        return (Module)data.elementAt(x);
    }

    public void removeRow(int x) {
        data.remove(x);
        this.fireTableRowsDeleted(0, x);
    }

    public Object getValueAt(int row, int column) {
        Module a = (Module) data.get(row);
        switch (column) {
            case 0:
                return a.getDiplayName();
            case 1:
                return a.getDefinition();
            default:
                return "";
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    public void removeAll() {
        int rowcount = this.getRowCount();
        data.removeAllElements();
        this.fireTableRowsDeleted(0, rowcount);

    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Module a = (Module) data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                a.setDiplayName((String) aValue);
                break;
            case 1:
                a.setDefinition((String) aValue);
                break;
            default:
                break;
        }


        this.fireTableCellUpdated(rowIndex, columnIndex);
    }
}