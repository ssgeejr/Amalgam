package amalgam.util;

import java.util.ArrayList;
import java.io.Serializable;

public class ClientListing implements Serializable{
  private ArrayList clients = new ArrayList();
  private ArrayList clientModules = new ArrayList();

  public ArrayList getClients() {
    return this.clients;
  }

  public void addClient(Client client) {
    clients.add(client);
  }

  public ArrayList getClientModules() {
    return this.clientModules;
  }

  public void addModule(Module module) {
    clientModules.add(module);
  }
}