package sasa.synapse.parser.sqllite;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

import sasa.synapse.parser.entities.ElementIOExample;
import sasa.synapse.parser.storage.StorageException;

public class ElementIOExampleStorage extends ElementRestStorage<ElementIOExample> {


	public ElementIOExampleStorage(JdbcConnectionSource source) throws StorageException {
		super(new ElementIOExample(), source);
	}
	
}
