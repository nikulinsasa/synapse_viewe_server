package sqllite;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Collections;

import org.junit.Test;

import sasa.synapse.parser.entities.SynapseElement;
import sasa.synapse.parser.sqllite.DBConnector;
import sasa.synapse.parser.sqllite.StorageSQLLite;
import sasa.synapse.parser.storage.StorageException;

public class StorageSQLLiteTest {

	@Test
	public void test() throws SQLException, StorageException {
		
		DBConnector<SynapseElement> dbc = new DBConnector<SynapseElement>(new SynapseElement());
		StorageSQLLite storage = new StorageSQLLite(dbc);
		
		SynapseElement se = new SynapseElement();
		se.setName("storageTest1");
		se.setXml("Value1");
		se.setType(SynapseElement.PROXY);
		storage.saveElement(se);
		
		SynapseElement p2 = storage.findSynapseElementByName("storageTest1");
		
		assertEquals(se.getXml(), p2.getXml());
		
		dbc.delete(Collections.singletonMap("name", "storageTest1"));
		
	}

}
