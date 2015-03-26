package amalgam.client;

import java.net.*;
import java.util.*;
import amalgam.util.LoggingProxy;
import java.io.*;
import amalgam.util.ModuleReader;
import amalgam.util.Module;
import amalgam.interfaces.*;
import amalgam.modules.*;

public class AmalgamClientThread extends Thread
	implements ClientServerCommunication{
//---------------------------------------------//
  private LoggingProxy logProxy;
  private Socket skt;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private ModuleReader modReader;
  private String clientName;
  private int serverResponse;
  private final boolean online = true;
//---------------------------------------------//

  public AmalgamClientThread(Socket _skt, String _clientName, LoggingProxy _lp, ModuleReader baseMod){
    skt = _skt;
    clientName = _clientName;
    logProxy = _lp;
    modReader = baseMod;
  }

  public void run(){
    try {
      oos = new ObjectOutputStream(skt.getOutputStream());
      oos.writeInt(NEW_CLIENT);
      oos.flush();
      oos.writeObject(clientName);
      oos.flush();
      oos.writeObject(modReader);
      oos.flush();
      ois = new ObjectInputStream(skt.getInputStream());
      if(ois.readInt() != ACK){
        logProxy.log("Server respnosed with a NON-ACK response. Is the client name unique?");
        oos.writeInt(CLOSE_CONNECTION);
        oos.flush();
        skt.close();
        System.exit(-1);
      }
      logProxy.log("====== SUCCESSFULLY REGISTERED WITH THE SERVER, NOW ENTERING LISTEN MODE ======");
      logProxy.log("~~~~~~~~~~~~~~~~~{ENTERING SERVER MODE (listening)}~~~~~~~~~~~~~~~~~");
      int inputValue = -1;
      while(online) {
        inputValue = ois.readInt();
        logProxy.log("[" + inputValue + "] recived @: " + new java.util.Date());
        System.out.println("--------- MSG RECIEVED ----------");
        Module command = (Module)ois.readObject();
        logProxy.log("Process the following command [" + command.getModName() + "]");
        logProxy.log("Arguements");
        for(int arg_i = 0; arg_i < command.size();arg_i++){
          logProxy.log("\tName [" + command.getArgName(arg_i) + "] Value [" + command.getArgValue(arg_i) + "]");
        }
        oos.writeInt(COMMAND_RESPONSE);
        oos.flush();
        try {
          logProxy.log("Attempting to instantiate: " + command.getClassName());
          AmalgamModuleInterface iface = (AmalgamModuleInterface) (Class.forName(command.getClassName())).newInstance();
          command = iface.executeCommand(command);
          command.setResponseResult(ACK);
        }catch (Exception x) {
          logProxy.log(x);
          command.setResponseResult(NACK);
          command.setException(x);
        }
        oos.writeObject(command);
        oos.flush();
      }
    }catch(Exception excp){
      logProxy.log(excp);
    }
  }

}
