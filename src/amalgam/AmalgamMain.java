package amalgam;

import amalgam.adminclient.*;
import amalgam.server.*;
import amalgam.client.*;

public class AmalgamMain {
  private AmalgamMain(String args[]){
    try{
      if(args.length > 0){
        if (args[0].equalsIgnoreCase("-h") ||
                   args[0].equalsIgnoreCase("--help")) {
                 showHelp();
        } else if (args[0].equalsIgnoreCase("-s") ||
                   args[0].equalsIgnoreCase("--server")) {
          String serverArgs[] = new String[0];
          if(args.length > 1){
            serverArgs = new String[(args.length - 1)];
            for(int i = 0;i < args.length;i++){
            serverArgs[i] = args[(i + 1)];
            }
          }
          new amalgam.server.AmalgamServer(serverArgs);
        } else if (args[0].equalsIgnoreCase("-c") ||
                   args[0].equalsIgnoreCase("--client")) {
          new amalgam.client.AmalgamClient();
        } else if (args[0].equalsIgnoreCase("-a") ||
                   args[0].equalsIgnoreCase("--admin")) {
          new amalgam.adminclient.Amalgam();
        }
      }else{
        new Amalgam();
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  public static void main(String args[]){new AmalgamMain(args);}

  private void showHelp(){
  System.out.println("Amalgam: Cross Platform Aministration (2005)"
                     +"\n\t-h,--help\tHelp Message (this one)"
                     +"\n\t-s,--server\tStart the Server"
                     +"\n\t-c,--client\tStart a client instance"
                     +"\n\t-a,--admin\tStart the Admin Client (default)");
  }


}