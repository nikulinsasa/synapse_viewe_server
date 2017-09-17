package sasa.synapse.parser.controllers;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.json.JSONArray;
import org.json.JSONObject;

import sasa.authorization.jersey.Security;
import sasa.synapse.parser.entities.SynapseElement;
import sasa.synapse.parser.storage.ISynapseStorage;
import sasa.synapse.parser.storage.MediatorItem;
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
			List<MediatorItem> _list = storage.findMediatorList(type);
			if(_list!=null){
				_list.sort(new Comparator<MediatorItem>() {

					@Override
					public int compare(MediatorItem o1, MediatorItem o2) {
						
						return o1.name.compareTo(o2.name);
					}
				});
				JSONArray names = new JSONArray();
				_list.forEach(item->{
					JSONObject row = new JSONObject();
					row.put("name", item.name);
					JSONArray parents = new JSONArray();
					row.put("parents", parents);
					item.parents.forEach(p->{
						parents.put(p);
					});
					names.put(row);
					
				});
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
	
	@GET
	@Path("/search")
	@Produces("text/xml")
	public String searchElement(@QueryParam("term") String term) {
		try {
			StringBuffer result = new StringBuffer("<elements>");
			List<SynapseElement> elements = storage.searchByName(term);
			for(SynapseElement item : elements){
				result.append("<item>");
				result.append(item.getXml());
				result.append("</item>");
			}
			result.append("</elements>");
			return result.toString();
		}catch(NullPointerException ne){
			return "<error>Результат поиска по "+term+" не дал результатов</error>";
		} catch (Exception e) {
			e.printStackTrace();
			return "<error>Произошла неожиданная ошибка</error>";
		}
	}
	
}
