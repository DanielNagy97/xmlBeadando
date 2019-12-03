package xml_DOM;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
//import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOM_Main {
	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	//private static final String NAMESPACE_URI = "beadando";
	private static final String INPUT_FILE_PATH = "data/minta.xml";
	private static final String OUTPUT_FILE_PATH = "data/lemezkolcsonzo-output.xml";


	public static void main(String[] args) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setValidating(true);
		documentBuilderFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			documentBuilder.setErrorHandler(new LemezErrorHandler());
			Document document = documentBuilder.parse(new File(INPUT_FILE_PATH));
			
			Scanner sc = new Scanner(System.in);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			while(true) {
				System.out.println("----------------------------");
				System.out.println("1. Album hozzáadása");
				System.out.println("2. Előadó hozzáadása");
				System.out.println("3. Előadó módosítása");
				System.out.println("4. Albumok évtized szerint");
				System.out.println("5. XML kiírása");
				System.out.println("6. Kilépés");
				int menu = Integer.parseInt(sc.next());
				switch(menu) {
				case 1:
					Album album = new Album();
					System.out.println("Kérem adja meg az album címét:");
					album.setCim(br.readLine());
					System.out.println("Kérem adja meg az album műfaját:");
					album.setMufaj(br.readLine());
					System.out.println("Kérem adja meg az album megjelenésének évét:");
					album.setMegjelenes_eve(sc.nextInt());
					System.out.println("Kérem adja meg az album előadójának a nevét:");
					album.setEloado(br.readLine());
					System.out.println("Kérem adja meg az album katalógusszámát:");
					album.setKatalogusszam(br.readLine());
					addAlbum(document, album);
					break;
				case 2:
					Eloado eloado = new Eloado();
					System.out.println("Kérem adja meg az előadó nevét:");
					eloado.setNev(br.readLine());
					System.out.println("Kérem adja meg az előadó országát:");
					eloado.setOrszag(br.readLine());
					addEloado(document, eloado);
					break;
				case 3:
					Eloado eloadoregi = new Eloado();
					System.out.println("Kérem adja meg a módosítandó előadó nevét:");
					eloadoregi.setNev(br.readLine());
					System.out.println("Kérem adja meg a módosítandó előadó országát:");
					eloadoregi.setOrszag(br.readLine());
					Eloado eloadouj = new Eloado();
					System.out.println("Kérem adja meg a(z) "+eloadoregi.getNev()+" előadó új nevét:");
					eloadouj.setNev(br.readLine());
					System.out.println("Kérem adja meg a(z) "+eloadoregi.getNev()+" előadó új országát:");
					eloadouj.setOrszag(br.readLine());
					modifyEloado(document, eloadoregi, eloadouj);
					break;
				case 4:
					System.out.println("Kérem adja meg az évtizedet!(pl 1970)");
					int evtized = Integer.parseInt(sc.next());
					lekerdezes(document,evtized);
					break;
				case 5:
						System.out.println("1.Kiírás konzolra.");
						System.out.println("2.Kiírás fileba.");
						
						int choose = Integer.parseInt(sc.next());
						switch(choose) {
						case 1:
							printDocument(document);
							break;
						case 2:
							printDocument(document, new File(OUTPUT_FILE_PATH));
							System.out.println("A Documentum kiírva a "+ OUTPUT_FILE_PATH +" helyre!");
							break;
						}
					break;
				default:
					break;
				}
				if(menu==6) {
					break;
				}	
			}
			
			br.close();
			sc.close();
			
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
			if (eloadoNode.getNodeName().equals("eloado")) {
				NodeList eloadoChildNodes = eloadoNode.getChildNodes();
				for (int j = 0; j < eloadoChildNodes.getLength(); j++) {
					Node eloadoChildNode = eloadoChildNodes.item(j);
					if (eloadoChildNode.getNodeName().equals("nev")) {
						eloadok.add(eloadoChildNode.getTextContent());
					}
				}
			}
		}
		return eloadok.indexOf(eloado);
	}
	
	
	public static void addAlbum(Document document, Album album) {
		Integer eloadoref = eloadoExistsIndex(document,String.valueOf(album.getEloado()));
		if (eloadoref == -1) {
			System.out.println("Nincsen "+'"'+ String.valueOf(album.getEloado())+'"'+" nevű előadó az adatbázisban!");
			return;
		}
		
		Node albumokNode = document.getElementsByTagName("albumok").item(0);

		Element albumElement = document.createElement("album");
		albumokNode.appendChild(albumElement);

		Attr eloadoAttribute = document.createAttribute("eloado");
		eloadoAttribute.setNodeValue(Integer.toString(eloadoref));
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
	
	public static void addEloado(Document document, Eloado eloado) {
		Node eloadokNode = document.getElementsByTagName("eloadok").item(0);
		
		NodeList eloadoNodes = eloadokNode.getChildNodes();
		
		Integer newEloadoref = 0;
		for (int i = 0; i < eloadoNodes.getLength(); i++) {
			Node eloadoNode = eloadoNodes.item(i);
			if (eloadoNode.getNodeName().equals("eloado")) {
				newEloadoref++;
			}
		}

		Element eloadoElement = document.createElement("eloado");
		eloadokNode.appendChild(eloadoElement);

		Attr eloadorefAttribute = document.createAttribute("eloadoref");
		eloadorefAttribute.setNodeValue(Integer.toString(newEloadoref));
		eloadoElement.setAttributeNode(eloadorefAttribute);

		Element nevElement = document.createElement("nev");
		nevElement.setTextContent(String.valueOf(eloado.getNev()));
		eloadoElement.appendChild(nevElement);
		
		Element orszagElement = document.createElement("orszag");
		orszagElement.setTextContent(String.valueOf(eloado.getOrszag()));
		eloadoElement.appendChild(orszagElement);
	}
	
	public static Node findEloadoByNev(Document document, String nev) {
		Node eloadokNode = document.getElementsByTagName("eloadok").item(0);
		NodeList eloadoNodeList = eloadokNode.getChildNodes();
		
		Node eloadoToModify = null;
		
		for (int i = 0; i < eloadoNodeList.getLength(); i++) {
			Node eloadoNode = eloadoNodeList.item(i);
			if (eloadoNode.getNodeName().equals("eloado")) {
				NodeList eloadoChildNodes = eloadoNode.getChildNodes();
				for (int j = 0; j < eloadoChildNodes.getLength(); j++) {
					Node eloadoChildNode = eloadoChildNodes.item(j);
					
					if (eloadoChildNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) eloadoChildNode;
						
						if ("nev".equals(eElement.getNodeName())) {
							if (String.valueOf(nev).equals(eElement.getTextContent())) {
								eloadoToModify = eloadoNode;
							}
						}
					}	
				}
			}
		}
		return eloadoToModify;
	}
	
	public static void lekerdezes(Document document, Integer evtized) {
		Integer count = 0;
		Node albumokNode = document.getElementsByTagName("albumok").item(0);
		NodeList albumNodeList = albumokNode.getChildNodes();
		for (int i = 0; i < albumNodeList.getLength(); i++) {
			Node albumNode = albumNodeList.item(i);
			if (albumNode.getNodeName().equals("album")) {
				NodeList albumChildNodes = albumNode.getChildNodes();
				for (int j = 0; j < albumChildNodes.getLength(); j++) {
					Node albumChildNode = albumChildNodes.item(j);
					
					if (albumChildNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) albumChildNode;
						
						if ("megjeleneseve".equals(eElement.getNodeName())) {
							Integer albumeve = Integer.valueOf(eElement.getTextContent());
							if (albumeve >= evtized && albumeve < evtized+10) {
								count++;
								System.out.println(albumChildNodes.item(j-4).getTextContent());
								System.out.println(albumeve);
								System.out.println("\n");
							}
						}
					}	
				}
			}
		}
		if(count > 0) {
			System.out.println("Összesen "+count+" darab album szerepel az adatbázisban a "+evtized+"-s évekből.");
		}
		else {
			System.out.println("Jelenleg nem található album az adatbázisban a "+evtized+"-s évekből.");
		}
	}
	
	public static void modifyEloado(Document document, Eloado eloadoregi, Eloado eloado) {
		
		Node eloadoNode = findEloadoByNev(document, String.valueOf(eloadoregi.getNev()));
		if ( eloadoNode == null) {
			System.out.println("A keresett "+'"'+String.valueOf(eloadoregi.getNev())+'"'+" nevű előadó nem található!");
			return;
		}
		
		NodeList eloadoChildNodes = eloadoNode.getChildNodes();

		for (int j = 0; j < eloadoChildNodes.getLength(); j++) {
			Node eloadoChildNode = eloadoChildNodes.item(j);
			
			if (eloadoChildNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) eloadoChildNode;
				
				if ("nev".equals(eElement.getNodeName())) {
					if (String.valueOf(eloadoregi.getNev()).equals(eElement.getTextContent())) {
						eElement.setTextContent(String.valueOf(eloado.getNev()));
					}
				}
				if ("orszag".equals(eElement.getNodeName())) {
					if (String.valueOf(eloadoregi.getOrszag()).equals(eElement.getTextContent())) {
						eElement.setTextContent(String.valueOf(eloado.getOrszag()));
					}	
				}
			}
		}
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
