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

public class ServicesTest extends JerseyTest {

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
	public void test() {
		JSONObject request = new JSONObject();
		request.put("user", "test");
		request.put("password", "test");
		Response answer = target("auth").request().post(Entity.text(request.toString()));
		JSONObject obj = new JSONObject( answer.readEntity(String.class) );
		
		
		Response r = target("/service/update-proxy").request().header("Authorization", obj.getString("token")).get();
		assertEquals("<ok/>",r.readEntity(String.class));
		
	}

}
