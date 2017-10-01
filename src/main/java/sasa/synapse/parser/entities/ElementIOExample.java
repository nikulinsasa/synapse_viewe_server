package sasa.synapse.parser.entities;

import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="element_examples")
public class ElementIOExample implements IElementFromJSON {
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(index = true)
	private String name;
	
	@DatabaseField
	private String inputExample;
	
	@DatabaseField
	private String outputExample;

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

	public String getInputExample() {
		return inputExample;
	}

	public void setInputExample(String inputExample) {
		this.inputExample = inputExample;
	}

	public String getOutputExample() {
		return outputExample;
	}

	public void setOutputExample(String outputExample) {
		this.outputExample = outputExample;
	}
	
	@Override
	public void fillFromJSON(JSONObject obj) {
		if(obj.has("input_example")){
			setInputExample(obj.getString("input_example"));			
		}
		if(obj.has("output_example")){
			setOutputExample(obj.getString("output_example"));
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
		json.put("input_example", getInputExample());
		json.put("output_example", getOutputExample());
		json.put("name", getName());
		return json;
	}
	
}
