package amalgam.modules;
import amalgam.util.Module;

public interface AmalgamModuleInterface {
  public Module executeCommand(Module mod) throws Exception;
}