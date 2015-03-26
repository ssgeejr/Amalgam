package amalgam.util;

import java.io.Serializable;
import java.util.Vector;
import amalgam.util.Arguement;
import amalgam.interfaces.ClientServerCommunication;

public class Module extends Vector implements ClientServerCommunication,Serializable{
  public final int MODULE_NAME   = 0;
  public final int DISPLAY_NAME  = 1;
  public final int MODULE_CLASS  = 2;
  public final int MODULE_DEF    = 3;
  public final int MODULE_TYPE   = 4;
  private String modName;
  private String diplayName;
  private String definition;
  private String className;
  private String moduleType[] = {"DEFAULT","CUSTOM"};
  private int threadID;
  private int adminID;
  private int moduleID;
  private int type = 0;
  private Arguement arg;
  private Exception excp;
  private String errorMsg;
  private int responseResult = NACK;
  private String responseString = "not-initialized";

  public String getValue(int id){
    if(id == MODULE_NAME)
      return modName;
    if(id == DISPLAY_NAME)
      return diplayName;
    if(id == MODULE_CLASS)
      return className;
    if(id == MODULE_DEF)
      return definition;
    if(id == MODULE_TYPE)
      return moduleType[type];
    else
      throw new NullPointerException("Value [" + id + "] not found");
  }

  public String getModName() {
    return this.modName;
  }

  public void setModName(String modName) {
    this.modName = modName;
  }

  public String getDefinition() {
    return this.definition;
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }

  public String getClassName() {
    return this.className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getDiplayName() {
    return this.diplayName;
  }

  public void setDiplayName(String diplayName) {
    this.diplayName = diplayName;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getModuleType(){
    return this.type;
  }

  public String getType() {
    return this.moduleType[type];
  }

  public int getThreadID() {
    return this.threadID;
  }

  public void setThreadID(int clientID) {
    this.threadID = clientID;
  }

  public int getModuleID() {
    return this.moduleID;
  }

  public void setModuleID(int moduleID) {
    this.moduleID = moduleID;
  }
//---------------------------------------------------//
  public void addArguement(String name){
    arg = new Arguement();
    arg.setName(name);
    this.addElement(arg);
  }
  public String getArgName(int i){
    return ((Arguement)this.elementAt(i)).getName();
  }
  public String getArgValue(int i){
    return ((Arguement)this.elementAt(i)).getValue();
  }
  public void setArgValue(int i, String val){
    arg = (Arguement)this.elementAt(i);
    arg.setValue(val);
  }

  public Exception getException() {
    return this.excp;
  }

  public void setException(Exception excp) {
    this.excp = excp;
  }

  public String getErrorMsg() {
    return this.errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public int getResponseResult() {
    return this.responseResult;
  }

  public void setResponseResult(int responseResult) {
    this.responseResult = responseResult;
  }

  public int getAdminID() {
    return this.adminID;
  }

  public void setAdminID(int adminID) {
    this.adminID = adminID;
  }

  public String getResponseString() {
    return this.responseString;
  }

  public void setResponseString(String responseString) {
    this.responseString = responseString;
  }

  public String toString(){return responseString;};

}
