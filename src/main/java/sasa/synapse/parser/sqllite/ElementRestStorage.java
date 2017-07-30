package sasa.synapse.parser.sqllite;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

import sasa.synapse.parser.entities.IElementFromJSON;
import sasa.synapse.parser.storage.StorageException;

public class ElementRestStorage<T extends IElementFromJSON> {

	private DBConnector<T> connector;
	
	private T obj;
	
	public ElementRestStorage(T _class,JdbcConnectionSource source) throws StorageException{
		try {
			obj = _class;
			connector = new DBConnector<T>(_class, source);
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());
		}
	}
	
	
	public JSONObject create(T obj) throws StorageException {
		try {
			connector.insert(obj);
			return obj.getJSON();
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());
		}
	}

	
	public void delete(Object id)  throws StorageException{
		try {
			connector.delete(Collections.singletonMap("id", id.toString()));
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());
		}
	}

	
	public JSONObject update(String id, T obj) throws StorageException{
		try {
			connector.update(obj, id.toString());
			return obj.getJSON();
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());
		}
	}

	
	public List<T> findBy(Map<String, Object> map) throws StorageException {
		try {
			return connector.find(map);
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());
		}
	}


	public T getObj() {
		return obj;
	}


	public void setObj(T obj) {
		this.obj = obj;
	}

	
	
}
