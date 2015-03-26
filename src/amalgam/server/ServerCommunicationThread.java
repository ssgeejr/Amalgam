package amalgam.server;
import java.net.*;
import java.io.*;
import amalgam.interfaces.*;
import amalgam.util.LoggingProxy;
import amalgam.util.*;
import java.util.Enumeration;

public class ServerCommunicationThread extends Thread
    implements ClientServerCommunication,
    ServerProxy{
  private LoggingProxy logProxy;
  private Socket openSocket;
  private boolean online = true;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private ServerCommunicationProtocol serverComProtocol;
  private final int threadID;
  private String clientThreadName;
  private boolean isClient = false;
  private int cmdResponse = -1;

  public ServerCommunicationThread(Socket skt, LoggingProxy _lp, ServerCommunicationProtocol _scp, int _threadID) throws Exception{
    openSocket = skt;
    logProxy = _lp;
    serverComProtocol = _scp;
    threadID = _threadID;
    ois = new ObjectInputStream(openSocket.getInputStream());
  }


  public void run() {
System.out.println("~~~~~~~~~~~~~~~~~~~~~~ NEW SERVER SOCKET INIT ~~~~~~~~~~~~~~~~~~~~~~");

    try {
      int inputValue = -1;
      oos = new ObjectOutputStream(openSocket.getOutputStream());

      while(online) {
        inputValue = ois.readInt();
        logProxy.log("[" + inputValue + "] recived @: " + new java.util.Date());
       switch (inputValue) {
         case SHUTDOWN:
           logProxy.log(new java.util.Date());
           logProxy.log("Shutdown command recieved...");
           System.exit(-1942);
           break;
         case CLOSE_CONNECTION:
           logProxy.log("--DISCONNECTION--");
           disconnect();
           break;
         case NEW_CLIENT:
           isClient = true;
           logProxy.log("--NEW_CLIENT--");
           clientThreadName = (String)ois.readObject();
           logProxy.log("Client [" + clientThreadName + "] is registering with the server on " + new java.util.Date());
           ModuleReader tmpModreader = (ModuleReader)ois.readObject();
           Enumeration enm = tmpModreader.getModules();
           Module amod = null;
           while(enm.hasMoreElements()){
             amod = (Module)enm.nextElement();
             logProxy.log("Module loaded [" + amod.getModName() + "]  Display Name [" + amod.getDiplayName() + "] Type [" + amod.getType() + "]");
           }
System.out.println("~~~~~~~~~~~>> REGISTER CLIENT [" + threadID + "] WITH THE DATABASE AND UPDATE ITS MODULE LIST <<~~~~~~~~~~~~~~");
           int databaseResponse = serverComProtocol.registerClient(this,threadID,clientThreadName, openSocket.getInetAddress().toString(),tmpModreader.getModules());
           oos.writeInt(databaseResponse);
           oos.flush();
           break;
         case NEW_ADMIN:
           logProxy.log("--NEW_ADMIN [" + threadID + "]--");
           oos.writeInt(threadID);
           oos.flush();
           oos.writeInt(ACK);
           oos.flush();
System.out.println("~~~~~~~~~~~>> REGISTER ADMIN WITH THE SERVER [" + threadID + "] <<~~~~~~~~~~~~~~");
           serverComProtocol.registerAdmin(this,threadID);
           break;
         case SEND_CLIENT_LIST:
           oos.writeObject(serverComProtocol.fetchClientListing());
           oos.flush();
           break;
         case HEARTBEAT:
           oos.writeInt(ACK);
           oos.flush();
           break;
         case PROCESS_COMMAND:
           logProxy.log("--PROCESS COMMAND--");
           Module command = (Module)ois.readObject();
           oos.writeInt(ACK);
           oos.flush();
           System.out.println("Tell client [" + command.getThreadID() + "] to process command [" + command.getModName() + "]");
           serverComProtocol.proxyCommand(command);
           break;
         case COMMAND_RESPONSE:
           Module responseMod = (Module)ois.readObject();
           serverComProtocol.proxyResponse(responseMod);
           break;
         default:
           disconnect();
         break;
       }//end switch
      } //end while
    }catch(SocketException se){
      disconnect();
    } catch(Exception e) {
      logProxy.log("ErrorStartingItegrityThread::ServerCommunicationThread::run ");
      disconnect();
      e.printStackTrace();
    } //end try-catch
  } //end startListener


  public synchronized void handleClientCommand(Module mod) throws Exception{
    System.out.println("---------- CLIENT " + clientThreadName + " -----------");
    oos.writeInt(EXECUTE_MODULE);
    oos.flush();
    oos.writeObject(mod);
    oos.flush();
    notify();
  }

  public void handleAdminCommand(Module mod) throws Exception{
    System.out.println("---------- RESPONDING TO ADMIN REQUEST -----------");
    System.out.println(" ########## I AM THREAD [" + threadID + "] ###########");
//    oos.writeInt(COMMAND_RESPONSE);
    oos.writeObject(mod);
    oos.flush();
  }


  private void pushDeadFile() throws Exception{
    byte[] bite = new byte[0];
    oos.writeObject(bite);
    oos.flush();
  }

  public void disconnect() {
    try {
      openSocket.close();
      online = false;
      if(isClient){
        try {serverComProtocol.removeClient(clientThreadName, threadID);
        } catch (Exception ex) {
          logProxy.log(ex);
        }
      }else{
        try {serverComProtocol.removeAdmin(threadID);
        } catch (Exception ex) {
          logProxy.log(ex);
        }
      }
    } catch(Exception e) {
      logProxy.log("ErrorDisconnecting::ServerCommunicationThread::disconnect:: ");
      e.printStackTrace();
    }
  }

  public String getClientName() {
    return this.clientThreadName;
  }

  public int getThreadID() {
    return this.threadID;
  }

}
