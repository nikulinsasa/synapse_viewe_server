package sasa.synapse.parser.controllers;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.json.JSONArray;
import org.json.JSONObject;

import sasa.authorization.jersey.Security;
import sasa.synapse.parser.entities.IElementFromJSON;
import sasa.synapse.parser.sqllite.ElementDescriptionStorage;
import sasa.synapse.parser.sqllite.ElementIOExampleStorage;
import sasa.synapse.parser.sqllite.ElementRestStorage;

@Security
@Path("/rest")
public class RestController {

	@Inject
	private ElementDescriptionStorage descriptionStorage;

	@Inject
	private ElementIOExampleStorage exampleStorage;
	
	@POST
	@Produces("text/json")
	@Path("/{type}")
	public String create(@PathParam("type") String type,String body) {
		JSONObject answer = new JSONObject();
		JSONObject obj = new JSONObject(body);
		try {
			@SuppressWarnings("unchecked")
			ElementRestStorage<IElementFromJSON> storage = getStorage(type);
			IElementFromJSON element = (IElementFromJSON)storage.getObj().getClass().newInstance();
			element.fillFromJSON(obj);
			answer = storage.create(element);
		} catch (Exception e) {
			answer.put("error", e.getMessage());
		}
		return answer.toString();
	}
	
	@GET
	@Produces("text/json")
	@Path("/{type}")
	public String find(@PathParam("type") String type,
			@QueryParam("name") String name) {
		JSONObject answer = new JSONObject();
		try {
			@SuppressWarnings("unchecked")
			ElementRestStorage<IElementFromJSON> storage = getStorage(type);
			List<IElementFromJSON> list = storage.findBy(Collections.singletonMap("name", name));
			JSONArray elements = new JSONArray();
			for(IElementFromJSON item : list){
				elements.put(item.getJSON());
			}
			answer.put("list", elements);
		} catch (Exception e) {
			answer.put("error", e.getMessage());
		}
		
		return answer.toString();
	}
	
	
	@PUT
	@Produces("text/json")
	@Path("/{type}/{id}")
	public String update(String body, @PathParam("type") String type,
			@PathParam("id") String id) {
		JSONObject answer = new JSONObject();
		JSONObject obj = new JSONObject(body);
		try {
			@SuppressWarnings("unchecked")
			ElementRestStorage<IElementFromJSON> storage = getStorage(type);
			List<IElementFromJSON> list = storage.findBy(Collections.singletonMap("id", id));
			if(list.isEmpty()){
				throw new Exception("Нет элемента");
			}
			IElementFromJSON element = list.get(0);
			element.fillFromJSON(obj);
			answer = storage.update(id, element);
			
		} catch (Exception e) {
			answer.put("error", e.getMessage());
		}
		
		return answer.toString();
	}
	
	@DELETE
	@Produces("text/json")
	@Path("/{type}/{id}")
	public String delete(@PathParam("type") String type,
			@PathParam("id") String id) {
		JSONObject answer = new JSONObject();
		try {
			@SuppressWarnings("unchecked")
			ElementRestStorage<IElementFromJSON> storage = getStorage(type);
			storage.delete(id);
			
			answer.put("message", "Удаление прошло успешно");
		} catch (Exception e) {
			answer.put("error", e.getMessage());
		}
		
		return answer.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private ElementRestStorage getStorage(String type) throws Exception {
		switch(type){
		case "description":return descriptionStorage;
		case "example":return exampleStorage;
		default:
			throw new Exception("Для указанного типа нет storage");
		}
	}
	
}
