/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Sameera
 */
public class Config {

    
    /*
    *Give Log variable to any class that need to use loging.
    *className -> what ever the class that need to use log file.(most of the time calling class name)
    *Logger-> return LOG varible which contain all loging functionalities.
    */
    public static Logger getLogger(Class className){
        Logger LOG;
        
        PropertyConfigurator.configure(Utility.PROPERTY_FILE_PATH);
        LOG = Logger.getLogger(className);
        return LOG;
    }
    
    
    public static Properties getPropValues() throws IOException {

        InputStream inputStream = null;
        Properties prop = new Properties();

        try {
            String propFileName = Utility.PROPERTY_FILE_PATH;
            inputStream = new FileInputStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
                return prop;
            } else {

            }

        } catch (IOException e) {

        } finally {
            inputStream.close();
        }
        return null;
    }

    // get xml query data 
    public static String getXMLData(String dataType, String key) {

        try {
            if ( dataType != null || key != null) {
                String XMLFilePath = "";
                XMLFilePath = (Utility.QUERY_FILE_PATH);
                File fXmlFile = new File(XMLFilePath);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();

                NodeList nList = doc.getElementsByTagName(dataType);

                for (int i = 0; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if (key.equals(eElement.getAttribute("key"))) {
                            return eElement.getElementsByTagName("entry").item(0).getTextContent();
                        }
                    }
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
