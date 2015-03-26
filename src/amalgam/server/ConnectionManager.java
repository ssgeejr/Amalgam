package amalgam.server;

import java.sql.*;
import amalgam.util.*;

public class ConnectionManager {

  private final TagReader tReader;
  private Connection conn;
  public ConnectionManager(TagReader _tr){
    tReader = _tr;
  }

//hyperCon = HYPER.getConnection("Persistent","jdbc:HypersonicSQL:HDO","sa","",500,true);

  public Connection getConnection() throws Exception{
    if(conn == null  ||  conn.isClosed()){
      Class.forName(tReader.getTagValue("database","driver"));
      conn = DriverManager.getConnection(tReader.getTagValue("database","url")
          		, tReader.getTagValue("database","username")
                        , tReader.getTagValue("database","password"));
    }
    return conn;
  }//end getConnection

  public void closeConnection(){
    try{
      if(conn != null  ||  !conn.isClosed()){ conn.close();}
    }catch(Exception ex){}
  }//end closeConnection
}
