package amalgam.interfaces;

public interface ClientServerCommunication {
  public static final int SHUTDOWN = -1942;
  public static final int NACK = -1;
  public static final int CLOSE_CONNECTION = 0;
  public static final int ACK = 1;
  public static final int EXECUTE_MODULE = 5;
  public static final int NEW_CLIENT = 10;
  public static final int NEW_ADMIN = 20;
  public static final int PROCESS_COMMAND = 30;
  public static final int HEARTBEAT = 40;
  public static final int SEND_CLIENT_LIST = 50;
  public static final int COMMAND_RESPONSE = 60;
}
