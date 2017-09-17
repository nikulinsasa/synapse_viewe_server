package sasa.synapse.parser.creator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Node;

import sasa.synapse.parser.entities.SynapseElement;
import sasa.synapse.parser.loader.SynapseLoader;
import sasa.synapse.parser.storage.ISynapseStorage;
import sasa.synapse.parser.storage.StorageException;

public class DataCreator {

	private static ISynapseStorage storage;
	
	public static void createProxiesRows(SynapseLoader sl,ISynapseStorage storage) throws StorageException {
		Iterator<Entry<String, Node>> iterator = sl.getProxy().getMap().iterator();
		DataCreator.storage = storage;
		while (iterator.hasNext()) {
			Entry<String, Node> item = iterator.next();
			SynapseElement se = createElement(item.getKey());
			se.setType(SynapseElement.PROXY);
			se.setName(item.getKey());
			
			ProxyCreator creator = new ProxyCreator(item.getValue());
			se.setXml(creator.create(sl.getSequencies()));
			storage.saveElement(se);
			
			HashMap<String, Integer> sequencies = creator.getUsedSequencies();
			for(Map.Entry<String, Integer> entry : sequencies.entrySet()){
				SynapseElement sequency = createElement(entry.getKey());
				sequency.setType(SynapseElement.SEQUENCE);
				sequency.setName(entry.getKey());
				sequency.addParent(se);
				sequency.setXml("");
				storage.saveElement(sequency);
			}
			
			
		}
	}
	
	public static void createSequenciesRows(SynapseLoader sl,ISynapseStorage storage) throws StorageException {
		Iterator<Entry<String, Node>> iterator = sl.getSequencies().getMap().iterator();
		DataCreator.storage = storage;
		while (iterator.hasNext()) {
			Entry<String, Node> item = iterator.next();
			SynapseElement se = createElement(item.getKey());
			se.setType(SynapseElement.SEQUENCE);
			se.setName(item.getKey());
			se.setXml(item.getValue());
			storage.saveElement(se);
		}
	}
	
	private static SynapseElement createElement(String key) throws StorageException {
		SynapseElement se = storage.findSynapseElementByName(key);
		if (se == null) {
			se = new SynapseElement();
		}
		return se;
	}
	
}
