package amalgam.server;
import java.util.Enumeration;
import amalgam.util.*;

public interface ServerCommunicationProtocol {
  public int registerClient(ServerCommunicationThread sct, int threadID, String clientName, String clientIP, Enumeration modList) throws Exception;
  public void registerAdmin(ServerCommunicationThread sct, int threadID) throws Exception;
  public void removeClient(String clientName, int threadID) throws Exception;
  public void removeAdmin(int threadID) throws Exception;
  public void respond(String adminName, Object obj) throws Exception;
  public ClientListing fetchClientListing() throws Exception;
  public void proxyCommand(Module mod) throws Exception;
  public void proxyResponse(Module mod) throws Exception;
}
