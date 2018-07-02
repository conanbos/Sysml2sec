package fr.inria;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlBase {

	protected ScriptEngine jsEngine;
	
	protected String runJavaScript(String script) {
		
		try {
			System.out.println("javascript:"+script);
			Object result = jsEngine.eval(script);
			if(result instanceof String) {
				return (String)result;
			} else {
				return null;
			}
		} catch(ScriptException e) { 
		   e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	protected Document load(String fileName) {
		
		Document document = null;
	
		try {
			SAXReader saxReader = new SAXReader();
	        document = saxReader.read(new File(fileName));
        } catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	
	
	
	
	
	protected boolean saveAs(String file, Document docuemnt) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new FileWriter(file), format);
			writer.write(docuemnt);
			writer.close();
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	
	
	protected JSONObject xml2json(JSONObject json, Element element) {
		
		for(Attribute attribute:element.attributes()) {
			json.put("_" + attribute.getName(), attribute.getValue());
		}
		
		//取所有子元素的名称
		Set<String> names = new HashSet<String>();
		for(Element child:element.elements()) {
			names.add(child.getName());
		}
		
		Iterator<String> it = names.iterator();
		
		while(it.hasNext()) {
			String name = it.next();
			List<Element> children = element.elements(name);
			JSONArray jsonChildren = new JSONArray();
			for(Element child:children) {
				JSONObject jsonChild = new JSONObject();
				for(Attribute attribute:child.attributes()) {
					jsonChild.put("_" + attribute.getName(), attribute.getValue());
				}
				if(child.elements().size()>0) {
					xml2json(jsonChild, child);
				}
				jsonChildren.add(jsonChild);
			}
			
			if(jsonChildren.size() == 1) {
				json.put("_" + name, jsonChildren.get(0));
			} else if(jsonChildren.size() > 1) {
				json.put("_" + name, jsonChildren);
			}
		}
		
		return json;
	}
	
	
	/**
	 * 获得元素的路径,不包括最外层
	 * @param element
	 * @param path
	 * @return
	 */
	protected String xmlPath(Element element, String path) {
		
		Element parent = element.getParent();
		while(parent != null) {
			int index = parent.elements(element.getName()).indexOf(element);//在父元素中的位置
			path      = "@" + element.getName() + "." + index + (path.isEmpty()? "":("/" + path));
			element   = element.getParent();
			parent    = element.getParent();
		}
		return "//" + path;
	}
	
	
	
}















