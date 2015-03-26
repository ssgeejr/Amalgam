package amalgam.adminclient;

import javax.swing.UIManager;
import java.awt.*;
import java.util.Properties;
import java.io.*;

public class Amalgam {
  private String UI = "javax.swing.plaf.metal.MetalLookAndFeel";
  //Construct the application
  public Amalgam() {
      //Center the window
      Properties prop = new Properties();
      int selid = 0;
      try {
        prop.load(new FileInputStream("./conf/client.prop"));
        UI = prop.getProperty("interface");
      } catch (Exception ex) {
        prop.setProperty("interface","System");
        ex.printStackTrace();
      }finally{
        try {
          prop.store(new FileOutputStream("./conf/client.prop"),"Amalgam Admin UI Properties");
        }catch(Exception ex){}
      }

      try {
        String lookAndFeelClass = UIManager.getSystemLookAndFeelClassName();
        if(UI.equals("Metal")){
          lookAndFeelClass = "javax.swing.plaf.metal.MetalLookAndFeel";
        }else if(UI.equals("System")){
          lookAndFeelClass = UIManager.getSystemLookAndFeelClassName();
          selid = 1;
        }else if(UI.equals("GTK")){
          lookAndFeelClass = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
          selid = 2;
        }else if(UI.equals("Cross Platform")){
          lookAndFeelClass = UIManager.getCrossPlatformLookAndFeelClassName();
          selid = 3;
        }else if(UI.equals("Motif")){
          lookAndFeelClass = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
          selid = 4;
        }
        UIManager.setLookAndFeel(lookAndFeelClass);

      }catch(Exception e) {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
        e.printStackTrace();
      }
      AmalgamUI frame = new AmalgamUI();
      frame.setProperties(prop);
      frame.validate();

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = frame.getSize();
      if(frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
      }
      if(frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
      }
      frame.setLocation( (screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
      frame.setLookAndFeelID(selid);
      frame.setVisible(true);
    //      frame.login();
  }

  //Main method
  public static void main(String[] args) {
    new Amalgam();
  }
}
