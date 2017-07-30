package sqllite;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Collections;

import org.junit.Test;

import sasa.synapse.parser.entities.SynapseElement;
import sasa.synapse.parser.sqllite.DBConnector;

public class DBConnectorTest {


	@Test
	public void test() throws SQLException {
		SynapseElement p = new SynapseElement();
		p.setName("test1");
		p.setXml("test body");
		p.setType(SynapseElement.PROXY);
		DBConnector<SynapseElement> dbc = new DBConnector<SynapseElement>(p);
		dbc.createTable();
		
		dbc.insert();
		
		SynapseElement p2 = dbc.findOne(Collections.singletonMap("name", "test1"));
		
		assertEquals("test body", p2.getXml());
		dbc.delete(Collections.singletonMap("name", "test1"));
		
	}

}
