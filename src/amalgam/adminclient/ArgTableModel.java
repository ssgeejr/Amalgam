package amalgam.adminclient;


import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import amalgam.util.*;

class ArgTableModel extends AbstractTableModel {
    private boolean failedSet = false;
    private final String[] columnNames = {"Arguement","Value"};
    private Vector data;
    public ArgTableModel() {
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
        Arguement arg = null;
        Enumeration enm = a.elements();
        while(enm.hasMoreElements()){
          arg = (Arguement)enm.nextElement();
          data.add(a);
          this.fireTableRowsInserted(data.indexOf(a), data.indexOf(a));
      }
    }

    public Module getProperty(int x) {
        return (Module)data.elementAt(x);
    }

    public void removeRow(int x) {
        data.remove(x);
        this.fireTableRowsDeleted(0, x);
    }

    public Object getValueAt(int row, int column) {

      if(column == 0)
        return ((Module) data.get(row)).getArgName(row);
      else{
        return ((Module) data.get(row)).getArgValue(row);
      }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex == 1);
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
        Module a = (Module) data.get((rowIndex));
        a.setArgValue(rowIndex, (String) aValue);
        data.set(rowIndex,a);
        this.fireTableCellUpdated(rowIndex, columnIndex);
    }
}