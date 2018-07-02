package fr.inria;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.script.ScriptEngine;

import org.dom4j.Document;
import org.dom4j.Element;
import net.sf.json.JSONObject;


public class XmlSource extends XmlBase{

	protected Document                 sourceDocument;
	protected Map<String, JSONObject>  sourceElements;
	protected Set<String>              sourceElementTypes;
		
	
	
	public XmlSource(String fileName, Set<String> sourceElementTypes, ScriptEngine engine) {
		
		
			this.sourceDocument = this.load(fileName);
			this.jsEngine = engine;
			this.sourceElementTypes = sourceElementTypes;
			this.sourceElements = new HashMap<String, JSONObject>();
				
		mappingToJsonParameters();
	}
	

	
	/**
	 * 映射源文档中所有选中的元素
	 */
	protected void mappingToJsonParameters() {
		
		runJavaScript("var eMap = [];");//对象Map,根据路径获得JSON对象
		
		Element root = sourceDocument.getRootElement();
		for(Element child:root.elements()) {//把所有需要的元素映射到javascript的变量中
			mappingElement(child);
		}
	}
	
	/**
	 * 映射所有选中的元素
	 * @param parent
	 */
	private void mappingElement(Element parent) {
		
		if(sourceElementTypes.contains(parent.getName())) {
			
			//映射parent的Element=>JSON并保存到Map中
			String path = xmlPath(parent, "");
			System.out.println(path);
			//该元素是需要处理的有效元素
			JSONObject jsonParent = xml2json(new JSONObject(), parent);
			//将元素写入sourceElements
			sourceElements.put(path, jsonParent);
			runJavaScript(String.format("var _%s = %s;", jsonParent.getString("_name"), jsonParent.toString(4)));
			runJavaScript(String.format("eMap['%s'] = _%s;", path, jsonParent.getString("_name")));
		} else {
			for(Element child:parent.elements()) {
				mappingElement(child);
			}
		}
	}



	public Document getSourceDocument() {
		return this.sourceDocument;
	}
	
	
	
	
	
}
