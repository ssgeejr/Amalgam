package amalgam.server;

import java.util.*;
import java.net.*;
import amalgam.util.*;
import amalgam.interfaces.ServerProxy;

public class AmalgamServer extends ServerDataManager{
//-------------------------------------------------------//
      private int SERVER_PORT = 8005;
      private TagReader tReader;
      private final Hashtable clientListing = new Hashtable();
      private serverCommProtocol serverCom;
      private int threadID = 0;
      private Hashtable clientContainer = new Hashtable();
      private Hashtable adminContainer = new Hashtable();
//-------------------------------------------------------//

  public AmalgamServer(String args[]){
    try {
      serverCom = new serverCommProtocol();
      tReader = new TagReader("./conf/amalgam.server.xml","amalgam");
      logProxy = new LoggingProxy(
          new Boolean(tReader.getTagValue("logging", "enabled")).booleanValue(),
          		       tReader.getTagValue("logging", "log-file"),
                               tReader.getTagValue("logging", "log-class"),
                               tReader.getTagValue("logging", "log-level"));
      if(args.length == 1
          && args[0].equalsIgnoreCase("-shutdown") ){
          new ShutdownServer();
      }else{

        SERVER_PORT = Integer.parseInt(tReader.getTagValue("server", "port"));
        setConnectionManager(new ConnectionManager(tReader));
        startServer();
      }
    } catch(Exception ex) {
      logProxy.log("Server failed to initialize", ex);
    }
  }

  private void startServer() {
    try {
      ServerSocket sktServer = new ServerSocket(SERVER_PORT);
      logProxy.log("Server Running on port " + SERVER_PORT + " of localhost");
      logProxy.log("Server started : " + new java.util.Date().toString());
      ServerCommunicationThread srvrThread = null;
      while (true) {
          srvrThread = new ServerCommunicationThread(sktServer.accept(),logProxy, serverCom, threadID);
          srvrThread.start();
          threadID++;
      } // end while
    } catch (Exception ioe) {
      System.out.println("################ AmalgamServer-Server has thrown an exception ################");
      ioe.printStackTrace();
      logProxy.log(ioe);
    } // end try-catch
  } // end startServer

  public synchronized void postResponse(String adminName, Object obj) throws Exception{

  }

  public synchronized int registerNewClient(ServerCommunicationThread sct, int threadID, String clientName, String clientIP, Enumeration modList) throws Exception{
    clientContainer.put(new Integer(sct.getThreadID()),sct);
    notify();
    return createNewClient(threadID,clientName,clientIP,modList);
  }

  public synchronized void registerNewAdmin(ServerCommunicationThread sct, int threadID) throws Exception{
    adminContainer.put(new Integer(sct.getThreadID()),sct);
    notify();
  }

  public synchronized void removeClientFromContainer(String clientName, int threadID){
    logProxy.log("Removing client by threadid of " + threadID);
    deleteClient(clientName,threadID);
    clientContainer.remove(new Integer(threadID));
     notify();
 }

  public synchronized void removeAdminFromContainer(int threadID){
    logProxy.log("Removing admin by threadid of " + threadID);
    adminContainer.remove(new Integer(threadID));
    notify();
  }

  public synchronized void serverProxyCommand(Module mod) throws Exception{
    ServerProxy stcp = (ServerProxy)clientContainer.get(new Integer(mod.getThreadID()));
    stcp.handleClientCommand(mod);
    notify();
  }

  public synchronized void serverProxyResponse(Module mod) throws Exception{
    ServerProxy stcp = (ServerProxy)adminContainer.get(new Integer(mod.getAdminID()));
    System.out.println("Admin Thread [" + mod.getAdminID() + "] FOUND, SENDING RESPONSE");
    stcp.handleAdminCommand(mod);
    notify();
  }

//-------------------------------------------------------------------------------/
//----------------------------- COMMUNICATION PROTOCOLS -------------------------/
//-------------------------------------------------------------------------------/
  class serverCommProtocol implements ServerCommunicationProtocol{
    public int registerClient(ServerCommunicationThread sct, int threadID, String clientName, String clientIP, Enumeration modList) throws Exception{
      return registerNewClient(sct,threadID,clientName,clientIP,modList);
    }
    public void removeClient(String clientName, int threadID) throws Exception{
      removeClientFromContainer(clientName, threadID);
    }
    public void respond(String adminName, Object obj) throws Exception{
      postResponse(adminName, obj);
    }
    public ClientListing fetchClientListing() throws Exception{
      return fetchClientInfo();
    }
    public void proxyCommand(Module mod) throws Exception{
      serverProxyCommand(mod);
    }
    public void registerAdmin(ServerCommunicationThread sct, int threadID) throws Exception{
      registerNewAdmin(sct,threadID);
    }
    public void removeAdmin(int threadID) throws Exception{
      removeAdminFromContainer(threadID);
    }
    public void proxyResponse(Module mod) throws Exception{
      serverProxyResponse(mod);
    }
  }

//-------------------------------------------------------------------------------/
  public static void main(String args[]){ new AmalgamServer(args); }
//-------------------------------------------------------------------------------/
}
