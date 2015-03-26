package amalgam.adminclient;

import javax.swing.JTextArea;

public class ResponseTextArea extends JTextArea{

  public void append(String msg){
    super.append(new java.util.Date().toString() + ": " + msg + "\n");
    super.setCaretPosition(this.getText().length());
  }
}