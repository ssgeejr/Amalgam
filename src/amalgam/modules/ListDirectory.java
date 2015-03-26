package amalgam.modules;

import amalgam.util.Module;
import java.io.*;
import java.util.*;
//import amalgam.util.

public class ListDirectory implements AmalgamModuleInterface{
    public Module executeCommand(Module mod){
      try{
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec("ls -lt /tmp");
        BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(proc.getInputStream()));
       String input = null;
       StringBuffer buf = new StringBuffer();
        while (( input = stdInput.readLine()) != null) {
          buf.append(input + "\n");
          System.out.println("\t>> " + input);
        }
        mod.setResponseString(buf.toString());
      }catch(Exception ex){
        mod.setErrorMsg("Failed execuing: " + mod.getModName());
        mod.setException(ex);
        ex.printStackTrace();
      }

    return mod;
  }
}