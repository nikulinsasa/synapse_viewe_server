package sasa.synapse.parser.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="dependency_synapse_elements")
public class DependencySynapseElements {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
    private SynapseElement parentElement;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
    private SynapseElement childElement;

	public SynapseElement getParentElement() {
		return parentElement;
	}

	public void setParentElement(SynapseElement parentElement) {
		this.parentElement = parentElement;
	}

	public SynapseElement getChildElement() {
		return childElement;
	}

	public void setChildElement(SynapseElement childElement) {
		this.childElement = childElement;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
