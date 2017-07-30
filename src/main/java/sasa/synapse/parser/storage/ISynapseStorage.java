package sasa.synapse.parser.storage;

import java.util.List;

import sasa.synapse.parser.entities.SynapseElement;

public interface ISynapseStorage {

	public void saveElement(SynapseElement se) throws StorageException;
	
	public SynapseElement findSynapseElementByName(String name) throws StorageException;
	
	public List<String> findNameList(String type) throws StorageException;
	
	public List<SynapseElement> searchByName(String term) throws StorageException;
	
	public void clearStorage() throws StorageException;
	
}
