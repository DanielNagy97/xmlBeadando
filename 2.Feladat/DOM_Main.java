package xml_DOM;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
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
import org.xml.sax.SAXException;


public class DOM_Main {
	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	private static final String NAMESPACE_URI = "beadando";
	private static final String INPUT_FILE_PATH = "data/minta.xml";
	private static final String OUTPUT_FILE_PATH = "data/autoDB-output.xml";
	//kimenet a konzolra, vagy file-ba irodjon
	private static final boolean PRINT_TO_CONSOLE = true;

	public static void main(String[] args) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setValidating(true);
		documentBuilderFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			documentBuilder.setErrorHandler(new LemezErrorHandler());
			Document document = documentBuilder.parse(new File(INPUT_FILE_PATH));
			
			System.out.print("Root element: ");
			System.out.println(document.getDocumentElement().getNodeName());
			
			System.out.print(eloadoExistsIndex(document,"The Doors"));
			

			addAlbum(document, new Album("Proba cim","P mf",1987,"Pink Floyd","AFR-41212"));

			if (PRINT_TO_CONSOLE) {
				printDocument(document);
			} else {
				printDocument(document, new File(OUTPUT_FILE_PATH));
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Integer eloadoExistsIndex(Document document, String eloado) {
		List<String> eloadok = new ArrayList<>();
		Node eloadokNode = document.getElementsByTagName("eloadok").item(0);
		NodeList eloadoNodeList = eloadokNode.getChildNodes();

		for (int i = 0; i < eloadoNodeList.getLength(); i++) {
			Node eloadoNode = eloadoNodeList.item(i);
			NodeList eloadoChildNodes = eloadoNode.getChildNodes();
			for (int j = 0; j < eloadoChildNodes.getLength(); j++) {
				Node eloadoChildNode = eloadoChildNodes.item(j);
				if (eloadoChildNode.getNodeName().equals("nev")) {
					eloadok.add(eloadoChildNode.getTextContent());
				}
			}
		}
		return eloadok.indexOf(eloado);
	}
	
	
	public static void addAlbum(Document document, Album album) {
		if (eloadoExistsIndex(document,"The Doors") == -1) {
			System.out.println();
		}
		
		Node albumokNode = document.getElementsByTagName("albumok").item(0);

		Element albumElement = document.createElement("album");
		albumokNode.appendChild(albumElement);

		Attr eloadoAttribute = document.createAttribute("eloado");
		eloadoAttribute.setNodeValue(String.valueOf(album.getEloado()));
		albumElement.setAttributeNode(eloadoAttribute);

		Attr katszAttribute = document.createAttribute("katalogusszam");
		katszAttribute.setNodeValue(String.valueOf(album.getKatalogusszam()));
		albumElement.setAttributeNode(katszAttribute);

		Element cimElement = document.createElement("cim");
		cimElement.setTextContent(String.valueOf(album.getCim()));
		albumElement.appendChild(cimElement);

		Element mufajElement = document.createElement( "mufaj");
		mufajElement.setTextContent(String.valueOf(album.getMufaj()));
		albumElement.appendChild(mufajElement);
		
		Element megjelenesElement = document.createElement( "megjeleneseve");
		megjelenesElement.setTextContent(String.valueOf(album.getMegjelenes_eve()));
		albumElement.appendChild(megjelenesElement);
	}


	public static void printDocument(Document document) {
		printDocument(document, null);
	}

	public static void printDocument(Document document, File outputFile) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(document);
			StreamResult result;
			
			if (outputFile == null) {
				result = new StreamResult(System.out);
			} else {
				result = new StreamResult(outputFile);
			}

			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

}
