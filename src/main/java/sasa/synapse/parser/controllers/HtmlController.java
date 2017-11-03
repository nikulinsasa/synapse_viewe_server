package sasa.synapse.parser.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.glassfish.jersey.jaxb.internal.XmlCollectionJaxbProvider.App;

@Path("/html/element")
public class HtmlController {

	@GET
	@Path("/{name}")
	@Produces("text/html")
	public String element(String name) {
		
		try {
			InputStream is = App.class.getResource("/webapp/index.html").openStream();
			byte[] b = new byte[1024];
			is.read(b);
			return new String(b);
		} catch (IOException e) {
			return e.getMessage();
		}
		
		
	}
	
}
