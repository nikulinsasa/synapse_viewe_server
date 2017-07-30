package sasa.synapse.parser.controllers;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import sasa.authorization.jersey.Security;
import sasa.synapse.parser.creator.DataCreator;
import sasa.synapse.parser.loader.SynapseLoader;
import sasa.synapse.parser.storage.ISynapseStorage;
import sasa.synapse.parser.storage.StorageException;

@Security
@Path("/service")
public class ServicesController {

	@Inject
	private ISynapseStorage storage;

	@Inject
	private SynapseLoader sl;
	
	@GET
	@Path("/update-proxy")
	@Produces("text/xml")
	public String updateSynapse() {

		try {
			storage.clearStorage();
			DataCreator.createProxiesRows(sl, storage);

		} catch (StorageException e) {
			e.printStackTrace();
			return "<error>Проблема с записью</error>";
		}
		return "<ok/>";
	}

}
