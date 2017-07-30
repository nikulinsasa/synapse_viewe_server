package sasa.synapse.parser.controllers;

import static org.junit.Assert.*;

import java.sql.SQLException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;
import org.junit.Test;

import sasa.authorization.jersey.SecurityFilter;
import sasa.synapse.parser.BinderOnlySqlLite;
import sasa.synapse.parser.BinderSynapseElementsStorage;
import sasa.synapse.parser.CrossDomainFilter;
import sasa.synapse.parser.entities.ElementDescription;
import sasa.synapse.parser.entities.SynapseElement;
import sasa.synapse.parser.sqllite.DBConnector;

public class RestControllerTest extends JerseyTest {

	@Override
    protected Application configure() {
		ResourceConfig resourceConfig = new ResourceConfig();
		try {
			resourceConfig.register(new BinderOnlySqlLite());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		resourceConfig.register(new BinderSynapseElementsStorage());
		resourceConfig.register(CrossDomainFilter.class);
		resourceConfig.register(SecurityFilter.class);
		resourceConfig.packages("sasa.synapse.parser.controllers");
		resourceConfig.packages("sasa.authorization.jersey.controllers");
        return resourceConfig;
    }
	
	
	@Test
	public void test() throws SQLException {
		
		DBConnector<ElementDescription> dbc = new DBConnector<ElementDescription>(new ElementDescription());
		dbc.createTable();
		dbc.deleteAll();
		
		JSONObject request = new JSONObject();
		request.put("user", "test");
		request.put("password", "test");
		Response answer = target("auth").request().post(Entity.text(request.toString()));
		JSONObject obj = new JSONObject( answer.readEntity(String.class) );
		String token = obj.getString("token");
		
		request = new JSONObject();
		request.put("description", "test description");
		request.put("name", "test");
		answer = target("rest/description").request().header("Authorization", token).post(Entity.text(request.toString()));
		obj = new JSONObject( answer.readEntity(String.class) );
		assertEquals("test description", obj.getString("description"));
		assertNotNull(obj.getInt("id"));
		
		obj.put("description", "description2");
		String path = "rest/description/"+obj.getInt("id");
		answer = target(path).request().header("Authorization", token).put(Entity.text(obj.toString()));
		obj = new JSONObject( answer.readEntity(String.class) );
		assertEquals("description2", obj.getString("description"));
		
		obj = null;
		answer = target("rest/description").queryParam("name", "test").request().header("Authorization", token).get();
		obj = new JSONObject( answer.readEntity(String.class) );
		obj = (JSONObject)obj.getJSONArray("list").get(0);
		assertEquals("description2", obj.getString("description"));
		
		answer = target("rest/description/"+obj.getInt("id")).request().header("Authorization", token).delete();
		
		answer = target("rest/description").request().header("Authorization", token).get();
		obj = new JSONObject( answer.readEntity(String.class) );
		assertEquals(0, obj.getJSONArray("list").length());
		
	}

}
