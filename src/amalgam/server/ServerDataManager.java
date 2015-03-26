package amalgam.server;

import java.sql.*;
import java.util.Enumeration;
import amalgam.util.LoggingProxy;
import amalgam.interfaces.ClientServerCommunication;
import amalgam.util.*;

public class ServerDataManager implements ClientServerCommunication{
  private Connection conn;
  private ConnectionManager conMan;
  protected LoggingProxy logProxy;
  public void setConnectionManager(ConnectionManager _cm) throws Exception{
    conMan = _cm;
    conn = conMan.getConnection();
    resetDataBase();
  }

  private void validateConnection() throws Exception{
    conn = conMan.getConnection();
  }

  private void resetDataBase(){
    PreparedStatement cleanClients = null;
    PreparedStatement cleanClienModules = null;
    PreparedStatement cleanArgs = null;
    try {
      logProxy.log("Cleaning database client and clientmodules tables");
      validateConnection();
      cleanClients = conn.prepareStatement("delete"
                +" from"
                +" clients");

      cleanClienModules = conn.prepareStatement("delete"
                +" from"
                +" clientmodules");
      cleanArgs = conn.prepareStatement("delete"
                +" from"
                +" arguements");
      cleanClients.executeUpdate();
      cleanClienModules.executeUpdate();
      cleanArgs.executeUpdate();

    } catch (Exception ex) {
      logProxy.log(ex);
    } finally {
      try {cleanClients.close();} catch (Exception ex) {}
      try {cleanClienModules.close();} catch (Exception ex) {}
      try {cleanArgs.close();} catch (Exception ex) {}
    }
  }

  public void deleteClient(String clientName, int threadID){
    PreparedStatement select = null;
    PreparedStatement remove = null;
    PreparedStatement removeMods = null;
    PreparedStatement removeArgs = null;
    ResultSet rset = null;
    try {
      validateConnection();

      select = conn.prepareStatement("select"
                +" clientid"
                +" from"
                +" clients"
                +" where"
                +" clientname = ?"
                +" and threadid = ?");
      select.setString(1,clientName);
      select.setInt(2,threadID);
      rset = select.executeQuery();
      if(rset.next()){
        remove = conn.prepareStatement("delete"
               +" from"
               +" clients"
               +" where threadid = ?");

        removeMods = conn.prepareStatement("delete"
               +" from"
               +" clientmodules"
               +" where threadid = ?");
        removeArgs = conn.prepareStatement("delete"
               +" from"
               +" arguements"
               +" where threadid = ?");
       remove.setInt(1,threadID);
       removeMods.setInt(1,threadID);
       removeArgs.setInt(1,threadID);

       remove.executeUpdate();
       removeMods.executeUpdate();
       removeArgs.executeUpdate();
      }

    } catch (Exception ex) {
      logProxy.log(ex);
    } finally {
      try {select.close();} catch (Exception ex) {}
      try {remove.close();} catch (Exception ex) {}
      try {removeMods.close();} catch (Exception ex) {}
      try {removeArgs.close();} catch (Exception ex) {}
      try {rset.close();} catch (Exception ex) {}
    }
  }


  public int createNewClient(int threadID, String clientName, String clientIP, Enumeration modlist) throws Exception{
      validateConnection();
      PreparedStatement exist = null;
      PreparedStatement insert = null;
      PreparedStatement getClientID = null;
      PreparedStatement insertMod = null;
      PreparedStatement selectModID = null;
      PreparedStatement insertArg = null;
      ResultSet rset = null;
      int result = NACK;
      try {
        validateConnection();
        exist = conn.prepareStatement("select"
                +" clientname"
                +" from"
                +" clients"
                +" where"
                +" clientname = ?");
        insert = conn.prepareStatement("insert"
                +" into"
                +" clients(threadid, clientname,clientip,onlinetime)"
                +" values(?,?,?,?)");
        exist.setString(1,clientName);
        rset = exist.executeQuery();
        if(!rset.next()){
          rset.close();
          insert.setInt(1, threadID);
          insert.setString(2, clientName);
          insert.setString(3, clientIP);
          insert.setString(4, new java.util.Date().toString());
          insert.executeUpdate();
//          getClientID = conn.prepareStatement("select"
//                                              + " clientid"
//                                              + " from"
//                                              + " clients"
//                                              + " where"
//                                              + " clientname = ?"
//                                              + " and threadid = ?");
//          getClientID.setString(1, clientName);
//          getClientID.setInt(2, threadID);
//          rset = getClientID.executeQuery();
//          rset.next();
//          int clientid = rset.getInt("clientid");
          int clientid = threadID;
          int modid = -1;
          Module mod = null;
          insertMod = conn.prepareStatement("insert"
                     + " into"
                     +" clientmodules(threadid,modulename,displayname,description,moduletype,classname)"
                     + " values(?,?,?,?,?,?)");
          selectModID = conn.prepareStatement("select"
                                              + " moduleid"
                                              + " from"
                                              + " clientmodules"
                                              + " where threadid = ?"
                                              + " and modulename = ?");
          insertArg = conn.prepareStatement("insert"
                                            + " into"
                                            + " arguements(threadid,moduleid,arguement)"
                                            + " values(?,?,?)");
          while (modlist.hasMoreElements()) {
            mod = (Module) modlist.nextElement();
            insertMod.setInt(1, clientid);
            insertMod.setString(2, mod.getModName());
            insertMod.setString(3, mod.getDiplayName());
            insertMod.setString(4, mod.getDefinition());
            insertMod.setInt(5, mod.getModuleType());
            insertMod.setString(6, mod.getClassName());
            insertMod.executeUpdate();
            //------------- LOAD ANY ARGUEMENTS -------------//
            if (mod.size() > 0) {
              selectModID.setInt(1, clientid);
              selectModID.setString(2, mod.getModName());
              rset = selectModID.executeQuery();
              if (rset.next()) {
                modid = rset.getInt("moduleid");
                for (int loc = 0; loc < mod.size(); loc++) {
                  insertArg.setInt(1, clientid);
                  insertArg.setInt(2, modid);
                  insertArg.setString(3, mod.getArgName(loc));
                  insertArg.executeUpdate();
                } //end for
              } //end rset.next
            } //end mod-size > 0
            result = ACK;
          }//end while
        }else{
          throw new Exception("Client Name already online");
        }
      } catch(Exception ex) {
        logProxy.log(ex);
      }finally{
        try {rset.close();} catch (Exception ex) {}
        try {exist.close(); } catch(Exception ex) {}
        try {insert.close(); } catch(Exception ex) {}
        try {getClientID.close(); } catch(Exception ex) {}
        try {insertMod.close(); } catch(Exception ex) {}
        try {insertArg.close(); } catch(Exception ex) {}
        try {selectModID.close(); } catch(Exception ex) {}
      }
      return result;
  }

  public String noNullLen(String value, int len){
    return null;
  }

  public ClientListing fetchClientInfo() throws Exception{
    validateConnection();
    ClientListing cl = new ClientListing();
    PreparedStatement clients = null;
    PreparedStatement modules = null;
    PreparedStatement args = null;
    try{
     clients = conn.prepareStatement("select"
               +" CLIENTID,"
               +" THREADID,"
               +" CLIENTNAME,"
               +" CLIENTIP,"
               +" ONLINETIME"
               +" from"
               +" clients"
               +" order by clientname");

     modules = conn.prepareStatement("select"
               +" MODULEID,"
               +" THREADID,"
               +" MODULENAME,"
               +" DISPLAYNAME,"
               +" DESCRIPTION,"
               +" MODULETYPE,"
               +" CLASSNAME"
               +" FROM"
               +" clientmodules"
               +" order by THREADID");
     args = conn.prepareStatement("select"
               +" arguement"
               +" FROM"
               +" arguements"
               +" where moduleid = ?");

      ResultSet rset = clients.executeQuery();
      ResultSet iset = null;
      Client client = null;
      while(rset.next()){
        client = new Client();
        client.setClientID(rset.getInt("CLIENTID"));
        client.setThreadID(rset.getInt("THREADID"));
        client.setClientIP(rset.getString("CLIENTIP"));
        client.setClientName(rset.getString("CLIENTNAME"));
        client.setOnlineTime(rset.getString("ONLINETIME"));
        cl.addClient(client);
      }

      rset.close();
      rset = modules.executeQuery();
      Module mod = null;
      while(rset.next()){
        mod = new Module();
        mod.setModName(rset.getString("MODULENAME"));
        mod.setThreadID(rset.getInt("THREADID"));
        mod.setDefinition(rset.getString("DESCRIPTION"));
        mod.setDiplayName(rset.getString("DISPLAYNAME"));
        mod.setModuleID(rset.getInt("MODULEID"));
        mod.setType(rset.getInt("MODULETYPE"));
        mod.setClassName(rset.getString("CLASSNAME"));
//---------- ARE THERE ANY ARGS WITH THIS MODULE -------------//
        args.setInt(1,mod.getModuleID());
        iset = args.executeQuery();
        while(iset.next()){
          mod.addArguement(iset.getString("arguement"));
        }
        cl.addModule(mod);
      }
    }catch(Exception ex){
      logProxy.log(ex);
    }finally{
      try{ clients.close();}catch(Exception ex){}
      try{ modules.close();}catch(Exception ex){}
      try{ args.close();}catch(Exception ex){}
    }

    return cl;
  }

}
