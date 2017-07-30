package sasa.synapse.parser;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import javax.servlet.ServletRegistration;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import sasa.authorization.jersey.SecurityFilter;


public class App 
{
    private static int getPort(int defaultPort) {
		// grab port from environment, otherwise fall back to default port 9998
		String httpPort = System.getProperty("jersey.test.port");
		if (null != httpPort) {
			try {
				return Integer.parseInt(httpPort);
			} catch (NumberFormatException e) {
			}
		}
		return defaultPort;
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost/").port(getPort(9998)).build();
	}

	public static final URI BASE_URI = getBaseURI();

	protected static HttpServer startServer(String[] args) throws IOException, SQLException {
		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.register(new BinderOnlySqlLite(args.length>0 && args[0].equals("INIT")));
		if(args.length>1){
			resourceConfig.register(new BinderSynapseElementsStorage(args[1]));			
		}else{
			resourceConfig.register(new BinderSynapseElementsStorage());
		}
		
		resourceConfig.register(CrossDomainFilter.class);
		resourceConfig.register(SecurityFilter.class);
		resourceConfig.packages("sasa.synapse.parser.controllers");
		resourceConfig.packages("sasa.authorization.jersey.controllers");
		System.out.println("Starting grizzly2...");

		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI);

		WebappContext context = new WebappContext("WebappContext");
		ServletRegistration registration = context.addServlet("ServletContainer", new ServletContainer(resourceConfig));
		registration.addMapping("/*");
		context.deploy(server);
		return server;
	}

	public static void main(String[] args) throws IOException, SQLException {
		// Grizzly 2 initialization
		startServer(args);

		System.out.println(String.format(
				"Jersey app started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...",
				BASE_URI));

	}
}
