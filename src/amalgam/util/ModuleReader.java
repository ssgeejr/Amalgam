package amalgam.util;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;

public class ModuleReader extends ArrayList implements Serializable{
  private Hashtable rootModules = null;
  private String rootTag = "modules";

  public ModuleReader() throws Exception{}

  public ModuleReader(InputStream istream) throws Exception{
    construct(istream);
  }

  public ModuleReader(String filename) throws Exception{
    construct(new FileInputStream(filename));
  }

  public ModuleReader(InputStream istream, String _rootTag) throws Exception{
    rootTag = _rootTag;
    construct(istream);
  }

  public ModuleReader(String filename, String _rootTag) throws Exception{
    rootTag = _rootTag;
    construct(new FileInputStream(filename));
  }

  public void setRootTag(String _rootTag){
    rootTag = _rootTag;
  }

  public void construct(String filename) throws Exception{
    construct(new FileInputStream(filename));
  }

  public void construct(InputStream fileInputStream) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(false);
    Document doc = factory.newDocumentBuilder().parse(fileInputStream);

//    parser.parse(new InputSource(new ByteArrayInputStream(msg.getBytes())));

    NodeList nodeLst = doc.getElementsByTagName(rootTag);
    Node rcpNode = null;
    NodeList nl = null;
    Node n;
    int cntr = 0;
    if (nodeLst.getLength() == 0)
      throw new Exception("No tags provided");
    Node noed = null;
    NamedNodeMap nodeAtts = null;
    Module module;
    rootModules = new Hashtable();

    while (cntr < nodeLst.getLength()) {
      rcpNode = nodeLst.item(cntr);
      nl = rcpNode.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
        n = nl.item(i);
        if (n.getNodeType() == 1) {
          module = new Module();
          nodeAtts = n.getAttributes();
          for(int knode = 0;knode < nodeAtts.getLength();knode++){
            noed = nodeAtts.item(knode);
            if(noed.getNodeName() == "name"){module.setModName(noed.getNodeValue());}
            else if(noed.getNodeName() == "display-name"){module.setDiplayName(noed.getNodeValue());}
            else if(noed.getNodeName() == "class"){module.setClassName(noed.getNodeValue());}
            else if(noed.getNodeName() == "definition"){module.setDefinition(noed.getNodeValue());}
            else if(noed.getNodeName() == "display-name"){module.setDiplayName(noed.getNodeValue());}
            else if(noed.getNodeName().startsWith("arg")){module.addArguement(noed.getNodeValue());}
          }
          addModule(module.getModName(),module);

        } //end if
      } //end for
      cntr++;
    } //end while
  } //end parse(String msg)

  public Enumeration getModules() throws Exception{
    return rootModules.elements();
  }

  public Module getModule(String modName) throws Exception {
    return (Module) ( rootModules.get(modName));
  }

  public String getModuleValue(String modName, int value) throws Exception{
    return ((Module) ( rootModules.get(modName))).getValue(value);
  }

  public void addModule(String modName, Module mod){
    if(this.contains(modName))
      this.remove(modName);

    this.add(modName);
    rootModules.put(modName,mod);
  }

  public void addModules(ModuleReader clientMods) throws Exception{
    Enumeration enm = clientMods.getModules();
    Module xmod = null;
    while(enm.hasMoreElements()){
      xmod = (Module)enm.nextElement();
      xmod.setType(1);
      addModule(xmod.getModName(),xmod);
    }
  }//end addModules

}
