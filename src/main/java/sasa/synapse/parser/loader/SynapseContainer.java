package sasa.synapse.parser.loader;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import sasa.synapse.parser.AbstractSynapseParser;

public class SynapseContainer {

	private HashMap<String, Node> data = new HashMap<>();
	private AbstractSynapseParser parser;
	
	public SynapseContainer(AbstractSynapseParser parser){
		this.parser = parser;
	}
	
	public void add(Document obj){
		parser.setObj(obj);
		if(parser.isValid()){
			data.put(parser.name(), parser.valeu());
		}
	}
	
	public Node get(String name) {
		return data.get(name);
	}
	
	public Set<Entry<String, Node>> getMap() {
		return data.entrySet();
	}
	
}
