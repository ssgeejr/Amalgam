package amalgam.interfaces;
import amalgam.util.Module;
public interface ServerProxy {
  public void handleClientCommand(Module mod) throws Exception;
  public void handleAdminCommand(Module mod) throws Exception;
}