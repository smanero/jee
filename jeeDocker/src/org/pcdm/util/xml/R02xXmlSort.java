package org.pcdm.util.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class R02xXmlSort {
	
	public static void sort(String filePath) {
		try {
			File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			Node nodeRoot = doc.getDocumentElement();
			System.out.println("Root element :" + nodeRoot.getNodeName());
			

			List<String> lstKeys = new ArrayList<String>();
			
			NodeList nList = nodeRoot.getChildNodes();
			for (int nIdx = 0; nIdx < nList.getLength(); nIdx++) {
				Node nNode = nList.item(nIdx);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//					Element eElement = (Element) nNode;
					String key = nNode.getNodeName();
					if (null != key) {
						lstKeys.add(key + "#" + nIdx);
					}
				}
			}

			// build sort new document
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			// document
			Document newDoc = docBuilder.newDocument();
			// root element
			Element newRoot = newDoc.createElement("Domain");
//			newRoot.setAttributeNode((Attr) nodeRoot.getAttributes().item(0));
//			newRoot.setAttributeNode((Attr) nodeRoot.getAttributes().item(1));
			newDoc.appendChild(newRoot);

			Object[] arKeys = lstKeys.toArray();
			Arrays.sort(arKeys);
			for (int nIdx = arKeys.length-1; nIdx >= 0; nIdx--) {
				Object item = arKeys[nIdx];
				System.out.println(item);
				String[] arIdx = ((String)item).split("#");
				int idx = Integer.parseInt(arIdx[1]);
				Node newNode = nList.item(idx).cloneNode(true);
				newRoot.appendChild(newDoc.adoptNode(newNode));
			}
			
			// Write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(newDoc);
			StreamResult result = new StreamResult(new File(filePath + ".sorted"));
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String argv[]) {
		R02xXmlSort.sort("D:\\tmp\\config.sd.xml");
		R02xXmlSort.sort("D:\\tmp\\config.sm.xml");	
	}

}
