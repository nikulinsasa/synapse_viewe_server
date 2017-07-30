package sasa.authorization.jersey.controllers;

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
import sasa.synapse.parser.CrossDomainFilter;

public class AuthorizationTest extends JerseyTest {

	@Override
    protected Application configure() {
		ResourceConfig resourceConfig = new ResourceConfig();
		try {
			resourceConfig.register(new BinderOnlySqlLite());
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		assertTrue(obj.has("token"));
		
		answer = target("auth/refresh/"+obj.getString("refresh_token")).request().get();
		obj = new JSONObject( answer.readEntity(String.class) );
		assertTrue(obj.has("token"));
		
		answer = target("auth/logout/"+obj.getString("token")).request().delete();
		obj = new JSONObject( answer.readEntity(String.class) );
		assertTrue(obj.has("msg"));
		assertFalse(obj.has("error"));
	}

}
