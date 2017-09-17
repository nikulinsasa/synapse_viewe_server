package sasa.synapse.parser.entities;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="synapse_elements")
public class SynapseElement {

	static public final String PROXY = "proxy";
	static public final String SEQUENCE = "sequency";
	static public final String ENDPOINT = "endpoint";
	static public final String API = "api";
	static public final String TASK = "task";
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String xml;

	@DatabaseField
	private String type;
	
	@ForeignCollectionField(foreignFieldName="childElement")
	private ForeignCollection<DependencySynapseElements> parents;
	
	private ArrayList<SynapseElement> arrayParents = new ArrayList<>();
	
	public int getId() {
		return id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public void setXml(Node node){
		this.xml =nodeToString(node);
	}
	
	private String nodeToString(Node node) {
	    StringWriter sw = new StringWriter();
	    try {
	      Transformer t = TransformerFactory.newInstance().newTransformer();
	      t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	      t.setOutputProperty(OutputKeys.INDENT, "yes");
	      t.transform(new DOMSource(node), new StreamResult(sw));
	    } catch (TransformerException te) {
	      System.out.println("nodeToString Transformer Exception");
	    }
	    return sw.toString();
	  }

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	public void addParent(SynapseElement parent) {
		arrayParents.add(parent);
	}
	
	public Iterator<DependencySynapseElements> getArrayParents() {
		ArrayList<DependencySynapseElements> dep = new ArrayList<>();
		for(SynapseElement el : arrayParents){
			DependencySynapseElements foo = new DependencySynapseElements();
			foo.setParentElement(el);
			foo.setChildElement(this);
			dep.add(foo);
		}
		return dep.iterator();
	}
	
	public boolean hasParent() {
		return !arrayParents.isEmpty();
	}
	
	public ForeignCollection<DependencySynapseElements> getParents() {
		return parents;
	}
	
}
