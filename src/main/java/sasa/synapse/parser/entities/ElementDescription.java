package sasa.synapse.parser.entities;

import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="element_descriptions")
public class ElementDescription implements IElementFromJSON {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(uniqueIndex = true)
	private String name;
	
	@DatabaseField
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void fillFromJSON(JSONObject obj) {
		if(obj.has("description")){
			setDescription(obj.getString("description"));
		}
		if(obj.has("name")){
			setName(obj.getString("name"));
		}
		if(obj.has("id")){
			setId(obj.getInt("id"));
		}
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("description", getDescription());
		json.put("name", getName());
		return json;
	}
	
	
	
}
