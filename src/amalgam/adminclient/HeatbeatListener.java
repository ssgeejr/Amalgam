package amalgam.adminclient;

public interface HeatbeatListener {
  public void sendPulse();
  public void heartbeatFailed();
}