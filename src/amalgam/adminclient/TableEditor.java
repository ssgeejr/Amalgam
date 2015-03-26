package amalgam.adminclient;

/*************************************************************************
 Copyright (C) 2003  Steve Gee
 stevesgee@cox.net

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *************************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class TableEditor extends AbstractCellEditor implements TableCellEditor {

    static JTextField jt = new JTextField();
    int maxLength = 20;
    boolean caps = false;

    String initVal = new String();

    // Constructors are located here.
    public TableEditor() {
        super();
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int rowIndex, int vColIndex) {
        // We're getting a string value in here.  So, just load it up.
        ((JTextField) jt).setBorder(null);
        initVal = ((String) value);
        jt.setText((String) value);
        return (JTextField) jt;
    }

    public Object getCellEditorValue() {
        try {
            return jt.getText();
        } catch (Exception e) {
            return "";
        }
    }
}