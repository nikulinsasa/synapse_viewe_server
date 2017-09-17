package sasa.synapse.parser.sqllite;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;

import sasa.synapse.parser.entities.DependencySynapseElements;
import sasa.synapse.parser.entities.SynapseElement;
import sasa.synapse.parser.storage.ISynapseStorage;
import sasa.synapse.parser.storage.MediatorItem;
import sasa.synapse.parser.storage.StorageException;

public class StorageSQLLite implements ISynapseStorage {

	private DBConnector<SynapseElement> connector;
	private DBConnector<DependencySynapseElements> depConnector;

	public StorageSQLLite(DBConnector<SynapseElement> se) throws SQLException {
		connector = se;
		depConnector = new DBConnector<DependencySynapseElements>(new DependencySynapseElements(), se.getSource());
		se.createTable();
		depConnector.createTable();
	}

	@Override
	public void saveElement(SynapseElement se) throws StorageException {
		try {
			connector.insert(se);
			if (se.hasParent()) {
				Iterator<DependencySynapseElements> parents = se.getArrayParents();
				while (parents.hasNext()) {
					depConnector.insert(parents.next());
				}
			}
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
	public List<MediatorItem> findMediatorList(String type) throws StorageException {
		List<SynapseElement> elements;
		try {
			elements = connector.find(Collections.singletonMap("type", type));
			if (elements == null) {
				return null;
			}
			List<MediatorItem> result = new ArrayList<MediatorItem>();
			Iterator<SynapseElement> _iterator = elements.iterator();
			while (_iterator.hasNext()) {
				SynapseElement element = _iterator.next();
				MediatorItem item = new MediatorItem();
				item.name = element.getName();

				element.getParents().iterator().forEachRemaining(_item -> {
					item.parents.add(_item.getParentElement().getName());
				});

				result.add(item);
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
			depConnector.deleteAll();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StorageException(e.getMessage());
		}
	}

	@Override
	public List<SynapseElement> searchByName(String term) throws StorageException {
		QueryBuilder<SynapseElement, String> qb = connector.createQueryBuilder();
		SelectArg arg = new SelectArg();
		arg.setValue("%" + term + "%");
		Where<SynapseElement, String> _where = qb.where();
		try {
			_where.like("name", arg);
			return qb.query();
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());
		}
	}

}
