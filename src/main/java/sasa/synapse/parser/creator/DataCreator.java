package sasa.synapse.parser.creator;

import java.util.Iterator;
import java.util.Map.Entry;

import org.w3c.dom.Node;

import sasa.synapse.parser.entities.SynapseElement;
import sasa.synapse.parser.loader.SynapseLoader;
import sasa.synapse.parser.storage.ISynapseStorage;
import sasa.synapse.parser.storage.StorageException;

public class DataCreator {

	public static void createProxiesRows(SynapseLoader sl,ISynapseStorage storage) throws StorageException {
		Iterator<Entry<String, Node>> iterator = sl.getProxy().getMap().iterator();

		while (iterator.hasNext()) {
			Entry<String, Node> item = iterator.next();
			SynapseElement se = storage.findSynapseElementByName(item.getKey());
			if (se == null) {
				se = new SynapseElement();
			}
			se.setType(SynapseElement.PROXY);
			se.setName(item.getKey());
			
			ProxyCreator creator = new ProxyCreator(item.getValue());
			se.setXml(creator.create(sl.getSequencies()));
			storage.saveElement(se);
		}
	}
	
}
