package amalgam.adminclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import amalgam.etc.*;

public class LoginDialog extends JDialog implements ActionListener {

  private JPanel panel1 = new JPanel();
  private JPanel panel2 = new JPanel();
  private JPanel insetsPanel1 = new JPanel();
  private JPanel insetsPanel3 = new JPanel();
  private JButton button1 = new JButton();
  private JLabel label2 = new JLabel();
  private ImageIcon image1 = new ImageIcon();
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JTextField jtUser = new JTextField();
  private JPasswordField jtPassword = new JPasswordField();
  private JLabel imageLabel = new JLabel();
  private FlowLayout flowLayout1 = new FlowLayout();
  private JButton jbFail = new JButton();
  private GridLayout gridLayout2 = new GridLayout();
  private JPanel jPanel3 = new JPanel();
  private GridLayout gridLayout4 = new GridLayout();
  private JLabel jLabel3 = new JLabel();


  public LoginDialog(Frame parent) {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception  {
    image1 = new ImageIcon(new amalgam.etc.ResourceLocator().getClass().getResource("login.jpg"));
    imageLabel.setIcon(image1);
    this.setTitle("Amalgam");
    panel1.setLayout(borderLayout1);
    panel2.setLayout(borderLayout2);
    label2.setFont(new java.awt.Font("Dialog", 1, 11));
    label2.setForeground(Color.red);
    label2.setHorizontalAlignment(SwingConstants.CENTER);
    label2.setText("Amalgam Admin Client Login");
    insetsPanel3.setLayout(gridLayout2);
    insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 10));
    insetsPanel3.setDebugGraphicsOptions(0);
    insetsPanel3.setToolTipText("");
    button1.setText("Login");
    button1.addActionListener(this);
    jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
    jLabel1.setText("Username      ");
    jLabel2.setRequestFocusEnabled(true);
    jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
    jLabel2.setHorizontalTextPosition(SwingConstants.TRAILING);
    jLabel2.setText("Password      ");
    insetsPanel1.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.RIGHT);
    jbFail.setText("Exit");
    jbFail.addActionListener(this);
    jtUser.setText("");
    jtUser.addKeyListener(new LoginDialog_jtUser_keyAdapter(this));
    jPanel3.setLayout(gridLayout4);
    gridLayout4.setColumns(1);
    gridLayout4.setRows(4);
    gridLayout2.setColumns(0);
    gridLayout2.setHgap(0);
    gridLayout2.setRows(2);
    jLabel3.setText("");
    jtPassword.addKeyListener(new LoginDialog_jtPassword_keyAdapter(this));
    this.getContentPane().add(panel1, null);
    panel2.add(insetsPanel3, BorderLayout.CENTER);
    insetsPanel3.add(label2, null);
    insetsPanel3.add(jPanel3, null);
    jPanel3.add(jLabel1, null);
    jPanel3.add(jtUser, null);
    jPanel3.add(jLabel2, null);
    jPanel3.add(jtPassword, null);
    jPanel3.add(jLabel3, null);
    insetsPanel1.add(jbFail, null);
    insetsPanel1.add(button1, null);
    panel1.add(insetsPanel1, BorderLayout.SOUTH);
    panel1.add(panel2, BorderLayout.NORTH);
    panel2.add(imageLabel,  BorderLayout.WEST);
    setResizable(false);
    this.setSize(new Dimension(428, 175));
  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(1);
    }
    super.processWindowEvent(e);
  }
  //Close the dialog
  void attemptLogin() {
    dispose();
  }
  //Close the dialog on a button event
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button1) {
      attemptLogin();
    }else if(e.getSource() == jbFail){
      System.exit(1);
    }
  }

  protected String getUser(){ return jtUser.getText(); }
  protected String getPasswd(){ return new String(jtPassword.getPassword());}

  void executeLogin(KeyEvent e) {
    if((int)e.getKeyChar() == 10){
      attemptLogin();
    }
  }

  void focusPassword(KeyEvent e) {
    if((int)e.getKeyChar() == 10){
      jtPassword.requestFocus();
    }
  }



}

class LoginDialog_jtPassword_keyAdapter extends java.awt.event.KeyAdapter {
  private LoginDialog adaptee;

  LoginDialog_jtPassword_keyAdapter(LoginDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void keyTyped(KeyEvent e) {
    adaptee.executeLogin(e);
  }
}

class LoginDialog_jtUser_keyAdapter extends java.awt.event.KeyAdapter {
  private LoginDialog adaptee;

  LoginDialog_jtUser_keyAdapter(LoginDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void keyPressed(KeyEvent e) {
    adaptee.focusPassword(e);
  }
}