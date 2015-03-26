package amalgam.adminclient;

import java.util.*;
import java.net.*;
import amalgam.util.*;
import java.io.*;
import javax.swing.JTextArea;
import amalgam.interfaces.ClientServerCommunication;
//extends Thread
public class ServerCommunictionManager implements ClientServerCommunication{
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private boolean ONLINE = true;
  private Socket socket;
  private LoggingProxy logProxy;
  private Heartbeat hearbeat;
  private JTextArea results;
  private heartbeatLsnr hbListener;
  private int adminID = -1;

  public ServerCommunictionManager(String host, int port,LoggingProxy lp, JTextArea jtaResults) throws Exception{
    socket = new Socket(host,port);
    hbListener = new heartbeatLsnr();
    logProxy = lp;
    hearbeat = new Heartbeat(hbListener);
    results = jtaResults;
  }
  public void offline(){ONLINE = false;}

  public ClientListing newClient() throws Exception{
    oos = new ObjectOutputStream(socket.getOutputStream());
    logProxy.log("...REGISTERING WITH SERVER...");
    oos.writeInt(NEW_ADMIN);
    oos.flush();
    ois = new ObjectInputStream(socket.getInputStream());
    adminID = ois.readInt();
    int responseid = ois.readInt();
    return fetchNewList();
  }

  public ClientListing fetchNewList() throws Exception{
    logProxy.log("Requisting client list...");
//    oos = new ObjectOutputStream(socket.getOutputStream());
    oos.writeInt(SEND_CLIENT_LIST);
    oos.flush();
//    ois = new ObjectInputStream(socket.getInputStream());
    return (ClientListing)ois.readObject();
  }

  public void disconnect(){
    try{
      oos.writeInt(CLOSE_CONNECTION);
      oos.flush();
    }catch(Exception ex){
    }finally{
      try{ socket.close();}catch(Exception ex){}
    }
  }

  public Module submitCommand(Module mod) throws Exception{
    oos.writeInt(PROCESS_COMMAND);
    oos.flush();
    oos.writeObject(mod);
    oos.flush();
    ois.readInt();
    results.append("Server ACK recieved, client is processing the event");
    Module incomingModule = (Module)ois.readObject();
    results.append(incomingModule.toString());
    results.append("==========================================================================\n");
    return incomingModule;
  }

//****************************************************************************/
  public void pushHeartbeat(){
    try{
      oos.writeInt(HEARTBEAT);
      oos.flush();
      ois.readInt();
System.out.println("ACK RECIEVED");
    }catch(Exception ex){
      logProxy.log(ex);
      System.exit(-1);
    }
    System.out.println("--SEND PULSE @ " + new java.util.Date() + "--");
  }
  public void heartbeatFailure(){
    System.out.println("HEARTBEATFAILED");
  }

class heartbeatLsnr implements HeatbeatListener {
    public void sendPulse(){
      pushHeartbeat();
    }
    public void heartbeatFailed(){
      heartbeatFailure();
    }
  }

  public int getAdminThreadID() {
    return this.adminID;
  }

  public void setAdminThreadID(int adminThreadID) {
    this.adminID = adminThreadID;
  }



}