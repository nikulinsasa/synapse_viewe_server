package sasa.synapse.parser.sqllite;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

import sasa.synapse.parser.entities.ElementDescription;
import sasa.synapse.parser.storage.StorageException;

public class ElementDescriptionStorage extends ElementRestStorage<ElementDescription> {

	public ElementDescriptionStorage( JdbcConnectionSource source) throws StorageException {
		super(new ElementDescription(), source);
	}

	
}
