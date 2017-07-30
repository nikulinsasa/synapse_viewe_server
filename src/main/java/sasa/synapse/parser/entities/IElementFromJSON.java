package sasa.synapse.parser.entities;

import org.json.JSONObject;

public interface IElementFromJSON {

	public void fillFromJSON(JSONObject obj);
	
	public JSONObject getJSON();
	
}
