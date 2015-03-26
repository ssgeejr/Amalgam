package amalgam.adminclient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import amalgam.util.Client;

class ClientTableModel extends AbstractTableModel {
    private boolean failedSet = false;
    private final String[] columnNames = {"Client Name","Client IP","Time"};
    private Vector data;
    public ClientTableModel() {
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

    public void addRow(Client a) {
        data.add(a);
        this.fireTableRowsInserted(data.indexOf(a), data.indexOf(a));
    }

    public Client getClient(int x) {
        return (Client)data.elementAt(x);
    }

    public void removeRow(int x) {
        data.remove(x);
        this.fireTableRowsDeleted(0, x);
    }

    public Object getValueAt(int row, int column) {
        Client a = (Client) data.get(row);
        switch (column) {
            case 0:
                return a.getClientName();
            case 1:
                return a.getClientIP();
            case 2:
                return a.getOnlineTime();
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
        Client a = (Client) data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                a.setClientName((String) aValue);
                break;
            case 1:
                a.setClientIP((String) aValue);
                break;
            case 2:
                a.setOnlineTime((String)aValue);
                break;
            default:
                break;
        }


        this.fireTableCellUpdated(rowIndex, columnIndex);
    }
}