package sasa.synapse.parser.sqllite;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import sasa.synapse.parser.entities.SynapseElement;
import sasa.synapse.parser.storage.ISynapseStorage;
import sasa.synapse.parser.storage.StorageException;

public class StorageSQLLite implements ISynapseStorage {

	private DBConnector<SynapseElement> connector;
	
	public StorageSQLLite(DBConnector<SynapseElement> se) throws SQLException{
		connector = se;
		se.createTable();
	}


	@Override
	public void saveElement(SynapseElement se) throws StorageException {
		try {
			connector.insert(se);
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());
		}
	}

	@Override
	public SynapseElement findSynapseElementByName(String name) throws StorageException {
		try {
			return connector.findOne(Collections.singletonMap("name", name));
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());
		}
	}

	@Override
	public List<String> findNameList(String type) throws StorageException {
		List<SynapseElement> elements;
		try {
			elements = connector.find(Collections.singletonMap("type", type));
			if(elements==null){
				return null;
			}
			List<String> result = new ArrayList<String>();
			Iterator<SynapseElement> _iterator = elements.iterator();
			while(_iterator.hasNext()){
				result.add(_iterator.next().getName());
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StorageException(e.getMessage());
		}
		
	}


	@Override
	public void clearStorage() throws StorageException {
		try {
			connector.deleteAll();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StorageException(e.getMessage());
		}
	}
	
}
