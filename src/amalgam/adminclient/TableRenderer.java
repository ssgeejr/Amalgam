package amalgam.adminclient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class TableRenderer extends JLabel implements TableCellRenderer {

  public TableRenderer() {
    super();
    setOpaque(true);
  }

  // Here, we can just add some crazy crap to customize how to render each one.
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {

    this.setForeground(Color.black);
    setText( (String) value);
    if(row % 2 == 1) {
      setBackground(Color.lightGray);
      setForeground(Color.black);
      if(isSelected == true) {
        setForeground(Color.white);
        setBackground(Color.black);
      }
    } else {
      setBackground(Color.yellow);
      setForeground(Color.black);
      if(isSelected == true) {
        setForeground(Color.white);
        setBackground(Color.black);
      }
    }

    return this;
  }
}