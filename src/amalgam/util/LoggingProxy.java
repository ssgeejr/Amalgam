package amalgam.util;

import java.util.logging.*;

public class LoggingProxy{

  private final boolean log;
  private Logger logger;
  public LoggingProxy(){
    log = false;
  }

  public LoggingProxy(boolean doLog, String logFile) throws Exception{
    log = doLog;
    setLogFile(doLog,logFile,null);
  }

  public LoggingProxy(boolean doLog, String logFile, String defaultLogLevel) throws Exception{
    log = doLog;
    setLogLevel(defaultLogLevel);
    setLogFile(doLog,logFile,null);
  }

  public LoggingProxy(boolean doLog, String logFile, String logClass, String defaultLogLevel) throws Exception{
    log = doLog;
    if(log){
      setLogFile(doLog, logFile, logClass);
      setLogLevel(defaultLogLevel);
    }
  }
//****************************************************************************//
  private void setLogFile(boolean doLog, String logFile, String logClass) throws Exception{
    if(doLog){
      if(logClass == null){
        logger = Logger.getLogger("amalgam.util.LoggingProxy");
      }else{
        logger = Logger.getLogger(logClass);
      }

      if(logFile == null){
        logger.addHandler(new FileHandler("amalgam.log"));
      }else{
        logger.addHandler(new FileHandler(logFile));
      }
      logger.setLevel(Level.INFO);
    }
  }//end setLogFile
//****************************************************************************//
  private void setLogLevel(String logLevel){
    if(logLevel == null){
      logger.setLevel( Level.INFO);
    }else if(logLevel.equalsIgnoreCase("SEVERE")){
      logger.setLevel( Level.SEVERE);
    }else if(logLevel.equalsIgnoreCase("WARNING")){
      logger.setLevel( Level.WARNING);
    }else if(logLevel.equalsIgnoreCase("INFO")){
      logger.setLevel( Level.INFO);
    }else if(logLevel.equalsIgnoreCase("FINE")){
      logger.setLevel( Level.FINE);
    }else if(logLevel.equalsIgnoreCase("FINER")){
      logger.setLevel( Level.FINER);
    }else if(logLevel.equalsIgnoreCase("FINEST")){
      logger.setLevel( Level.FINEST);
    }else{
      logger.setLevel( Level.INFO);
    }
  }//end setLogLevel
//****************************************************************************//

  public void log(Object msg){
    if(log){
      logger.log(logger.getLevel(), msg.toString());
    }else{
      System.out.println(msg);
    }
  }//end log

  public void log(Object msg, Exception ex){
System.out.println("]]  " + msg);
    if(log){
      logger.log(logger.getLevel(), msg.toString(), ex);
    }else{
      System.out.println(msg);
      ex.printStackTrace();
    }
  }//end log

  public void log(Exception excp){
    if(log){
      logger.log(Level.INFO,"",excp);
    }else{
      excp.printStackTrace();
    }
  }//end log

  public void log(Level newLevel, Object msg){
    if(log){
      logger.log(newLevel,msg.toString());
    }else{
      System.out.println(newLevel.toString() + "recieved..but logging not enabled\n" + msg);
    }
  }//end log


}
