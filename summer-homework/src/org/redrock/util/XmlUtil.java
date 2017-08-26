package org.redrock.util;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;


/**
 * xml中区分 Node Element Attribute三者区别，注意Node的种类
 */
public class XmlUtil {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        //把字符串转化为Document对象
        String xml = CurlUtil.getContent("http://hongyan.cqupt.edu.cn/MagicLoop/index.php?s=/addon/InquiryExam/InquiryExam/testAtHome&type=qmcj&xh=2015211878", null, "GET");
        //System.out.println(xml);
        StringReader reader = new StringReader(xml);
        InputSource source = new InputSource(reader);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(source);
        //获取根节点元素
        StringBuilder stringBuilder = new StringBuilder();
        Element rootNode = document.getDocumentElement();
        readNode(rootNode, stringBuilder);
        String out = stringBuilder.toString();
        //System.out.println(out);
        NodeList nodeList = rootNode.getChildNodes();
        Node child = nodeList.item(4);
        //System.out.println(child);
        nodeList = child.getChildNodes();

        Node child1 = null;
        int sum = 0;
        for (int i = 0; i<15; i++) {
            Node child2 = nodeList.item(i);
            //child1 = Integer.parseInt(child2.getLastChild().getTextContent());
            //sum = child1 + sum;
            sum = sum + Integer.parseInt(child2.getLastChild().getTextContent());
        }
        //System.out.println(sum);
        int aver = sum/15;
        System.out.println(aver);
    }

/*
    */
/**
     * 递归解析xml
     * @param node
     * @param builder
     */

    public static void readNode(Node node, StringBuilder builder) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            if (!node.getTextContent().trim().equals("")) {
                builder.append(node.getNodeValue());
            }
        } else {
            String nName = node.getNodeName();
            builder.append("<").append(nName);
            if (node.hasAttributes()) {
                NamedNodeMap attrs = node.getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    Node attr = attrs.item(i);
                    String aName = attr.getNodeName();
                    String value = attr.getNodeValue();
                    builder.append(" ").append(aName).append("=").append(value);
                }
            }
            builder.append(">");
            if (node.hasChildNodes()) {
                NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node childNode = childNodes.item(i);
                    readNode(childNode, builder);
                }
            }
            builder.append("</").append(nName).append(">");
        }
    }

}
