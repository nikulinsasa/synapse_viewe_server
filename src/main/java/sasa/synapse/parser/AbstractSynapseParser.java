package sasa.synapse.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class AbstractSynapseParser {

	protected String elementName;
	
	protected Node obj;
	
	public AbstractSynapseParser(String elementName){
		this.elementName = elementName;
	}
	
	public boolean isValid() {
		return obj!=null && obj.getAttributes().getNamedItem("name")!=null;
	}
	
	public String name() {
		if(obj==null){
			return null;
		}
		return obj.getAttributes().getNamedItem("name").getNodeValue();
	}
	
	public Node valeu() {
		return obj;
	}
	
	public void setObj(Document obj){
		this.obj = obj.getFirstChild();
	}
	
}
