package amalgam.adminclient;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * <p>Title: GUIComponents</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2002 - 2005</p>
 *
 * <p>Company: Core Data Resources</p>
 *
 * @author James Warlick
 * @version 1.0
 */


/**
 * JTableColumnListener is custom implementation of JTable, providing full row selection,
 * custom edit handling, and keyboard navigation.  It extends JTable, so it
 * provides all functionality normally found in JTable.  In addition, it
 * implements GCEditableComponent, so it can be used in GCDetailPanel objects
 * to have full edit detection.
 */
public class JTableColumnListener extends JTable {

    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition,
                                        boolean pressed) {

        int key = ks.getKeyCode();
        boolean returnVal;

        if (key == e.VK_TAB ||
            key == e.VK_ENTER ||
            key == e.VK_UP ||
            key == e.VK_DOWN ||
            key == e.VK_LEFT ||
            key == e.VK_RIGHT) {

            returnVal = super.processKeyBinding(ks, e, condition, pressed);

            if (pressed == false && ks.isOnKeyRelease()) {
                if (this.isCellEditable(this.getSelectedRow(),
                                        this.getSelectedColumn())) {
                    this.editCellAt(this.getSelectedRow(),
                                    this.getSelectedColumn(), e);
                    JComponent j = (JComponent)this.getEditorComponent();
                    j.grabFocus();
                }
            }
            return returnVal;
        } else {
            return super.processKeyBinding(ks, e, condition, pressed);
        }
    }
}