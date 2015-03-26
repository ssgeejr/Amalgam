package amalgam.server;


import java.net.*;
import amalgam.interfaces.*;
import java.io.*;

public class ShutdownServer implements ClientServerCommunication{
  public ShutdownServer() {
    ObjectOutputStream oos = null;
    Socket socket = null;
    try{
      Thread timer = new Thread();
      System.out.println("Shutting down Server...");
      socket = new Socket("localhost",1025);
      oos = new ObjectOutputStream(socket.getOutputStream());
      oos.writeInt(SHUTDOWN);
      oos.flush();
      timer.sleep(1500);
    }catch(ConnectException ce){
    //-- this server is now shutodown
    }catch(SocketException se){
    //-- this server is now shutodown
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      try{ oos.close();}catch(Exception ex){}
      try{ socket.close();}catch(Exception ex){}
    }
  }
  public static void main(String[] args) {
    new ShutdownServer();
  }

}
