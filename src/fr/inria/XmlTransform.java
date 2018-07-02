package fr.inria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.IOException;



import javax.script.ScriptEngine;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class XmlTransform extends XmlBase {

	protected Document ruleDocument;
	protected Document sourceDocument;
	protected Document destinationDocument;
	
	protected Set<String> sourceElementTypes;
	
	public XmlTransform(String fileName, ScriptEngine engine) {
		try
		{
			this.ruleDocument       = this.load(fileName);
			this.sourceElementTypes = new HashSet<String>();
			this.jsEngine           = engine;
			this.sourceElementTypes = null;
			loadJavaScript();
			createDestinationDocument();
		}
		catch (Exception e)
		{
			System.out.println("load file error");
		}
	}
	
	
	protected void loadJavaScript() {
		Element jsElement = (Element)ruleDocument.selectSingleNode("//javascript");
		String javascript = (String)jsElement.getData();
		this.runJavaScript(javascript);
	}
	
	protected void createDestinationDocument() {
		destinationDocument = DocumentHelper.createDocument();
		Element root = (Element)ruleDocument.selectSingleNode("//template[@match='/']");
		if(root != null) {
			for(Element element:root.elements()) {
				destinationDocument.add((Element)element.clone());
			}
		}
	}
	
	public void save(String fileName) {
		this.saveAs(fileName, destinationDocument);
	}
	
	
	public Set<String> getSourceElementTypes() {
		if(sourceElementTypes == null) {
			sourceElementTypes = new HashSet<String>();
			
			for(Element element:ruleDocument.getRootElement().elements("template")) {
				
				String elementType = element.attributeValue("match");
				
				if(elementType!=null && !elementType.equals("/") && !sourceElementTypes.contains(elementType)) {
					sourceElementTypes.add(elementType);
				}
			}
		}
		return sourceElementTypes;
	}
	
	public void setSourceDocument(Document document) {
		this.sourceDocument = document;
	}
	
	
	public void transform() {
		for(Element element:ruleDocument.getRootElement().elements("template")) {
			if(element.attributeValue("match").equals("/")) continue;
			transformByTemplate(element);
		}
	}
	
	private void transformByTemplate(Element template) {//template can't change
		
		List<Node> nodes = this.sourceDocument.selectNodes("//"+template.attributeValue("match"));
		String    target = template.attributeValue("target");
		Element location = (Element)destinationDocument.selectSingleNode(target);
		for(Node node:nodes) {
			if(node.getNodeType() != Element.ELEMENT_NODE) continue;
			Element element = (Element)node;//element是原文件中的元素
			String  name    = "_" + element.attributeValue("name");
			
			Element templateClone = (Element)template.clone();
			for(Element templateElement:templateClone.elements()) {
				assignAttributes(templateElement, name);
				assignChildElement(templateElement, name);
				templateClone.remove(templateElement);//从原来的父元素中脱离出来
				location.add(templateElement);
			}
		}
	}
	
	/**
	 * set sub element
	 * @param parent
	 */
	private void assignChildElement(Element parent,String elementName) {
		for(Element child:parent.elements()) {
			assignAttributes(child, elementName);
			assignChildElement(child, elementName);
		}
	}
	
	/**
	 * set attribute
	 * @param element
	 */
	private void assignAttributes(Element element,String elementName) {
		
		List<Attribute> attributes = element.attributes();
		for(Attribute attribute:attributes) {
			String value = attribute.getValue();
			if(value.startsWith("${") && value.endsWith("}")) {
				value = value.substring(2, value.length()-1);
				if(value.contains("@")) {//@repalce element
					value = value.replaceAll("@", elementName+".");
				}
				//formula
				value = this.runJavaScript(value);
				if(value != null) {
					attribute.setValue(value);
				}
			}
		}
	}
}













