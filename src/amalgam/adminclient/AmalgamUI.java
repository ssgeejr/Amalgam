package amalgam.adminclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.event.*;
import java.util.*;
import amalgam.*;
import amalgam.util.*;
import java.net.*;
import java.io.*;

public class AmalgamUI extends JFrame implements CellEditorListener{
//*****************************************//
  private int USERID;
  private LoggingProxy logProxy;
  private ServerCommunictionManager serverComm;
  private int selectedClientRow = -1;
  private int selectedModuleRow = -1;
  private String selectClientName = "";
//*****************************************//
  private ClientTableModel clientModel = new ClientTableModel();
  private ModuleTableModel moduleModel = new ModuleTableModel();
  private ArgTableModel argModel = new ArgTableModel();
  private JPanel contentPane = new JPanel();
  private JMenuBar jMenuBar1 = new JMenuBar();
  private JMenu jMenuFile = new JMenu();
  private JMenuItem jMenuFileExit = new JMenuItem();
  private JMenu jMenuHelp = new JMenu();
  private JMenuItem jMenuHelpAbout = new JMenuItem();
  private JLabel statusBar = new JLabel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel mainNorthPanel = new JPanel();
  private FlowLayout flowLayout1 = new FlowLayout();
  private JButton jbRefresh = new JButton();
  private JTabbedPane mainCenterTabPanel = new JTabbedPane();
  private JPanel tabMain = new JPanel();
  private JPanel tabClientResponse = new JPanel();
  private JMenu jmLookAndFeel = new JMenu();
  private JRadioButtonMenuItem jrbMetal = new JRadioButtonMenuItem();
  private JRadioButtonMenuItem jrbSystem = new JRadioButtonMenuItem();
  private JRadioButtonMenuItem jrbGTK = new JRadioButtonMenuItem();
  private JRadioButtonMenuItem jrbCrossPlatform = new JRadioButtonMenuItem();
  private JRadioButtonMenuItem jrbMotif = new JRadioButtonMenuItem();
  private TagReader tReader = null;
  private JButton jbSubmit = new JButton();
  private ClientListing clientListing = null;
  private GridLayout tabGridLayout = new GridLayout();
  private JPanel serverPanel = new JPanel();
  private BorderLayout borderLayout2 = new BorderLayout();
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JLabel jLabel1 = new JLabel();
  private JPanel modulePanel = new JPanel();
  private BorderLayout borderLayout3 = new BorderLayout();
  private JPanel jPanel1 = new JPanel();
  private GridLayout gridLayout2 = new GridLayout();
  private JPanel moduleTablePanel = new JPanel();
  private JScrollPane jpsModuleList = new JScrollPane();
  private JScrollPane jspArgList = new JScrollPane();
  private JLabel jLabel3 = new JLabel();
  private JLabel jLabel4 = new JLabel();
//  private JTable argTable = new JTable();
  private JTableColumnListener argTable = new JTableColumnListener();
  private JTable clientTable = new JTable();
  private JTable moduleTable = new JTable();
  private Properties prop;
  private JMenuItem jmiRefreshServers = new JMenuItem();
  private BorderLayout borderLayout4 = new BorderLayout();
  private JScrollPane resultScrollPanel = new JScrollPane();
  private ResponseTextArea jtaResults = new ResponseTextArea();
  private JPanel jpResultNorth = new JPanel();
  private FlowLayout flowLayout2 = new FlowLayout();
  private JButton jbResultSave = new JButton();
  private JButton jbResultClear = new JButton();
  private int adminThreadID;
  //Construct the frame
  public void setProperties(Properties _prop){
    prop = _prop;
  }

  public AmalgamUI() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      tReader = new TagReader("./conf/amalgam.clientadmin.xml","amalgam");
      logProxy = new LoggingProxy(
          new Boolean(tReader.getTagValue("logging", "enabled")).booleanValue(),
          		       tReader.getTagValue("logging", "log-file"),
                               tReader.getTagValue("logging", "log-class"),
                               tReader.getTagValue("logging", "log-level"));
      logProxy.log("Client Admin online....");

      serverComm = new ServerCommunictionManager(tReader.getTagValue("adminclient", "server"),
                                                 Integer.parseInt(tReader.getTagValue("adminclient", "port")),
                                                 logProxy,
                                                 jtaResults);
      jbInit();
      clientListing = serverComm.newClient();
      adminThreadID = serverComm.getAdminThreadID();
      initClientList();
    }catch(Exception e) {
      logProxy.log(e);
    }
  }
  //Component initialization
  private void jbInit() throws Exception  {
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout1);
    this.setResizable(false);
    this.setSize(new Dimension(700, 600));
    this.setTitle("Amalgam Admin Client");
    statusBar.setText(" ");
    jMenuFile.setText("File");
    jMenuFileExit.setText("Exit");
    jMenuFileExit.addActionListener(new AmalgamUI_jMenuFileExit_ActionAdapter(this));
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.addActionListener(new AmalgamUI_jMenuHelpAbout_ActionAdapter(this));
    mainNorthPanel.setLayout(flowLayout1);
    jbRefresh.setText("Refresh Listing");
    jbRefresh.addActionListener(new AmalgamUI_jbRefresh_actionAdapter(this));
    flowLayout1.setAlignment(FlowLayout.RIGHT);
    flowLayout1.setHgap(3);
    flowLayout1.setVgap(3);
    tabMain.setLayout(tabGridLayout);
    jmLookAndFeel.setText("Look & Feel");
    jrbMetal.setSelected(true);
    jrbMetal.setText("Metal");
    jrbMetal.addActionListener(new lookAndFeelAdapter(this));
    jrbSystem.setText("System");
    jrbSystem.addActionListener(new lookAndFeelAdapter(this));
    jrbGTK.setText("GTK");
    jrbGTK.addActionListener(new lookAndFeelAdapter(this));
    jrbCrossPlatform.setText("Cross Platform");
    jrbCrossPlatform.addActionListener(new lookAndFeelAdapter(this));
    jrbMotif.setText("Motif");
    jrbMotif.addActionListener(new lookAndFeelAdapter(this));
    jbSubmit.setText("Execute Command");
    jbSubmit.addActionListener(new AmalgamUI_jbSubmit_actionAdapter(this));
    tabGridLayout.setColumns(1);
    tabGridLayout.setHgap(5);
    tabGridLayout.setRows(2);
    serverPanel.setLayout(borderLayout2);
    jLabel1.setText("Server List");
    modulePanel.setLayout(borderLayout3);
    borderLayout3.setHgap(5);
    borderLayout3.setVgap(5);
    borderLayout2.setHgap(5);
    borderLayout2.setVgap(5);
    jPanel1.setLayout(gridLayout2);
    moduleTablePanel.setLayout(null);
    jpsModuleList.setBounds(new Rectangle(4, 35, 487, 180));
    jspArgList.setBounds(new Rectangle(499, 34, 186, 181));
    jLabel3.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel3.setText("Module List");
    jLabel3.setBounds(new Rectangle(4, 16, 95, 14));
    jLabel4.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel4.setText("Arguements");
    jLabel4.setBounds(new Rectangle(500, 16, 109, 14));
    clientTable.addMouseListener(new AmalgamUI_clientTable_mouseAdapter(this));
    moduleTable.addMouseListener(new AmalgamUI_moduleTable_mouseAdapter(this));
    jmiRefreshServers.setText("Refresh Servers");
    jmiRefreshServers.addActionListener(new AmalgamUI_jmiRefreshServers_actionAdapter(this));
    tabClientResponse.setLayout(borderLayout4);
    jtaResults.setText("");
    jtaResults.setTabSize(8);
    jpResultNorth.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.RIGHT);
    flowLayout2.setHgap(3);
    flowLayout2.setVgap(3);
    jbResultSave.setText("Save Output");
    jbResultSave.addActionListener(new AmalgamUI_jbResultSave_actionAdapter(this));
    jbResultClear.setText("Clear Output");
    jbResultClear.addActionListener(new AmalgamUI_jbResultClear_actionAdapter(this));
    jMenuFile.add(jmiRefreshServers);
    jMenuFile.addSeparator();
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jmLookAndFeel);
    jMenuBar1.add(jMenuHelp);
    this.setJMenuBar(jMenuBar1);
    contentPane.add(statusBar, BorderLayout.WEST);
    contentPane.add(mainNorthPanel,  BorderLayout.NORTH);
    mainNorthPanel.add(jPanel1, null);
    mainNorthPanel.add(jbSubmit, null);
    mainNorthPanel.add(jbRefresh, null);
    contentPane.add(mainCenterTabPanel,  BorderLayout.CENTER);
    mainCenterTabPanel.add(tabMain,  "Administration");
    mainCenterTabPanel.add(tabClientResponse,   "Client Response");
    tabClientResponse.add(resultScrollPanel,  BorderLayout.CENTER);
    jmLookAndFeel.add(jrbMetal);
    jmLookAndFeel.add(jrbSystem);
    jmLookAndFeel.add(jrbGTK);
    jmLookAndFeel.add(jrbCrossPlatform);
    jmLookAndFeel.add(jrbMotif);
//------
    tabMain.add(serverPanel, null);
    serverPanel.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(clientTable, null);
    serverPanel.add(jLabel1,  BorderLayout.NORTH);
    tabMain.add(modulePanel, null);
    modulePanel.add(moduleTablePanel,  BorderLayout.CENTER);
    moduleTablePanel.add(jpsModuleList, null);
    jpsModuleList.getViewport().add(moduleTable, null);
    moduleTablePanel.add(jspArgList, null);
    jspArgList.getViewport().add(argTable, null);
    moduleTablePanel.add(jLabel3, null);
    moduleTablePanel.add(jLabel4, null);
    resultScrollPanel.getViewport().add(jtaResults, null);
    tabClientResponse.add(jpResultNorth, BorderLayout.NORTH);
    jpResultNorth.add(jbResultClear, null);
    jpResultNorth.add(jbResultSave, null);
//---------------------------------------------------------------------------//
    clientTable.setModel(clientModel);
    clientTable.getDefaultEditor(String.class).addCellEditorListener(this);
    clientTable.setDefaultEditor(String.class, new TableEditor());
    clientTable.setDefaultRenderer(String.class, new TableRenderer());
    initServerColumns(clientTable);
    clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//------------------**------------------
    moduleTable.setModel(moduleModel);
    moduleTable.getDefaultEditor(String.class).addCellEditorListener(this);
    moduleTable.setDefaultEditor(String.class, new TableEditor());
    moduleTable.setDefaultRenderer(String.class, new TableRenderer());
    initModuleColumns(moduleTable);
//------------------**------------------
    argTable.setModel(argModel);
    argTable.getDefaultEditor(String.class).addCellEditorListener(this);
    argTable.setDefaultEditor(String.class, new TableEditor());
    argTable.setDefaultRenderer(String.class, new TableRenderer());
  }

//*************************************************************************************/
  public void editingStopped(ChangeEvent e) {}
  public void editingCanceled(ChangeEvent e) {}
  public boolean validateEdit(Object o, int row, int col) {return true; }
  public void jMenuFileExit_actionPerformed(ActionEvent e) { serverComm.disconnect();System.exit(0);}
//*************************************************************************************/
  //Help | About action performed
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    AboutDialog dlg = new AboutDialog(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.setVisible(true);
  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }

//*************************************************************************************/
//*************************************************//
  private void initServerColumns(JTable table) {
    TableColumn column = table.getColumnModel().getColumn(0);
    column.setPreferredWidth(75);
    column = table.getColumnModel().getColumn(1);
    column.setPreferredWidth(50);
    column = table.getColumnModel().getColumn(2);
    column.setPreferredWidth(200);
  }

  private void initModuleColumns(JTable table) {
    TableColumn column = table.getColumnModel().getColumn(0);
    column.setPreferredWidth(75);
    column = table.getColumnModel().getColumn(1);
    column.setPreferredWidth(250);
  }
//*************************************************//
  protected void login() {
    LoginDialog dlg = new LoginDialog(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation( (frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.setVisible(true);
//    dlg.show();
    try {
//      if(USERID < 0) {
//        login();
//      }
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
//*************************************************//
  private void initClientList() throws Exception{
    selectedClientRow = -1;
    selectedModuleRow = -1;
    selectClientName = "";
    Iterator itr = clientListing.getClients().iterator();
    clientModel.removeAll();
    argModel.removeAll();
    moduleModel.removeAll();
    while(itr.hasNext()){
      clientModel.addRow((Client)itr.next());
    }
  }


  public void setLookAndFeelID(int id){
    jrbMetal.setSelected(false);
    if(id == 0) jrbMetal.setSelected(true);
    else if(id == 1) jrbSystem.setSelected(true);
    else if(id == 2) jrbGTK.setSelected(true);
    else if(id == 3) jrbCrossPlatform.setSelected(true);
    else if(id == 4) jrbMotif.setSelected(true);
  }

  void swapLookAndFeel(String lookandfeel) {
    jrbMetal.setSelected(false);
    jrbSystem.setSelected(false);
    jrbGTK.setSelected(false);
    jrbCrossPlatform.setSelected(false);
    jrbMotif.setSelected(false);

    String lookAndFeelClass = UIManager.getSystemLookAndFeelClassName();
    if(lookandfeel == "Metal"){
      lookAndFeelClass = "javax.swing.plaf.metal.MetalLookAndFeel";
      jrbMetal.setSelected(true);
      prop.setProperty("interface","Metal");
    }else if(lookandfeel == "System"){
      lookAndFeelClass = UIManager.getSystemLookAndFeelClassName();
      jrbSystem.setSelected(true);
      prop.setProperty("interface","System");
    }else if(lookandfeel == "GTK"){
      lookAndFeelClass = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
      jrbGTK.setSelected(true);
      prop.setProperty("interface","GTK");
    }else if(lookandfeel == "Cross Platform"){
      lookAndFeelClass = UIManager.getCrossPlatformLookAndFeelClassName();
      jrbCrossPlatform.setSelected(true);
      prop.setProperty("interface","Cross Platform");
    }else if(lookandfeel == "Motif"){
      lookAndFeelClass = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
      jrbMotif.setSelected(true);
      prop.setProperty("interface","Motif");
    }
    try {
      prop.store(new FileOutputStream("./conf/client.prop"),"Amalgam Admin UI Properties");
    }catch(Exception ex){}

    try {
      UIManager.setLookAndFeel(lookAndFeelClass);
      SwingUtilities.updateComponentTreeUI(this);
    } catch (Exception e) {e.printStackTrace();}
  }


//  public String getLookAndFeel(String lookandfeel){
//    return lookAndFeelClass;
//  }

  public void showError(String error){
    try{
      ErrorDialog dlg = new ErrorDialog(this, error);
      Dimension dlgSize = dlg.getPreferredSize();
      Dimension frmSize = getSize();
      Point loc = getLocation();
      dlg.setLocation( (frmSize.width - 515) / 2 + loc.x, (frmSize.height - 217) / 2 + loc.y);
      dlg.setModal(true);
      dlg.setVisible(true);
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  void loadClientModules() {
    if(selectedClientRow != clientTable.getSelectedRow()){
      selectedModuleRow = -1;
      selectClientName = "";
      selectedClientRow = clientTable.getSelectedRow();
      Client client = clientModel.getClient(selectedClientRow);
      selectClientName = client.getClientName();
      moduleModel.removeAll();
      argModel.removeAll();
      Iterator itr = clientListing.getClientModules().iterator();
      Module mod = null;
      while(itr.hasNext()){
        mod = (Module)itr.next();
        if(mod.getThreadID() == client.getThreadID()){
          moduleModel.addRow(mod);
        }//end if
      }//end while
    }//end if
  }

  void executeKeyCommand(KeyEvent e) {
    if( (int) e.getKeyChar() == 10) {
      executeCommand();
    }
  }

  void executeCommand() {
    try{
      if (clientTable.getSelectedRow() > -1) {
        if (moduleTable.getSelectedRow() > -1) {
          if (argTable.isEditing())
            argTable.getCellEditor().stopCellEditing();
          Module mod = moduleModel.getModule(moduleTable.getSelectedRow());
          mod.setAdminID(adminThreadID);
          post("Executing: [" + mod.getDiplayName() + "] on Server [" +
               selectClientName + "]");
          for (int loc = 0; loc < mod.size(); loc++) {
            post("\tArguement Name: [" + mod.getArgName(loc) + "] Value: [" +
                 mod.getArgValue(loc) + "]");
          }
          post("------------- Client Resopnse -------------");
          serverComm.submitCommand(mod);
        }
      }
    }catch(Exception ex){
      showError(ex.toString());
      post(ex.toString());
      logProxy.log(ex);
    }
  }

  void loadModuleArgs() {
    if (moduleTable.getSelectedRow() > -1
        && selectedModuleRow != moduleTable.getSelectedRow()) {
      if(argTable.isEditing())
        argTable.getCellEditor().stopCellEditing();

      Module zmod = moduleModel.getModule(moduleTable.getSelectedRow());
      argModel.removeAll();
      if (zmod.size() > 0) {argModel.addRow(zmod);}
      selectedModuleRow = moduleTable.getSelectedRow();
    }//end rowcount > 0
  }

  void refreshServers(ActionEvent e) {
    try{
      initClientList();
    }catch(Exception ex){
      logProxy.log(ex);
    }
  }


  private void post(String input){
    jtaResults.append(input);
  }

  void clearClientResponse(ActionEvent e) {
    jtaResults.setText("");
  }

  void saveClientResultInfo(ActionEvent e) {
    DataOutputStream outputStream = null;
    try{
      String filepath = openSaveDialog();
      outputStream = new DataOutputStream(new FileOutputStream(filepath));
      outputStream.writeBytes(jtaResults.getText());
      outputStream.flush();
    }catch(FileCancelledException fce){
    }catch(Exception ex){
       logProxy.log(ex);
    } finally {
      try {outputStream.close();} catch (Exception ex) {}
    }
  }

  private String openSaveDialog() throws FileCancelledException, Exception{
    FileDialog dialog = new FileDialog(this,"Save File",1);
    dialog.setVisible(true);
    if(dialog.getFile() == null) throw new FileCancelledException();
    dialog.dispose();
    return dialog.getDirectory() + dialog.getFile();
  }
//******************************************************//
//----------------- END OF CLASS -----------------------//
//******************************************************//
}

class AmalgamUI_jMenuFileExit_ActionAdapter implements ActionListener {
  private AmalgamUI adaptee;

  AmalgamUI_jMenuFileExit_ActionAdapter(AmalgamUI adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuFileExit_actionPerformed(e);
  }
}

class AmalgamUI_jMenuHelpAbout_ActionAdapter implements ActionListener {
  private AmalgamUI adaptee;

  AmalgamUI_jMenuHelpAbout_ActionAdapter(AmalgamUI adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuHelpAbout_actionPerformed(e);
  }
}

class lookAndFeelAdapter implements java.awt.event.ActionListener {
  private AmalgamUI adaptee;

  lookAndFeelAdapter(AmalgamUI adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.swapLookAndFeel(e.getActionCommand());
  }
}

class AmalgamUI_jbSubmit_actionAdapter implements java.awt.event.ActionListener {
  private AmalgamUI adaptee;

  AmalgamUI_jbSubmit_actionAdapter(AmalgamUI adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.executeCommand();
  }
}

class AmalgamUI_clientTable_mouseAdapter extends java.awt.event.MouseAdapter {
  private AmalgamUI adaptee;

  AmalgamUI_clientTable_mouseAdapter(AmalgamUI adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.loadClientModules();
  }
}

class AmalgamUI_moduleTable_mouseAdapter extends java.awt.event.MouseAdapter {
  private AmalgamUI adaptee;

  AmalgamUI_moduleTable_mouseAdapter(AmalgamUI adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.loadModuleArgs();
  }
}

class AmalgamUI_jbRefresh_actionAdapter implements java.awt.event.ActionListener {
  private AmalgamUI adaptee;

  AmalgamUI_jbRefresh_actionAdapter(AmalgamUI adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.refreshServers(e);
  }
}

class AmalgamUI_jmiRefreshServers_actionAdapter implements java.awt.event.ActionListener {
  private AmalgamUI adaptee;

  AmalgamUI_jmiRefreshServers_actionAdapter(AmalgamUI adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.refreshServers(e);
  }
}

class AmalgamUI_jbResultClear_actionAdapter implements java.awt.event.ActionListener {
  private AmalgamUI adaptee;

  AmalgamUI_jbResultClear_actionAdapter(AmalgamUI adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.clearClientResponse(e);
  }
}

class AmalgamUI_jbResultSave_actionAdapter implements java.awt.event.ActionListener {
  private AmalgamUI adaptee;

  AmalgamUI_jbResultSave_actionAdapter(AmalgamUI adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.saveClientResultInfo(e);
  }
}

