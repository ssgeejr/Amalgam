package amalgam.adminclient;

public class Heartbeat extends Thread {
  private boolean ISONLINE = true;
  private HeatbeatListener listener;
  public Heartbeat(HeatbeatListener hl){
    listener = hl;
  }
  public void run(){
    try{
      while(ISONLINE){
        this.sleep(300000);
        listener.sendPulse();
      }
    }catch(Exception ex){
      listener.heartbeatFailed();
    }
  }

  public void offline(){ISONLINE = false;}
}