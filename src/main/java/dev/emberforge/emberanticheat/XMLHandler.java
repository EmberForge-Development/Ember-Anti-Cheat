package net.kineticdevelopment.kineticanticheat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHandler {
	public Integer debugMode = Integer.valueOf(0);
	private String filePath = "";
	public List<String> defaultMods = new ArrayList<String>();
  
	public XMLHandler(String filePath) {
		this.filePath = filePath;
    
		loadXML();
	}	
  
	public void reload() {
		loadXML();
	}
  
	private void loadXML() {
		try
		{
			File xmlFile = new File(this.filePath + "/kac.xml");
			if (xmlFile.exists()) {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				
				doc.getDocumentElement().normalize();
        
				NodeList nList = doc.getElementsByTagName("ModList");
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == 1) {
						Element eElement = (Element)nNode;
						for (int i = 0; i < eElement.getElementsByTagName("Mod").getLength(); i++) {
							this.defaultMods.add(eElement.getElementsByTagName("Mod").item(i).getTextContent());
						}
					}
				}
			}
			else {
				writeXML();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
  
	private void writeXML() {
		System.out.println("Approved mod list not found, creating file.");
		try {
			File xmlFile = new File(this.filePath + "/kac.xml");
			xmlFile.createNewFile();
      
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
      
			Element rootElement = doc.createElement("ModList");
			doc.appendChild(rootElement);
      
			Element modElementmcp = doc.createElement("Mod");
			rootElement.appendChild(modElementmcp);
			Element modElementfml = doc.createElement("Mod");
			rootElement.appendChild(modElementfml);
			Element modElementforge = doc.createElement("Mod");
			rootElement.appendChild(modElementforge);
			Element modElementchecker = doc.createElement("Mod");
			rootElement.appendChild(modElementchecker);
			Element modElementminecraft = doc.createElement("Mod");
			rootElement.appendChild(modElementminecraft);
      
			Attr attrmcp = doc.createAttribute("name");
			attrmcp.setValue("mcp");
			modElementmcp.setAttributeNode(attrmcp);
			modElementmcp.appendChild(doc.createElement("modID").appendChild(doc.createTextNode("mcp")));
      
			Attr attrfml = doc.createAttribute("name");
			attrfml.setValue("FML");
			modElementfml.setAttributeNode(attrfml);
			modElementfml.appendChild(doc.createElement("modID").appendChild(doc.createTextNode("FML")));
      
			Attr attrforge = doc.createAttribute("name");
			attrforge.setValue("forge");
			modElementforge.setAttributeNode(attrforge);
			modElementforge.appendChild(doc.createElement("modID").appendChild(doc.createTextNode("forge")));
      
			Attr attrmodchecker = doc.createAttribute("name");
			attrmodchecker.setValue("kinetic anti-cheat");
			modElementchecker.setAttributeNode(attrmodchecker);
			modElementchecker.appendChild(doc.createElement("modID").appendChild(doc.createTextNode("kinetic anti-cheat")));
			
			Attr attrminecraft = doc.createAttribute("name");
			attrminecraft.setValue("minecraft");
			modElementminecraft.setAttributeNode(attrminecraft);
			modElementminecraft.appendChild(doc.createElement("modID").appendChild(doc.createTextNode("minecraft")));
      
      
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(xmlFile);
      
			transformer.transform(source, result);
      
			System.out.println("File created at: " + this.filePath);
			System.out.println("Be sure to edit this file or nobody will be able to join! Default values have been added.");
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
