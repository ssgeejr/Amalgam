package amalgam.util;

import java.io.Serializable;

public class Client implements Serializable{
  private int clientID;
  private int threadID;
  private String clientName;
  private String clientIP;
  private String onlineTime;

  public int getClientID() {
    return this.clientID;
  }

  public void setClientID(int clientID) {
    this.clientID = clientID;
  }

  public int getThreadID() {
    return this.threadID;
  }

  public void setThreadID(int threadID) {
    this.threadID = threadID;
  }

  public String getClientName() {
    return this.clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getClientIP() {
    return this.clientIP;
  }

  public void setClientIP(String clientIP) {
    this.clientIP = clientIP;
  }

  public String getOnlineTime() {
    return this.onlineTime;
  }

  public void setOnlineTime(String onlineTime) {
    this.onlineTime = onlineTime;
  }

}