package org.jz.orahle;



import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConnectionServiceImpl implements Serializable {

    //data model
    private List<DbConnection> connectionList = new ArrayList<>();
    //initialize book data
    
    public ConnectionServiceImpl() throws IOException, ParserConfigurationException, SAXException 
    {
        File connectionsFile = new File(System.getProperty("orahle.connections-file", "conf/orahle-connections.xml"));
            
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(connectionsFile);
 
	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();
	NodeList connectionNodeList = doc.getElementsByTagName("connection");
        for (int i = 0; i < connectionNodeList.getLength(); i++)
        {
            Node connectionNode = connectionNodeList.item(i);
            if (connectionNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element connectionElement = (Element) connectionNode;
                DbConnection dbConnection = new DbConnection();
                /**/
                NodeList paramsNodeList = connectionElement.getChildNodes();
                for (int j = 0; j < paramsNodeList.getLength(); j++)
                {
                    Node paramNode = paramsNodeList.item(j);
                    if (paramNode.getNodeType() == Node.ELEMENT_NODE)
                    {
                        switch (paramNode.getNodeName()) {
                            case "alias" :
                                dbConnection.setAlias(paramNode.getTextContent());
                                break;
                            case "url" :
                                dbConnection.setUrl(paramNode.getTextContent());
                                break;
                            case "user" :
                                dbConnection.setUser(paramNode.getTextContent());
                                break;
                            case "description" :
                                dbConnection.setDescription(paramNode.getTextContent());
                                break;
                        }
                    }
                }
                /**/
                connectionList.add(dbConnection);
            }
        }
    }

    public List<DbConnection> getConnections()
    {
        System.out.println("SIZE : " + connectionList.size());
        return connectionList;
    }

    public List<DbConnection> findAll()
    {
        return connectionList;
    }

    public List<DbConnection> search(String keyword)
    {
        List<DbConnection> result = new LinkedList<>();
        if (keyword == null || "".equals(keyword))
        {
            result = connectionList;
        }
        else
        {
            for (DbConnection c : connectionList)
            {
                if (c.getAlias().toLowerCase().contains(keyword.toLowerCase())
                        || c.getUrl().toLowerCase().contains(keyword.toLowerCase()))
                {
                    result.add(c);
                }
            }
        }
        return result;
    }

    public DbConnection getConnections(String dbAlias)
    {
        for (DbConnection dbConnection : connectionList)
        {
            if (dbConnection.getAlias().equalsIgnoreCase(dbAlias))
            {
                return dbConnection;
            }
        }
        return null;
    }
}
