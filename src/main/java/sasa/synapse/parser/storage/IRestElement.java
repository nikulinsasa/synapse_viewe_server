package sasa.synapse.parser.storage;

import java.util.Map;

public interface IRestElement {

	public void create(Object obj) throws StorageException;
	
	public void delete(Object id) throws StorageException;
	
	public void update(Object id, Object obj) throws StorageException;
	
	public void findBy(Map<Object, Object> map) throws StorageException;
	
}
