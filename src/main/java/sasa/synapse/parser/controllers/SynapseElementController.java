package sasa.synapse.parser.controllers;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONArray;
import org.json.JSONObject;

import sasa.authorization.jersey.Security;
import sasa.synapse.parser.storage.ISynapseStorage;
import sasa.synapse.parser.storage.StorageException;

@Security
@Path("/element")
public class SynapseElementController {

	@Inject
	private ISynapseStorage storage;

	@GET
	@Path("/list/{type}")
	public String all(@PathParam("type") String type){
		JSONObject obj = new JSONObject();
		try {
			List<String> _list = storage.findNameList(type);
			if(_list!=null){
				_list.sort(new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						
						return o1.compareTo(o2);
					}
				});
				Iterator<String> list = _list.iterator();
				JSONArray names = new JSONArray();
				while(list.hasNext()){
					names.put(list.next());
				}
				obj.put("names", names);
			}
		} catch (StorageException e) {
			e.printStackTrace();
			obj.put("error", e.getMessage());
		}
		
		return obj.toString();
	}
	
	@GET
	@Path("/{name}")
	@Produces("text/xml")
	public String getElement(@PathParam("name") String name) {
		try {
			return storage.findSynapseElementByName(name).getXml();
		}catch(NullPointerException ne){
			return "<error>Не найден элемент с именем "+name+"</error>";
		} catch (Exception e) {
			e.printStackTrace();
			return "<error>Произошла неожиданная ошибка</error>";
		}
	}
	
}
