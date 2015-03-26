package amalgam.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Iterator;

public class ErrorDialog extends JDialog{

//  private StringComboBoxModel errorListModel = new StringComboBoxModel();
  private JPanel panel1 = new JPanel();
  private JPanel panel2 = new JPanel();
  private JPanel insetsPanel1 = new JPanel();
  private JButton button1 = new JButton();
  private ImageIcon image1 = new ImageIcon();
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private JLabel imageLabel = new JLabel();
  private FlowLayout flowLayout1 = new FlowLayout();
  private JPanel jPanel1 = new JPanel();
  private BorderLayout borderLayout3 = new BorderLayout();
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JPanel jPanel2 = new JPanel();
  private GridLayout gridLayout1 = new GridLayout();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JLabel jLabel3 = new JLabel();
  private boolean wasCancelled = true;
  private JPanel jPanel3 = new JPanel();
  private JTextArea jtaErrors = new JTextArea();

  public ErrorDialog(Frame parent,String error) {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
      jtaErrors.setText(error);
    }catch(Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception  {
    image1 = new ImageIcon(new amalgam.etc.ResourceLocator().getClass().getResource("error.gif"));
    imageLabel.setMaximumSize(new Dimension(97, 106));
    imageLabel.setMinimumSize(new Dimension(97, 106));
    imageLabel.setPreferredSize(new Dimension(97, 106));
    imageLabel.setRequestFocusEnabled(false);
    imageLabel.setIcon(image1);
    this.setTitle("Error Dialog");
    panel1.setLayout(borderLayout1);
    panel2.setLayout(borderLayout2);
    button1.setText("Close");
    button1.addActionListener(new ErrorDialog_button1_actionAdapter(this));
    insetsPanel1.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.RIGHT);
    jPanel1.setLayout(borderLayout3);
    jPanel2.setLayout(gridLayout1);
    gridLayout1.setRows(3);
    jLabel3.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel3.setForeground(Color.red);
    jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel3.setText("An Error(s) has occurred");
    jLabel1.setText("");
    panel1.setMinimumSize(new Dimension(515, 217));
    panel1.setPreferredSize(new Dimension(515, 217));
    this.getContentPane().add(panel1, null);
    panel2.add(imageLabel, BorderLayout.WEST);
    panel2.add(jPanel1,  BorderLayout.CENTER);
    insetsPanel1.add(button1, null);
    panel1.add(insetsPanel1, BorderLayout.SOUTH);
    panel1.add(panel2, BorderLayout.NORTH);
    jPanel1.add(jScrollPane1,  BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jtaErrors, null);
    jPanel1.add(jPanel2,  BorderLayout.NORTH);
    jPanel2.add(jLabel1, null);
    jPanel2.add(jLabel3, null);
    jPanel2.add(jLabel2, null);
    jPanel1.add(jPanel3,  BorderLayout.EAST);
    setResizable(false);
    this.setSize(new Dimension(515, 217));
  }

  public void close(){
    this.dispose();
  }
}

class ErrorDialog_button1_actionAdapter implements java.awt.event.ActionListener {
  private ErrorDialog adaptee;

  ErrorDialog_button1_actionAdapter(ErrorDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.close();
  }
}