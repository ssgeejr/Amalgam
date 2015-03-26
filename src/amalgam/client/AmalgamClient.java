package amalgam.client;

import java.util.*;
import java.net.*;
import amalgam.util.TagReader;
import amalgam.util.ModuleReader;
import amalgam.util.LoggingProxy;
import amalgam.util.Module;

public class AmalgamClient {
  private TagReader tReader;
  private ModuleReader modReader;
  private ModuleReader customModReader;
  private LoggingProxy logProxy;
  private String clientName;
//  private String FILE_SEPERATOR = System.getProperties().getProperty("file.separator");
  public AmalgamClient(){
    try{
      customModReader = new ModuleReader();
      tReader = new TagReader("./conf/amalgam.client.xml","amalgam");
      clientName = tReader.getTagValue("client","name");
      if(clientName == null || clientName.length() < 1)
        throw new Exception("Client name [ amalgam|client|name ] -MUST BE DEFINED-. Check ./conf/amalgam.client.xml and reconfigure your client properties");
      logProxy = new LoggingProxy(
                new Boolean(tReader.getTagValue("logging", "enabled")).booleanValue(),
                            tReader.getTagValue("logging", "log-file"),
                            tReader.getTagValue("logging", "log-class"),
                            tReader.getTagValue("logging", "log-level"));

      logProxy.log("Client initialized on " + new java.util.Date());
      logProxy.log("Client name set to [" + clientName + "]");

      modReader = new ModuleReader(tReader.getTagValue("modules","modules"));
//      if(new Boolean(tReader.getTagValue("modules","use-custom-mods")).booleanValue()){
//        customModReader = new ModuleReader(tReader.getTagValue("modules","custom-modules"));
//      }
      logProxy.log("Modules Loaded: " + modReader.size());
//      logProxy.log("Custom Modules Loaded: " + customModReader.size());
//      modReader.addModules(customModReader);
//      logProxy.log("Total Modules Submitted to Server : " + modReader.size());
      Enumeration enm = modReader.getModules();
      Module amod = null;
      while(enm.hasMoreElements()){
        amod = (Module)enm.nextElement();
        logProxy.log("Module loaded [" + amod.getModName() + "]  Display Name [" + amod.getDiplayName() + "] Type [" + amod.getType() + "]");
      }

      logProxy.log("Opening connection to host [" + tReader.getTagValue("server","host") + "] on port [" + tReader.getTagValue("server","port") + "]");

      AmalgamClientThread clientThread = new AmalgamClientThread(new Socket(tReader.getTagValue("server","host"),
               Integer.parseInt(tReader.getTagValue("server","port"))),
               clientName,
               logProxy,
               modReader);
      clientThread.start();
    }catch(Exception excp){
      excp.printStackTrace();
    }
  }

//************************************************************************//
  public static void main(String args[]){
    new AmalgamClient();
  }

}
