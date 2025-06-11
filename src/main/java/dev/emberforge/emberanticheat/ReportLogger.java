package dev.emberforge.emberanticheat;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ReportLogger {
  private String filePath = "";
  
  public ReportLogger(String filePath) {
    this.filePath = filePath;
    configureDirectory();
  }
  
  private void configureDirectory() {
    File location = new File(this.filePath + "/Illegal_Clients/");
    if (!location.exists()) {
      location.mkdir();
    }
  }
  
  public void generateReport(String playerName, ArrayList<String> leftoverMods) {
    System.out.println("Creating report for player: " + playerName);
    try {
      DateFormat datetime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
      
      File xmlFile = new File(this.filePath + "/Illegal_Clients/" + playerName + "_" + datetime.format(new Date()) + ".xml");
      xmlFile.createNewFile();
      
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();
      
      Element rootElement = doc.createElement("playerName");
      doc.appendChild(rootElement);
      for (int i = 0; i < leftoverMods.size(); i++) {
        Element modElement = doc.createElement("Mod");
        rootElement.appendChild(modElement);
        Attr attrmcp = doc.createAttribute("name");
        attrmcp.setValue((String)leftoverMods.get(i));
        modElement.setAttributeNode(attrmcp);
        modElement.appendChild(doc.createElement("modID").appendChild(doc.createTextNode((String)leftoverMods.get(i))));
      }
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(xmlFile);
      
      transformer.transform(source, result);
      
      System.out.println("Report created at: " + xmlFile.getPath());
    }
    catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    }
    catch (TransformerException tfe) {
      tfe.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}