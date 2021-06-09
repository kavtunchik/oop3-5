package by.bsuir.plugins;

import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class AttributesAndTags implements Plugin {
    private static final HashSet<String> PRIMITIVES = new HashSet<>(Arrays.asList(
            "byte", "short", "int", "long", "float", "double", "boolean", "char", "string"
    ));

    @Override
    public String saveTransform(String output) {
        return XML.toString(XML.toJSONObject(output));
    }

    @Override
    public String loadTransform(String input) {
        try {
            // Создание документа
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(input)));
            // Замена тегов атрибутами
            Element element = document.getDocumentElement();
            NodeList childNodes = element.getChildNodes();
            replaceTagsByAttributes(element, childNodes);
            // Запись изменений
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            // Результат
            return stringWriter.getBuffer().toString();
        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private void replaceTagsByAttributes(Element parent, NodeList nodeList) {
        LinkedList<Element> delete = new LinkedList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (hasChildElements(element)) {
                    replaceTagsByAttributes(element, element.getChildNodes());
                } else if (!PRIMITIVES.contains(element.getNodeName())) {
                    parent.setAttribute(element.getNodeName(), element.getTextContent());
                    delete.add(element);
                }
            }
        }
        for (Element element : delete) {
            parent.removeChild(element);
        }
    }

    private boolean hasChildElements(Element element) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
        }
        return false;
    }
}
